package com.leandroftm.fitnessmanagement.repository;

import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgramExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingProgramExerciseRepository extends JpaRepository<TrainingProgramExercise, Long> {
}
