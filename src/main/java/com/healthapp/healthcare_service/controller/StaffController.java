package com.healthapp.healthcare_service.controller;

import com.healthapp.healthcare_service.dto.*;
import com.healthapp.healthcare_service.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Staff", description = "Staff management endpoints")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/staff")
public class StaffController {
    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @Operation(
            summary = "Get all patients",
            description = "Retrieve a list of all patients in the system",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved patient list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    @GetMapping("/patients")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity.ok(staffService.getAllPatients());
    }

    @Operation(
            summary = "Search patients",
            description = "Search patients by name, email, or other identifiable information",
            parameters = {
                    @Parameter(name = "query", description = "Search term", required = true,
                            example = "john", in = ParameterIn.QUERY)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved matching patients",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class, type = "array"))),
            @ApiResponse(responseCode = "400", description = "Invalid search query"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    @GetMapping("/patients/search")
    public ResponseEntity<List<PatientDto>> searchPatients(@RequestParam String query) {
        return ResponseEntity.ok(staffService.searchPatients(query));
    }

    @Operation(
            summary = "Get patient by ID",
            description = "Retrieve detailed information about a specific patient",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the patient to retrieve",
                            required = true, example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved patient details",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patients/{patientId}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long patientId) {
        return ResponseEntity.ok(staffService.getPatientById(patientId));
    }

    @Operation(
            summary = "Get patient vitals",
            description = "Retrieve all vital sign records for a specific patient",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the patient",
                            required = true, example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved patient vitals",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VitalDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patients/{patientId}/vitals")
    public ResponseEntity<List<VitalDto>> getPatientVitals(@PathVariable Long patientId) {
        return ResponseEntity.ok(staffService.getPatientVitals(patientId));
    }

    @Operation(
            summary = "Get patient vitals by type",
            description = "Retrieve vital sign records filtered by type (e.g., blood_pressure, heart_rate)",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the patient",
                            required = true, example = "1"),
                    @Parameter(name = "type", description = "Type of vital signs to retrieve",
                            required = true, example = "blood_pressure")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered vitals",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VitalDto.class, type = "array"))),
            @ApiResponse(responseCode = "400", description = "Invalid vital type specified"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Patient not found or no vitals of specified type")
    })
    @GetMapping("/patients/{patientId}/vitals/{type}")
    public ResponseEntity<List<VitalDto>> getPatientVitalsByType(
            @PathVariable Long patientId,
            @PathVariable String type) {
        return ResponseEntity.ok(staffService.getPatientVitalsByType(patientId, type));
    }

    @Operation(
            summary = "Get patient allergies",
            description = "Retrieve all allergy records for a specific patient",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the patient",
                            required = true, example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved patient allergies",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AllergyDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patients/{patientId}/allergies")
    public ResponseEntity<List<AllergyDto>> getPatientAllergies(@PathVariable Long patientId) {
        return ResponseEntity.ok(staffService.getPatientAllergies(patientId));
    }

    @Operation(
            summary = "Add patient allergy",
            description = "Add a new allergy record for a patient",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the patient",
                            required = true, example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added allergy record",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AllergyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid allergy data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PostMapping("/patients/{patientId}/allergies")
    public ResponseEntity<AllergyDto> addPatientAllergy(
            @PathVariable Long patientId,
            @Valid @RequestBody AllergyDto allergyDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.addPatientAllergy(patientId, allergyDto, userDetails.getUsername()));
    }

    @Operation(
            summary = "Get staff appointments",
            description = "Retrieve all appointments for the authenticated staff member"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved appointments",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDto>> getStaffAppointments(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.getStaffAppointments(userDetails.getUsername()));
    }

    @Operation(
            summary = "Get appointment by ID",
            description = "Retrieve details of a specific appointment",
            parameters = {
                    @Parameter(name = "appointmentId", description = "ID of the appointment",
                            required = true, example = "456")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved appointment",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(staffService.getAppointmentById(appointmentId));
    }

    @Operation(
            summary = "Update appointment",
            description = "Update details of an existing appointment",
            parameters = {
                    @Parameter(name = "appointmentId", description = "ID of the appointment to update",
                            required = true, example = "456")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated appointment",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid appointment data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PutMapping("/appointments/{appointmentId}")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody AppointmentDto appointmentDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.updateAppointment(appointmentId, appointmentDto, userDetails.getUsername()));
    }

    @Operation(
            summary = "Get messages with patient",
            description = "Retrieve message history between staff member and patient",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the patient",
                            required = true, example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved messages",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/patients/{patientId}/messages")
    public ResponseEntity<List<MessageDto>> getMessagesWithPatient(
            @PathVariable Long patientId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.getMessagesWithPatient(patientId, userDetails.getUsername()));
    }

    @Operation(
            summary = "Send message to patient",
            description = "Send a new message to a patient from the authenticated staff member",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the recipient patient",
                            required = true, example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message successfully sent",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid message content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PostMapping("/patients/{patientId}/messages")
    public ResponseEntity<MessageDto> sendMessageToPatient(
            @PathVariable Long patientId,
            @Valid @RequestBody MessageDto messageDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.sendMessageToPatient(patientId, messageDto, userDetails.getUsername()));
    }

    @Operation(
            summary = "Get staff profile",
            description = "Retrieve profile information of the authenticated staff member"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved staff profile",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StaffDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Staff profile not found")
    })
    @GetMapping("/profile")
    public ResponseEntity<StaffDto> getStaffProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.getStaffProfile(userDetails.getUsername()));
    }

    @Operation(
            summary = "Update staff profile",
            description = "Update profile information of the authenticated staff member"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated staff profile",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StaffDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid profile data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Staff profile not found")
    })
    @PutMapping("/profile")
    public ResponseEntity<StaffDto> updateStaffProfile(
            @Valid @RequestBody StaffDto staffDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.updateStaffProfile(staffDto, userDetails.getUsername()));
    }

    @Operation(
            summary = "Get patient's proxies by staff id",
            description = "Retrieve all patient's healthcare proxies by a staff member"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved patient's proxy list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HealthcareProxyDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Staff not found")
    })
    @GetMapping("/{staffId}/assigned-patients")
    public ResponseEntity<List<HealthcareProxyDto>> getAssignedPatientsProxies(
            @PathVariable Long staffId) {
        return ResponseEntity.ok(staffService.getAssignedPatientsProxies(staffId));
    }
}