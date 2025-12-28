package com.leandroftm.fitnessmanagement.exception.domain;

public class DuplicateExerciseOrderException extends DomainException {
    public DuplicateExerciseOrderException(Long programId, int exerciseOrder) {
        super("The program id " + programId + " already has the exercise order " + exerciseOrder);
    }
}
