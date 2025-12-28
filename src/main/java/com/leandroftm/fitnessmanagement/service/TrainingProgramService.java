package com.leandroftm.fitnessmanagement.service;

import com.leandroftm.fitnessmanagement.domain.entity.TrainingProgram;
import com.leandroftm.fitnessmanagement.domain.enums.TrainingProgramStatus;
import com.leandroftm.fitnessmanagement.dto.TrainingProgramCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.TrainingProgramListDTO;
import com.leandroftm.fitnessmanagement.dto.TrainingProgramUpdateDTO;
import com.leandroftm.fitnessmanagement.exception.domain.DuplicateTrainingProgramNameException;
import com.leandroftm.fitnessmanagement.exception.domain.TrainingProgramNotFoundException;
import com.leandroftm.fitnessmanagement.exception.domain.TrainingProgramInactiveException;
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

    public void update(Long id, TrainingProgramUpdateDTO dto) {
        TrainingProgram program = findByIdOrThrow(id);

        if(program.getStatus() == TrainingProgramStatus.INACTIVE) {
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

    //TODO


    //ACTIVATE

    //DEACTIVATE

    //ADD EXERCISE

    //REMOVE EXERCISE
    /*
    no repeat same exercise on same program
    no alter inactive program
    no add inactive exercise
    required exercise order
    program can be: activate/deactivate
    */

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
