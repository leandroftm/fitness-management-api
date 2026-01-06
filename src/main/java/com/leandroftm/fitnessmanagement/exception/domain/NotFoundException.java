package com.leandroftm.fitnessmanagement.exception.domain;

public abstract class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super(message);
    }
}
