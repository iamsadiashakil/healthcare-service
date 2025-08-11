package com.healthapp.healthcare_service.service;

import com.healthapp.healthcare_service.dto.*;
import com.healthapp.healthcare_service.entity.HealthcareProxy;
import com.healthapp.healthcare_service.entity.PasswordResetToken;
import com.healthapp.healthcare_service.entity.Patient;
import com.healthapp.healthcare_service.entity.Staff;
import com.healthapp.healthcare_service.exception.InvalidDataException;
import com.healthapp.healthcare_service.exception.ResourceNotFoundException;
import com.healthapp.healthcare_service.repository.HealthcareProxyRepository;
import com.healthapp.healthcare_service.repository.PasswordResetTokenRepository;
import com.healthapp.healthcare_service.repository.PatientRepository;
import com.healthapp.healthcare_service.repository.StaffRepository;
import com.healthapp.healthcare_service.util.JwtUtil;
import com.healthapp.healthcare_service.util.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final HealthcareProxyRepository healthcareProxyRepository;
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);

        // Determine user type
        String userType = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF")) ? "STAFF" : "PATIENT_PROXY";

        return new AuthResponse(jwt, userType);
    }

    public void registerPatientProxy(HealthcareProxyRegistrationDto registrationDto) {
        // Check if email exists
        if (healthcareProxyRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new InvalidDataException("email already registered");
        }
        Patient patient = patientRepository.findById(registrationDto.getPatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new InvalidDataException("Password and confirmation do not match");
        }

        HealthcareProxy healthcareProxy = HealthcareProxy.builder()
                .name(registrationDto.getName())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .phone(registrationDto.getPhone())
                .relationship(registrationDto.getRelationship())
                .patient(patient)
                .build();

        healthcareProxyRepository.save(healthcareProxy);
    }

    public void registerStaff(StaffRegistrationDto registrationDto) {
        // Check if email exists
        if (staffRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new InvalidDataException("email already registered");
        }

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new InvalidDataException("Password and confirmation do not match");
        }

       Staff staff = Staff.builder()
               .name(registrationDto.getName())
               .email(registrationDto.getEmail())
               .password(passwordEncoder.encode(registrationDto.getPassword()))
               .phone(registrationDto.getPhone())
               .role(registrationDto.getRole())
               .joinDate(registrationDto.getJoinDate())
               .build();

        staffRepository.save(staff);
    }

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
            throw new InvalidDataException("Password reset token has expired");
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
