package com.ideas2it.training.patient.metrics.service;

import com.ideas2it.training.patient.metrics.model.AuditLogFilter;
import com.ideas2it.training.patient.metrics.model.AuditPayload;
import com.ideas2it.training.patient.metrics.repository.AuditDataRepository;
import com.ideas2it.training.patient.metrics.utils.exception.AuditLogQueryException;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);

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

    /**
     * Retrieves filtered audit logs based on the provided filter.
     *
     * @param filter the filter criteria for audit logs
     * @return list of matching audit payloads
     * @throws IllegalArgumentException if filter values are invalid
     * @throws AuditLogQueryException   if the query fails
     */
    public List<AuditPayload> getFilteredAuditLogs(AuditLogFilter filter) {
        validateFilter(filter);

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (filter.getUserId() != null) {
            criteriaList.add(Criteria.where("userId").is(filter.getUserId()));
        }
        if (filter.getPatientId() != null) {
            criteriaList.add(Criteria.where("patientId").is(filter.getPatientId()));
        }
        if (StringUtils.hasText(filter.getEntityType())) {
            criteriaList.add(Criteria.where("entityType").is(filter.getEntityType().trim()));
        }
        if (filter.getFrom() != null && filter.getTo() != null) {
            if (filter.getFrom().isAfter(filter.getTo())) {
                throw new IllegalArgumentException("'from' date must be before 'to' date.");
            }
            criteriaList.add(Criteria.where("logDate").gte(filter.getFrom()).lte(filter.getTo()));
        } else if (filter.getFrom() != null) {
            criteriaList.add(Criteria.where("logDate").gte(filter.getFrom()));
        } else if (filter.getTo() != null) {
            criteriaList.add(Criteria.where("logDate").lte(filter.getTo()));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        try {
            List<AuditPayload> results = mongoTemplate.find(query, AuditPayload.class);
            log.info("Audit log query successful. Filter: {}, Results: {}", filter, results.size());
            return results;
        } catch (Exception e) {
            log.error("Failed to query audit logs. Filter: {}", filter, e);
            throw new AuditLogQueryException("Failed to query audit logs", e);
        }
    }

    /**
     * Validates the audit log filter.
     *
     * @param filter the filter to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateFilter(AuditLogFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Filter must not be null.");
        }
        if (filter.getUserId() != null && filter.getUserId() < 0) {
            throw new IllegalArgumentException("User ID must be positive.");
        }
        if (filter.getPatientId() != null && filter.getPatientId() < 0) {
            throw new IllegalArgumentException("Patient ID must be positive.");
        }
        if (filter.getEntityType() != null && filter.getEntityType().length() > 50) {
            throw new IllegalArgumentException("Entity type is too long.");
        }
    }
}
