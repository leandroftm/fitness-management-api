package com.leandroftm.fitnessmanagement.infra.client.cep;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CepClient {

    private static final String BASE_URL = "https://viacep.com.br/ws";

    private final RestTemplate restTemplate;

    public CepClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CepResponseDTO findZipCode(String zipCode) {
        try {
            String url = BASE_URL + "/" + zipCode + "/json";
            return restTemplate.getForObject(url, CepResponseDTO.class);
        } catch (Exception e) {
            throw new CepClientException("Error calling CEP service", e);
        }
    }
}
