package com.leandroftm.fitnessmanagement.exception.domain.student;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class DuplicateEmailException extends DomainException {
    public DuplicateEmailException(String email) {
        super("Email already exists " + email);
    }
}
