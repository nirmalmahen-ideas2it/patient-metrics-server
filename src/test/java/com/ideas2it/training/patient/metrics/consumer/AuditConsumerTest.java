package com.ideas2it.training.patient.metrics.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideas2it.training.patient.metrics.model.AuditPayload;
import com.ideas2it.training.patient.metrics.repository.AuditDataRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuditConsumerTest {

    private AuditConsumer auditConsumer;

    @Mock
    private ObjectMapper mockObjectMapper;

    @Mock
    private AuditDataRepository mockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        auditConsumer = new AuditConsumer(mockObjectMapper, mockRepository);
    }

    @Test
    void testConsumeAuditData_Success() throws JsonProcessingException {
        // Arrange
        String validJson = "{\n" +
            "  \"userId\": 1,\n" +
            "  \"action\": \"CREATE\",\n" +
            "  \"entityId\": 1,\n" +
            "  \"patientId\": 1\n" +
            "}";
        AuditPayload auditPayload = AuditPayload
            .builder()
            .userId(1L)
            .action("CREATE")
            .entityId(1L)
            .patientId(1L)
            .build();
        ConsumerRecord<String, String> record = new ConsumerRecord<>("audit-data", 0, 0, null, validJson);

        when(mockObjectMapper.readValue(validJson, AuditPayload.class)).thenReturn(auditPayload);

        // Act
        auditConsumer.consumeAuditData(record);

        // Assert
        verify(mockObjectMapper).readValue(validJson, AuditPayload.class);
        verify(mockRepository).save(auditPayload);
    }

    @Test
    void testConsumeAuditData_InvalidJson() throws JsonProcessingException {
        // Arrange
        String invalidJson = "invalid-json";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("audit-data", 0, 0, null, invalidJson);

        when(mockObjectMapper.readValue(invalidJson, AuditPayload.class)).thenThrow(JsonProcessingException.class);

        // Act & Assert
        assertThrows(JsonProcessingException.class, () -> auditConsumer.consumeAuditData(record));
        verify(mockObjectMapper).readValue(invalidJson, AuditPayload.class);
        verifyNoInteractions(mockRepository);
    }

    @Test
    void testConsumeAuditData_RepositorySaveFailure() throws JsonProcessingException {
        // Arrange
        String validJson = "{\n" +
            "  \"userId\": 1,\n" +
            "  \"action\": \"CREATE\",\n" +
            "  \"entityId\": 1,\n" +
            "  \"patientId\": 1\n" +
            "}";
        AuditPayload auditPayload = AuditPayload
            .builder()
            .userId(1L)
            .action("CREATE")
            .entityId(1L)
            .patientId(1L)
            .build();
        ConsumerRecord<String, String> record = new ConsumerRecord<>("audit-data", 0, 0, null, validJson);

        when(mockObjectMapper.readValue(validJson, AuditPayload.class)).thenReturn(auditPayload);
        doThrow(RuntimeException.class).when(mockRepository).save(auditPayload);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> auditConsumer.consumeAuditData(record));
        verify(mockObjectMapper).readValue(validJson, AuditPayload.class);
        verify(mockRepository).save(auditPayload);
    }

}
