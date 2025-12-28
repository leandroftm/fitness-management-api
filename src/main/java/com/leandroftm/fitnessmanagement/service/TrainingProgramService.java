package com.leandroftm.fitnessmanagement.service;

import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgram;
import com.leandroftm.fitnessmanagement.domain.enums.TrainingProgramStatus;
import com.leandroftm.fitnessmanagement.dto.TrainingProgramCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.TrainingProgramListDTO;
import com.leandroftm.fitnessmanagement.repository.TrainingProgramRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TrainingProgramService {

    private final TrainingProgramRepository trainingProgramRepository;

    public TrainingProgramService(TrainingProgramRepository trainingProgramRepository) {
        this.trainingProgramRepository = trainingProgramRepository;
    }

    public Long create(TrainingProgramCreateRequestDTO dto) {
        TrainingProgram trainingProgram = trainingProgramRepository.save(toEntity(dto));
        return trainingProgram.getId();
    }

    @Transactional(readOnly = true)
    public Page<TrainingProgramListDTO> findAll(Pageable pageable) {
        return trainingProgramRepository.findAllByStatus(TrainingProgramStatus.ACTIVE, pageable).map(TrainingProgramListDTO::new);
    }

    private TrainingProgram toEntity(TrainingProgramCreateRequestDTO dto) {
        TrainingProgram trainingProgram = new TrainingProgram();

        trainingProgram.setName(dto.name());
        trainingProgram.setDescription(dto.description());

        return trainingProgram;
    }
}
