package com.healthapp.healthcare_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class StaffDto {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$",
            message = "Phone number must be at least 10 digits")
    private String phone;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "Doctor|Nurse|Admin|Staff",
            message = "Role must be Doctor, Nurse, Admin or Staff")
    private String role;

    @PastOrPresent(message = "Join date cannot be in the future")
    private LocalDate joinDate;
}