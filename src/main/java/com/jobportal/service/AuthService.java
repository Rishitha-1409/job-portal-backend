package com.jobportal.service;

import com.jobportal.dto.AuthResponse;
import com.jobportal.dto.LoginRequest;
import com.jobportal.dto.RegisterRequest;
import com.jobportal.entity.User;

public interface AuthService {

    /**
     * Register a new user and return a JWT token.
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticate a user with email + password and return a JWT token.
     */
    AuthResponse login(LoginRequest request);

    /**
     * Load the currently authenticated user by email.
     */
    User getCurrentUser(String email);
}
