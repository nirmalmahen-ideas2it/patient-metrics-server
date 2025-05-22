package com.ideas2it.training.patient.metrics.repository;

import com.ideas2it.training.patient.metrics.model.AuditPayload;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditDataRepository extends MongoRepository<AuditPayload, String> {

    List<AuditPayload> findByUserIdAndLogDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<AuditPayload> findByPatientIdAndLogDateBetween(Long patientId, LocalDateTime startDate, LocalDateTime endDate);

    List<AuditPayload> findByEntityTypeAndLogDateBetween(String entityType, LocalDateTime startDate, LocalDateTime endDate);

    List<AuditPayload> findByEntityId(Long entityId);

}
