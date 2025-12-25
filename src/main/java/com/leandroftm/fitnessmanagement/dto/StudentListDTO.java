package com.leandroftm.fitnessmanagement.dto;

import com.leandroftm.fitnessmanagement.domain.entity.Student;
import com.leandroftm.fitnessmanagement.domain.enums.Gender;

public record StudentListDTO(
        Long id,
        String fullName,
        String email,
        String phoneNumber,
        Gender gender
) {
    public StudentListDTO(Student student) {
        this(student.getId(), student.getFullName(), student.getEmail(), student.getPhoneNumber(), student.getGender());
    }
}
