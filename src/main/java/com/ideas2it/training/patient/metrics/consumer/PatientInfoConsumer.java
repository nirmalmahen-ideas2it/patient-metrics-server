package com.ideas2it.training.patient.metrics.consumer;

import com.ideas2it.training.patient.metrics.mapper.PatientInfoElasticsearchMapper;
import com.ideas2it.training.patient.metrics.model.patient.PatientInfo;
import com.ideas2it.training.patient.metrics.model.patient.PatientInfoDocument;
import com.ideas2it.training.patient.metrics.repository.PatientInfoSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientInfoConsumer {

    private final PatientInfoElasticsearchMapper mapper;
    private final PatientInfoSearchRepository repository;

    @RabbitListener(queues = "${rabbitmq.queue.patient}")
    public void receiveMessage(PatientInfo patientInfo) {
        log.info("Received patient info from RabbitMQ: {}", patientInfo.getId());
        PatientInfoDocument document = mapper.toDocument(patientInfo);
        repository.save(document);
        log.info("Saved patient info to Elasticsearch: {}", document.getId());
    }
}

