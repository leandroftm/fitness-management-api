package com.leandroftm.fitnessmanagement.service;

import com.leandroftm.fitnessmanagement.domain.entity.Exercise;
import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgram;
import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgramExercise;
import com.leandroftm.fitnessmanagement.domain.enums.ExerciseStatus;
import com.leandroftm.fitnessmanagement.domain.enums.TrainingProgramStatus;
import com.leandroftm.fitnessmanagement.dto.TrainingProgramExerciseCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.TrainingProgramExerciseListDTO;
import com.leandroftm.fitnessmanagement.exception.domain.*;
import com.leandroftm.fitnessmanagement.repository.ExerciseRepository;
import com.leandroftm.fitnessmanagement.repository.TrainingProgramExerciseRepository;
import com.leandroftm.fitnessmanagement.repository.TrainingProgramRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TrainingProgramExerciseService {

    private final TrainingProgramExerciseRepository trainingProgramExerciseRepository;
    private final ExerciseRepository exerciseRepository;
    private final TrainingProgramRepository programRepository;

    public TrainingProgramExerciseService(
            TrainingProgramExerciseRepository trainingProgramExerciseRepository,
            ExerciseRepository exerciseRepository,
            TrainingProgramRepository programRepository
    ) {
        this.trainingProgramExerciseRepository = trainingProgramExerciseRepository;
        this.exerciseRepository = exerciseRepository;
        this.programRepository = programRepository;
    }

    public Long create(TrainingProgramExerciseCreateRequestDTO dto, Long programId) {
        TrainingProgram program = findProgramById(programId);
        Exercise exercise = findExerciseById(dto.exerciseId());

        validateCreate(program, exercise, dto);

        TrainingProgramExercise trainingProgramExercise = new TrainingProgramExercise();
        trainingProgramExercise.setTrainingProgram(program);
        trainingProgramExercise.setExercise(exercise);
        trainingProgramExercise.setExerciseOrder(dto.exerciseOrder());
        return trainingProgramExerciseRepository.save(trainingProgramExercise).getId();
    }

    @Transactional(readOnly = true)
    public Page<TrainingProgramExerciseListDTO> list(Pageable pageable, Long programId) {
        TrainingProgram program = findProgramById(programId);

        return trainingProgramExerciseRepository.findAllByTrainingProgram(program, pageable).map(TrainingProgramExerciseListDTO::new);
    }

    public void remove(Long programId, Long exerciseId) {
        TrainingProgram program = findProgramById(programId);

        if (program.getStatus() == TrainingProgramStatus.INACTIVE) {
            throw new TrainingProgramInactiveException(programId);
        }

        Exercise exercise = findExerciseById(exerciseId);

        TrainingProgramExercise programExercise = trainingProgramExerciseRepository
                .findByTrainingProgramAndExercise(program, exercise).orElseThrow(
                        () -> new ExerciseNotInTrainingProgramException(programId, exerciseId)
                );
        trainingProgramExerciseRepository.delete(programExercise);
    }

    private void validateCreate(TrainingProgram trainingProgram, Exercise exercise, TrainingProgramExerciseCreateRequestDTO dto) {

        //Training Program
        if (trainingProgram.getStatus() == TrainingProgramStatus.INACTIVE) {
            throw new TrainingProgramInactiveException(trainingProgram.getId());
        }

        //Exercise
        if (exercise.getStatus() == ExerciseStatus.INACTIVE) {
            throw new ExerciseInactiveException(exercise.getId());
        }

        //Duplicate Exercise in Training Program
        if (trainingProgramExerciseRepository.existsByTrainingProgramAndExercise(trainingProgram, exercise)) {
            throw new DuplicateExerciseInProgramException(trainingProgram.getId(), exercise.getId());
        }

        //Exercise order already exists on training program
        if (trainingProgramExerciseRepository.existsByTrainingProgramAndExerciseOrder(trainingProgram, dto.exerciseOrder())) {
            throw new DuplicateExerciseOrderException(trainingProgram.getId(), dto.exerciseOrder());
        }

    }

    private TrainingProgram findProgramById(Long programId) {
        return programRepository.findById(programId).orElseThrow(() -> new TrainingProgramNotFoundException(programId));
    }

    private Exercise findExerciseById(Long id) {
        return exerciseRepository.findById(id).orElseThrow(() -> new ExerciseNotFoundException(id));
    }
}
