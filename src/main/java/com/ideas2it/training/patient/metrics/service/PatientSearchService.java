package com.ideas2it.training.patient.metrics.service;

import com.ideas2it.training.patient.metrics.model.patient.PatientInfoDocument;
import com.ideas2it.training.patient.metrics.repository.PatientInfoSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for searching patients with robust edge case handling.
 * Edge cases handled for all methods:
 * - Null input
 * - Empty or whitespace input
 * - Invalid format (for date)
 * - Non-existent date (for date)
 * - Future date (for date)
 * - Unusually old date (for date)
 * - Special characters
 * - Very long input
 * - Input with only whitespace
 * - Repository/database errors
 * - No results found (returns empty list)
 */
@Service
@RequiredArgsConstructor
public class PatientSearchService {

    private static final Logger log = LoggerFactory.getLogger(PatientSearchService.class);
    private static final String INDEX_NAME = "patient_info";
    private final PatientInfoSearchRepository repository;
    private static final int MAX_INPUT_LENGTH = 100;
    private static final LocalDate MIN_DATE = LocalDate.of(1900, 1, 1);

    /**
     * Searches for patients by birth date string (yyyy-MM-dd).
     * 
     * @param dobString the date of birth string
     * @return list of matching patients
     * @throws IllegalArgumentException for invalid input
     * @throws PatientSearchException   for repository errors
     */
    public List<PatientInfoDocument> searchByBirthDate(String dobString) {
        if (dobString == null || dobString.trim().isEmpty()) {
            throw new IllegalArgumentException("Date of birth string must not be null or empty");
        }
        if (dobString.length() > MAX_INPUT_LENGTH) {
            throw new IllegalArgumentException("Date of birth string is too long");
        }
        LocalDate dob;
        try {
            dob = LocalDate.parse(dobString.trim(), DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd", e);
        }
        if (dob.isAfter(LocalDate.now())) {
            log.warn("Future birth date provided: {}", dob);
        }
        if (dob.isBefore(MIN_DATE)) {
            log.warn("Unusually old birth date provided: {}", dob);
        }
        try {
            List<PatientInfoDocument> results = repository.findByBirthDate(dob);
            return results != null ? results : Collections.emptyList();
        } catch (Exception e) {
            throw new PatientSearchException("Database error during searchByBirthDate", e);
        }
    }

    /**
     * Searches for patients by first name (case-insensitive, partial match).
     * 
     * @param name the first name
     * @return list of matching patients
     * @throws IllegalArgumentException for invalid input
     * @throws PatientSearchException   for repository errors
     */
    public List<PatientInfoDocument> findByFirstNameContainingIgnoreCase(String name) {
        String validated = validateAndTrimString(name, "First name");
        try {
            List<PatientInfoDocument> results = repository.findByFirstNameContainingIgnoreCase(validated);
            return results != null ? results : Collections.emptyList();
        } catch (Exception e) {
            throw new PatientSearchException("Database error during findByFirstNameContainingIgnoreCase", e);
        }
    }

    /**
     * Searches for patients by last name (case-insensitive, partial match).
     * 
     * @param name the last name
     * @return list of matching patients
     * @throws IllegalArgumentException for invalid input
     * @throws PatientSearchException   for repository errors
     */
    public List<PatientInfoDocument> findByLastNameContainingIgnoreCase(String name) {
        String validated = validateAndTrimString(name, "Last name");
        try {
            List<PatientInfoDocument> results = repository.findByLastNameContainingIgnoreCase(validated);
            return results != null ? results : Collections.emptyList();
        } catch (Exception e) {
            throw new PatientSearchException("Database error during findByLastNameContainingIgnoreCase", e);
        }
    }

    /**
     * Searches for patients by medical record number (exact match).
     * 
     * @param mrn the medical record number
     * @return list of matching patients
     * @throws IllegalArgumentException for invalid input
     * @throws PatientSearchException   for repository errors
     */
    public List<PatientInfoDocument> findByMedicalRecordNumber(String mrn) {
        String validated = validateAndTrimString(mrn, "Medical record number");
        try {
            List<PatientInfoDocument> results = repository.findByMedicalRecordNumber(validated);
            return results != null ? results : Collections.emptyList();
        } catch (Exception e) {
            throw new PatientSearchException("Database error during findByMedicalRecordNumber", e);
        }
    }

    /**
     * Validates and trims input string for search methods.
     * 
     * @param input     the input string
     * @param fieldName the field name for error messages
     * @return trimmed and validated string
     * @throws IllegalArgumentException for invalid input
     */
    private String validateAndTrimString(String input, String fieldName) {
        if (input == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be empty or whitespace");
        }
        if (trimmed.length() > MAX_INPUT_LENGTH) {
            throw new IllegalArgumentException(fieldName + " is too long");
        }
        return trimmed;
    }
}
