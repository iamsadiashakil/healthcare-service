package com.healthapp.healthcare_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AllergyDto {
    private Long id;
    private String name;
    private String type;
    private String severity;
    private String reaction;
    private LocalDate notedOn;
    private Long patientId;
}
