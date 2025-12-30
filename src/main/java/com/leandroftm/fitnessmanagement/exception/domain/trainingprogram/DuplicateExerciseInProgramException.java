package com.leandroftm.fitnessmanagement.exception.domain.trainingprogram;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class DuplicateExerciseInProgramException extends DomainException {
    public DuplicateExerciseInProgramException(Long programId, Long exerciseId) {
        super("Exercise with id " + exerciseId + " already exists in the program with id " + programId);
    }
}
