package com.leandroftm.fitnessmanagement.exception.domain.trainingprogram;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class TrainingProgramInactiveException extends DomainException {
    public TrainingProgramInactiveException(Long id) {
        super("Training program with the id " + id + " is inactive and can not be updated");
    }
}
