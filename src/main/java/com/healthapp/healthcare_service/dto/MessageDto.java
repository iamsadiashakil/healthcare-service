package com.healthapp.healthcare_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDto {
    private Long id;

    @NotBlank(message = "Message text is required")
    @Size(max = 2000, message = "Message must be less than 2000 characters")
    private String text;

    @NotBlank(message = "sender type is required")
    @Pattern(regexp = "STAFF|PATIENT_PROXY",
            message = "Role must be PATIENT_PROXY or STAFF")
    private String senderType;

    @PastOrPresent(message = "Timestamp cannot be in the future")
    private LocalDateTime timestamp;

    @NotNull(message = "Staff ID is required")
    private Long staffId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Healthcare proxy ID is required")
    private Long healthcareProxyId;
}