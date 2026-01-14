package com.leandroftm.fitnessmanagement.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandroftm.fitnessmanagement.domain.enums.Gender;
import com.leandroftm.fitnessmanagement.dto.student.AddressCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.student.StudentCreateRequestDTO;
import com.leandroftm.fitnessmanagement.dto.student.StudentUpdateDTO;
import com.leandroftm.fitnessmanagement.infra.client.cep.CepClient;
import com.leandroftm.fitnessmanagement.infra.client.cep.CepResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StudentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CepClient cepClient;

    private static final String CEP_DEFAULT = "01001-000";


    //CREATE
    @Test
    void shouldCreateStudentWhenZipCodeIsValid() throws Exception {

        mockValidCep();
        StudentCreateRequestDTO studentDto = validStudent();

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/students/")));
    }


    @Test
    void shouldReturnBadRequestWhenZipCodeIsInvalid() throws Exception {
        when(cepClient.findZipCode("00000000"))
                .thenReturn(new CepResponseDTO(
                        null,
                        null,
                        null,
                        null,
                        "00000-000",
                        true
                ));

        StudentCreateRequestDTO studentDto = createStudent(
                "Sarah Lyons",
                "sarahlyons@email.com",
                "98 99999-9999",
                Gender.FEMALE,
                "00000-000"
        );

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Invalid zip code")));

        verify(cepClient).findZipCode("00000000");
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsDuplicated() throws Exception {
        mockValidCep();
        StudentCreateRequestDTO studentDto = validStudent();

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated());

        StudentCreateRequestDTO student2Dto = createStudent(
                "Sarah Lyons",
                "owynlyons@email.com",
                "98 99999-9999",
                Gender.FEMALE,
                CEP_DEFAULT
        );

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student2Dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Email already exists")));
    }

    //LIST
    @Test
    void shouldListActiveStudents() throws Exception {
        mockValidCep();
        StudentCreateRequestDTO studentDto = validStudent();
        StudentCreateRequestDTO student2Dto = createStudent(
                "Sarah Lyons",
                "sarahlyons@email.com",
                "98 99999-9999",
                Gender.FEMALE,
                CEP_DEFAULT
        );

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student2Dto)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[*].name").value(hasItems("Owyn Lyons", "Sarah Lyons")))
                .andExpect(jsonPath("$.content[*].email").value(hasItems("owynlyons@email.com", "sarahlyons@email.com")))
                .andExpect(jsonPath("$.content[*].active").value(everyItem(is(true))));
    }

    @Test
    void shouldNotListInactiveStudents() throws Exception {
        mockValidCep();
        StudentCreateRequestDTO studentDto = validStudent();

        MvcResult result = mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(patch("/students/{id}/deactivate", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    //UPDATE
    @Test
    void shouldUpdateActiveStudent() throws Exception {
        mockValidCep();
        StudentCreateRequestDTO studentDto = validStudent();

        MvcResult result = mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        StudentUpdateDTO updateDto = new StudentUpdateDTO(
                "Owyn Sentinel Lyons",
                "99 99999-9999",
                "owynsentinellyons@email.com",
                Gender.MALE,
                new AddressCreateRequestDTO(CEP_DEFAULT, "99", "N/C")
        );

        mockMvc.perform(put("/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].name").value(hasItem("Owyn Sentinel Lyons")))
                .andExpect(jsonPath("$.content[*].email").value(hasItem("owynsentinellyons@email.com")))
                .andExpect(jsonPath("$.content[*].active").value(everyItem(is(true))));
    }

    @Test
    void shouldReturnBadRequestWhenUpdateInactiveStudent() throws Exception {
        mockValidCep();
        StudentCreateRequestDTO studentDto = validStudent();

        MvcResult result = mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);
        mockMvc.perform(patch("/students/{id}/deactivate", id))
                .andExpect(status().isNoContent());

        StudentUpdateDTO updateDto = new StudentUpdateDTO(
                "Owyn Sentinel Lyons",
                "99 99999-9999",
                "owynsentinellyons@email.com",
                Gender.MALE,
                new AddressCreateRequestDTO(CEP_DEFAULT, "99", "N/C")
        );

        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        mockMvc.perform(put("/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("inactive")));
    }

    //DEACTIVE
    @Test
    void shouldDeactivateActiveStudent() throws Exception {
        mockValidCep();
        StudentCreateRequestDTO studentDto = validStudent();

        MvcResult result = mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));

        mockMvc.perform(patch("/students/{id}/deactivate", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void shouldReturnBadRequestWhenDeactivateInactiveStudent() throws Exception {
        mockValidCep();
        StudentCreateRequestDTO studentDto = validStudent();

        MvcResult result = mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = extractId(result);

        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));

        mockMvc.perform(patch("/students/{id}/deactivate", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        mockMvc.perform(patch("/students/{id}/deactivate", id))
                .andExpect(status().isBadRequest());
    }

    //HELPER
    private StudentCreateRequestDTO createStudent(String fullName, String email, String phoneNumber, Gender gender, String cep) {
        AddressCreateRequestDTO addressDto = new AddressCreateRequestDTO(
                cep,
                "99",
                "N/C"
        );

        return new StudentCreateRequestDTO(
                fullName,
                email,
                phoneNumber,
                gender,
                addressDto
        );
    }

    private StudentCreateRequestDTO validStudent() {
        return new StudentCreateRequestDTO(
                "Owyn Lyons",
                "owynlyons@email.com",
                "99 99999-9999",
                Gender.MALE,
                new AddressCreateRequestDTO(CEP_DEFAULT, "99", "N/C")
        );
    }

    private void mockValidCep() {
        when(cepClient.findZipCode("01001000"))
                .thenReturn(new CepResponseDTO(
                        "Praça da Sé",
                        "Sé",
                        "São Paulo",
                        "SP",
                        CEP_DEFAULT,
                        false
                ));
    }

    private Long extractId(MvcResult result) {
        String location = result.getResponse().getHeader("Location");
        assertNotNull(location, "Location header must be present");

        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }
}
