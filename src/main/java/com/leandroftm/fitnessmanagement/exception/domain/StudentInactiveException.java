package com.leandroftm.fitnessmanagement.exception.domain;

public class StudentInactiveException extends DomainException {
    public StudentInactiveException(Long id) {
        super("Student with id " + id + " is inactive.");
    }
}
