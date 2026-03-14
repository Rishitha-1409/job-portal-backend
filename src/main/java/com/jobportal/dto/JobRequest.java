package com.jobportal.dto;

import com.jobportal.entity.Job;
import java.time.LocalDateTime;

public class JobRequest {

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

    // Optional: job status
    private Job.JobStatus status;

    public JobRequest() {}

    // ----------- GETTERS -----------

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public String getLocation() {
        return location;
    }

    public boolean isRemote() {
        return remote;
    }

    public String getJobType() {
        return jobType;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public String getCategory() {
        return category;
    }

    public String getTags() {
        return tags;
    }

    public String getSkillsRequired() {
        return skillsRequired;
    }

    public Integer getSalaryMin() {
        return salaryMin;
    }

    public Integer getSalaryMax() {
        return salaryMax;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isSalaryVisible() {
        return salaryVisible;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Job.JobStatus getStatus() {
        return status;
    }

    // ----------- SETTERS -----------

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setSkillsRequired(String skillsRequired) {
        this.skillsRequired = skillsRequired;
    }

    public void setSalaryMin(Integer salaryMin) {
        this.salaryMin = salaryMin;
    }

    public void setSalaryMax(Integer salaryMax) {
        this.salaryMax = salaryMax;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setSalaryVisible(boolean salaryVisible) {
        this.salaryVisible = salaryVisible;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setStatus(Job.JobStatus status) {
        this.status = status;
    }
}