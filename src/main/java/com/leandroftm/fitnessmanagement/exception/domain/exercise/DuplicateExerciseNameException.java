package com.leandroftm.fitnessmanagement.exception.domain.exercise;


import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class DuplicateExerciseNameException extends DomainException {
    public DuplicateExerciseNameException(String name) {
        super("Exercise with the name '" + name + "' already exists.");
    }
}
