package com.healthapp.healthcare_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AllergyDto {
    private Long id;

    @NotBlank(message = "Allergy name is required")
    @Size(max = 100, message = "Allergy name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Allergy type is required")
    @Pattern(regexp = "Food|Drug|Environmental|Other",
            message = "Allergy type must be Food, Drug, Environmental or Other")
    private String type;

    @NotBlank(message = "Severity is required")
    @Pattern(regexp = "Mild|Moderate|Severe",
            message = "Severity must be Mild, Moderate or Severe")
    private String severity;

    @NotBlank(message = "Reaction description is required")
    @Size(max = 500, message = "Reaction description must be less than 500 characters")
    private String reaction;

    @PastOrPresent(message = "Noted date cannot be in the future")
    private LocalDate notedOn;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
}