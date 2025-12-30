package com.leandroftm.fitnessmanagement.exception.domain.trainingprogram;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;
import jakarta.validation.constraints.NotBlank;

public class DuplicateTrainingProgramNameException extends DomainException {
    public DuplicateTrainingProgramNameException(@NotBlank String name) {
        super("Training program with the name " + name + " already exists");
    }
}
