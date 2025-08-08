package com.healthapp.healthcare_service.controller;

import com.healthapp.healthcare_service.dto.AuthRequest;
import com.healthapp.healthcare_service.dto.AuthResponse;
import com.healthapp.healthcare_service.dto.PasswordResetDto;
import com.healthapp.healthcare_service.dto.ForgotPasswordDto;
import com.healthapp.healthcare_service.entity.Patient;
import com.healthapp.healthcare_service.entity.Staff;
import com.healthapp.healthcare_service.repository.PatientRepository;
import com.healthapp.healthcare_service.repository.StaffRepository;
import com.healthapp.healthcare_service.service.PasswordService;
import com.healthapp.healthcare_service.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Authentication, user registration and password reset endpoints")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;
    private final PasswordService passwordService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          PatientRepository patientRepository, StaffRepository staffRepository, PasswordService passwordService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.patientRepository = patientRepository;
        this.staffRepository = staffRepository;
        this.passwordService = passwordService;
    }

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
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
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
                .anyMatch(a -> a.getAuthority().equals("ROLE_STAFF")) ? "STAFF" : "PATIENT";

        return ResponseEntity.ok(new AuthResponse(jwt, userType));
    }

    @Operation(
            summary = "Register new patient",
            description = "Create a new patient account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Patient registration details",
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
    @PostMapping("/register/patient")
    public ResponseEntity<AuthResponse> registerPatient(@Valid @RequestBody AuthRequest authRequest) {
        // Check if email exists
        if (patientRepository.findByEmail(authRequest.getEmail()).isPresent() ||
                staffRepository.findByEmail(authRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Patient patient = new Patient();
        patient.setEmail(authRequest.getEmail());
        patient.setPassword(authRequest.getPassword()); // Password will be encoded by service layer
        // Set other patient fields...

        patientRepository.save(patient);

        return login(authRequest);
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
    public ResponseEntity<AuthResponse> registerStaff(@Valid @RequestBody AuthRequest authRequest) {
        // Check if email exists
        if (patientRepository.findByEmail(authRequest.getEmail()).isPresent() ||
                staffRepository.findByEmail(authRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Staff staff = new Staff();
        staff.setEmail(authRequest.getEmail());
        staff.setPassword(authRequest.getPassword()); // Password will be encoded by service layer
        // Set other staff fields...

        staffRepository.save(staff);

        return login(authRequest);
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
        passwordService.requestPasswordReset(request);
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
        passwordService.resetPassword(resetDto);
        return ResponseEntity.ok().build();
    }
}