package com.leandroftm.fitnessmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequestDTO(
        @NotBlank
        @Size(max = 255)
        String street,
        @NotBlank
        @Size(max = 10)
        String number,
        @Size(max = 50)
        String complement,
        @NotBlank
        @Size(max = 50)
        String city,
        @NotBlank
        @Size(max = 50)
        String state,
        @NotBlank
        @Size(max = 10)
        String zipCode
) {
}
