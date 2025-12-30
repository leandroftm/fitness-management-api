package com.leandroftm.fitnessmanagement.exception.domain.student;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class StudentInactiveException extends DomainException {
    public StudentInactiveException(Long id) {
        super("Student with id " + id + " is inactive.");
    }
}
