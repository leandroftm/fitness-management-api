package com.leandroftm.fitnessmanagement.exception.domain.student;

import com.leandroftm.fitnessmanagement.exception.domain.NotFoundException;

public class StudentNotFoundException extends NotFoundException {
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
}
