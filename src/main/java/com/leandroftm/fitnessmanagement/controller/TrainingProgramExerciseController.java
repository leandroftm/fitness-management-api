package com.leandroftm.fitnessmanagement.controller;

import com.leandroftm.fitnessmanagement.dto.ExerciseListDTO;
import com.leandroftm.fitnessmanagement.dto.TrainingProgramExerciseCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.TrainingProgramExerciseListDTO;
import com.leandroftm.fitnessmanagement.dto.TrainingProgramExerciseUpdateDTO;
import com.leandroftm.fitnessmanagement.service.TrainingProgramExerciseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/training-programs/{programId}/exercises")
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

    @GetMapping
    public ResponseEntity<Page<TrainingProgramExerciseListDTO>> list(
            @PageableDefault(size = 20) Pageable pageable,
            @PathVariable Long programId
    ) {
        Page<TrainingProgramExerciseListDTO> page = trainingProgramExerciseService.list(pageable, programId);

        return ResponseEntity.ok(page);
    }

    @PutMapping("/{exerciseId}/order")
    public ResponseEntity<Void> updateOrder(
            @PathVariable Long programId,
            @PathVariable Long exerciseId,
            @Valid @RequestBody TrainingProgramExerciseUpdateDTO dto) {
        trainingProgramExerciseService.updateExerciseOrder(programId, exerciseId, dto.exerciseOrder());

        return ResponseEntity.noContent().build();
    }


}
