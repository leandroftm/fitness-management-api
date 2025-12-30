package com.leandroftm.fitnessmanagement.exception.domain.exercise;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class ExerciseNotFoundException extends DomainException {
    public ExerciseNotFoundException(Long id) {
        super("Exercise not found with id: " + id);
    }
}
