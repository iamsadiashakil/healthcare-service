package com.healthapp.healthcare_service.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private Long id;

    private PatientDTO patient;

    private DoctorDTO doctor;

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "SCHEDULED|COMPLETED|CANCELLED", message = "Status must be SCHEDULED, COMPLETED, or CANCELLED")
    private String status;
}
