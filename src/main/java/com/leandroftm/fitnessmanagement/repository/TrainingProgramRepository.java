package com.leandroftm.fitnessmanagement.repository;

import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgram;
import com.leandroftm.fitnessmanagement.domain.enums.TrainingProgramStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {
    Page<TrainingProgram> findAllByStatus(TrainingProgramStatus trainingProgramStatus, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);
}
