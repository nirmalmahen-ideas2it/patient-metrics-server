package com.ideas2it.training.patient.metrics.repository;


import com.ideas2it.training.patient.metrics.model.patient.PatientInfoDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataElasticsearchTest
@TestPropertySource(properties = "VAULT_URL=http://localhost:8200")
class PatientInfoSearchRepositoryTest {

    @Autowired
    private PatientInfoSearchRepository patientInfoSearchRepository;

    @BeforeEach
    void setUp() {
        patientInfoSearchRepository.deleteAll();

        PatientInfoDocument patient1 = PatientInfoDocument.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .medicalRecordNumber("MRN123")
            .birthDate(LocalDate.of(1990, 5, 15))
            .build();

        PatientInfoDocument patient2 = PatientInfoDocument.builder()
            .id(2L)
            .firstName("Jane")
            .lastName("Smith")
            .medicalRecordNumber("MRN124")
            .birthDate(LocalDate.of(1985, 8, 20))
            .build();

        PatientInfoDocument patient3 = PatientInfoDocument.builder()
            .id(3L)
            .firstName("Johnny")
            .lastName("Doe")
            .medicalRecordNumber("MRN125")
            .birthDate(LocalDate.of(1990, 5, 15))
            .build();

        patientInfoSearchRepository.save(patient1);
        patientInfoSearchRepository.save(patient2);
        patientInfoSearchRepository.save(patient3);
    }

    @Test
    void testFindByFirstNameContainingIgnoreCase() {
        List<PatientInfoDocument> results = patientInfoSearchRepository.findByFirstNameContainingIgnoreCase("john");

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(patient -> patient.getFirstName().toLowerCase().contains("john")));
    }

    @Test
    void testFindByLastNameContainingIgnoreCase() {
        List<PatientInfoDocument> results = patientInfoSearchRepository.findByLastNameContainingIgnoreCase("doe");

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(patient -> patient.getLastName().toLowerCase().contains("doe")));
    }

    @Test
    void testFindByMedicalRecordNumber() {
        List<PatientInfoDocument> results = patientInfoSearchRepository.findByMedicalRecordNumber("MRN123");

        assertEquals(1, results.size());
        assertEquals("John", results.get(0).getFirstName());
    }

    @Test
    void testFindByBirthDate() {
        List<PatientInfoDocument> results = patientInfoSearchRepository.findByBirthDate(LocalDate.of(1990, 5, 15));

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(patient -> patient.getBirthDate().equals(LocalDate.of(1990, 5, 15))));
    }
}
