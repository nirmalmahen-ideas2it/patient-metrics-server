package com.ideas2it.training.patient.metrics.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.ideas2it.training.patient.metrics.model.patient.PatientInfo;
import com.ideas2it.training.patient.metrics.model.patient.PatientInfoDocument;
import com.ideas2it.training.patient.metrics.repository.PatientInfoSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientSearchService {

    private final PatientInfoSearchRepository repository;



    private static final String INDEX_NAME = "patient_info";

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

