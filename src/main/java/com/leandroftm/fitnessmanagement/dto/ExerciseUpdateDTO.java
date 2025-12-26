package com.leandroftm.fitnessmanagement.dto;

import com.leandroftm.fitnessmanagement.domain.enums.MuscleGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ExerciseUpdateDTO(
        @NotBlank
        @Size(min = 2, max = 100)
        String name,
        @NotBlank
        @Size(max = 500)
        String description,
        @NotBlank
        @Size(max = 255)
        String videoUrl,
        @NotNull
        MuscleGroup muscleGroup
) {
}
