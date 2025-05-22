package com.ideas2it.training.patient.metrics.consumer;

import com.ideas2it.training.patient.metrics.mapper.PatientInfoElasticsearchMapper;
import com.ideas2it.training.patient.metrics.model.patient.PatientInfo;
import com.ideas2it.training.patient.metrics.model.patient.PatientInfoDocument;
import com.ideas2it.training.patient.metrics.repository.PatientInfoSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class PatientInfoConsumerTest {

    private PatientInfoConsumer patientInfoConsumer;

    @Mock
    private PatientInfoElasticsearchMapper mockMapper;

    @Mock
    private PatientInfoSearchRepository mockRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        patientInfoConsumer = new PatientInfoConsumer(mockMapper, mockRepository);
    }

    @Test
    void testReceiveMessage_Success() {
        // Arrange
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setId(1L);
        PatientInfoDocument document = new PatientInfoDocument();
        document.setId(1L);

        when(mockMapper.toDocument(patientInfo)).thenReturn(document);

        // Act
        patientInfoConsumer.receiveMessage(patientInfo);

        // Assert
        verify(mockMapper).toDocument(patientInfo);
        verify(mockRepository).save(document);
    }

    @Test
    void testReceiveMessage_MappingFailure() {
        // Arrange
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setId(1L);

        when(mockMapper.toDocument(patientInfo)).thenThrow(RuntimeException.class);

        // Act & Assert
        try {
            patientInfoConsumer.receiveMessage(patientInfo);
        } catch (RuntimeException e) {
            // Expected exception
        }

        verify(mockMapper).toDocument(patientInfo);
        verifyNoInteractions(mockRepository);
    }

    @Test
    void testReceiveMessage_RepositorySaveFailure() {
        // Arrange
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setId(1L);
        PatientInfoDocument document = new PatientInfoDocument();
        document.setId(1L);

        when(mockMapper.toDocument(patientInfo)).thenReturn(document);
        doThrow(RuntimeException.class).when(mockRepository).save(document);

        // Act & Assert
        try {
            patientInfoConsumer.receiveMessage(patientInfo);
        } catch (RuntimeException e) {
            // Expected exception
        }

        verify(mockMapper).toDocument(patientInfo);
        verify(mockRepository).save(document);
    }

    @Test
    void testReceiveMessage_NullPatientInfo() {
        // Act & Assert
        try {
            patientInfoConsumer.receiveMessage(null);
        } catch (NullPointerException e) {
            // Expected exception
        }

        verifyNoInteractions(mockMapper, mockRepository);
    }
}
