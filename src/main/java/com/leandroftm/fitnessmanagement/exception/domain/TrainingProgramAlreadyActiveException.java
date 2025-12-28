package com.leandroftm.fitnessmanagement.exception.domain;

public class TrainingProgramAlreadyActiveException extends DomainException {
    public TrainingProgramAlreadyActiveException(Long id) {
        super("The training program with the id " + id + " has already been active.");
    }
}
