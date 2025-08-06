package com.healthapp.healthcare_service.dto;

import lombok.Data;

@Data
public class PatientDto {
    private Long id;
    private String name;
    private int age;
    private String sex;
    private String bloodGroup;
    private boolean isActive;
}
