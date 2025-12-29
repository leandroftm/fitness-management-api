package com.leandroftm.fitnessmanagement.repository;

import com.leandroftm.fitnessmanagement.domain.entity.Exercise;
import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgram;
import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgramExercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingProgramExerciseRepository extends JpaRepository<TrainingProgramExercise, Long> {
    boolean existsByTrainingProgramAndExercise(TrainingProgram program, Exercise exercise);

    boolean existsByTrainingProgramAndExerciseOrder(TrainingProgram program, int exerciseOrder);

    Page<TrainingProgramExercise> findAllByTrainingProgram(TrainingProgram trainingProgram, Pageable pageable);

    Optional<TrainingProgramExercise> findByTrainingProgramAndExercise(TrainingProgram program, Exercise exercise);
}
