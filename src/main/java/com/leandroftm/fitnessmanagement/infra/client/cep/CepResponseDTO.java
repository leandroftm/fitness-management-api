package com.leandroftm.fitnessmanagement.infra.client.cep;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CepResponseDTO(
        @JsonProperty("logradouro")
        String street,
        @JsonProperty("bairro")
        String neighborhood,
        @JsonProperty("cidade")
        String city,
        @JsonProperty("estado")
        String state,
        @JsonProperty("cep")
        String zipCode,
        @JsonProperty("erro")
        Boolean error
) {
}
