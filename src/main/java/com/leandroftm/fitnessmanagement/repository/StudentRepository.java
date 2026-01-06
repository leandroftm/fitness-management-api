package com.leandroftm.fitnessmanagement.repository;

import com.leandroftm.fitnessmanagement.domain.entity.Student;
import com.leandroftm.fitnessmanagement.domain.enums.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    Page<Student> findAllByStatus(StudentStatus status, Pageable pageable);
}
