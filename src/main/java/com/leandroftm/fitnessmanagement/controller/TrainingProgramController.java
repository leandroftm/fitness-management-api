package com.leandroftm.fitnessmanagement.controller;

import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramListDTO;
import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramUpdateDTO;
import com.leandroftm.fitnessmanagement.service.TrainingProgramService;
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
@RequestMapping("/training-programs")
public class TrainingProgramController {

    private final TrainingProgramService trainingProgramService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody TrainingProgramCreateRequestDTO dto) {
        Long id = trainingProgramService.create(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<Page<TrainingProgramListDTO>> findAll(@PageableDefault(size = 20) Pageable pageable) {
        Page<TrainingProgramListDTO> page = trainingProgramService.findAll(pageable);

        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody TrainingProgramUpdateDTO dto) {
        trainingProgramService.update(id, dto);
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        trainingProgramService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        trainingProgramService.activate(id);
        return ResponseEntity.noContent().build();
    }
}
