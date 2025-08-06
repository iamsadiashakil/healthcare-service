package com.healthapp.healthcare_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StaffDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private LocalDate joinDate;
}