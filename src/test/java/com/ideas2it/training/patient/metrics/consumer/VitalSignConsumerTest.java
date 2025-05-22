package com.ideas2it.training.patient.metrics.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideas2it.training.patient.metrics.model.VitalSignResponse;
import com.ideas2it.training.patient.metrics.model.patient.PatientInfo;
import com.ideas2it.training.patient.metrics.repository.VitalSignsRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class VitalSignConsumerTest {

    private VitalSignConsumer vitalSignConsumer;

    @Mock
    private ObjectMapper mockObjectMapper;

    @Mock
    private VitalSignsRepository mockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vitalSignConsumer = new VitalSignConsumer(mockObjectMapper, mockRepository);
    }

    @Test
    void testConsume_Success() throws JsonProcessingException {
        // Arrange
        String validJson = "{\n" +
            "  \"id\": 1,\n" +
            "  \"patient\": {\n" +
            "    \"id\": 123\n" +
            "  },\n" +
            "  \"pulse\": 72\n" +
            "}";
        VitalSignResponse vitalSignResponse =VitalSignResponse
            .builder()
            .id(1L)
            .patient(PatientInfo.builder().id(123L).build())
            .pulse(72)
            .build();
        ConsumerRecord<String, String> record = new ConsumerRecord<>("vital-signs-topic", 0, 0, null, validJson);

        when(mockObjectMapper.readValue(validJson, VitalSignResponse.class)).thenReturn(vitalSignResponse);

        // Act
        vitalSignConsumer.consume(record);

        // Assert
        verify(mockObjectMapper).readValue(validJson, VitalSignResponse.class);
        verify(mockRepository).save(vitalSignResponse);
    }

    @Test
    void testConsume_InvalidJson() throws JsonProcessingException {
        // Arrange
        String invalidJson = "invalid-json";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("vital-signs-topic", 0, 0, null, invalidJson);

        when(mockObjectMapper.readValue(invalidJson, VitalSignResponse.class)).thenThrow(JsonProcessingException.class);

        // Act & Assert
        assertThrows(JsonProcessingException.class, () -> vitalSignConsumer.consume(record));
        verify(mockObjectMapper).readValue(invalidJson, VitalSignResponse.class);
        verifyNoInteractions(mockRepository);
    }

    @Test
    void testConsume_RepositorySaveFailure() throws JsonProcessingException {
        // Arrange
        String validJson = "{\n" +
            "  \"id\": 1,\n" +
            "  \"patient\": {\n" +
            "    \"id\": 123\n" +
            "  },\n" +
            "  \"pulse\": 72\n" +
            "}";
        VitalSignResponse vitalSignResponse =VitalSignResponse
            .builder()
            .id(1L)
            .patient(PatientInfo.builder().id(123L).build())
            .pulse(72)
            .build();
        ConsumerRecord<String, String> record = new ConsumerRecord<>("vital-signs-topic", 0, 0, null, validJson);

        when(mockObjectMapper.readValue(validJson, VitalSignResponse.class)).thenReturn(vitalSignResponse);
        doThrow(RuntimeException.class).when(mockRepository).save(vitalSignResponse);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> vitalSignConsumer.consume(record));
        verify(mockObjectMapper).readValue(validJson, VitalSignResponse.class);
        verify(mockRepository).save(vitalSignResponse);
    }

}
