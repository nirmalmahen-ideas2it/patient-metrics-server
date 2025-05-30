package com.ideas2it.training.patient.metrics.repository;

import com.ideas2it.training.patient.metrics.model.AuditPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@TestPropertySource(properties = {
    "spring.cloud.config.enabled=false",
    "spring.cloud.vault.enabled=false",
    "spring.cloud.consul.enabled=false"
})
class AuditDataRepositoryTest {

    @Autowired
    private AuditDataRepository auditDataRepository;

    @BeforeEach
    void setUp() {
        auditDataRepository.deleteAll();

        AuditPayload payload1 = AuditPayload
            .builder()
            .entityId(1L)
            .patientId(1L)
            .userId(1L)
            .entityType("Patient")
            .logDate(LocalDateTime.now().minusDays(1))
            .build();
        AuditPayload payload2 = AuditPayload
            .builder()
            .entityId(2L)
            .patientId(2L)
            .userId(2L)
            .entityType("Doctor")
            .logDate(LocalDateTime.now().minusDays(2))
            .build();
        AuditPayload payload3 = AuditPayload
            .builder()
            .entityId(3L)
            .patientId(3L)
            .userId(3L)
            .entityType("Patient")
            .logDate(LocalDateTime.now().minusDays(3))
            .build();

        auditDataRepository.save(payload1);
        auditDataRepository.save(payload2);
        auditDataRepository.save(payload3);
    }

    @Test
    void testFindByUserIdAndLogDateBetween() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(2);
        LocalDateTime endDate = LocalDateTime.now();

        List<AuditPayload> results = auditDataRepository.findByUserIdAndLogDateBetween(1L, startDate, endDate);

        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getPatientId());
    }

    @Test
    void testFindByPatientIdAndLogDateBetween() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(4);
        LocalDateTime endDate = LocalDateTime.now();

        List<AuditPayload> results = auditDataRepository.findByPatientIdAndLogDateBetween(2L, startDate, endDate);

        assertEquals(1, results.size());
        assertEquals(2L, results.get(0).getUserId());
    }

    @Test
    void testFindByEntityTypeAndLogDateBetween() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(4);
        LocalDateTime endDate = LocalDateTime.now();

        List<AuditPayload> results = auditDataRepository.findByEntityTypeAndLogDateBetween("Patient", startDate, endDate);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(payload -> "Patient".equals(payload.getEntityType())));
    }

    @Test
    void testFindByEntityId() {
        List<AuditPayload> results = auditDataRepository.findByEntityId(1L);

        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getUserId());
    }
}
