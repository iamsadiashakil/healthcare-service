package com.healthapp.healthcare_service.controller;

import com.healthapp.healthcare_service.dto.HealthcareProxyDto;
import com.healthapp.healthcare_service.dto.MessageDto;
import com.healthapp.healthcare_service.dto.PatientDto;
import com.healthapp.healthcare_service.service.HealthcareProxyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "Healthcare Proxy", description = "Healthcare proxy management endpoints")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/proxies")
public class HealthcareProxyController {
    private final HealthcareProxyService proxyService;

    public HealthcareProxyController(HealthcareProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @Operation(
            summary = "Get proxy profile",
            description = "Retrieve the authenticated healthcare proxy's profile"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved proxy profile",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HealthcareProxyDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Proxy not found")
    })
    @GetMapping("/profile")
    public ResponseEntity<HealthcareProxyDto> getProxyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(proxyService.getProxyProfile(userDetails.getUsername()));
    }

    @Operation(
            summary = "Update proxy profile",
            description = "Update the authenticated healthcare proxy's profile"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated proxy profile",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HealthcareProxyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid profile data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Proxy not found")
    })
    @PutMapping("/profile")
    public ResponseEntity<HealthcareProxyDto> updateProxyProfile(
            @Valid @RequestBody HealthcareProxyDto proxyDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(proxyService.updateProxyProfile(proxyDto, userDetails.getUsername()));
    }

    @Operation(
            summary = "Get assigned patient",
            description = "Retrieve the patient assigned to the healthcare proxy"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved patient",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "No patient assigned")
    })
    @GetMapping("/patient")
    public ResponseEntity<PatientDto> getAssignedPatient(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(proxyService.getAssignedPatient(userDetails.getUsername()));
    }

    @Operation(
            summary = "Send message to staff",
            description = "Send a message to staff caring for the assigned patient",
            parameters = {
                    @Parameter(name = "staffId", description = "ID of the staff member", required = true, example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message successfully sent",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid message content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT"),
            @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Staff not found or no patient assigned")
    })
    @PostMapping("/patient/messages")
    public ResponseEntity<MessageDto> sendMessageToStaff(
            @RequestParam Long staffId,
            @Valid @RequestBody MessageDto messageDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(proxyService.sendMessageToStaff(staffId, messageDto, userDetails.getUsername()));
    }
}