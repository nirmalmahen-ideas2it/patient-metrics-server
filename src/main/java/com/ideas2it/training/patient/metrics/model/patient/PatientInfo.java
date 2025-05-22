package com.ideas2it.training.patient.metrics.model.patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for returning Patient data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientInfo {
    private Long id;
    private String medicalRecordNumber;
    private LocalDate startOfCareDate;
    private String status;
    private String firstName;
    private String lastName;
    private String sex;
    private LocalDate birthDate;
    private ReferralInfo referralInfo;
    private Diagnoses diagnoses;
    private PhysicianInfo primaryPhysician;
}

