package com.leandroftm.fitnessmanagement.repository;

import com.leandroftm.fitnessmanagement.domain.entity.Exercise;
import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgram;
import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgramExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingProgramExerciseRepository extends JpaRepository<TrainingProgramExercise, Long> {
    boolean existsByTrainingProgramAndExercise(TrainingProgram program, Exercise exercise);

    boolean existsByTrainingProgramAndExerciseOrder(TrainingProgram program, int exerciseOrder);
}
