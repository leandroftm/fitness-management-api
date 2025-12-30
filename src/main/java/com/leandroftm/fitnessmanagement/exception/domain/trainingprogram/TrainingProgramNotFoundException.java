package com.leandroftm.fitnessmanagement.exception.domain.trainingprogram;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class TrainingProgramNotFoundException extends DomainException {
    public TrainingProgramNotFoundException(Long id) {
        super("Training not found with id " + id);
    }
}
