package com.healthapp.healthcare_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ForgotPasswordDto {
    @Email
    @NotBlank
    private String email;
}
