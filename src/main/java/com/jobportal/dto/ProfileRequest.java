package com.jobportal.dto;

public class ProfileRequest {

    private String bio;
    private String phone;
    private String location;
    private String website;

    private String linkedInUrl;
    private String githubUrl;

    private String avatarUrl;
    private String resumeUrl;

    // Job seeker
    private String skills;
    private String currentTitle;
    private Integer yearsOfExperience;
    private String education;
    private String availability;
    private Integer expectedSalary;

    // Employer
    private String companyName;
    private String companySize;
    private String industry;
    private String companyWebsite;
    private String companyDescription;
    private String companyLogoUrl;
    private String companyLocation;

    public ProfileRequest() {}

    public String getBio() { return bio; }
    public String getPhone() { return phone; }
    public String getLocation() { return location; }
    public String getWebsite() { return website; }
    public String getLinkedInUrl() { return linkedInUrl; }
    public String getGithubUrl() { return githubUrl; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getResumeUrl() { return resumeUrl; }
    public String getSkills() { return skills; }
    public String getCurrentTitle() { return currentTitle; }
    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public String getEducation() { return education; }
    public String getAvailability() { return availability; }
    public Integer getExpectedSalary() { return expectedSalary; }

    public String getCompanyName() { return companyName; }
    public String getCompanySize() { return companySize; }
    public String getIndustry() { return industry; }
    public String getCompanyWebsite() { return companyWebsite; }
    public String getCompanyDescription() { return companyDescription; }
    public String getCompanyLogoUrl() { return companyLogoUrl; }
    public String getCompanyLocation() { return companyLocation; }

    // setters

    public void setBio(String bio) { this.bio = bio; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setLocation(String location) { this.location = location; }
    public void setWebsite(String website) { this.website = website; }
    public void setLinkedInUrl(String linkedInUrl) { this.linkedInUrl = linkedInUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
    public void setSkills(String skills) { this.skills = skills; }
    public void setCurrentTitle(String currentTitle) { this.currentTitle = currentTitle; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }
    public void setEducation(String education) { this.education = education; }
    public void setAvailability(String availability) { this.availability = availability; }
    public void setExpectedSalary(Integer expectedSalary) { this.expectedSalary = expectedSalary; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setCompanySize(String companySize) { this.companySize = companySize; }
    public void setIndustry(String industry) { this.industry = industry; }
    public void setCompanyWebsite(String companyWebsite) { this.companyWebsite = companyWebsite; }
    public void setCompanyDescription(String companyDescription) { this.companyDescription = companyDescription; }
    public void setCompanyLogoUrl(String companyLogoUrl) { this.companyLogoUrl = companyLogoUrl; }
    public void setCompanyLocation(String companyLocation) { this.companyLocation = companyLocation; }
}