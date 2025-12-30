package com.leandroftm.fitnessmanagement.exception.domain.student;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class StudentNotFoundException extends DomainException {
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
}
