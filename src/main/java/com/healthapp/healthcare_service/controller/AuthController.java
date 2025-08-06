package com.healthapp.healthcare_service.controller;

import com.healthapp.healthcare_service.dto.AuthRequest;
import com.healthapp.healthcare_service.dto.AuthResponse;
import com.healthapp.healthcare_service.entity.Patient;
import com.healthapp.healthcare_service.entity.Staff;
import com.healthapp.healthcare_service.repository.PatientRepository;
import com.healthapp.healthcare_service.repository.StaffRepository;
import com.healthapp.healthcare_service.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          PatientRepository patientRepository, StaffRepository staffRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.patientRepository = patientRepository;
        this.staffRepository = staffRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
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

    @PostMapping("/register/patient")
    public ResponseEntity<AuthResponse> registerPatient(@RequestBody AuthRequest authRequest) {
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

    @PostMapping("/register/staff")
    public ResponseEntity<AuthResponse> registerStaff(@RequestBody AuthRequest authRequest) {
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
}
