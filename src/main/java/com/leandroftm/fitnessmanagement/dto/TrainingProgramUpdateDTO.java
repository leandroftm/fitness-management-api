package com.leandroftm.fitnessmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TrainingProgramUpdateDTO(
        @NotBlank
        @Size(min = 2, max = 100)
        String name,
        @NotBlank
        @Size(min = 2, max = 500)
        String description
) {
}
