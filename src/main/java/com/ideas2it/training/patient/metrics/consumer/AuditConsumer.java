package com.ideas2it.training.patient.metrics.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideas2it.training.patient.metrics.model.AuditPayload;
import com.ideas2it.training.patient.metrics.repository.AuditDataRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AuditConsumer {

    private final ObjectMapper objectMapper;
    private final AuditDataRepository repository;

    public AuditConsumer(ObjectMapper objectMapper, AuditDataRepository repository) {
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @KafkaListener(topics = "audit-data", groupId = "audit-consumer-group")
    public void consumeAuditData(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String response = record.value();
        AuditPayload auditPayload = objectMapper.readValue(response, AuditPayload.class);
        repository.save(auditPayload);
        System.out.println("Consumed message: " + auditPayload);
    }
}
