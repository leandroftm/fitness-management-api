package com.leandroftm.fitnessmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressCreateRequestDTO(
        @NotBlank
        String zipCode,

        @NotBlank
        String number,
        String complement
) {
}
