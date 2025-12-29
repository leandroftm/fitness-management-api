package com.leandroftm.fitnessmanagement.exception.domain;

public class ExerciseNotInTrainingProgramException extends DomainException {
    public ExerciseNotInTrainingProgramException(Long programId, Long exerciseId) {
        super("Exercise " + exerciseId + " is not part of training program " + programId);
    }
}
