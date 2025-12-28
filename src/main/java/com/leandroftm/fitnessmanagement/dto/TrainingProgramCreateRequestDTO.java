package com.leandroftm.fitnessmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TrainingProgramCreateRequestDTO(
        @NotBlank
        @Size(min = 2, max = 100)
        String name,
        @NotBlank
        @Size(min = 2, max = 500)
        String description
) {
}
