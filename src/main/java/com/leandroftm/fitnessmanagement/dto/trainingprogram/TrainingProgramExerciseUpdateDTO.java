package com.leandroftm.fitnessmanagement.dto.trainingprogram;

import jakarta.validation.constraints.Min;

public record TrainingProgramExerciseUpdateDTO(
        @Min(1)
        int exerciseOrder
) {
}
