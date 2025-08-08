package com.healthapp.healthcare_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VitalDto {
    private Long id;

    @NotBlank(message = "Reading is required")
    @Size(max = 50, message = "Reading must be less than 50 characters")
    private String reading;

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "BloodPressure|HeartRate|Temperature|OxygenLevel|Weight|Height",
            message = "Invalid vital type")
    private String type;

    @PastOrPresent(message = "Measurement time cannot be in the future")
    private LocalDateTime measuredAt;

    @Pattern(regexp = "Normal|High|Low|Critical",
            message = "Status must be Normal, High, Low or Critical")
    private String status;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
}