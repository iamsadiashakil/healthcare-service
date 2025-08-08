package com.healthapp.healthcare_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PatientDto {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be positive")
    @Max(value = 120, message = "Age must be reasonable")
    private Integer age;

    @NotBlank(message = "Sex is required")
    @Pattern(regexp = "Male|Female|Other", message = "Sex must be Male, Female, or Other")
    private String sex;

    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Invalid blood group format")
    private String bloodGroup;

    private boolean isActive;
}