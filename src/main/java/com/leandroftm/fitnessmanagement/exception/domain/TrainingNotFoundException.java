package com.leandroftm.fitnessmanagement.exception.domain;

public class TrainingNotFoundException extends DomainException {
    public TrainingNotFoundException(Long id) {
        super("Training not found with id " + id);
    }
}
