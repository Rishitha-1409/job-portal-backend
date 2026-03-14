package com.jobportal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    private String resumeUrl;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String employerNotes;

    private boolean viewedByEmployer;

    private LocalDateTime interviewScheduledAt;

    private LocalDateTime appliedAt;

    private LocalDateTime updatedAt;

    public Application() {}

    public Long getId() {
        return id;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getEmployerNotes() {
        return employerNotes;
    }

    public void setEmployerNotes(String employerNotes) {
        this.employerNotes = employerNotes;
    }

    public boolean isViewedByEmployer() {
        return viewedByEmployer;
    }

    public void setViewedByEmployer(boolean viewedByEmployer) {
        this.viewedByEmployer = viewedByEmployer;
    }

    public LocalDateTime getInterviewScheduledAt() {
        return interviewScheduledAt;
    }

    public void setInterviewScheduledAt(LocalDateTime interviewScheduledAt) {
        this.interviewScheduledAt = interviewScheduledAt;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public enum ApplicationStatus {
        PENDING,
        REVIEWING,
        SHORTLISTED,
        INTERVIEW_SCHEDULED,
        ACCEPTED,
        REJECTED,
        WITHDRAWN
    }
}