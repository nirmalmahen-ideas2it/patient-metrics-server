package com.ideas2it.training.patient.metrics.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideas2it.training.patient.metrics.model.VitalSignResponse;
import com.ideas2it.training.patient.metrics.repository.AuditDataRepository;
import com.ideas2it.training.patient.metrics.repository.VitalSignsRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class VitalSignConsumer {

    private final ObjectMapper objectMapper;
    private final VitalSignsRepository repository;

    public VitalSignConsumer(ObjectMapper objectMapper, VitalSignsRepository repository) {
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @KafkaListener(topics = "vital-signs-topic", groupId = "vital-sign-consumer-group")
    public void consume(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String response=record.value();
        VitalSignResponse vitalsSignResponse = objectMapper.readValue(response, VitalSignResponse.class);
        repository.save(vitalsSignResponse);
        System.out.println("Consumed message: " + vitalsSignResponse);
    }

}
