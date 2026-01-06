package com.leandroftm.fitnessmanagement.exception.domain.trainingprogram;

import com.leandroftm.fitnessmanagement.exception.domain.NotFoundException;

public class TrainingProgramNotFoundException extends NotFoundException {
    public TrainingProgramNotFoundException(Long id) {
        super("Training not found with id " + id);
    }
}
