package com.leandroftm.fitnessmanagement.exception.domain;

public class ExerciseAlreadyActiveException extends DomainException {
    public ExerciseAlreadyActiveException(Long id) {
        super("Exercise with the id " + id + " is already active.");
    }
}
