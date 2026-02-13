package com.leandroftm.fitnessmanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramCreateRequestDTO;

import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramUpdateDTO;
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
public class TrainingProgramControllerTest {

    //.\mvnw -Dtest=TrainingProgramControllerTest test

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //CREATE
    @Test
    void shouldCreateTrainingProgram() throws Exception {
        TrainingProgramCreateRequestDTO dto = trainingProgram(
                "Push Day",
                "Chest and triceps workout"
        );
        mockMvc.perform(post("/training-programs")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/training-programs/")));
    }

    @Test
    void shouldReturnBadRequestWhenDuplicatedName() throws Exception {
        TrainingProgramCreateRequestDTO dto = trainingProgram(
                "Leg Day",
                "Leg workout"
        );

        mockMvc.perform(post("/training-programs")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/training-programs")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }


    //LIST
    @Test
    void shouldListActiveTrainingPrograms() throws Exception {
        TrainingProgramCreateRequestDTO dto = trainingProgram(
                "Pull Day",
                "Back and biceps"
        );

        mockMvc.perform(post("/training-programs")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/training-programs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[*].name").value(hasItem("Pull Day")));
    }

    @Test
    void shouldListAfterDeactivateTrainingProgram() throws Exception {
        TrainingProgramCreateRequestDTO dto = trainingProgram(
                "Push Day",
                "Chest and triceps workout");

        Long id = createTrainingProgram(dto);

        mockMvc.perform(patch("/training-programs/{id}/deactivate", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/training-programs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    //UPDATE
    @Test
    void shouldUpdateTrainingProgram() throws Exception {
        TrainingProgramCreateRequestDTO dto = trainingProgram(
                "Push Day",
                "Chest and triceps workout");

        Long id = createTrainingProgram(dto);
        TrainingProgramUpdateDTO updateDTO = updateTrainingProgram(
                "Leg Day",
                "Leg workout");


        mockMvc.perform(put("/training-programs/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNoContent());


        mockMvc.perform(get("/training-programs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].name").value(hasItem("Leg Day")))
                .andExpect(jsonPath("$.content[*].description").value(hasItem("Leg workout")));

    }


    @Test
    void shouldReturnBadRequestWhenUpdateTrainingProgramToDuplicatedName() throws Exception {
        Long id1 = createTrainingProgram(trainingProgram("Push Day", "Chest and triceps workout"));
        Long id2 = createTrainingProgram(trainingProgram("Leg Day", "Leg workout"));

        TrainingProgramUpdateDTO updateDTO = updateTrainingProgram("Push Day", "Any");

        mockMvc.perform(put("/training-programs/{id}", id2)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenDeactivateAlreadyInactiveTrainingProgram() throws Exception {
        TrainingProgramCreateRequestDTO dto = trainingProgram(
                "Push Day",
                "Chest and triceps workout"
        );

        Long id = createTrainingProgram(dto);

        mockMvc.perform(patch("/training-programs/{id}/deactivate", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(patch("/training-programs/{id}/deactivate", id)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    //HELPER METHODS
    private Long createTrainingProgram(TrainingProgramCreateRequestDTO dto) throws Exception {
        MvcResult result = mockMvc.perform(post("/training-programs")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        return extractId(result);
    }

    private TrainingProgramUpdateDTO updateTrainingProgram(String name, String description) {
        return new TrainingProgramUpdateDTO(name, description);
    }

    private TrainingProgramCreateRequestDTO trainingProgram(String name, String description) {
        return new TrainingProgramCreateRequestDTO(name, description);
    }

    private Long extractId(MvcResult result) {
        String location = result.getResponse().getHeader("Location");
        assertNotNull(location, "Location header must be present");

        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }
}
