package com.leandroftm.fitnessmanagement.controller;

import com.leandroftm.fitnessmanagement.dto.TrainingProgramExerciseCreateRequestDTO;
import com.leandroftm.fitnessmanagement.service.TrainingProgramExerciseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/training-program/{programId}/exercises")
public class TrainingProgramExerciseController {

    private final TrainingProgramExerciseService trainingProgramExerciseService;

    public TrainingProgramExerciseController(TrainingProgramExerciseService trainingProgramExerciseService) {
        this.trainingProgramExerciseService = trainingProgramExerciseService;
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @Valid @RequestBody TrainingProgramExerciseCreateRequestDTO dto,
            @PathVariable Long programId
    ) {
        Long id = trainingProgramExerciseService.create(dto, programId);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
