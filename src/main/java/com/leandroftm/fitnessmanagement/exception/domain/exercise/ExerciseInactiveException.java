package com.leandroftm.fitnessmanagement.exception.domain.exercise;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class ExerciseInactiveException extends DomainException {
    public ExerciseInactiveException(Long id) {
        super("Exercise " + id + " inactive.");
    }
}
