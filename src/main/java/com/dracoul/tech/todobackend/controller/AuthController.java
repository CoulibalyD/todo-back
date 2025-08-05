package com.dracoul.tech.todobackend.controller;

import com.dracoul.tech.todobackend.dto.AuthResponse;
import com.dracoul.tech.todobackend.dto.LoginRequest;
import com.dracoul.tech.todobackend.dto.RegisterRequest;
import com.dracoul.tech.todobackend.dto.UserDto;
import com.dracoul.tech.todobackend.entity.User;
import com.dracoul.tech.todobackend.repo.UserRepository;
import com.dracoul.tech.todobackend.security.AuthService;
import com.dracoul.tech.todobackend.security.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        log.info("POST /login - Attempting login for username: {}", request.getUsername());
        try {
            AuthResponse response = authService.login(request);
            log.info("Login successful for username: {}", request.getUsername());
            log.debug("Generated token for {}: {}", request.getUsername(), response.getToken());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            log.warn("Login failed for username: {} - Reason: {}", request.getUsername(), e.getMessage());
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        log.info("POST /register - Registering new user with username: {}", request.getUsername());
        log.debug("Registration details - Email: {}, Role: {}", request.getEmail(), request.getUsername());

        try {
            authService.register(request);
            log.info("User registered successfully: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "User registered successfully",
                            "username", request.getUsername(),
                            "timestamp", Instant.now()
                    ));
        } catch (DataIntegrityViolationException e) {
            log.error("Registration failed - Username/email already exists: {}", request.getUsername());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username/email already exists");
        } catch (Exception e) {
            log.error("Unexpected registration error for {}: {}", request.getUsername(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Registration failed");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        log.info("GET /me - Fetching current user details");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            log.warn("Unauthorized access attempt to /me endpoint");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        log.debug("Current authenticated username: {}", username);

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.error("User not found in database: {}", username);
                        return new UsernameNotFoundException("User not found");
                    });

            UserDto dto = new UserDto(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    List.of(user.getRole().name())
            );

            log.info("Successfully fetched user details for: {}", username);
            log.debug("User details: {}", dto);

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error fetching user details for {}: {}", username, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching user data");
        }
    }
}