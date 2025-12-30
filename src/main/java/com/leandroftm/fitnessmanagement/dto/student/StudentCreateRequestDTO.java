package com.leandroftm.fitnessmanagement.dto.student;

import com.leandroftm.fitnessmanagement.domain.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudentCreateRequestDTO(
        @NotBlank
        @Size(min = 3, max = 255)
        String fullName,
        @NotBlank
        @Email
        @Size(max = 255)
        String email,
        @NotBlank
        @Size(min = 8, max = 20)
        String phoneNumber,
        @NotNull
        Gender gender,
        @NotNull
        AddressCreateRequestDTO address
) {
}
