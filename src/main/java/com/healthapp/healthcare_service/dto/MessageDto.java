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

    private boolean isUserMessage;

    @PastOrPresent(message = "Timestamp cannot be in the future")
    private LocalDateTime timestamp;

    @NotNull(message = "Staff ID is required")
    private Long staffId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
}