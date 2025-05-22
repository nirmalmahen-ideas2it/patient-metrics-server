package com.ideas2it.training.patient.metrics.service;

import com.ideas2it.training.patient.metrics.model.patient.PatientInfoDocument;
import com.ideas2it.training.patient.metrics.repository.PatientInfoSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PatientSearchServiceTest {

    private PatientSearchService patientSearchService;

    @Mock
    private PatientInfoSearchRepository mockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patientSearchService = new PatientSearchService(mockRepository);
    }

    @Test
    void testSearchByBirthDate_Success() {
        // Arrange
        String dobString = "1990-05-15";
        LocalDate dob = LocalDate.parse(dobString);
        PatientInfoDocument patient = PatientInfoDocument.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .birthDate(dob)
            .build();
        when(mockRepository.findByBirthDate(dob)).thenReturn(Collections.singletonList(patient));

        // Act
        List<PatientInfoDocument> results = patientSearchService.searchByBirthDate(dobString);

        // Assert
        assertEquals(1, results.size());
        assertEquals("John", results.get(0).getFirstName());
        verify(mockRepository).findByBirthDate(dob);
    }

    @Test
    void testSearchByBirthDate_NoResults() {
        // Arrange
        String dobString = "2000-01-01";
        LocalDate dob = LocalDate.parse(dobString);
        when(mockRepository.findByBirthDate(dob)).thenReturn(Collections.emptyList());

        // Act
        List<PatientInfoDocument> results = patientSearchService.searchByBirthDate(dobString);

        // Assert
        assertEquals(0, results.size());
        verify(mockRepository).findByBirthDate(dob);
    }

    @Test
    void testFindByFirstNameContainingIgnoreCase_Success() {
        // Arrange
        String name = "john";
        PatientInfoDocument patient1 = PatientInfoDocument.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();
        PatientInfoDocument patient2 = PatientInfoDocument.builder()
            .id(2L)
            .firstName("Johnny")
            .lastName("Smith")
            .build();
        when(mockRepository.findByFirstNameContainingIgnoreCase(name)).thenReturn(Arrays.asList(patient1, patient2));

        // Act
        List<PatientInfoDocument> results = patientSearchService.findByFirstNameContainingIgnoreCase(name);

        // Assert
        assertEquals(2, results.size());
        verify(mockRepository).findByFirstNameContainingIgnoreCase(name);
    }

    @Test
    void testFindByLastNameContainingIgnoreCase_Success() {
        // Arrange
        String name = "doe";
        PatientInfoDocument patient = PatientInfoDocument.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();
        when(mockRepository.findByLastNameContainingIgnoreCase(name)).thenReturn(Collections.singletonList(patient));

        // Act
        List<PatientInfoDocument> results = patientSearchService.findByLastNameContainingIgnoreCase(name);

        // Assert
        assertEquals(1, results.size());
        assertEquals("Doe", results.get(0).getLastName());
        verify(mockRepository).findByLastNameContainingIgnoreCase(name);
    }

    @Test
    void testFindByMedicalRecordNumber_Success() {
        // Arrange
        String mrn = "MRN123";
        PatientInfoDocument patient = PatientInfoDocument.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .medicalRecordNumber(mrn)
            .build();
        when(mockRepository.findByMedicalRecordNumber(mrn)).thenReturn(Collections.singletonList(patient));

        // Act
        List<PatientInfoDocument> results = patientSearchService.findByMedicalRecordNumber(mrn);

        // Assert
        assertEquals(1, results.size());
        assertEquals("MRN123", results.get(0).getMedicalRecordNumber());
        verify(mockRepository).findByMedicalRecordNumber(mrn);
    }

    @Test
    void testFindByMedicalRecordNumber_NoResults() {
        // Arrange
        String mrn = "INVALID_MRN";
        when(mockRepository.findByMedicalRecordNumber(mrn)).thenReturn(Collections.emptyList());

        // Act
        List<PatientInfoDocument> results = patientSearchService.findByMedicalRecordNumber(mrn);

        // Assert
        assertEquals(0, results.size());
        verify(mockRepository).findByMedicalRecordNumber(mrn);
    }
}
