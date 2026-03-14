package com.jobportal.service.impl;

import com.jobportal.dto.AuthResponse;
import com.jobportal.dto.LoginRequest;
import com.jobportal.dto.RegisterRequest;
import com.jobportal.entity.Profile;
import com.jobportal.entity.User;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.UserRepository;
import com.jobportal.service.AuthService;
import com.jobportal.util.JwtUtil;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // ================= REGISTER =================

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + req.getEmail());
        }

        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole());
        user.setAccountStatus(User.AccountStatus.ACTIVE);

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setCompanyName(req.getCompanyName());
        profile.setCompanyWebsite(req.getCompanyWebsite());
        profile.setIndustry(req.getIndustry());
        profile.setSkills(req.getSkills());
        profile.setCurrentTitle(req.getCurrentTitle());

        user.setProfile(profile);

        User saved = userRepository.save(user);

        System.out.println("Registered new user: " + saved.getEmail());

        String token = jwtUtil.generateToken(saved);

        return buildAuthResponse(saved, token);
    }

    // ================= LOGIN =================

    @Override
    public AuthResponse login(LoginRequest req) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()
                )
        );

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "email", req.getEmail()));

        String token = jwtUtil.generateToken(user);

        System.out.println("User logged in: " + user.getEmail());

        return buildAuthResponse(user, token);
    }

    // ================= CURRENT USER =================

    @Override
    public User getCurrentUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "email", email));
    }

    // ================= HELPER =================

    private AuthResponse buildAuthResponse(User user, String token) {

        AuthResponse response = new AuthResponse();

        response.setAccessToken(token);
        response.setTokenType("Bearer");
        response.setUserId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setExpiresIn(jwtUtil.getExpirationMs());

        return response;
    }
}