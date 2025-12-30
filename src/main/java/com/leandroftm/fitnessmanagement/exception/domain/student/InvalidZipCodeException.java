package com.leandroftm.fitnessmanagement.exception.domain.student;

import com.leandroftm.fitnessmanagement.exception.domain.DomainException;

public class InvalidZipCodeException extends DomainException {
    public InvalidZipCodeException(String zipCode){
        super("Invalid zip code: " + zipCode);
    }
}
