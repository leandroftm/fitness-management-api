package com.leandroftm.fitnessmanagement.dto.student;

import com.leandroftm.fitnessmanagement.domain.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudentUpdateDTO(

        @NotBlank
        @Size(min = 3, max = 255)
        String fullName,

        @NotBlank
        @Size(min = 8, max = 20)
        String phoneNumber,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @NotNull
        Gender gender,

        @NotNull
        AddressCreateRequestDTO address
) {
}
