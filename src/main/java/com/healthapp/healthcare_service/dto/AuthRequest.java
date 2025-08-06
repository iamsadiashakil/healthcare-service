package com.healthapp.healthcare_service.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email; // Add email field
    private String password; // Add password field
}
