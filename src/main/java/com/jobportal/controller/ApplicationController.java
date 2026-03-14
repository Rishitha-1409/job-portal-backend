package com.jobportal.controller;

import com.jobportal.dto.ApiResponse;
import com.jobportal.dto.ApplicationRequest;
import com.jobportal.dto.ApplicationResponse;
import com.jobportal.entity.Application;
import com.jobportal.service.ApplicationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // ================= APPLY FOR JOB =================

    @PostMapping("/{jobId}")
    public ApiResponse<ApplicationResponse> applyToJob(
            @PathVariable Long jobId,
            @RequestBody ApplicationRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        ApplicationResponse response =
                applicationService.applyToJob(jobId, request, email);

        return ApiResponse.ok("Application submitted successfully", response);
    }

    // ================= GET MY APPLICATIONS =================

    @GetMapping("/my")
    public ApiResponse<Page<ApplicationResponse>> getMyApplications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        String email = authentication.getName();

        Pageable pageable = PageRequest.of(page, size);

        Page<ApplicationResponse> applications =
                applicationService.getMyApplications(email, pageable);

        return ApiResponse.ok(applications);
    }

    // ================= GET APPLICATIONS FOR A JOB (EMPLOYER) =================

    @GetMapping("/job/{jobId}")
    public ApiResponse<Page<ApplicationResponse>> getApplicationsForJob(
            @PathVariable Long jobId,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        String employerEmail = authentication.getName();

        Pageable pageable = PageRequest.of(page, size);

        Page<ApplicationResponse> applications =
                applicationService.getApplicationsForJob(jobId, employerEmail, pageable);

        return ApiResponse.ok(applications);
    }

    // ================= FILTER APPLICATIONS BY STATUS =================

    @GetMapping("/job/{jobId}/status")
    public ApiResponse<Page<ApplicationResponse>> getApplicationsForJobByStatus(
            @PathVariable Long jobId,
            @RequestParam Application.ApplicationStatus status,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        String employerEmail = authentication.getName();

        Pageable pageable = PageRequest.of(page, size);

        Page<ApplicationResponse> applications =
                applicationService.getApplicationsForJobByStatus(
                        jobId, status, employerEmail, pageable);

        return ApiResponse.ok(applications);
    }

    // ================= GET APPLICATION BY ID =================

    @GetMapping("/{applicationId}")
    public ApiResponse<ApplicationResponse> getApplicationById(
            @PathVariable Long applicationId,
            Authentication authentication
    ) {

        String email = authentication.getName();

        ApplicationResponse response =
                applicationService.getApplicationById(applicationId, email);

        return ApiResponse.ok(response);
    }

    // ================= UPDATE APPLICATION STATUS =================

    @PatchMapping("/{applicationId}/status")
    public ApiResponse<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {

        String employerEmail = authentication.getName();

        Application.ApplicationStatus status =
                Application.ApplicationStatus.valueOf(body.get("status").toUpperCase());

        String employerNotes = body.get("notes");

        ApplicationResponse response =
                applicationService.updateApplicationStatus(
                        applicationId, status, employerNotes, employerEmail);

        return ApiResponse.ok("Application status updated", response);
    }

    // ================= WITHDRAW APPLICATION =================

    @DeleteMapping("/{applicationId}")
    public ApiResponse<ApplicationResponse> withdrawApplication(
            @PathVariable Long applicationId,
            Authentication authentication
    ) {

        String email = authentication.getName();

        ApplicationResponse response =
                applicationService.withdrawApplication(applicationId, email);

        return ApiResponse.ok("Application withdrawn successfully", response);
    }
}