package com.healthapp.healthcare_service.controller;

import com.healthapp.healthcare_service.dto.*;
import com.healthapp.healthcare_service.service.StaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {
    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity.ok(staffService.getAllPatients());
    }

    @GetMapping("/patients/search")
    public ResponseEntity<List<PatientDto>> searchPatients(@RequestParam String query) {
        return ResponseEntity.ok(staffService.searchPatients(query));
    }

    @GetMapping("/patients/{patientId}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long patientId) {
        return ResponseEntity.ok(staffService.getPatientById(patientId));
    }

    @GetMapping("/patients/{patientId}/vitals")
    public ResponseEntity<List<VitalDto>> getPatientVitals(@PathVariable Long patientId) {
        return ResponseEntity.ok(staffService.getPatientVitals(patientId));
    }

    @GetMapping("/patients/{patientId}/vitals/{type}")
    public ResponseEntity<List<VitalDto>> getPatientVitalsByType(
            @PathVariable Long patientId,
            @PathVariable String type) {
        return ResponseEntity.ok(staffService.getPatientVitalsByType(patientId, type));
    }

    /*@PostMapping("/patients/{patientId}/vitals")
    public ResponseEntity<VitalDto> addPatientVital(
            @PathVariable Long patientId,
            @RequestBody VitalDto vitalDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.addPatientVital(patientId, vitalDto, userDetails.getUsername()));
    }*/

    @GetMapping("/patients/{patientId}/allergies")
    public ResponseEntity<List<AllergyDto>> getPatientAllergies(@PathVariable Long patientId) {
        return ResponseEntity.ok(staffService.getPatientAllergies(patientId));
    }

    @PostMapping("/patients/{patientId}/allergies")
    public ResponseEntity<AllergyDto> addPatientAllergy(
            @PathVariable Long patientId,
            @RequestBody AllergyDto allergyDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.addPatientAllergy(patientId, allergyDto, userDetails.getUsername()));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDto>> getStaffAppointments(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.getStaffAppointments(userDetails.getUsername()));
    }

    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(staffService.getAppointmentById(appointmentId));
    }

    @PutMapping("/appointments/{appointmentId}")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @PathVariable Long appointmentId,
            @RequestBody AppointmentDto appointmentDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.updateAppointment(appointmentId, appointmentDto, userDetails.getUsername()));
    }

    @GetMapping("/patients/{patientId}/messages")
    public ResponseEntity<List<MessageDto>> getMessagesWithPatient(
            @PathVariable Long patientId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.getMessagesWithPatient(patientId, userDetails.getUsername()));
    }

    @PostMapping("/patients/{patientId}/messages")
    public ResponseEntity<MessageDto> sendMessageToPatient(
            @PathVariable Long patientId,
            @RequestBody MessageDto messageDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.sendMessageToPatient(patientId, messageDto, userDetails.getUsername()));
    }

    @GetMapping("/profile")
    public ResponseEntity<StaffDto> getStaffProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.getStaffProfile(userDetails.getUsername()));
    }

    @PutMapping("/profile")
    public ResponseEntity<StaffDto> updateStaffProfile(
            @RequestBody StaffDto staffDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(staffService.updateStaffProfile(staffDto, userDetails.getUsername()));
    }
}
