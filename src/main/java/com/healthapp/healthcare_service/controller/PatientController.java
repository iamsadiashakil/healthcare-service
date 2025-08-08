package com.healthapp.healthcare_service.controller;

import com.healthapp.healthcare_service.dto.AllergyDto;
import com.healthapp.healthcare_service.dto.MessageDto;
import com.healthapp.healthcare_service.dto.StaffDto;
import com.healthapp.healthcare_service.dto.VitalDto;
import com.healthapp.healthcare_service.service.PatientService;
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

@Tag(name = "Patient", description = "Patient management endpoints")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(
            summary = "Get patient allergies",
            description = "Retrieve all allergy records for a specific patient",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the patient", required = true, example = "1")
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
    @GetMapping("/{patientId}/allergies")
    public ResponseEntity<List<AllergyDto>> getAllergiesByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getAllergiesByPatientId(patientId));
    }

    @Operation(
            summary = "Get patient vitals",
            description = "Retrieve all vital sign records for a specific patient",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the patient", required = true, example = "1")
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
    @GetMapping("/{patientId}/vitals")
    public ResponseEntity<List<VitalDto>> getVitalsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getVitalsByPatientId(patientId));
    }

    @Operation(
            summary = "Get patient vitals by type",
            description = "Retrieve vital sign records of a specific type for a patient",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the patient", required = true, example = "1"),
                    @Parameter(name = "type", description = "Type of vital signs to retrieve (e.g., 'blood_pressure', 'heart_rate')",
                            required = true, example = "blood_pressure")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved patient vitals by type",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VitalDto.class, type = "array"))),
            @ApiResponse(responseCode = "400", description = "Invalid vital type specified"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Patient not found or no vitals of specified type")
    })
    @GetMapping("/{patientId}/vitals/{type}")
    public ResponseEntity<List<VitalDto>> getVitalsByPatientIdAndType(
            @PathVariable Long patientId,
            @PathVariable String type) {
        return ResponseEntity.ok(patientService.getVitalsByPatientIdAndType(patientId, type));
    }

    @Operation(
            summary = "Get patient messages",
            description = "Retrieve all messages for a patient, optionally filtered by staff member",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the patient", required = true, example = "1"),
                    @Parameter(name = "staffId", description = "Optional ID of staff member to filter by",
                            in = ParameterIn.QUERY, example = "456")
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
    @GetMapping("/{patientId}/messages")
    public ResponseEntity<List<MessageDto>> getMessagesByPatientId(
            @PathVariable Long patientId,
            @RequestParam(required = false) Long staffId) {
        if (staffId != null) {
            return ResponseEntity.ok(patientService.getMessagesByPatientIdAndStaffId(patientId, staffId));
        }
        return ResponseEntity.ok(patientService.getMessagesByPatientId(patientId));
    }

    @Operation(
            summary = "Send message to patient",
            description = "Create and send a new message to a patient from the authenticated staff member",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the recipient patient", required = true, example = "1")
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
    @PostMapping("/{patientId}/messages")
    public ResponseEntity<MessageDto> sendMessage(
            @PathVariable Long patientId,
            @Valid @RequestBody MessageDto messageDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(patientService.sendMessage(patientId, messageDto, userDetails.getUsername()));
    }

    @Operation(
            summary = "Get staff associated with patient",
            description = "Retrieve all staff members who are associated with a specific patient",
            parameters = {
                    @Parameter(name = "patientId", description = "ID of the patient", required = true, example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved staff list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StaffDto.class, type = "array"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/{patientId}/staff")
    public ResponseEntity<List<StaffDto>> getStaffForPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getStaffForPatient(patientId));
    }
}