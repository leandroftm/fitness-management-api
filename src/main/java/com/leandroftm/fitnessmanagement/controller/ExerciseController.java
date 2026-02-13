package com.leandroftm.fitnessmanagement.controller;

import com.leandroftm.fitnessmanagement.domain.entity.Exercise;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseDetailsDTO;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseListDTO;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseUpdateDTO;
import com.leandroftm.fitnessmanagement.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ExerciseCreateRequestDTO dto) {
        Long id = exerciseService.create(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<Page<ExerciseListDTO>> list(@PageableDefault(size = 10) Pageable pageable) {
        Page<ExerciseListDTO> page = exerciseService.list(pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDetailsDTO> getById(@PathVariable Long id) {
        ExerciseDetailsDTO dto = exerciseService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody ExerciseUpdateDTO dto, @PathVariable Long id) {
        exerciseService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        exerciseService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        exerciseService.activate(id);
        return ResponseEntity.noContent().build();
    }
}
