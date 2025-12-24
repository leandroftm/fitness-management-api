package com.leandroftm.fitnessmanagement.exception.dto;

public record ApiErrorDTO(
        int status,
        String error,
        String message,
        String path
) {
}
