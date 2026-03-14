package com.jobportal.config;

import com.jobportal.repository.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Lazy
    private final JwtFilter jwtFilter;
    private final UserRepository userRepository;

    public SecurityConfig(@Lazy JwtFilter jwtFilter, UserRepository userRepository) {
        this.jwtFilter = jwtFilter;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)

            // Allow H2 console
            .headers(h -> h.frameOptions(
                    HeadersConfigurer.FrameOptionsConfig::sameOrigin))

            .authorizeHttpRequests(auth -> auth

                // ================= PUBLIC =================

                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                // Public job browsing
                .requestMatchers(HttpMethod.GET,
                        "/api/jobs",
                        "/api/jobs/{id}",
                        "/api/jobs/search")
                        .permitAll()

                // ================= ADMIN =================

                .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                // ================= EMPLOYER =================

                // Employer can see their own jobs
                .requestMatchers(HttpMethod.GET, "/api/jobs/employer")
                        .hasRole("EMPLOYER")

                // Create job
                .requestMatchers(HttpMethod.POST, "/api/jobs")
                        .hasRole("EMPLOYER")

                // Update job
                .requestMatchers(HttpMethod.PUT, "/api/jobs/**")
                        .hasRole("EMPLOYER")

                // Update job status
                .requestMatchers(HttpMethod.PATCH, "/api/jobs/**")
                        .hasRole("EMPLOYER")

                // Delete job
                .requestMatchers(HttpMethod.DELETE, "/api/jobs/**")
                        .hasRole("EMPLOYER")

                // View applicants for a job
                .requestMatchers(HttpMethod.GET, "/api/applications/job/**")
                        .hasRole("EMPLOYER")

                // Update application status
                .requestMatchers(HttpMethod.PATCH, "/api/applications/*/status")
                        .hasRole("EMPLOYER")

                // ================= JOB SEEKER =================

                // Apply for job
                .requestMatchers(HttpMethod.POST, "/api/applications/**")
                        .hasRole("JOB_SEEKER")

                // View own applications
                .requestMatchers("/api/applications/my")
                        .hasRole("JOB_SEEKER")

                // ================= AUTHENTICATED =================

                .anyRequest().authenticated()
            )

            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authenticationProvider(authenticationProvider())

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ================= USER DETAILS =================

    @Bean
    public UserDetailsService userDetailsService() {

        return email -> userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + email));
    }

    // ================= AUTH PROVIDER =================

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    // ================= AUTH MANAGER =================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    // ================= PASSWORD ENCODER =================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(12);
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors()
            .and()
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}