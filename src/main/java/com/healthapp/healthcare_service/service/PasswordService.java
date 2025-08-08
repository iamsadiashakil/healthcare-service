package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.dto.ForgotPasswordDto;
import com.healthapp.healthcare_service.dto.PasswordResetDto;
import com.healthapp.healthcare_service.entity.PasswordResetToken;
import com.healthapp.healthcare_service.exception.InvalidTokenException;
import com.healthapp.healthcare_service.exception.ResourceNotFoundException;
import com.healthapp.healthcare_service.repository.HealthcareProxyRepository;
import com.healthapp.healthcare_service.repository.PasswordResetTokenRepository;
import com.healthapp.healthcare_service.repository.StaffRepository;
import com.healthapp.healthcare_service.util.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final HealthcareProxyRepository healthcareProxyRepository;
    private final StaffRepository staffRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    @Transactional
    public void requestPasswordReset(ForgotPasswordDto request) {
        // Clean up expired tokens first
        tokenRepository.deleteAllExpiredSince(LocalDateTime.now());

        // Check if email exists in either patients or staff
        boolean emailExists = healthcareProxyRepository.findByEmail(request.getEmail()).isPresent() ||
                staffRepository.findByEmail(request.getEmail()).isPresent();

        if (!emailExists) {
            throw new ResourceNotFoundException("Email not found");
        }
        // Generate token
        String token = tokenGenerator.generateToken();

        // Create and save token
        PasswordResetToken resetToken = new PasswordResetToken(token, request.getEmail());
        tokenRepository.save(resetToken);

        // Send email
        emailService.sendPasswordResetEmail(request.getEmail(), token);
    }

    @Transactional
    public void resetPassword(PasswordResetDto resetDto) {
        // Validate passwords match
        if (!resetDto.getNewPassword().equals(resetDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords don't match");
        }

        // Find token
        PasswordResetToken token = tokenRepository.findByToken(resetDto.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid password reset token"));

        // Check if expired
        if (token.isExpired()) {
            tokenRepository.delete(token);
            throw new InvalidTokenException("Password reset token has expired");
        }

        // Find user by email (check both patients and staff)
        healthcareProxyRepository.findByEmail(token.getUserEmail())
                .ifPresent(patient -> {
                    patient.setPassword(passwordEncoder.encode(resetDto.getNewPassword()));
                    healthcareProxyRepository.save(patient);
                    tokenRepository.delete(token);
                });

        staffRepository.findByEmail(token.getUserEmail())
                .ifPresent(staff -> {
                    staff.setPassword(passwordEncoder.encode(resetDto.getNewPassword()));
                    staffRepository.save(staff);
                    tokenRepository.delete(token);
                });
    }
}
