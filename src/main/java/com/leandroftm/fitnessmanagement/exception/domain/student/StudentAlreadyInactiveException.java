package com.leandroftm.fitnessmanagement.exception.domain.student;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class StudentAlreadyInactiveException extends DomainException {
    public StudentAlreadyInactiveException(Long id) {
        super("Student with id " + id + " is already inactive.");
    }
}
