package com.leandroftm.fitnessmanagement.exception.domain;

public class ExerciseNotFoundException extends DomainException {
    public ExerciseNotFoundException(Long id) {
        super("Exercise not found with id: " + id);
    }
}
