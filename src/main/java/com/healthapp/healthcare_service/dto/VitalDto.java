package com.healthapp.healthcare_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VitalDto {
    private Long id;
    private String reading;
    private String type;
    private LocalDateTime measuredAt;
    private String status;
    private Long patientId;
}
