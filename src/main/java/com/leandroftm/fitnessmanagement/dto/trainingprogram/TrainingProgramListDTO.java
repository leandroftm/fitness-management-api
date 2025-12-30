package com.leandroftm.fitnessmanagement.dto.trainingprogram;

import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgram;

public record TrainingProgramListDTO(
        Long id,
        String name,
        String description
) {

    public TrainingProgramListDTO(TrainingProgram program) {
        this(program.getId(), program.getName(), program.getDescription());
    }
}
