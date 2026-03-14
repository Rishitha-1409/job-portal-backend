package com.jobportal.service.impl;

import com.jobportal.dto.JobRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.entity.Job;
import com.jobportal.entity.Profile;
import com.jobportal.entity.User;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.JobService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public JobServiceImpl(JobRepository jobRepository,
                          UserRepository userRepository,
                          ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    // ----------------------------------------------------
    // CREATE JOB
    // ----------------------------------------------------

    @Override
    @Transactional
    public JobResponse createJob(JobRequest req, String employerEmail) {

        User employer = findUserByEmail(employerEmail);

        Job job = new Job();

        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setRequirements(req.getRequirements());
        job.setResponsibilities(req.getResponsibilities());

        job.setLocation(req.getLocation());
        job.setRemote(req.isRemote());

        job.setJobType(Job.JobType.valueOf(req.getJobType().toUpperCase()));
        job.setExperienceLevel(Job.ExperienceLevel.valueOf(req.getExperienceLevel().toUpperCase()));

        job.setCategory(req.getCategory());
        job.setTags(req.getTags());
        job.setSkillsRequired(req.getSkillsRequired());

        job.setSalaryMin(req.getSalaryMin());
        job.setSalaryMax(req.getSalaryMax());

        job.setCurrency(req.getCurrency() != null ? req.getCurrency() : "USD");

        job.setSalaryVisible(req.isSalaryVisible());
        job.setDeadline(req.getDeadline());

        job.setStatus(Job.JobStatus.ACTIVE);
        job.setPostedBy(employer);

        Job saved = jobRepository.save(job);

        return toJobResponse(saved);
    }

    // ----------------------------------------------------
    // GET JOB BY ID
    // ----------------------------------------------------

    @Override
    @Transactional
    public JobResponse getJobById(Long id) {

        Job job = findJobById(id);

        // increment view count
        job.setViewCount(job.getViewCount() + 1);
        jobRepository.save(job);

        return toJobResponse(job);
    }

    // ----------------------------------------------------
    // GET ALL ACTIVE JOBS
    // ----------------------------------------------------

    @Override
    public Page<JobResponse> getAllActiveJobs(Pageable pageable) {

        return jobRepository
                .findByStatus(Job.JobStatus.ACTIVE, pageable)
                .map(this::toJobResponse);
    }

    // ----------------------------------------------------
    // SEARCH JOBS
    // ----------------------------------------------------

    @Override
    public Page<JobResponse> searchJobs(
            String keyword,
            String location,
            String category,
            Job.JobType jobType,
            Job.ExperienceLevel experienceLevel,
            Boolean remote,
            Pageable pageable
    ) {

        return jobRepository
                .searchJobs(keyword, location, category, jobType, experienceLevel, remote, pageable)
                .map(this::toJobResponse);
    }

    // ----------------------------------------------------
    // EMPLOYER JOBS
    // ----------------------------------------------------

    @Override
    public List<JobResponse> getMyJobs(String employerEmail) {

        User employer = findUserByEmail(employerEmail);

        return jobRepository
                .findByPostedBy(employer)
                .stream()
                .map(this::toJobResponse)
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------
    // UPDATE JOB
    // ----------------------------------------------------

    @Override
    @Transactional
    public JobResponse updateJob(Long id, JobRequest req, String employerEmail) {

        Job job = findJobById(id);

        assertOwnership(job, employerEmail);

        job.setTitle(req.getTitle());
        job.setDescription(req.getDescription());
        job.setRequirements(req.getRequirements());
        job.setResponsibilities(req.getResponsibilities());

        job.setLocation(req.getLocation());
        job.setRemote(req.isRemote());

        job.setJobType(Job.JobType.valueOf(req.getJobType().toUpperCase()));
        job.setExperienceLevel(Job.ExperienceLevel.valueOf(req.getExperienceLevel().toUpperCase()));

        job.setCategory(req.getCategory());
        job.setTags(req.getTags());
        job.setSkillsRequired(req.getSkillsRequired());

        job.setSalaryMin(req.getSalaryMin());
        job.setSalaryMax(req.getSalaryMax());

        if (req.getCurrency() != null)
            job.setCurrency(req.getCurrency());

        job.setSalaryVisible(req.isSalaryVisible());
        job.setDeadline(req.getDeadline());

        Job updated = jobRepository.save(job);

        return toJobResponse(updated);
    }

    // ----------------------------------------------------
    // UPDATE STATUS
    // ----------------------------------------------------

    @Override
    public JobResponse updateJobStatus(Long id, Job.JobStatus status, String employerEmail) {

        Job job = findJobById(id);

        assertOwnership(job, employerEmail);

        job.setStatus(status);

        return toJobResponse(jobRepository.save(job));
    }

    // ----------------------------------------------------
    // DELETE JOB
    // ----------------------------------------------------

    @Override
    public void deleteJob(Long id, String employerEmail) {

        Job job = findJobById(id);

        assertOwnership(job, employerEmail);

        jobRepository.delete(job);
    }

    // ----------------------------------------------------
    // HELPER METHODS
    // ----------------------------------------------------

    private Job findJobById(Long id) {

        return jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job", "id", id));
    }

    private User findUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "email", email));
    }

    private void assertOwnership(Job job, String employerEmail) {

        if (!job.getPostedBy().getEmail().equals(employerEmail)) {

            throw new AccessDeniedException(
                    "You do not have permission to modify this job"
            );
        }
    }

    // ----------------------------------------------------
    // ENTITY → DTO
    // ----------------------------------------------------

    private JobResponse toJobResponse(Job job) {

        Profile profile = job.getPostedBy() != null
                ? job.getPostedBy().getProfile()
                : null;

        long appCount = applicationRepository.countByJob(job);

        JobResponse response = new JobResponse();

        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setRequirements(job.getRequirements());
        response.setResponsibilities(job.getResponsibilities());

        response.setLocation(job.getLocation());
        response.setRemote(job.isRemote());

        response.setJobType(job.getJobType().name());
        response.setExperienceLevel(job.getExperienceLevel().name());

        response.setCategory(job.getCategory());
        response.setTags(job.getTags());
        response.setSkillsRequired(job.getSkillsRequired());

        response.setSalaryMin(job.getSalaryMin());
        response.setSalaryMax(job.getSalaryMax());

        response.setCurrency(job.getCurrency());
        response.setSalaryVisible(job.isSalaryVisible());

        response.setDeadline(job.getDeadline());
        response.setStatus(job.getStatus().name());

        response.setViewCount(job.getViewCount());
        response.setApplicationCount((int) appCount);

        response.setEmployerId(job.getPostedBy().getId());
        response.setEmployerName(job.getPostedBy().getFullName());

        if (profile != null) {
            response.setCompanyName(profile.getCompanyName());
            response.setCompanyLogoUrl(profile.getCompanyLogoUrl());
            response.setCompanyLocation(profile.getCompanyLocation());
        }

        response.setCreatedAt(job.getCreatedAt());
        response.setUpdatedAt(job.getUpdatedAt());

        return response;
    }

    @Override
    @Transactional
    public void incrementJobView(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job", "id", id));

        job.setViewCount(job.getViewCount() + 1);

        jobRepository.save(job);
    }
    @Override
    public Page<JobResponse> getJobsByEmployer(String email, Pageable pageable) {

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "email", email));

        return jobRepository
                .findByPostedBy(employer, pageable)
                .map(this::toJobResponse);
    }
}
