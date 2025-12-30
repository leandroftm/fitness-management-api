package com.leandroftm.fitnessmanagement.exception.domain.exercise;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class ExerciseNotInTrainingProgramException extends DomainException {
    public ExerciseNotInTrainingProgramException(Long programId, Long exerciseId) {
        super("Exercise " + exerciseId + " is not part of training program " + programId);
    }
}
