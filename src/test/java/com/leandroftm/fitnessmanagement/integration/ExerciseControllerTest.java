package com.leandroftm.fitnessmanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandroftm.fitnessmanagement.domain.enums.MuscleGroup;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseUpdateDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = "USER")
@ActiveProfiles("test")
@Transactional
public class ExerciseControllerTest {

    //.\mvnw -Dtest=ExerciseControllerTest test

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //CREATE
    @Test
    void shouldCreateActiveExercise() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        mockMvc.perform(post("/exercises")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/exercises/")))
                .andDo(print());

    }

    @Test
    void shouldReturnBadRequestWhenExerciseNameIsDuplicated() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        mockMvc.perform(post("/exercises")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/exercises")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isBadRequest());
    }

    //LIST
    @Test
    void shouldListActiveExercises() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();
        ExerciseCreateRequestDTO exercise2Dto = createExercise(
                "Dead Lift",
                "lift a weight from the floor to a standing position, keeping it close to your body, and then return it with control",
                "https://www.youtube.com/shorts/ZaTM37cfiDs",
                MuscleGroup.LEGS
        );

        mockMvc.perform(post("/exercises")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/exercises")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exercise2Dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/exercises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[*].active").value(everyItem(is(true))))
                .andDo(print())
        ;
    }

    @Test
    void shouldNotListInactiveExercises() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();


        MvcResult result = mockMvc.perform(post("/exercises")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        mockMvc.perform(patch("/exercises/{id}/deactivate", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(is("INACTIVE")));

        mockMvc.perform(get("/exercises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    //UPDATE
    @Test
    void shouldUpdateActiveExercise() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        MvcResult result = mockMvc.perform(post("/exercises")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        ExerciseUpdateDTO updateDto = new ExerciseUpdateDTO(
                "Inclined Bench Press",
                "The bench press is a fundamental upper-body strength exercise where you push a weight upwards while lying on a bench," +
                        " primarily targeting the chest (pectorals), shoulders (deltoids), and triceps, with core and back muscles providing stability.",
                "https://www.youtube.com/shorts/98HWfiRonkE",
                MuscleGroup.CHEST
        );

        mockMvc.perform(put("/exercises/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Inclined Bench Press"))
                .andExpect(jsonPath("$.videoUrl").value("https://www.youtube.com/shorts/98HWfiRonkE"));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingInactiveExercise() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        MvcResult result = mockMvc.perform(post("/exercises")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(is("ACTIVE")));

        mockMvc.perform(patch("/exercises/{id}/deactivate", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(is("INACTIVE")));

        ExerciseUpdateDTO updateDto = new ExerciseUpdateDTO(
                "Inclined Bench Press",
                "The bench press is a fundamental upper-body strength exercise where you push a weight upwards while lying on a bench," +
                        " primarily targeting the chest (pectorals), shoulders (deltoids), and triceps, with core and back muscles providing stability.",
                "https://www.youtube.com/shorts/98HWfiRonkE",
                MuscleGroup.CHEST
        );

        mockMvc.perform(put("/exercises/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }

    //ACTIVATE/DEACTIVATE
    @Test
    void shouldDeactivateActiveExercise() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        MvcResult result = mockMvc.perform(post("/exercises")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(is("ACTIVE")));

        mockMvc.perform(patch("/exercises/{id}/deactivate", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(is("INACTIVE")));
    }

    @Test
    void shouldActivateDisabledExercise() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        MvcResult result = mockMvc.perform(post("/exercises")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(patch("/exercises/{id}/deactivate", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(is("INACTIVE")));

        mockMvc.perform(patch("/exercises/{id}/activate", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(is("ACTIVE")));
    }

    @Test
    void shouldReturnBadRequestWhenDeactivatingAlreadyDisabledExercise() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        MvcResult result = mockMvc.perform(post("/exercises")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(patch("/exercises/{id}/deactivate", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(is("INACTIVE")));

        mockMvc.perform(patch("/exercises/{id}/deactivate", id)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(("inactive"))));
    }

    //HELPER
    private ExerciseCreateRequestDTO exercise() {
        return createExercise(
                "Bench Press" + System.nanoTime(),
                "The bench press is a fundamental upper-body strength exercise where you push a weight upwards while lying on a bench," +
                        " primarily targeting the chest (pectorals), shoulders (deltoids), and triceps, with core and back muscles providing stability.",
                "https://www.youtube.com/shorts/hWbUlkb5Ms4",
                MuscleGroup.CHEST
        );
    }

    private ExerciseCreateRequestDTO createExercise(String name, String description, String videoUrl, MuscleGroup muscleGroup) {
        return new ExerciseCreateRequestDTO(name, description, videoUrl, muscleGroup);
    }

    private Long extractId(MvcResult result) {
        String location = result.getResponse().getHeader("Location");
        assertNotNull(location, "Location header must be present");
        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }
}
