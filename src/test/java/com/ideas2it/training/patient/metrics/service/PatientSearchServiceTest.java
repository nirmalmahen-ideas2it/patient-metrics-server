package com.ideas2it.training.patient.metrics.service;

import com.ideas2it.training.patient.metrics.model.patient.PatientInfoDocument;
import com.ideas2it.training.patient.metrics.repository.PatientInfoSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientSearchServiceTest {

    @Mock
    private PatientInfoSearchRepository repository;

    @InjectMocks
    private PatientSearchService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- searchByBirthDate ---
    @Test
    void testSearchByBirthDate_NullInput_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.searchByBirthDate(null));
    }

    @Test
    void testSearchByBirthDate_EmptyInput_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.searchByBirthDate("   "));
    }

    @Test
    void testSearchByBirthDate_InvalidFormat_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.searchByBirthDate("not-a-date"));
    }

    @Test
    void testSearchByBirthDate_NonExistentDate_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.searchByBirthDate("2023-02-30"));
    }

    @Test
    void testSearchByBirthDate_FutureDate_LogsWarningAndReturns() {
        LocalDate future = LocalDate.now().plusYears(10);
        when(repository.findByBirthDate(future)).thenReturn(Collections.emptyList());
        List<PatientInfoDocument> result = service.searchByBirthDate(future.toString());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchByBirthDate_OldDate_LogsWarningAndReturns() {
        LocalDate old = LocalDate.of(1800, 1, 1);
        when(repository.findByBirthDate(old)).thenReturn(Collections.emptyList());
        List<PatientInfoDocument> result = service.searchByBirthDate(old.toString());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchByBirthDate_RepositoryError_ThrowsException() {
        LocalDate date = LocalDate.of(2000, 1, 1);
        when(repository.findByBirthDate(date)).thenThrow(new RuntimeException("DB error"));
        assertThrows(PatientSearchException.class, () -> service.searchByBirthDate(date.toString()));
    }

    @Test
    void testSearchByBirthDate_Valid_ReturnsResults() {
        LocalDate date = LocalDate.of(2000, 1, 1);
        PatientInfoDocument doc = new PatientInfoDocument();
        when(repository.findByBirthDate(date)).thenReturn(Collections.singletonList(doc));
        List<PatientInfoDocument> result = service.searchByBirthDate(date.toString());
        assertEquals(1, result.size());
    }

    // --- findByFirstNameContainingIgnoreCase ---
    @Test
    void testFindByFirstNameContainingIgnoreCase_Null_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.findByFirstNameContainingIgnoreCase(null));
    }

    @Test
    void testFindByFirstNameContainingIgnoreCase_Empty_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.findByFirstNameContainingIgnoreCase("   "));
    }

    @Test
    void testFindByFirstNameContainingIgnoreCase_TooLong_ThrowsException() {
        String longName = "a".repeat(101);
        assertThrows(IllegalArgumentException.class, () -> service.findByFirstNameContainingIgnoreCase(longName));
    }

    @Test
    void testFindByFirstNameContainingIgnoreCase_SpecialChars() {
        String name = "O'Reilly";
        when(repository.findByFirstNameContainingIgnoreCase(name)).thenReturn(Collections.emptyList());
        List<PatientInfoDocument> result = service.findByFirstNameContainingIgnoreCase(name);
        assertNotNull(result);
    }

    @Test
    void testFindByFirstNameContainingIgnoreCase_RepositoryError_ThrowsException() {
        String name = "John";
        when(repository.findByFirstNameContainingIgnoreCase(name)).thenThrow(new RuntimeException("DB error"));
        assertThrows(PatientSearchException.class, () -> service.findByFirstNameContainingIgnoreCase(name));
    }

    // --- findByLastNameContainingIgnoreCase ---
    @Test
    void testFindByLastNameContainingIgnoreCase_Null_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.findByLastNameContainingIgnoreCase(null));
    }

    @Test
    void testFindByLastNameContainingIgnoreCase_Empty_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.findByLastNameContainingIgnoreCase("   "));
    }

    @Test
    void testFindByLastNameContainingIgnoreCase_TooLong_ThrowsException() {
        String longName = "b".repeat(101);
        assertThrows(IllegalArgumentException.class, () -> service.findByLastNameContainingIgnoreCase(longName));
    }

    @Test
    void testFindByLastNameContainingIgnoreCase_SpecialChars() {
        String name = "Smith-Jones";
        when(repository.findByLastNameContainingIgnoreCase(name)).thenReturn(Collections.emptyList());
        List<PatientInfoDocument> result = service.findByLastNameContainingIgnoreCase(name);
        assertNotNull(result);
    }

    @Test
    void testFindByLastNameContainingIgnoreCase_RepositoryError_ThrowsException() {
        String name = "Doe";
        when(repository.findByLastNameContainingIgnoreCase(name)).thenThrow(new RuntimeException("DB error"));
        assertThrows(PatientSearchException.class, () -> service.findByLastNameContainingIgnoreCase(name));
    }

    // --- findByMedicalRecordNumber ---
    @Test
    void testFindByMedicalRecordNumber_Null_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.findByMedicalRecordNumber(null));
    }

    @Test
    void testFindByMedicalRecordNumber_Empty_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.findByMedicalRecordNumber("   "));
    }

    @Test
    void testFindByMedicalRecordNumber_TooLong_ThrowsException() {
        String longMrn = "c".repeat(101);
        assertThrows(IllegalArgumentException.class, () -> service.findByMedicalRecordNumber(longMrn));
    }

    @Test
    void testFindByMedicalRecordNumber_SpecialChars() {
        String mrn = "MRN-1234/2023";
        when(repository.findByMedicalRecordNumber(mrn)).thenReturn(Collections.emptyList());
        List<PatientInfoDocument> result = service.findByMedicalRecordNumber(mrn);
        assertNotNull(result);
    }

    @Test
    void testFindByMedicalRecordNumber_RepositoryError_ThrowsException() {
        String mrn = "MRN-1";
        when(repository.findByMedicalRecordNumber(mrn)).thenThrow(new RuntimeException("DB error"));
        assertThrows(PatientSearchException.class, () -> service.findByMedicalRecordNumber(mrn));
    }
}
