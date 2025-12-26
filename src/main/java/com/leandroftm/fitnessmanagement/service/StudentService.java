package com.leandroftm.fitnessmanagement.service;


import com.leandroftm.fitnessmanagement.domain.enums.StudentStatus;
import com.leandroftm.fitnessmanagement.dto.AddressRequestDTO;
import com.leandroftm.fitnessmanagement.dto.StudentCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.StudentListDTO;
import com.leandroftm.fitnessmanagement.dto.StudentUpdateDTO;
import com.leandroftm.fitnessmanagement.domain.entity.Address;
import com.leandroftm.fitnessmanagement.domain.entity.Student;
import com.leandroftm.fitnessmanagement.exception.domain.DuplicateEmailException;
import com.leandroftm.fitnessmanagement.exception.domain.StudentAlreadyInactiveException;
import com.leandroftm.fitnessmanagement.exception.domain.StudentNotFoundException;
import com.leandroftm.fitnessmanagement.repository.StudentRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        return studentRepository.findAllByStatus(StudentStatus.ACTIVE, pageable).map(StudentListDTO::new);
    }

    @Transactional
    public void update(Long id, StudentUpdateDTO dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        if (!student.getEmail().equals(dto.email())) {
            validateEmailUnique(dto.email());
            student.setEmail(dto.email());
        }
        student.setFullName(dto.fullName());
        student.setPhoneNumber(dto.phoneNumber());
        student.setGender(dto.gender());

        updateAddress(student.getAddress(), dto.address());
    }

    @Transactional
    public void deactivate(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        if (student.getStatus() == StudentStatus.INACTIVE) {
            throw new StudentAlreadyInactiveException(id);
        }
        student.setStatus(StudentStatus.INACTIVE);
        student.setUpdatedAt(LocalDateTime.now());
    }

    private void updateAddress(Address address, AddressRequestDTO dto) {
        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setComplement(dto.complement());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setZipCode(dto.zipCode());
    }

    private Student toEntity(StudentCreateRequestDTO dto) {
        Address address = new Address();
        updateAddress(address, dto.address());

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
            throw new DuplicateEmailException(email);
        }
    }
}
