package com.leandroftm.fitnessmanagement.exception.domain;

public class StudentNotFoundException extends DomainException {
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
}
