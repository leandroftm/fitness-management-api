package com.leandroftm.fitnessmanagement.exception.domain;

public class TrainingProgramNotFoundException extends DomainException {
    public TrainingProgramNotFoundException(Long id) {
        super("Training not found with id " + id);
    }
}
