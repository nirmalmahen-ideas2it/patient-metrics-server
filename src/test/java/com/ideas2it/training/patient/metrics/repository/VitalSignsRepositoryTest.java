package com.ideas2it.training.patient.metrics.repository;

import com.ideas2it.training.patient.metrics.model.VitalSignResponse;
import com.ideas2it.training.patient.metrics.model.patient.PatientInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@TestPropertySource(properties = "VAULT_URL=http://localhost:8200")
class VitalSignsRepositoryTest {

    @Autowired
    private VitalSignsRepository vitalSignsRepository;

    @BeforeEach
    void setUp() {
        vitalSignsRepository.deleteAll();

        VitalSignResponse vital1 = VitalSignResponse.builder()
            .id(1L)
            .patient(PatientInfo.builder().id(1L).build())
            .pulse(72)
            .build();

        VitalSignResponse vital2 = VitalSignResponse.builder()
            .id(2L)
            .patient(PatientInfo.builder().id(2L).build())
            .bloodPressure("120")
            .build();

        VitalSignResponse vital3 = VitalSignResponse.builder()
            .id(3L)
            .patient(PatientInfo.builder().id(3L).build())
            .temperature(98.6)
            .build();

        vitalSignsRepository.save(vital1);
        vitalSignsRepository.save(vital2);
        vitalSignsRepository.save(vital3);
    }

    @Test
    void testFindAll() {
        List<VitalSignResponse> results = vitalSignsRepository.findAll();
        assertEquals(3, results.size());
    }

}
