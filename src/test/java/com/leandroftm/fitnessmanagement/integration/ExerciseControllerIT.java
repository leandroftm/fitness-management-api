package com.leandroftm.fitnessmanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandroftm.fitnessmanagement.domain.enums.MuscleGroup;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.exercise.ExerciseUpdateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExerciseControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //CREATE
    @Test
    void shouldCreateActiveExercise() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        mockMvc.perform(post("/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/exercises/")));
    }

    @Test
    void shouldReturnBadRequestWhenExerciseNameIsDuplicated() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        ExerciseCreateRequestDTO exercise2Dto = createExercise(
                "Bench Press",
                "lift a weight from the floor to a standing position, keeping it close to your body, and then return it with control",
                "https://www.youtube.com/shorts/ZaTM37cfiDs",
                MuscleGroup.LEGS
        );

        mockMvc.perform(post("/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exercise2Dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("already exists"));
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exercise2Dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/exercises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[*].name").value(hasItems("Bench Press", "Dead Lift")))
                .andExpect(jsonPath("$.content[*].active").value(everyItem(is(true))));
    }

    @Test
    void shouldNotListInactiveExercises() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();


        MvcResult result = mockMvc.perform(post("/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));

        mockMvc.perform(patch("/exercises/{id}/deactivate", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(is(false)));

        mockMvc.perform(get("/exercises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    //UPDATE
    @Test
    void shouldUpdateActiveExercise() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        MvcResult result = mockMvc.perform(post("/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));

        ExerciseUpdateDTO updateDto = new ExerciseUpdateDTO(
                "Inclined Bench Press",
                "The bench press is a fundamental upper-body strength exercise where you push a weight upwards while lying on a bench," +
                        " primarily targeting the chest (pectorals), shoulders (deltoids), and triceps, with core and back muscles providing stability.",
                "https://www.youtube.com/shorts/98HWfiRonkE",
                MuscleGroup.CHEST
        );

        mockMvc.perform(put("/exercises/{id}", id)
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(is(true)));

        mockMvc.perform(patch("/exercises/{id}/deactivate", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(is(false)));

        ExerciseUpdateDTO updateDto = new ExerciseUpdateDTO(
                "Inclined Bench Press",
                "The bench press is a fundamental upper-body strength exercise where you push a weight upwards while lying on a bench," +
                        " primarily targeting the chest (pectorals), shoulders (deltoids), and triceps, with core and back muscles providing stability.",
                "https://www.youtube.com/shorts/98HWfiRonkE",
                MuscleGroup.CHEST
        );

        mockMvc.perform(put("/exercises/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }

    //ACTIVATE/DEACTIVATE
    @Test
    void shouldDeactivateActiveExercise() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        MvcResult result = mockMvc.perform(post("/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(is(true)));

        mockMvc.perform(patch("/exercises/{id}/deactivate", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(is(false)));
    }

    @Test
    void shouldActivateDisabledExercise() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        MvcResult result = mockMvc.perform(post("/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(patch("/exercises/{id}/deactivate", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(is(false)));

        mockMvc.perform(patch("/exercises/{id}/activate", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(is(true)));
    }

    @Test
    void shouldReturnBadRequestWhenDeactivatingAlreadyDisabledExercise() throws Exception {
        ExerciseCreateRequestDTO exerciseDto = exercise();

        MvcResult result = mockMvc.perform(post("/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exerciseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(patch("/exercises/{id}/deactivate", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(is(false)));

        mockMvc.perform(patch("/exercises/{id}/deactivate", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(("inactive"))));
    }

    //HELPER
    private ExerciseCreateRequestDTO exercise() {
        return createExercise(
                "Bench Press",
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
