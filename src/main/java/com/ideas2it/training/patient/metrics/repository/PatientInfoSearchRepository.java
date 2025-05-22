package com.ideas2it.training.patient.metrics.repository;

import com.ideas2it.training.patient.metrics.model.patient.PatientInfoDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientInfoSearchRepository extends ElasticsearchRepository<PatientInfoDocument, Long> {
    List<PatientInfoDocument> findByFirstNameContainingIgnoreCase(String firstName);

    List<PatientInfoDocument> findByLastNameContainingIgnoreCase(String lastName);

    List<PatientInfoDocument> findByMedicalRecordNumber(String medicalRecordNumber);

    List<PatientInfoDocument> findByBirthDate(LocalDate birthDate);
}
