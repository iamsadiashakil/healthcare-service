package com.healthapp.healthcare_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {
    private Long id;
    private String text;
    private boolean isUserMessage;
    private LocalDateTime timestamp;
    private Long staffId;
    private Long patientId;
}
