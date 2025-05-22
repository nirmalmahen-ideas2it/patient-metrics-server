package com.ideas2it.training.patient.metrics.model.patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for returning Physician data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhysicianInfo {
    private Long id;
    private String name;
    private String contactNumber;
    private String specialization;
    private String licenseNumber;
    private String hospital;
}

