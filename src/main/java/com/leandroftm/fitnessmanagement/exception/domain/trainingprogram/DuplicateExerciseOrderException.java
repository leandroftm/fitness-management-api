package com.leandroftm.fitnessmanagement.exception.domain.trainingprogram;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class DuplicateExerciseOrderException extends DomainException {
    public DuplicateExerciseOrderException(Long programId, int exerciseOrder) {
        super("The program id " + programId + " already has the exercise order " + exerciseOrder);
    }
}
