package com.leandroftm.fitnessmanagement.service;

import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgram;
import com.leandroftm.fitnessmanagement.domain.enums.TrainingProgramStatus;
import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramListDTO;
import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramUpdateDTO;
import com.leandroftm.fitnessmanagement.exception.domain.trainingprogram.*;
import com.leandroftm.fitnessmanagement.repository.TrainingProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainingProgramService {

    private final TrainingProgramRepository trainingProgramRepository;

    public Long create(TrainingProgramCreateRequestDTO dto) {
        if (trainingProgramRepository.existsByNameIgnoreCase(dto.name())) {
            throw new DuplicateTrainingProgramNameException(dto.name());
        }

        TrainingProgram trainingProgram = trainingProgramRepository.save(toEntity(dto));
        return trainingProgram.getId();
    }

    @Transactional(readOnly = true)
    public Page<TrainingProgramListDTO> findAll(Pageable pageable) {
        return trainingProgramRepository.findAllByStatus(TrainingProgramStatus.ACTIVE, pageable).map(TrainingProgramListDTO::new);
    }

    public void update(Long id, TrainingProgramUpdateDTO dto) {
        TrainingProgram program = findByIdOrThrow(id);

        if (program.getStatus() == TrainingProgramStatus.INACTIVE) {
            throw new TrainingProgramInactiveException(id);
        }

        if (!program.getName().equals(dto.name())) {
            if (trainingProgramRepository.existsByNameIgnoreCase(dto.name())) {
                throw new DuplicateTrainingProgramNameException(dto.name());
            }
            program.setName(dto.name());
        }

        program.setDescription(dto.description());
    }

    public void deactivate(Long id) {
        TrainingProgram program = findByIdOrThrow(id);

        if (program.getStatus() == TrainingProgramStatus.INACTIVE) {
            throw new TrainingProgramAlreadyInactiveException(id);
        }
        program.setStatus(TrainingProgramStatus.INACTIVE);
        program.setDeactivatedAt(LocalDateTime.now());
    }

    public void activate(Long id) {
        TrainingProgram program = findByIdOrThrow(id);

        if (program.getStatus() == TrainingProgramStatus.ACTIVE) {
            throw new TrainingProgramAlreadyActiveException(id);
        }

        program.setStatus(TrainingProgramStatus.ACTIVE);
        program.setDeactivatedAt(null);
    }

    private TrainingProgram findByIdOrThrow(Long id) {
        return trainingProgramRepository.findById(id).orElseThrow(
                () -> new TrainingProgramNotFoundException(id)
        );
    }

    private TrainingProgram toEntity(TrainingProgramCreateRequestDTO dto) {
        TrainingProgram trainingProgram = new TrainingProgram();

        trainingProgram.setName(dto.name());
        trainingProgram.setDescription(dto.description());

        return trainingProgram;
    }
}
