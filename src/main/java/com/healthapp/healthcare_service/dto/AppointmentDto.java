package com.healthapp.healthcare_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDto {
    private Long id;
    private Long patientId;
    private Long staffId;
    private LocalDateTime time;
    private String status;
    private String notes;
    private String prescription;
}
