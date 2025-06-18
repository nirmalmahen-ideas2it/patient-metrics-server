package com.ideas2it.training.patient.metrics.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideas2it.training.patient.metrics.model.VitalSignResponse;
import com.ideas2it.training.patient.metrics.observer.VitalSignsMonitor;
import com.ideas2it.training.patient.metrics.repository.VitalSignsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VitalSignConsumer {

    private final ObjectMapper objectMapper;
    private final VitalSignsRepository repository;
    private final VitalSignsMonitor vitalSignsMonitor;

    public VitalSignConsumer(ObjectMapper objectMapper,
            VitalSignsRepository repository,
            VitalSignsMonitor vitalSignsMonitor) {
        this.objectMapper = objectMapper;
        this.repository = repository;
        this.vitalSignsMonitor = vitalSignsMonitor;
    }

    @KafkaListener(topics = "vital-signs-topic", groupId = "vital-sign-consumer-group")
    public void consume(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String response = record.value();
        VitalSignResponse vitalsSignResponse = objectMapper.readValue(response, VitalSignResponse.class);
        repository.save(vitalsSignResponse);

        // Notify all observers about the new vital signs
        vitalSignsMonitor.notifyObservers(vitalsSignResponse);

        log.info("Processed vital signs for patient: {}", vitalsSignResponse.getPatient().getId());
    }
}
