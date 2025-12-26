package com.leandroftm.fitnessmanagement.exception.domain;


public class DuplicateExerciseNameException extends DomainException {
    public DuplicateExerciseNameException(String name) {
        super("Exercise with the name '" + name + "' already exists.");
    }
}
