package com.leandroftm.fitnessmanagement.repository;

import com.leandroftm.fitnessmanagement.domain.entity.Exercise;
import com.leandroftm.fitnessmanagement.domain.enums.ExerciseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Page<Exercise> findAllByStatus(ExerciseStatus exerciseStatus, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);
}
