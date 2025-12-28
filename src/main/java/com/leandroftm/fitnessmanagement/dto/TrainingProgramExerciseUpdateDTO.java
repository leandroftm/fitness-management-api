package com.leandroftm.fitnessmanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TrainingProgramExerciseUpdateDTO(
        @Min(1)
        int exerciseOrder
) {
}
