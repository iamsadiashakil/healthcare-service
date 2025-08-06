package com.healthapp.healthcare_service.controller;

import com.healthapp.healthcare_service.dto.AllergyDto;
import com.healthapp.healthcare_service.dto.MessageDto;
import com.healthapp.healthcare_service.dto.StaffDto;
import com.healthapp.healthcare_service.dto.VitalDto;
import com.healthapp.healthcare_service.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/{patientId}/allergies")
    public ResponseEntity<List<AllergyDto>> getAllergiesByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getAllergiesByPatientId(patientId));
    }

    @GetMapping("/{patientId}/vitals")
    public ResponseEntity<List<VitalDto>> getVitalsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getVitalsByPatientId(patientId));
    }

    @GetMapping("/{patientId}/vitals/{type}")
    public ResponseEntity<List<VitalDto>> getVitalsByPatientIdAndType(
            @PathVariable Long patientId,
            @PathVariable String type) {
        return ResponseEntity.ok(patientService.getVitalsByPatientIdAndType(patientId, type));
    }

    @GetMapping("/{patientId}/messages")
    public ResponseEntity<List<MessageDto>> getMessagesByPatientId(
            @PathVariable Long patientId,
            @RequestParam(required = false) Long staffId) {
        if (staffId != null) {
            return ResponseEntity.ok(patientService.getMessagesByPatientIdAndStaffId(patientId, staffId));
        }
        return ResponseEntity.ok(patientService.getMessagesByPatientId(patientId));
    }

    @PostMapping("/{patientId}/messages")
    public ResponseEntity<MessageDto> sendMessage(
            @PathVariable Long patientId,
            @RequestBody MessageDto messageDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(patientService.sendMessage(patientId, messageDto, userDetails.getUsername()));
    }

    @GetMapping("/{patientId}/staff")
    public ResponseEntity<List<StaffDto>> getStaffForPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.getStaffForPatient(patientId));
    }
}