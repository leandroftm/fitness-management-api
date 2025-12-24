package com.leandroftm.fitnessmanagement.service;


import com.leandroftm.fitnessmanagement.dto.StudentCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.StudentListDTO;
import com.leandroftm.fitnessmanagement.entity.Address;
import com.leandroftm.fitnessmanagement.entity.Student;
import com.leandroftm.fitnessmanagement.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional
    public Long create(StudentCreateRequestDTO dto) {
        validateEmailUnique(dto.email());
        Student student = studentRepository.save(toEntity(dto));
        return student.getId();
    }

    public Page<StudentListDTO> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable).map(StudentListDTO::new);
    }



    private Student toEntity(StudentCreateRequestDTO dto) {
        Address address = new Address();
        address.setStreet(dto.address().street());
        address.setNumber(dto.address().number());
        address.setComplement(dto.address().complement());
        address.setCity(dto.address().city());
        address.setState(dto.address().state());
        address.setZipCode(dto.address().zipCode());

        Student student = new Student();
        student.setFullName(dto.fullName());
        student.setEmail(dto.email());
        student.setPhoneNumber(dto.phoneNumber());
        student.setGender(dto.gender());
        student.setAddress(address);

        return student;

    }

    private void validateEmailUnique(String email) {
        if (studentRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }
    }
}
