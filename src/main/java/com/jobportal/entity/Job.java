package com.jobportal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    @Column(length = 2000)
    private String requirements;

    @Column(length = 2000)
    private String responsibilities;

    private String location;

    private boolean remote;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    private String category;

    private String tags;

    private String skillsRequired;

    private Integer salaryMin;

    private Integer salaryMax;

    private String currency;

    private boolean salaryVisible;

    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private long viewCount;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private User postedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Job() {}

    // ---------- GETTERS ----------

    public Long getId() { return id; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getRequirements() { return requirements; }

    public String getResponsibilities() { return responsibilities; }

    public String getLocation() { return location; }

    public boolean isRemote() { return remote; }

    public JobType getJobType() { return jobType; }

    public ExperienceLevel getExperienceLevel() { return experienceLevel; }

    public String getCategory() { return category; }

    public String getTags() { return tags; }

    public String getSkillsRequired() { return skillsRequired; }

    public Integer getSalaryMin() { return salaryMin; }

    public Integer getSalaryMax() { return salaryMax; }

    public String getCurrency() { return currency; }

    public boolean isSalaryVisible() { return salaryVisible; }

    public LocalDateTime getDeadline() { return deadline; }

    public JobStatus getStatus() { return status; }

    public long getViewCount() { return viewCount; }

    public User getPostedBy() { return postedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ---------- SETTERS ----------

    public void setTitle(String title) { this.title = title; }

    public void setDescription(String description) { this.description = description; }

    public void setRequirements(String requirements) { this.requirements = requirements; }

    public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }

    public void setLocation(String location) { this.location = location; }

    public void setRemote(boolean remote) { this.remote = remote; }

    public void setJobType(JobType jobType) { this.jobType = jobType; }

    public void setExperienceLevel(ExperienceLevel experienceLevel) { this.experienceLevel = experienceLevel; }

    public void setCategory(String category) { this.category = category; }

    public void setTags(String tags) { this.tags = tags; }

    public void setSkillsRequired(String skillsRequired) { this.skillsRequired = skillsRequired; }

    public void setSalaryMin(Integer salaryMin) { this.salaryMin = salaryMin; }

    public void setSalaryMax(Integer salaryMax) { this.salaryMax = salaryMax; }

    public void setCurrency(String currency) { this.currency = currency; }

    public void setSalaryVisible(boolean salaryVisible) { this.salaryVisible = salaryVisible; }

    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public void setStatus(JobStatus status) { this.status = status; }

    public void setViewCount(long viewCount) { this.viewCount = viewCount; }

    public void setPostedBy(User postedBy) { this.postedBy = postedBy; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ---------- ENUMS ----------

    public enum JobStatus {
        ACTIVE,
        CLOSED,
        DRAFT
    }

    public enum JobType {
        FULL_TIME,
        PART_TIME,
        CONTRACT,
        INTERNSHIP
    }

    public enum ExperienceLevel {
        ENTRY_LEVEL,
        MID_LEVEL,
        SENIOR_LEVEL
    }
}