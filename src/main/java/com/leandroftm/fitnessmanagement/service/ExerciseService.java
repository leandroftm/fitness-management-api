package com.leandroftm.fitnessmanagement.service;

import com.leandroftm.fitnessmanagement.domain.entity.Exercise;
import com.leandroftm.fitnessmanagement.domain.enums.ExerciseStatus;
import com.leandroftm.fitnessmanagement.dto.ExerciseCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.ExerciseListDTO;
import com.leandroftm.fitnessmanagement.dto.ExerciseUpdateDTO;
import com.leandroftm.fitnessmanagement.exception.domain.DuplicateExerciseNameException;
import com.leandroftm.fitnessmanagement.exception.domain.ExerciseAlreadyActiveException;
import com.leandroftm.fitnessmanagement.exception.domain.ExerciseAlreadyInactiveException;
import com.leandroftm.fitnessmanagement.exception.domain.ExerciseNotFoundException;
import com.leandroftm.fitnessmanagement.repository.ExerciseRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@Transactional
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public Long create(ExerciseCreateRequestDTO dto) {
        if (exerciseRepository.existsByNameIgnoreCase(dto.name())) {
            throw new DuplicateExerciseNameException(dto.name());
        }
        Exercise exercise = exerciseRepository.save(toEntity(dto));
        return exercise.getId();
    }

    @Transactional(readOnly = true)
    public Page<ExerciseListDTO> list(Pageable pageable) {
        return exerciseRepository.findAllByStatus(ExerciseStatus.ACTIVE, pageable).map(ExerciseListDTO::new);
    }

    public void update(Long id, ExerciseUpdateDTO dto) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(id));

        if (!exercise.getName().equals(dto.name())) {
            if (exerciseRepository.existsByNameIgnoreCase(dto.name())) {
                throw new DuplicateExerciseNameException(dto.name());
            }
            exercise.setName(dto.name());
        }

        exercise.setDescription(dto.description());
        exercise.setVideoUrl(dto.videoUrl());
        exercise.setMuscleGroup(dto.muscleGroup());
    }

    @Transactional
    public void deactivate(Long id) {
        Exercise exercise = findByIdOrThrow(id);

        if (exercise.getStatus() == ExerciseStatus.INACTIVE) {
            throw new ExerciseAlreadyInactiveException(id);
        }
        exercise.setStatus(ExerciseStatus.INACTIVE);
        exercise.setDeactivatedAt(LocalDateTime.now());
    }

    @Transactional
    public void activate(Long id) {
        Exercise exercise = findByIdOrThrow(id);

        if (exercise.getStatus() == ExerciseStatus.ACTIVE) {
            throw new ExerciseAlreadyActiveException(id);
        }
        exercise.setStatus(ExerciseStatus.ACTIVE);
        exercise.setDeactivatedAt(null);
    }

    private Exercise findByIdOrThrow(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(id));
    }

    private Exercise toEntity(ExerciseCreateRequestDTO dto) {
        Exercise exercise = new Exercise();

        exercise.setName(dto.name());
        exercise.setDescription(dto.description());
        exercise.setVideoUrl(dto.videoUrl());
        exercise.setMuscleGroup(dto.muscleGroup());

        return exercise;
    }
}
