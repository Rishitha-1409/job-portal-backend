package com.jobportal.service.impl;

import com.jobportal.dto.ProfileRequest;
import com.jobportal.dto.UserResponse;
import com.jobportal.entity.Profile;
import com.jobportal.entity.User;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ================= READ =================

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", id));

        return toUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "email", email));

        return toUserResponse(user);
    }

    // ================= UPDATE PROFILE =================

    @Override
    @Transactional
    public Profile updateProfile(String email, ProfileRequest req) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "email", email));

        Profile profile = getOrCreateProfile(user);

        if (req.getBio() != null) profile.setBio(req.getBio());
        if (req.getPhone() != null) profile.setPhone(req.getPhone());
        if (req.getLocation() != null) profile.setLocation(req.getLocation());
        if (req.getWebsite() != null) profile.setWebsite(req.getWebsite());
        if (req.getLinkedInUrl() != null) profile.setLinkedInUrl(req.getLinkedInUrl());
        if (req.getGithubUrl() != null) profile.setGithubUrl(req.getGithubUrl());
        if (req.getAvatarUrl() != null) profile.setAvatarUrl(req.getAvatarUrl());

        // Job Seeker fields
        if (req.getSkills() != null) profile.setSkills(req.getSkills());
        if (req.getResumeUrl() != null) profile.setResumeUrl(req.getResumeUrl());
        if (req.getCurrentTitle() != null) profile.setCurrentTitle(req.getCurrentTitle());
        if (req.getYearsOfExperience() != null) profile.setYearsOfExperience(req.getYearsOfExperience());
        if (req.getEducation() != null) profile.setEducation(req.getEducation());
        if (req.getAvailability() != null) profile.setAvailability(req.getAvailability());
        if (req.getExpectedSalary() != null) profile.setExpectedSalary(req.getExpectedSalary());

        // Employer fields
        if (req.getCompanyName() != null) profile.setCompanyName(req.getCompanyName());
        if (req.getCompanySize() != null) profile.setCompanySize(req.getCompanySize());
        if (req.getIndustry() != null) profile.setIndustry(req.getIndustry());
        if (req.getCompanyWebsite() != null) profile.setCompanyWebsite(req.getCompanyWebsite());
        if (req.getCompanyDescription() != null) profile.setCompanyDescription(req.getCompanyDescription());
        if (req.getCompanyLogoUrl() != null) profile.setCompanyLogoUrl(req.getCompanyLogoUrl());
        if (req.getCompanyLocation() != null) profile.setCompanyLocation(req.getCompanyLocation());

        user.setProfile(profile);

        userRepository.save(user);

        System.out.println("Profile updated for user: " + email);

        return profile;
    }

    @Override
    public Profile getOrCreateProfile(User user) {

        if (user.getProfile() != null) {
            return user.getProfile();
        }

        Profile profile = new Profile();
        profile.setUser(user);

        return profile;
    }

    // ================= ADMIN =================

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(
            String keyword,
            User.Role role,
            User.AccountStatus status,
            Pageable pageable
    ) {

        return userRepository
                .searchUsers(keyword, role, status, pageable)
                .map(this::toUserResponse);
    }

    @Override
    @Transactional
    public UserResponse updateAccountStatus(Long userId, User.AccountStatus status) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", userId));

        user.setAccountStatus(status);

        User updated = userRepository.save(user);

        System.out.println("Account status updated for user: " + userId);

        return toUserResponse(updated);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", userId));

        userRepository.delete(user);

        System.out.println("User deleted: " + userId);
    }

    // ================= DTO MAPPER =================

    private UserResponse toUserResponse(User user) {

        Profile profile = user.getProfile();

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setAccountStatus(user.getAccountStatus());

        if (profile != null) {
            response.setProfileId(profile.getId());
            response.setAvatarUrl(profile.getAvatarUrl());
            response.setCompanyName(profile.getCompanyName());
        }

        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}