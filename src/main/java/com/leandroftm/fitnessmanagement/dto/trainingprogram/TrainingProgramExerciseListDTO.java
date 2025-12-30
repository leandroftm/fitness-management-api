package com.leandroftm.fitnessmanagement.dto.trainingprogram;

import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgramExercise;

public record TrainingProgramExerciseListDTO(
        Long id,
        Long exerciseId,
        String exerciseName,
        String videoUrl,
        int exerciseOrder
) {
    public TrainingProgramExerciseListDTO(TrainingProgramExercise trainingProgramExercise) {
        this(
                trainingProgramExercise.getId(),
                trainingProgramExercise.getExercise().getId(),
                trainingProgramExercise.getExercise().getName(),
                trainingProgramExercise.getExercise().getVideoUrl(),
                trainingProgramExercise.getExerciseOrder()
        );
    }
}
