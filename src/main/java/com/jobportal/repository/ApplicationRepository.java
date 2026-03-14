package com.jobportal.repository;

import com.jobportal.entity.Application;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // ── Finder Methods ────────────────────────────────────────────────────────

    Page<Application> findByApplicant(User applicant, Pageable pageable);

    Page<Application> findByJob(Job job, Pageable pageable);

    Page<Application> findByJobAndStatus(Job job, Application.ApplicationStatus status, Pageable pageable);

    Optional<Application> findByApplicantAndJob(User applicant, Job job);

    boolean existsByApplicantAndJob(User applicant, Job job);

    // ── Stats ─────────────────────────────────────────────────────────────────

    long countByApplicant(User applicant);

    long countByJob(Job job);

    long countByStatus(Application.ApplicationStatus status);

    @Query("""
        SELECT COUNT(a) FROM Application a
        WHERE a.job.postedBy = :employer
    """)
    long countByEmployer(@Param("employer") User employer);

    @Query("""
        SELECT a.status, COUNT(a) FROM Application a
        WHERE a.job = :job
        GROUP BY a.status
    """)
    java.util.List<Object[]> countByStatusForJob(@Param("job") Job job);
}
