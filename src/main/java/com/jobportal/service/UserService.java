package com.jobportal.service;

import com.jobportal.dto.ProfileRequest;
import com.jobportal.dto.UserResponse;
import com.jobportal.entity.Profile;
import com.jobportal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponse getUserById(Long id);

    UserResponse getMyProfile(String email);

    Profile updateProfile(String email, ProfileRequest request);

    Profile getOrCreateProfile(User user);

    // Admin operations
    Page<UserResponse> getAllUsers(String keyword, User.Role role, User.AccountStatus status, Pageable pageable);

    UserResponse updateAccountStatus(Long userId, User.AccountStatus status);

    void deleteUser(Long userId);
}
