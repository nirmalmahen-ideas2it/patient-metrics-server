package com.ideas2it.training.patient.metrics.service;

import com.ideas2it.training.patient.metrics.model.AuditPayload;
import com.ideas2it.training.patient.metrics.repository.AuditDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final MongoTemplate mongoTemplate;
    private final AuditDataRepository auditRepository;

    public List<AuditPayload> getLogsByUserIdAndDateRange(Long userId, LocalDateTime from, LocalDateTime to) {
        return auditRepository.findByUserIdAndLogDateBetween(userId, from, to);
    }

    public List<AuditPayload> getLogsByPatientIdAndDateRange(Long patientId, LocalDateTime from, LocalDateTime to) {
        return auditRepository.findByPatientIdAndLogDateBetween(patientId, from, to);
    }

    public List<AuditPayload> getLogsByEntityTypeAndDateRange(String entityType, LocalDateTime from, LocalDateTime to) {
        return auditRepository.findByEntityTypeAndLogDateBetween(entityType, from, to);
    }

    public List<AuditPayload> getLogsByEntityId(Long entityId) {
        return auditRepository.findByEntityId(entityId);
    }

    public List<AuditPayload> getFilteredAuditLogs(Long userId,
                                                   Long patientId,
                                                   String entityType,
                                                   LocalDateTime from,
                                                   LocalDateTime to) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        if (userId != null) {
            criteria = criteria.and("userId").is(userId);
        }
        if (patientId != null) {
            criteria = criteria.and("patientId").is(patientId);
        }
        if (entityType != null && !entityType.isBlank()) {
            criteria = criteria.and("entityType").is(entityType);
        }
        if (from != null && to != null) {
            criteria = criteria.and("logDate").gte(from).lte(to);
        } else if (from != null) {
            criteria = criteria.and("logDate").gte(from);
        } else if (to != null) {
            criteria = criteria.and("logDate").lte(to);
        }

        query.addCriteria(criteria);
        return mongoTemplate.find(query, AuditPayload.class);
    }
}
