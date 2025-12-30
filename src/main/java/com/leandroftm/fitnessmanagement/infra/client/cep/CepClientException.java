package com.leandroftm.fitnessmanagement.infra.client.cep;

public class CepClientException extends RuntimeException {
    public CepClientException(String message) {
        super(message);
    }

    public CepClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
