package com.leandroftm.fitnessmanagement.dto;

import com.leandroftm.fitnessmanagement.domain.entity.Exercise;
import com.leandroftm.fitnessmanagement.domain.enums.MuscleGroup;

public record ExerciseListDTO(
        Long id,
        String name,
        String videoUrl,
        MuscleGroup muscleGroup
        ) {
    public ExerciseListDTO(Exercise exercise) {
        this(
                exercise.getId(),
                exercise.getName(),
                exercise.getVideoUrl(),
                exercise.getMuscleGroup()
        );
    }
}
