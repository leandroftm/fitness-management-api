package com.leandroftm.fitnessmanagement.exception.domain;

public class StudentAlreadyInactiveException extends DomainException {
    public StudentAlreadyInactiveException(Long id) {
        super("Student with id " + id + " is already inactive.");
    }
}
