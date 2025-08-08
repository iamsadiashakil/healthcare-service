package com.healthapp.healthcare_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class HealthcareProxyDto {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[+]?[0-9]{7,15}$",
            message = "Invalid phone number"
    )
    private String phone;

    @NotBlank(message = "Relationship is required")
    @Pattern(regexp = "Spouse|Child|Sibling|Parent",
            message = "Role must be Spouse, Child, Sibling or Parent")
    private String relationship;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
}
