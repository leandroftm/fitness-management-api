package com.leandroftm.fitnessmanagement.dto.exercise;

import com.leandroftm.fitnessmanagement.domain.entity.Exercise;
import com.leandroftm.fitnessmanagement.domain.enums.ExerciseStatus;
import com.leandroftm.fitnessmanagement.domain.enums.MuscleGroup;

public record ExerciseDetailsDTO(Long id,
                                 String name,
                                 String videoUrl,
                                 MuscleGroup muscleGroup,
                                 ExerciseStatus status
) {
    public ExerciseDetailsDTO(Exercise exercise) {
        this(
                exercise.getId(),
                exercise.getName(),
                exercise.getVideoUrl(),
                exercise.getMuscleGroup(),
                exercise.getStatus()
        );
    }
}
