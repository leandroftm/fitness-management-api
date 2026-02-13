package com.leandroftm.fitnessmanagement.service;

import com.leandroftm.fitnessmanagement.domain.entity.Exercise;
import com.leandroftm.fitnessmanagement.domain.enums.ExerciseStatus;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseDetailsDTO;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseListDTO;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseUpdateDTO;
import com.leandroftm.fitnessmanagement.exception.domain.exercise.*;
import com.leandroftm.fitnessmanagement.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

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

        if (exercise.getStatus() == ExerciseStatus.INACTIVE) {
            throw new ExerciseInactiveException(id);
        }


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


    public void deactivate(Long id) {
        Exercise exercise = findByIdOrThrow(id);

        if (exercise.getStatus() == ExerciseStatus.INACTIVE) {
            throw new ExerciseAlreadyInactiveException(id);
        }
        exercise.setStatus(ExerciseStatus.INACTIVE);
        exercise.setDeactivatedAt(LocalDateTime.now());
    }


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

    @Transactional(readOnly = true)
    public ExerciseDetailsDTO getById(Long id) {
        Exercise exercise = findByIdOrThrow(id);

        return new ExerciseDetailsDTO(exercise);
    }

}
