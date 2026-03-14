package com.jobportal.controller;

import com.jobportal.dto.ApiResponse;
import com.jobportal.dto.JobResponse;
import com.jobportal.dto.UserResponse;
import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final JobService jobService;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    @Autowired
    public AdminController(
            UserService userService,
            JobService jobService,
            UserRepository userRepository,
            JobRepository jobRepository,
            ApplicationRepository applicationRepository) {

        this.userService = userService;
        this.jobService = jobService;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    // ================= DASHBOARD =================

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard() {

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("jobSeekers", userRepository.countByRole(User.Role.JOB_SEEKER));
        stats.put("employers", userRepository.countByRole(User.Role.EMPLOYER));

        stats.put("totalJobs", jobRepository.count());
        stats.put("activeJobs", jobRepository.countByStatus(Job.JobStatus.ACTIVE));

        stats.put("totalApplications", applicationRepository.count());
        stats.put("pendingApplications",
                applicationRepository.countByStatus(Application.ApplicationStatus.PENDING));

        return ResponseEntity.ok(ApiResponse.ok("Dashboard data", stats));
    }

    // ================= USERS =================

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) User.Role role,
            @RequestParam(required = false) User.AccountStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<UserResponse> users =
                userService.getAllUsers(keyword, role, status, pageable);

        return ResponseEntity.ok(ApiResponse.ok(users));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {

        UserResponse user = userService.getUserById(id);

        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        User.AccountStatus status =
                User.AccountStatus.valueOf(body.get("status").toUpperCase());

        UserResponse updated =
                userService.updateAccountStatus(id, status);

        return ResponseEntity.ok(ApiResponse.ok("User status updated", updated));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok(ApiResponse.ok("User deleted successfully"));
    }

    // ================= JOBS =================

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<Page<JobResponse>>> getAllJobs(
            @RequestParam(required = false) Job.JobStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<JobResponse> jobs;

        if (status != null) {
            jobs = jobRepository
                    .findByStatus(status, pageable)
                    .map(job -> jobService.getJobById(job.getId()));
        } else {
            jobs = jobRepository
                    .findAll(pageable)
                    .map(job -> jobService.getJobById(job.getId()));
        }

        return ResponseEntity.ok(ApiResponse.ok(jobs));
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable Long id) {

        JobResponse job = jobService.getJobById(id);

        return ResponseEntity.ok(ApiResponse.ok(job));
    }

    @PatchMapping("/jobs/{id}/status")
    public ResponseEntity<ApiResponse<JobResponse>> updateJobStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        Job.JobStatus status =
                Job.JobStatus.valueOf(body.get("status").toUpperCase());

        JobResponse updated =
                jobService.updateJobStatus(id, status, null);

        return ResponseEntity.ok(ApiResponse.ok("Job status updated", updated));
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {

        jobService.deleteJob(id, null);

        return ResponseEntity.ok(ApiResponse.ok("Job deleted successfully"));
    }
}