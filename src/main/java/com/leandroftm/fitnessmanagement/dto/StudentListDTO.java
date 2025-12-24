package com.leandroftm.fitnessmanagement.dto;

import com.leandroftm.fitnessmanagement.entity.Student;
import com.leandroftm.fitnessmanagement.enums.Gender;

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
