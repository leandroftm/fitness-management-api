package com.leandroftm.fitnessmanagement.exception.domain;

public class ExerciseInactiveException extends DomainException {
    public ExerciseInactiveException(Long id) {
        super("Exercise " + id + " inactive.");
    }
}
