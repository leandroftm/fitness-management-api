package com.leandroftm.fitnessmanagement.exception.domain;

public class DuplicateEmailException extends DomainException {
    public DuplicateEmailException(String email) {
        super("Email already registered " + email);
    }
}
