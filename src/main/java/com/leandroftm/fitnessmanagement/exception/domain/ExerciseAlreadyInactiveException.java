package com.leandroftm.fitnessmanagement.exception.domain;

public class ExerciseAlreadyInactiveException extends DomainException {
    public ExerciseAlreadyInactiveException(Long id) {
        super("Exercise with the id " + id + " is already inactive.");
    }
}
