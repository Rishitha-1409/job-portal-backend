package com.jobportal.repository;

import com.jobportal.entity.Job;
import com.jobportal.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByStatus(Job.JobStatus status, Pageable pageable);

    long countByStatus(Job.JobStatus status);

    List<Job> findByPostedBy(User employer);

    Page<Job> findByCategory(String category, Pageable pageable);

    Page<Job> findByLocationContainingIgnoreCase(String location, Pageable pageable);

    @Query("""
    SELECT j FROM Job j
    WHERE (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
    AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
    AND (:category IS NULL OR j.category = :category)
    AND (:jobType IS NULL OR j.jobType = :jobType)
    AND (:experienceLevel IS NULL OR j.experienceLevel = :experienceLevel)
    AND (:remote IS NULL OR j.remote = :remote)
    """)
    Page<Job> searchJobs(
            String keyword,
            String location,
            String category,
            Job.JobType jobType,
            Job.ExperienceLevel experienceLevel,
            Boolean remote,
            Pageable pageable
    );
    Page<Job> findByPostedBy(User employer, Pageable pageable);
}

