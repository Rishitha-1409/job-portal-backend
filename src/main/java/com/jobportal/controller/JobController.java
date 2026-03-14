package com.jobportal.controller;

import com.jobportal.dto.JobRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.entity.Job;
import com.jobportal.service.JobService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // ================= GET ALL JOBS =================

    @GetMapping
    public Page<JobResponse> getAllJobs(Pageable pageable) {
        return jobService.getAllActiveJobs(pageable);
    }

    // ================= GET JOB BY ID =================

    @GetMapping("/{id}")
    public JobResponse getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    // ================= CREATE JOB =================

    @PostMapping
    public JobResponse createJob(
            @RequestBody JobRequest request,
            Authentication authentication
    ) {

        String employerEmail = authentication.getName();

        return jobService.createJob(request, employerEmail);
    }
 // ================= EMPLOYER JOBS =================

    @GetMapping("/employer")
    public Page<JobResponse> getEmployerJobs(
            Authentication authentication,
            Pageable pageable
    ) {

        String employerEmail = authentication.getName();

        return jobService.getJobsByEmployer(employerEmail, pageable);
    }
    // ================= UPDATE JOB =================

    @PutMapping("/{id}")
    public JobResponse updateJob(
            @PathVariable Long id,
            @RequestBody JobRequest request,
            Authentication authentication
    ) {

        String employerEmail = authentication.getName();

        return jobService.updateJob(id, request, employerEmail);
    }

    // ================= DELETE JOB =================

    @DeleteMapping("/{id}")
    public void deleteJob(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String employerEmail = authentication.getName();

        jobService.deleteJob(id, employerEmail);
    }
}