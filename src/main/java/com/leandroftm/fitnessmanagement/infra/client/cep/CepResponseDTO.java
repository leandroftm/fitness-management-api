package com.leandroftm.fitnessmanagement.infra.client.cep;

public record CepResponseDTO(
        String street,
        String neighborhood,
        String city,
        String state,
        String zipCode,
        Boolean error
) {
}
