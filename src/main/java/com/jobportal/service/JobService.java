package com.jobportal.service;

import com.jobportal.dto.JobRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.entity.Job;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {

    JobResponse createJob(JobRequest request, String employerEmail);

    JobResponse getJobById(Long id);

    Page<JobResponse> getAllActiveJobs(Pageable pageable);

    Page<JobResponse> searchJobs(
            String keyword,
            String location,
            String category,
            Job.JobType jobType,
            Job.ExperienceLevel experienceLevel,
            Boolean remote,
            Pageable pageable
    );

    List<JobResponse> getMyJobs(String employerEmail);

    JobResponse updateJob(Long id, JobRequest request, String employerEmail);

    JobResponse updateJobStatus(Long id, Job.JobStatus status, String employerEmail);

    void deleteJob(Long id, String employerEmail);

    void incrementJobView(Long id);
    Page<JobResponse> getJobsByEmployer(String email, Pageable pageable);

}