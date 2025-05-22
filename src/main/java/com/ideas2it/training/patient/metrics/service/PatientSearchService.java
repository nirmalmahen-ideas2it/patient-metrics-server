package com.ideas2it.training.patient.metrics.service;

import com.ideas2it.training.patient.metrics.model.patient.PatientInfoDocument;
import com.ideas2it.training.patient.metrics.repository.PatientInfoSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientSearchService {

    private static final String INDEX_NAME = "patient_info";
    private final PatientInfoSearchRepository repository;

    public List<PatientInfoDocument> searchByBirthDate(String dobString) {
        LocalDate dob = LocalDate.parse(dobString, DateTimeFormatter.ISO_DATE);

        return repository.findByBirthDate(dob);

    }

    public List<PatientInfoDocument> findByFirstNameContainingIgnoreCase(String name) {
        return repository.findByFirstNameContainingIgnoreCase(name);
    }

    public List<PatientInfoDocument> findByLastNameContainingIgnoreCase(String name) {
        return repository.findByLastNameContainingIgnoreCase(name);
    }

    public List<PatientInfoDocument> findByMedicalRecordNumber(String mrn) {
        return repository.findByMedicalRecordNumber(mrn);
    }
}

