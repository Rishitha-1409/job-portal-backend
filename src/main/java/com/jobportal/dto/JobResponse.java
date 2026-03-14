package com.jobportal.dto;

import java.time.LocalDateTime;

public class JobResponse {

    private Long id;

    private String title;
    private String description;
    private String requirements;
    private String responsibilities;

    private String location;
    private boolean remote;

    private String jobType;
    private String experienceLevel;

    private String category;
    private String tags;
    private String skillsRequired;

    private Integer salaryMin;
    private Integer salaryMax;
    private String currency;
    private boolean salaryVisible;

    private LocalDateTime deadline;

    private String status;

    private long viewCount;
    private int applicationCount;

    private Long employerId;
    private String employerName;

    private String companyName;
    private String companyLogoUrl;
    private String companyLocation;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobResponse() {}

    // ---------- GETTERS ----------

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getRequirements() { return requirements; }
    public String getResponsibilities() { return responsibilities; }
    public String getLocation() { return location; }
    public boolean isRemote() { return remote; }
    public String getJobType() { return jobType; }
    public String getExperienceLevel() { return experienceLevel; }
    public String getCategory() { return category; }
    public String getTags() { return tags; }
    public String getSkillsRequired() { return skillsRequired; }
    public Integer getSalaryMin() { return salaryMin; }
    public Integer getSalaryMax() { return salaryMax; }
    public String getCurrency() { return currency; }
    public boolean isSalaryVisible() { return salaryVisible; }
    public LocalDateTime getDeadline() { return deadline; }
    public String getStatus() { return status; }
    public long getViewCount() { return viewCount; }
    public int getApplicationCount() { return applicationCount; }
    public Long getEmployerId() { return employerId; }
    public String getEmployerName() { return employerName; }
    public String getCompanyName() { return companyName; }
    public String getCompanyLogoUrl() { return companyLogoUrl; }
    public String getCompanyLocation() { return companyLocation; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ---------- SETTERS ----------

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }
    public void setLocation(String location) { this.location = location; }
    public void setRemote(boolean remote) { this.remote = remote; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }
    public void setCategory(String category) { this.category = category; }
    public void setTags(String tags) { this.tags = tags; }
    public void setSkillsRequired(String skillsRequired) { this.skillsRequired = skillsRequired; }
    public void setSalaryMin(Integer salaryMin) { this.salaryMin = salaryMin; }
    public void setSalaryMax(Integer salaryMax) { this.salaryMax = salaryMax; }
    public void setCurrency(String currency) { this.currency = currency; }
    public void setSalaryVisible(boolean salaryVisible) { this.salaryVisible = salaryVisible; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }
    public void setStatus(String status) { this.status = status; }
    public void setViewCount(long viewCount) { this.viewCount = viewCount; }
    public void setApplicationCount(int applicationCount) { this.applicationCount = applicationCount; }
    public void setEmployerId(Long employerId) { this.employerId = employerId; }
    public void setEmployerName(String employerName) { this.employerName = employerName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setCompanyLogoUrl(String companyLogoUrl) { this.companyLogoUrl = companyLogoUrl; }
    public void setCompanyLocation(String companyLocation) { this.companyLocation = companyLocation; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
