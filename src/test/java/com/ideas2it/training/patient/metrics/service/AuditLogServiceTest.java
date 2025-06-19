package com.ideas2it.training.patient.metrics.service;

import com.ideas2it.training.patient.metrics.model.AuditLogFilter;
import com.ideas2it.training.patient.metrics.model.AuditPayload;
import com.ideas2it.training.patient.metrics.repository.AuditDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuditLogServiceTest {

    private AuditLogService auditLogService;

    @Mock
    private MongoTemplate mockMongoTemplate;

    @Mock
    private AuditDataRepository mockAuditRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        auditLogService = new AuditLogService(mockMongoTemplate, mockAuditRepository);
    }

    @Test
    void testGetLogsByUserIdAndDateRange() {
        // Arrange
        Long userId = 101L;
        LocalDateTime from = LocalDateTime.now().minusDays(5);
        LocalDateTime to = LocalDateTime.now();
        AuditPayload payload = new AuditPayload();
        when(mockAuditRepository.findByUserIdAndLogDateBetween(userId, from, to))
                .thenReturn(Collections.singletonList(payload));

        // Act
        List<AuditPayload> result = auditLogService.getLogsByUserIdAndDateRange(userId, from, to);

        // Assert
        assertEquals(1, result.size());
        verify(mockAuditRepository).findByUserIdAndLogDateBetween(userId, from, to);
    }

    @Test
    void testGetLogsByPatientIdAndDateRange() {
        // Arrange
        Long patientId = 201L;
        LocalDateTime from = LocalDateTime.now().minusDays(5);
        LocalDateTime to = LocalDateTime.now();
        AuditPayload payload = new AuditPayload();
        when(mockAuditRepository.findByPatientIdAndLogDateBetween(patientId, from, to))
                .thenReturn(Collections.singletonList(payload));

        // Act
        List<AuditPayload> result = auditLogService.getLogsByPatientIdAndDateRange(patientId, from, to);

        // Assert
        assertEquals(1, result.size());
        verify(mockAuditRepository).findByPatientIdAndLogDateBetween(patientId, from, to);
    }

    @Test
    void testGetLogsByEntityTypeAndDateRange() {
        // Arrange
        String entityType = "Patient";
        LocalDateTime from = LocalDateTime.now().minusDays(5);
        LocalDateTime to = LocalDateTime.now();
        AuditPayload payload = new AuditPayload();
        when(mockAuditRepository.findByEntityTypeAndLogDateBetween(entityType, from, to))
                .thenReturn(Collections.singletonList(payload));

        // Act
        List<AuditPayload> result = auditLogService.getLogsByEntityTypeAndDateRange(entityType, from, to);

        // Assert
        assertEquals(1, result.size());
        verify(mockAuditRepository).findByEntityTypeAndLogDateBetween(entityType, from, to);
    }

    @Test
    void testGetLogsByEntityId() {
        // Arrange
        Long entityId = 301L;
        AuditPayload payload = new AuditPayload();
        when(mockAuditRepository.findByEntityId(entityId))
                .thenReturn(Collections.singletonList(payload));

        // Act
        List<AuditPayload> result = auditLogService.getLogsByEntityId(entityId);

        // Assert
        assertEquals(1, result.size());
        verify(mockAuditRepository).findByEntityId(entityId);
    }

    @Test
    void testGetFilteredAuditLogs_ValidFilter_ReturnsResults() {
        AuditLogFilter filter = AuditLogFilter.builder()
                .userId(1L)
                .from(LocalDateTime.now().minusDays(1))
                .to(LocalDateTime.now())
                .build();

        List<AuditPayload> expected = Collections.singletonList(new AuditPayload());
        when(mockMongoTemplate.find(any(Query.class), eq(AuditPayload.class))).thenReturn(expected);

        List<AuditPayload> result = auditLogService.getFilteredAuditLogs(filter);

        assertEquals(1, result.size());
        verify(mockMongoTemplate, times(1)).find(any(Query.class), eq(AuditPayload.class));
    }

    @Test
    void testGetFilteredAuditLogs_InvalidUserId_ThrowsException() {
        AuditLogFilter filter = AuditLogFilter.builder().userId(-1L).build();
        assertThrows(IllegalArgumentException.class, () -> auditLogService.getFilteredAuditLogs(filter));
    }

    @Test
    void testGetFilteredAuditLogs_NullFilter_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> auditLogService.getFilteredAuditLogs(null));
    }
}
