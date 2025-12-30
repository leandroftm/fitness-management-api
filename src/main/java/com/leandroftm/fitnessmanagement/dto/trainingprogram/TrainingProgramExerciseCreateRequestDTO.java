package com.leandroftm.fitnessmanagement.dto.trainingprogram;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TrainingProgramExerciseCreateRequestDTO(
        @NotNull
        Long exerciseId,
        @Min(1)
        int exerciseOrder
) {
}
