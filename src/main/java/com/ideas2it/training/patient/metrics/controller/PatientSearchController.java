package com.ideas2it.training.patient.metrics.controller;


import com.ideas2it.training.patient.metrics.model.patient.PatientInfoDocument;
import com.ideas2it.training.patient.metrics.repository.PatientInfoSearchRepository;
import com.ideas2it.training.patient.metrics.service.PatientSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient-search")
@RequiredArgsConstructor
public class PatientSearchController {

    private final PatientSearchService service;

    @GetMapping("/firstname/{name}")
    public List<PatientInfoDocument> searchByFirstName(@PathVariable String name) {
        return service.findByFirstNameContainingIgnoreCase(name);
    }

    @GetMapping("/lastname/{name}")
    public List<PatientInfoDocument> searchByLastName(@PathVariable String name) {
        return service.findByLastNameContainingIgnoreCase(name);
    }

    @GetMapping("/dob/{dob}") // Format: YYYY-MM-DD
    public List<PatientInfoDocument> searchByDOB(@PathVariable String dob) {
        return service.searchByBirthDate(dob);
    }

    @GetMapping("/mrn/{mrn}")
    public List<PatientInfoDocument> searchByMRN(@PathVariable String mrn) {
        return service.findByMedicalRecordNumber(mrn);
    }
}

