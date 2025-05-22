package com.ideas2it.training.patient.metrics.mapper;

import com.ideas2it.training.patient.metrics.model.patient.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PatientInfoElasticsearchMapperTest {

    private final PatientInfoElasticsearchMapper mapper = PatientInfoElasticsearchMapper.INSTANCE;

    @Test
    void testToDocument_Success() {
        // Arrange
        ReferralInfo referralInfo = new ReferralInfo("Dr. Smith");
        Diagnoses diagnoses = new Diagnoses("Hypertension");
        PhysicianInfo physicianInfo = PhysicianInfo.builder()
            .id(1L)
            .licenseNumber("12345")
            .name("Dr. John")
            .hospital("City Hospital")
            .build();
        PatientInfo patientInfo = PatientInfo.builder()
            .id(1L)
            .medicalRecordNumber("MRN123")
            .startOfCareDate(LocalDate.of(2023, 1, 1))
            .status("Active")
            .firstName("John")
            .lastName("Doe")
            .sex("Male")
            .birthDate(LocalDate.of(1990, 5, 15))
            .referralInfo(referralInfo)
            .diagnoses(diagnoses)
            .primaryPhysician(physicianInfo)
            .build();

        // Act
        PatientInfoDocument document = mapper.toDocument(patientInfo);

        // Assert
        assertEquals(patientInfo.getId(), document.getId());
        assertEquals(patientInfo.getMedicalRecordNumber(), document.getMedicalRecordNumber());
        assertEquals(patientInfo.getStartOfCareDate(), document.getStartOfCareDate());
        assertEquals(patientInfo.getBirthDate(), document.getBirthDate());
        assertEquals(referralInfo.getReferrerName(), document.getReferralName());
        assertEquals(diagnoses.getPrimaryDiagnosis(), document.getPrimaryDiagnosis());
        assertEquals(physicianInfo.getId(), document.getPhysicianId());
        assertEquals(physicianInfo.getLicenseNumber(), document.getPhysicianLicenseNumber());
        assertEquals(physicianInfo.getName(), document.getPhysicianName());
        assertEquals(physicianInfo.getHospital(), document.getPhysicianHospital());
    }

    @Test
    void testToDocument_NullFields() {
        // Arrange
        PatientInfo patientInfo = PatientInfo.builder()
            .id(1L)
            .medicalRecordNumber("MRN123")
            .build();

        // Act
        PatientInfoDocument document = mapper.toDocument(patientInfo);

        // Assert
        assertEquals(patientInfo.getId(), document.getId());
        assertEquals(patientInfo.getMedicalRecordNumber(), document.getMedicalRecordNumber());
        assertNull(document.getStartOfCareDate());
        assertNull(document.getBirthDate());
        assertNull(document.getReferralName());
        assertNull(document.getPrimaryDiagnosis());
        assertNull(document.getPhysicianId());
        assertNull(document.getPhysicianLicenseNumber());
        assertNull(document.getPhysicianName());
        assertNull(document.getPhysicianHospital());
    }
}
