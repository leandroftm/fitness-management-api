package com.leandroftm.fitnessmanagement.dto.student;

import com.leandroftm.fitnessmanagement.domain.entity.Student;
import com.leandroftm.fitnessmanagement.domain.enums.Gender;
import com.leandroftm.fitnessmanagement.domain.enums.StudentStatus;

public record StudentDetailsDTO(
        Long id,
        String fullName,
        String email,
        String phoneNumber,
        Gender gender,
        StudentStatus status
) {
    public StudentDetailsDTO(Student student) {
        this(
                student.getId(),
                student.getFullName(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getGender(),
                student.getStatus()
        );
    }
}
