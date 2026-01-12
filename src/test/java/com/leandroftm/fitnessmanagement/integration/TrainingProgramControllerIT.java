package com.leandroftm.fitnessmanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramCreateRequestDTO;

import com.leandroftm.fitnessmanagement.dto.trainingprogram.TrainingProgramUpdateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TrainingProgramControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTrainingProgram() throws Exception {
        TrainingProgramCreateRequestDTO dto =
                new TrainingProgramCreateRequestDTO(
                        "Push Day",
                        "Chest and triceps workout"
                );
        mockMvc.perform(post("/training-programs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location")
                );
    }

    @Test
    void shouldListActiveTrainingPrograms() throws Exception {
        TrainingProgramCreateRequestDTO dto =
                new TrainingProgramCreateRequestDTO(
                        "Pull Day",
                        "Back and biceps"
                );

        mockMvc.perform(post("/training-programs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/training-programs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Pull Day"));
    }

    @Test
    void shouldReturnBadRequestWhenDuplicatedName() throws Exception {
        TrainingProgramCreateRequestDTO dto =
                new TrainingProgramCreateRequestDTO(
                        "Leg Day",
                        "Leg workout"
                );

        mockMvc.perform(post("/training-programs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/training-programs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeactivateTrainingProgram() throws Exception {
        TrainingProgramCreateRequestDTO dto =
                new TrainingProgramCreateRequestDTO(
                        "Push Day",
                        "Chest and triceps workout"
                );

        MvcResult result = mockMvc.perform(post("/training-programs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(patch("/training-programs/{id}/deactivate", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldListAfterDeactivateTrainingProgram() throws Exception {
        TrainingProgramCreateRequestDTO dto = new TrainingProgramCreateRequestDTO(
                "Push Day",
                "Chest and triceps workout");
        MvcResult result = mockMvc.perform(post("/training-programs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(patch("/training-programs/{id}/deactivate", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/training-programs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void shouldUpdateTrainingProgram() throws Exception {
        TrainingProgramCreateRequestDTO dto = new TrainingProgramCreateRequestDTO(
                "Push Day",
                "Chest and triceps workout");

        MvcResult result = mockMvc.perform(post("/training-programs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        TrainingProgramUpdateDTO updateDTO = new TrainingProgramUpdateDTO(
                "Leg Day",
                "Leg workout");

        mockMvc.perform(put("/training-programs/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNoContent());


        mockMvc.perform(get("/training-programs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Leg Day"))
                .andExpect(jsonPath("$.content[0].description").value("Leg workout"));
    }

    public Long extractId(MvcResult result) {
        String location = result.getResponse().getHeader("Location");
        assertNotNull(location, "Location header must be present");

        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }
}
