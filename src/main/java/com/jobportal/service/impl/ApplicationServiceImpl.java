package com.jobportal.service.impl;

import com.jobportal.dto.ApplicationRequest;
import com.jobportal.dto.ApplicationResponse;
import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.entity.Profile;
import com.jobportal.entity.User;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.ApplicationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  JobRepository jobRepository,
                                  UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    // ================= APPLY =================

    @Override
    @Transactional
    public ApplicationResponse applyToJob(Long jobId, ApplicationRequest req, String applicantEmail) {

        User applicant = findUserByEmail(applicantEmail);
        Job job = findJobById(jobId);

        if (job.getStatus() != Job.JobStatus.ACTIVE) {
            throw new IllegalStateException("This job posting is no longer accepting applications.");
        }

        if (applicationRepository.existsByApplicantAndJob(applicant, job)) {
            throw new IllegalStateException("You have already applied to this job.");
        }

        String resumeUrl = req.getResumeUrl();

        if (resumeUrl == null && applicant.getProfile() != null) {
            resumeUrl = applicant.getProfile().getResumeUrl();
        }

        Application application = new Application();
        application.setApplicant(applicant);
        application.setJob(job);
        application.setCoverLetter(req.getCoverLetter());
        application.setResumeUrl(resumeUrl);
        application.setStatus(Application.ApplicationStatus.PENDING);
        application.setViewedByEmployer(false);

        Application saved = applicationRepository.save(application);

        System.out.println("Application submitted: " + saved.getId());

        return toApplicationResponse(saved);
    }

    // ================= READ =================

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getMyApplications(String applicantEmail, Pageable pageable) {

        User applicant = findUserByEmail(applicantEmail);

        return applicationRepository
                .findByApplicant(applicant, pageable)
                .map(this::toApplicationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getApplicationsForJob(Long jobId, String employerEmail, Pageable pageable) {

        Job job = findJobById(jobId);

        assertJobOwnership(job, employerEmail);

        return applicationRepository
                .findByJob(job, pageable)
                .map(this::toApplicationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getApplicationsForJobByStatus(
            Long jobId,
            Application.ApplicationStatus status,
            String employerEmail,
            Pageable pageable
    ) {

        Job job = findJobById(jobId);

        assertJobOwnership(job, employerEmail);

        return applicationRepository
                .findByJobAndStatus(job, status, pageable)
                .map(this::toApplicationResponse);
    }

    @Override
    @Transactional
    public ApplicationResponse getApplicationById(Long applicationId, String userEmail) {

        Application app = findApplicationById(applicationId);

        assertApplicationAccess(app, userEmail);

        boolean isEmployer =
                app.getJob().getPostedBy().getEmail().equals(userEmail);

        if (isEmployer && !app.isViewedByEmployer()) {

            app.setViewedByEmployer(true);

            applicationRepository.save(app);
        }

        return toApplicationResponse(app);
    }

    // ================= UPDATE =================

    @Override
    @Transactional
    public ApplicationResponse updateApplicationStatus(
            Long applicationId,
            Application.ApplicationStatus status,
            String employerNotes,
            String employerEmail
    ) {

        Application app = findApplicationById(applicationId);

        assertJobOwnership(app.getJob(), employerEmail);

        app.setStatus(status);

        if (employerNotes != null) {
            app.setEmployerNotes(employerNotes);
        }

        Application updated = applicationRepository.save(app);

        System.out.println("Application status updated: " + applicationId);

        return toApplicationResponse(updated);
    }

    // ================= WITHDRAW =================

    @Override
    @Transactional
    public ApplicationResponse withdrawApplication(Long applicationId, String applicantEmail) {

        Application app = findApplicationById(applicationId);

        if (!app.getApplicant().getEmail().equals(applicantEmail)) {
            throw new AccessDeniedException("You can only withdraw your own applications.");
        }

        if (app.getStatus() == Application.ApplicationStatus.WITHDRAWN) {
            throw new IllegalStateException("Application is already withdrawn.");
        }

        app.setStatus(Application.ApplicationStatus.WITHDRAWN);

        Application updated = applicationRepository.save(app);

        return toApplicationResponse(updated);
    }

    // ================= HELPERS =================

    private User findUserByEmail(String email) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "email", email));
    }

    private Job findJobById(Long id) {

        return jobRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job", "id", id));
    }

    private Application findApplicationById(Long id) {

        return applicationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Application", "id", id));
    }

    private void assertJobOwnership(Job job, String employerEmail) {

        if (!job.getPostedBy().getEmail().equals(employerEmail)) {
            throw new AccessDeniedException(
                    "You are not authorized to access this job's applications."
            );
        }
    }

    private void assertApplicationAccess(Application app, String userEmail) {

        boolean isApplicant =
                app.getApplicant().getEmail().equals(userEmail);

        boolean isEmployer =
                app.getJob().getPostedBy().getEmail().equals(userEmail);

        if (!isApplicant && !isEmployer) {
            throw new AccessDeniedException(
                    "You are not authorized to view this application."
            );
        }
    }

    private ApplicationResponse toApplicationResponse(Application app) {

        Profile applicantProfile = app.getApplicant().getProfile();
        Profile employerProfile = app.getJob().getPostedBy().getProfile();

        ApplicationResponse response = new ApplicationResponse();

        response.setId(app.getId());
        response.setJobId(app.getJob().getId());
        response.setJobTitle(app.getJob().getTitle());
        response.setCompanyName(
                employerProfile != null ? employerProfile.getCompanyName() : null
        );
        response.setJobLocation(app.getJob().getLocation());
        response.setApplicantId(app.getApplicant().getId());
        response.setApplicantName(app.getApplicant().getFullName());
        response.setApplicantEmail(app.getApplicant().getEmail());
        response.setApplicantAvatarUrl(
                applicantProfile != null ? applicantProfile.getAvatarUrl() : null
        );
        response.setCoverLetter(app.getCoverLetter());
        response.setResumeUrl(app.getResumeUrl());
        response.setStatus(app.getStatus());
        response.setEmployerNotes(app.getEmployerNotes());
        response.setViewedByEmployer(app.isViewedByEmployer());
        response.setInterviewScheduledAt(app.getInterviewScheduledAt());
        response.setAppliedAt(app.getAppliedAt());
        response.setUpdatedAt(app.getUpdatedAt());

        return response;
    }
}