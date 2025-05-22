package com.ideas2it.training.patient.metrics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideas2it.training.patient.metrics.model.patient.PatientInfoDocument;
import com.ideas2it.training.patient.metrics.service.PatientSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PatientSearchController.class,
    excludeAutoConfiguration = {OAuth2ResourceServerAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = "USER")
@TestPropertySource(properties = "VAULT_URL=http://localhost:8200")
class PatientSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientSearchService patientSearchService;

    private PatientInfoDocument patient1;
    private PatientInfoDocument patient2;

    @BeforeEach
    void setUp() {
        patient1 = PatientInfoDocument.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .birthDate(LocalDate.of(1990, 5, 15))
            .medicalRecordNumber("MRN123")
            .build();

        patient2 = PatientInfoDocument.builder()
            .id(2L)
            .firstName("Jane")
            .lastName("Smith")
            .birthDate(LocalDate.of(1985, 8, 20))
            .medicalRecordNumber("MRN124")
            .build();
    }

    @Test
    void testSearchByFirstName() throws Exception {
        Mockito.when(patientSearchService.findByFirstNameContainingIgnoreCase("john"))
            .thenReturn(Arrays.asList(patient1));

        mockMvc.perform(get("/api/patient-search/firstname/{name}", "john"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(patient1))));
    }

    @Test
    void testSearchByLastName() throws Exception {
        Mockito.when(patientSearchService.findByLastNameContainingIgnoreCase("doe"))
            .thenReturn(Arrays.asList(patient1));

        mockMvc.perform(get("/api/patient-search/lastname/{name}", "doe"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(patient1))));
    }

    @Test
    void testSearchByDOB() throws Exception {
        Mockito.when(patientSearchService.searchByBirthDate("1990-05-15"))
            .thenReturn(Arrays.asList(patient1));

        mockMvc.perform(get("/api/patient-search/dob/{dob}", "1990-05-15"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(patient1))));
    }

    @Test
    void testSearchByMRN() throws Exception {
        Mockito.when(patientSearchService.findByMedicalRecordNumber("MRN123"))
            .thenReturn(Arrays.asList(patient1));

        mockMvc.perform(get("/api/patient-search/mrn/{mrn}", "MRN123"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(patient1))));
    }

    @Test
    void testSearchByFirstName_NoResults() throws Exception {
        Mockito.when(patientSearchService.findByFirstNameContainingIgnoreCase("invalid"))
            .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/patient-search/firstname/{name}", "invalid"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));
    }
}
