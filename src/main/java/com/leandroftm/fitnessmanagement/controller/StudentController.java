package com.leandroftm.fitnessmanagement.controller;

import com.leandroftm.fitnessmanagement.dto.student.StudentCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.student.StudentListDTO;
import com.leandroftm.fitnessmanagement.dto.student.StudentUpdateDTO;
import com.leandroftm.fitnessmanagement.service.StudentService;
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
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody StudentCreateRequestDTO dto) {
        Long id = studentService.create(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<Page<StudentListDTO>> list(@PageableDefault(size = 10) Pageable pageable) {
        Page<StudentListDTO> page = studentService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody StudentUpdateDTO dto) {
        studentService.update(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        studentService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
