package com.jobportal.service;

import com.jobportal.dto.ApplicationRequest;
import com.jobportal.dto.ApplicationResponse;
import com.jobportal.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationService {

    ApplicationResponse applyToJob(Long jobId, ApplicationRequest request, String applicantEmail);

    Page<ApplicationResponse> getMyApplications(String applicantEmail, Pageable pageable);

    Page<ApplicationResponse> getApplicationsForJob(Long jobId, String employerEmail, Pageable pageable);

    Page<ApplicationResponse> getApplicationsForJobByStatus(
        Long jobId, Application.ApplicationStatus status, String employerEmail, Pageable pageable
    );

    ApplicationResponse getApplicationById(Long applicationId, String userEmail);

    ApplicationResponse updateApplicationStatus(
        Long applicationId, Application.ApplicationStatus status,
        String employerNotes, String employerEmail
    );

    ApplicationResponse withdrawApplication(Long applicationId, String applicantEmail);
}
