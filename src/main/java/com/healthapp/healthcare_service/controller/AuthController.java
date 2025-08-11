package com.healthapp.healthcare_service.controller;

import com.healthapp.healthcare_service.dto.*;
import com.healthapp.healthcare_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Authentication, user registration and password reset endpoints")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "User login",
            description = "Authenticate user and return JWT token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRequest.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request format"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - invalid credentials"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.login(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(
            summary = "Register new patient's healthcare proxy",
            description = "Create a new patient's healthcare proxy account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Patient's healthcare proxy registration details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRequest.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registration successful and JWT token returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - email already exists or invalid data"
            )
    })
    @PostMapping("/register/healthcare-proxy")
    public ResponseEntity<Void> registerPatientProxy(@Valid @RequestBody HealthcareProxyRegistrationDto registrationDto) {
        authService.registerPatientProxy(registrationDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Register new staff member",
            description = "Create a new staff account (admin privileges required)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Staff registration details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRequest.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registration successful and JWT token returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - email already exists or invalid data"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - admin privileges required"
            )
    })
    @PostMapping("/register/staff")
    public ResponseEntity<Void> registerStaff(@Valid @RequestBody StaffRegistrationDto registrationDto) {
        authService.registerStaff(registrationDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Request forgot password",
            description = "Initiate a forgot password process by providing an email address. " +
                    "If the email exists in the system, a reset link will be sent.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Email address for forgot password request",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ForgotPasswordDto.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Forgot password request processed (regardless of whether email exists)"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid email format"
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests - rate limiting applied"
            )
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordDto request) {
        authService.requestPasswordReset(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Confirm password reset",
            description = "Complete the password reset process by providing the token and new password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Password reset confirmation details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PasswordResetDto.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password successfully reset"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request - passwords don't match or invalid token or Token is expired"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Token not found"
            )
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetDto resetDto) {
        authService.resetPassword(resetDto);
        return ResponseEntity.ok().build();
    }
}