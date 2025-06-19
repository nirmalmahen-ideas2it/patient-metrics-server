package com.ideas2it.training.patient.metrics.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

/**
 * DTO for filtering audit logs.
 */
@Value
@Builder
public class AuditLogFilter {
    Long userId;
    Long patientId;
    String entityType;
    LocalDateTime from;
    LocalDateTime to;
}