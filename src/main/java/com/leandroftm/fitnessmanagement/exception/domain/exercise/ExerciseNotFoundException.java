package com.leandroftm.fitnessmanagement.exception.domain.exercise;

import com.leandroftm.fitnessmanagement.exception.domain.NotFoundException;

public class ExerciseNotFoundException extends NotFoundException {
    public ExerciseNotFoundException(Long id) {
        super("Exercise not found with id: " + id);
    }
}
