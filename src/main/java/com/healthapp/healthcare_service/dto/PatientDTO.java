package com.healthapp.healthcare_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class PatientDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
    private String phone;
}
