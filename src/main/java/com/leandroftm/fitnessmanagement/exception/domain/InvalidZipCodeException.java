package com.leandroftm.fitnessmanagement.exception.domain;

public class InvalidZipCodeException extends DomainException{
    public InvalidZipCodeException(String zipCode){
        super("Invalid zip code: " + zipCode);
    }
}
