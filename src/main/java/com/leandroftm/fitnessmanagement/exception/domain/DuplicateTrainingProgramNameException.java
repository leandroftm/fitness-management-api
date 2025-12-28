package com.leandroftm.fitnessmanagement.exception.domain;

import jakarta.validation.constraints.NotBlank;

public class DuplicateTrainingProgramNameException extends DomainException {
    public DuplicateTrainingProgramNameException(@NotBlank String name) {
        super("Training program with the name " + name + " already exists");
    }
}
