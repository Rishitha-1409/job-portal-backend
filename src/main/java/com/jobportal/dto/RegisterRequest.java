package com.jobportal.dto;

import com.jobportal.entity.User.Role;

public class RegisterRequest {

    private String fullName;
    private String email;
    private String password;
    private Role role;

    private String companyName;
    private String companyWebsite;
    private String industry;

    private String skills;
    private String currentTitle;

    public RegisterRequest(){}

    public String getFullName(){ return fullName; }

    public void setFullName(String fullName){ this.fullName = fullName; }

    public String getEmail(){ return email; }

    public void setEmail(String email){ this.email = email; }

    public String getPassword(){ return password; }

    public void setPassword(String password){ this.password = password; }

    public Role getRole(){ return role; }

    public void setRole(Role role){ this.role = role; }

    public String getCompanyName(){ return companyName; }

    public void setCompanyName(String companyName){ this.companyName = companyName; }

    public String getCompanyWebsite(){ return companyWebsite; }

    public void setCompanyWebsite(String companyWebsite){ this.companyWebsite = companyWebsite; }

    public String getIndustry(){ return industry; }

    public void setIndustry(String industry){ this.industry = industry; }

    public String getSkills(){ return skills; }

    public void setSkills(String skills){ this.skills = skills; }

    public String getCurrentTitle(){ return currentTitle; }

    public void setCurrentTitle(String currentTitle){ this.currentTitle = currentTitle; }
}