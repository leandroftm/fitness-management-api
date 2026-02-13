package com.leandroftm.fitnessmanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandroftm.fitnessmanagement.domain.enums.MuscleGroup;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramExerciseCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramExerciseUpdateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = "USER")
@ActiveProfiles("test")
@Transactional
public class TrainingProgramExerciseControllerTest {

    //.\mvnw -Dtest=TrainingProgramExerciseControllerTest test

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String PROGRAM_EXERCISES = "/training-programs/{programId}/exercises";

    //CREATE
    @Test
    void shouldCreateExerciseWhenTrainingProgramIsActive() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise(
                "Bench Press",
                "Push a weight upwards while lying on a bench",
                "https://www.youtube.com/shorts/hWbUlkb5Ms4",
                MuscleGroup.CHEST
        );

        TrainingProgramCreateRequestDTO trainingDto = trainingProgram(
                "Chest Day",
                "Chest and triceps"
        );

        Long programId = createTrainingProgram(trainingDto);
        Long exerciseId = createExercise(exerciseDto);

        TrainingProgramExerciseCreateRequestDTO linkDto = trainingProgramExercise(exerciseId, 1);

        mockMvc.perform(post(PROGRAM_EXERCISES, programId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        containsString("/training-programs/" + programId + "/exercises/")));
    }

    @Test
    void shouldReturnBadRequestWhenDuplicatedExerciseInSameProgram() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise(
                "Bench Press",
                "Push a weight upwards while lying on a bench",
                "https://www.youtube.com/shorts/hWbUlkb5Ms4",
                MuscleGroup.CHEST
        );
        TrainingProgramCreateRequestDTO trainingDto = trainingProgram(
                "Chest Day",
                "Chest and triceps"
        );

        Long programId = createTrainingProgram(trainingDto);
        Long exerciseId = createExercise(exerciseDto);

        TrainingProgramExerciseCreateRequestDTO linkDto = trainingProgramExercise(exerciseId, 1);

        mockMvc.perform(post(PROGRAM_EXERCISES, programId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post(PROGRAM_EXERCISES, programId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenExerciseIsInactive() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise(
                "Bench Press",
                "Push a weight upwards while lying on a bench",
                "https://www.youtube.com/shorts/hWbUlkb5Ms4",
                MuscleGroup.CHEST
        );

        TrainingProgramCreateRequestDTO trainingDto = trainingProgram(
                "Chest Day",
                "Chest and triceps"
        );

        Long programId = createTrainingProgram(trainingDto);
        Long exerciseId = createExercise(exerciseDto);

        mockMvc.perform(patch("/exercises/{exerciseId}/deactivate", exerciseId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{exerciseId}", exerciseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));

        TrainingProgramExerciseCreateRequestDTO linkDto = trainingProgramExercise(exerciseId, 1);

        mockMvc.perform(post(PROGRAM_EXERCISES, programId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkDto)))
                .andExpect(jsonPath("$.message", containsString("inactive")));
    }

    @Test
    void shouldReturnBadRequestWhenAddingExerciseToInactiveTrainingProgram() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise(
                "Dead Lift",
                "lift a weight from the floor to a standing position, keeping it close to your body, and then return it with control",
                "https://www.youtube.com/shorts/ZaTM37cfiDs",
                MuscleGroup.LEGS
        );

        TrainingProgramCreateRequestDTO trainingDto = trainingProgram(
                "Leg Day",
                "Lower body focus"
        );

        Long programId = createTrainingProgram(trainingDto);
        Long exerciseId = createExercise(exerciseDto);

        mockMvc.perform(patch("/training-programs/{programId}/deactivate", programId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        TrainingProgramExerciseCreateRequestDTO linkDto = trainingProgramExercise(exerciseId, 1);

        mockMvc.perform(post(PROGRAM_EXERCISES, programId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenAddingExerciseWithDuplicatedOrderInTrainingProgram() throws Exception {
        ExerciseCreateRequestDTO exerciseDto1 = exercise(
                "Dead Lift",
                "lift a weight from the floor to a standing position, keeping it close to your body, and then return it with control",
                "https://www.youtube.com/shorts/ZaTM37cfiDs",
                MuscleGroup.LEGS
        );

        ExerciseCreateRequestDTO exerciseDto2 = exercise(
                "Stiff-Leg Deadlift",
                "Strength exercise requiring slightly bent knees, a straight back, and controlled hip hinge movement to build strength and flexibility",
                "https://www.youtube.com/watch?v=CN_7cz3P-1U",
                MuscleGroup.LEGS
        );

        TrainingProgramCreateRequestDTO trainingDto = trainingProgram(
                "Leg Day",
                "Lower body focus"
        );

        Long exercise1Id = createExercise(exerciseDto1);
        Long exercise2Id = createExercise(exerciseDto2);
        Long programId = createTrainingProgram(trainingDto);

        TrainingProgramExerciseCreateRequestDTO link1Dto = trainingProgramExercise(exercise1Id, 1);
        TrainingProgramExerciseCreateRequestDTO link2Dto = trainingProgramExercise(exercise2Id, 1);

        mockMvc.perform(post(PROGRAM_EXERCISES, programId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(link1Dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(PROGRAM_EXERCISES, programId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[*].exerciseId").value(hasItem(exercise1Id.intValue())))
                .andExpect(jsonPath("$.content[*].exerciseOrder").value(1));

        mockMvc.perform(post(PROGRAM_EXERCISES, programId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(link2Dto)))
                .andExpect(status().isBadRequest());
    }

    //LIST
    @Test
    void shouldListExercisesInActiveTrainingProgram() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise(
                "Pull Ups",
                "Pulling your body up to a bar with an overhand grip",
                "https://www.youtube.com/shorts/Lic9H2TUCxk",
                MuscleGroup.BACK
        );

        TrainingProgramCreateRequestDTO trainingDto = trainingProgram(
                "Back Day",
                "Back and biceps"
        );

        Long programId = createTrainingProgram(trainingDto);
        Long exerciseId = createExercise(exerciseDto);

        TrainingProgramExerciseCreateRequestDTO linkDto = trainingProgramExercise(exerciseId, 1);

        mockMvc.perform(post(PROGRAM_EXERCISES, programId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(PROGRAM_EXERCISES, programId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[*].exerciseId").value(hasItem(exerciseId.intValue())))
                .andExpect(jsonPath("$.content[*].exerciseName").value(hasItem(exerciseDto.name())))
                .andExpect(jsonPath("$.content[*].videoUrl").value(hasItem(exerciseDto.videoUrl())))
                .andExpect(jsonPath("$.content[*].exerciseOrder").value(hasItem(linkDto.exerciseOrder())));
    }

    //UPDATE
    @Test
    void shouldUpdateExerciseOrderWhenTrainingProgramIsActive() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise(
                "Bench Press",
                "Push a weight upwards while lying on a bench",
                "https://www.youtube.com/shorts/hWbUlkb5Ms4",
                MuscleGroup.CHEST
        );

        TrainingProgramCreateRequestDTO trainingDto = trainingProgram(
                "Chest Day",
                "Chest and triceps"
        );

        Long programId = createTrainingProgram(trainingDto);
        Long exerciseId = createExercise(exerciseDto);

        TrainingProgramExerciseCreateRequestDTO linkDto = trainingProgramExercise(exerciseId, 1);

        mockMvc.perform(post(PROGRAM_EXERCISES, programId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(PROGRAM_EXERCISES, programId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].exerciseOrder").value(hasItem(1)));

        TrainingProgramExerciseUpdateDTO updateLinkDto = new TrainingProgramExerciseUpdateDTO(2);

        mockMvc.perform(put("/training-programs/{programId}/exercises/{exerciseId}/order", programId, exerciseId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateLinkDto)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(PROGRAM_EXERCISES, programId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].exerciseId").value(hasItem(exerciseId.intValue())))
                .andExpect(jsonPath("$.content[*].exerciseOrder").value(hasItem(updateLinkDto.exerciseOrder())));
    }

    //DELETE
    @Test
    void shouldDeleteExerciseFromActiveTrainingProgram() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise(
                "Bench Press",
                "Push a weight upwards while lying on a bench",
                "https://www.youtube.com/shorts/hWbUlkb5Ms4",
                MuscleGroup.CHEST
        );

        TrainingProgramCreateRequestDTO trainingDto = trainingProgram(
                "Chest Day",
                "Chest and triceps"
        );

        Long programId = createTrainingProgram(trainingDto);
        Long exerciseId = createExercise(exerciseDto);

        TrainingProgramExerciseCreateRequestDTO linkDto = trainingProgramExercise(exerciseId, 1);

        mockMvc.perform(post(PROGRAM_EXERCISES, programId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(PROGRAM_EXERCISES, programId))
                .andExpect(status().isOk());

        mockMvc.perform(delete(PROGRAM_EXERCISES + "/{exerciseId}", programId, exerciseId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestWhenDeletingExerciseFromInactiveProgram() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise(
                "Bench Press",
                "Push a weight upwards while lying on a bench",
                "https://www.youtube.com/shorts/hWbUlkb5Ms4",
                MuscleGroup.CHEST
        );

        TrainingProgramCreateRequestDTO trainingDto = trainingProgram(
                "Chest Day",
                "Chest and triceps"
        );

        Long programId = createTrainingProgram(trainingDto);
        Long exerciseId = createExercise(exerciseDto);

        TrainingProgramExerciseCreateRequestDTO linkDto = trainingProgramExercise(exerciseId, 1);

        mockMvc.perform(post(PROGRAM_EXERCISES, programId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(linkDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(patch("/training-programs/{programId}/deactivate", programId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete(PROGRAM_EXERCISES + "/{exerciseId}", programId, exerciseId)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }


    //HELPER METHODS
    private TrainingProgramExerciseCreateRequestDTO trainingProgramExercise(Long exerciseId, int exerciseOrder) {
        return new TrainingProgramExerciseCreateRequestDTO(exerciseId, exerciseOrder);
    }

    private TrainingProgramCreateRequestDTO trainingProgram(String name, String description) {
        return new TrainingProgramCreateRequestDTO(name, description);
    }

    private ExerciseCreateRequestDTO exercise(String name, String description, String videoUrl, MuscleGroup muscleGroup) {
        return new ExerciseCreateRequestDTO(name, description, videoUrl, muscleGroup);
    }

    private Long createTrainingProgram(TrainingProgramCreateRequestDTO dto) throws Exception {
        MvcResult result = mockMvc.perform(post("/training-programs")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        return extractId(result);
    }

    private Long createExercise(ExerciseCreateRequestDTO exerciseDto) throws Exception {
        MvcResult result = mockMvc.perform(post("/exercises")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        return extractId(result);
    }

    private Long extractId(MvcResult result) {
        String location = result.getResponse().getHeader("Location");
        assertNotNull(location, "Location header must be present");

        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }
}
