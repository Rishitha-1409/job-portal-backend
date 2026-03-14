package com.jobportal.dto;

import com.jobportal.entity.User.Role;
import com.jobportal.entity.User.AccountStatus;
import java.time.LocalDateTime;

public class UserResponse {

    private Long id;
    private String fullName;
    private String email;

    private Role role;
    private AccountStatus accountStatus;

    private Long profileId;

    private String avatarUrl;
    private String companyName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponse(){}

    public Long getId(){ return id; }

    public void setId(Long id){ this.id = id; }

    public String getFullName(){ return fullName; }

    public void setFullName(String fullName){ this.fullName = fullName; }

    public String getEmail(){ return email; }

    public void setEmail(String email){ this.email = email; }

    public Role getRole(){ return role; }

    public void setRole(Role role){ this.role = role; }

    public AccountStatus getAccountStatus(){ return accountStatus; }

    public void setAccountStatus(AccountStatus accountStatus){ this.accountStatus = accountStatus; }

    public Long getProfileId(){ return profileId; }

    public void setProfileId(Long profileId){ this.profileId = profileId; }

    public String getAvatarUrl(){ return avatarUrl; }

    public void setAvatarUrl(String avatarUrl){ this.avatarUrl = avatarUrl; }

    public String getCompanyName(){ return companyName; }

    public void setCompanyName(String companyName){ this.companyName = companyName; }

    public LocalDateTime getCreatedAt(){ return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt(){ return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt){ this.updatedAt = updatedAt; }
}