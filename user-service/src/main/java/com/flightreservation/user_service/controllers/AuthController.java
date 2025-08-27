package com.flightreservation.user_service.controllers;

import com.flightreservation.user_service.DTO.auth.ChangePasswordRequest;
import com.flightreservation.user_service.DTO.auth.ForgotPasswordRequest;
import com.flightreservation.user_service.DTO.auth.LoginRequest;
import com.flightreservation.user_service.DTO.auth.LoginResponse;
import com.flightreservation.user_service.DTO.auth.ResetPasswordRequest;
import com.flightreservation.user_service.config.JwtUtil;
import com.flightreservation.user_service.entities.User;
import com.flightreservation.user_service.exception.ResourceNotFoundException;
import com.flightreservation.user_service.repositories.UserRepository;
import com.flightreservation.user_service.services.PasswordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final PasswordService passwordService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        log.info("Authentication successful for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + request.getEmail()));

        String token = jwtUtil.generateToken(user.getEmail());
        log.info("Token generated for email: {}", request.getEmail());

        return new LoginResponse(token, user.getRole().name(), user.getEmail());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        String token = passwordService.generatePasswordResetToken(request.getEmail());
        // Email gönderme işlemi burada eklenebilir
        return ResponseEntity.ok("Password reset link sent to your email: " + token);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        passwordService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password has been reset successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequest request,
                                                 @RequestParam String email) {
        passwordService.changePassword(email, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }
}
