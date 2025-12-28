package com.leandroftm.fitnessmanagement.exception.domain;

public class TrainingProgramAlreadyInactiveException extends DomainException {
    public TrainingProgramAlreadyInactiveException(Long id) {
        super("The training program with the id " + id + " has already been inactive.");
    }
}
