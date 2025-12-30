package com.leandroftm.fitnessmanagement.exception.domain.trainingprogram;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class TrainingProgramAlreadyActiveException extends DomainException {
    public TrainingProgramAlreadyActiveException(Long id) {
        super("The training program with the id " + id + " has already been active.");
    }
}
