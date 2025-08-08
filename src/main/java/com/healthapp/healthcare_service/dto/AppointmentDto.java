package com.healthapp.healthcare_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentDto {
    private Long id;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Staff ID is required")
    private Long staffId;

    @Future(message = "Appointment time must be in the future")
    private LocalDateTime time;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "Scheduled|Completed|Cancelled|No-Show",
            message = "Status must be Scheduled, Completed, Cancelled or No-Show")
    private String status;

    @Size(max = 1000, message = "Notes must be less than 1000 characters")
    private String notes;

    @Size(max = 1000, message = "Prescription must be less than 1000 characters")
    private String prescription;
}