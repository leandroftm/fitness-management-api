package com.leandroftm.fitnessmanagement.service;


import com.leandroftm.fitnessmanagement.domain.enums.StudentStatus;
import com.leandroftm.fitnessmanagement.dto.AddressCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.StudentCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.StudentListDTO;
import com.leandroftm.fitnessmanagement.dto.StudentUpdateDTO;
import com.leandroftm.fitnessmanagement.domain.entity.Address;
import com.leandroftm.fitnessmanagement.domain.entity.Student;
import com.leandroftm.fitnessmanagement.exception.domain.*;
import com.leandroftm.fitnessmanagement.infra.client.cep.CepClient;
import com.leandroftm.fitnessmanagement.infra.client.cep.CepResponseDTO;
import com.leandroftm.fitnessmanagement.repository.StudentRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final CepClient cepClient;

    public Long create(StudentCreateRequestDTO dto) {
        validateEmailUnique(dto.email());

        Address address = buildAddressFromCep(dto.address());

        Student student = new Student();
        student.setFullName(dto.fullName());
        student.setEmail(dto.email());
        student.setPhoneNumber(dto.phoneNumber());
        student.setGender(dto.gender());
        student.setAddress(address);

        studentRepository.save(student);
        return student.getId();
    }

    @Transactional(readOnly = true)
    public Page<StudentListDTO> findAll(Pageable pageable) {
        return studentRepository.findAllByStatus(StudentStatus.ACTIVE, pageable).map(StudentListDTO::new);
    }

    public void update(Long id, StudentUpdateDTO dto) {
        Student student = findByIdOrThrow(id);
        if (student.getStatus() == StudentStatus.INACTIVE) {
            throw new StudentInactiveException(id);
        }

        if (!student.getEmail().equals(dto.email())) {
            validateEmailUnique(dto.email());
            student.setEmail(dto.email());
        }

        student.setFullName(dto.fullName());
        student.setPhoneNumber(dto.phoneNumber());
        student.setGender(dto.gender());

        Address address = student.getAddress();
        updateAddressFromCep(address, dto.address());
    }

    public void deactivate(Long id) {
        Student student = findByIdOrThrow(id);

        if (student.getStatus() == StudentStatus.INACTIVE) {
            throw new StudentAlreadyInactiveException(id);
        }
        student.setStatus(StudentStatus.INACTIVE);
        student.setUpdatedAt(LocalDateTime.now());
    }

    private Student findByIdOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    private void updateAddressFromCep(Address address, AddressCreateRequestDTO dto) {
        String normalizeZipCode = normalizeZipCode(dto.zipCode());
        CepResponseDTO cepResponse = cepClient.findZipCode(normalizeZipCode);
        if (cepResponse == null || Boolean.TRUE.equals(cepResponse.error())) {
            throw new InvalidZipCodeException(dto.zipCode());
        }

        address.setZipCode(cepResponse.zipCode());
        address.setStreet(cepResponse.street());
        address.setCity(cepResponse.city());
        address.setState(cepResponse.state());
        address.setNumber(dto.number());
        address.setComplement(dto.complement());
    }

    private Address buildAddressFromCep(AddressCreateRequestDTO dto) {
        String normalizeZipCode = normalizeZipCode(dto.zipCode());
        CepResponseDTO cepResponse = cepClient.findZipCode(normalizeZipCode);

        if (cepResponse == null || Boolean.TRUE.equals(cepResponse.error())) {
            throw new InvalidZipCodeException(dto.zipCode());
        }

        Address address = new Address();
        address.setZipCode(cepResponse.zipCode());
        address.setStreet(cepResponse.street());
        address.setCity(cepResponse.city());
        address.setState(cepResponse.state());
        address.setNumber(dto.number());
        address.setComplement(dto.complement());

        return address;
    }

    private String normalizeZipCode(String zipCode) {
        return zipCode.replaceAll("\\D", "");
    }

    private void validateEmailUnique(String email) {
        if (studentRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }
    }
}
