package com.ideas2it.training.patient.metrics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "audit_data")
public class AuditPayload {
    private Long userId;
    private String username;
    private Long patientId;
    private String patientName;
    private String entityType;
    private Long entityId;
    private LocalDateTime logDate;
    private String action;
    private String description;
    private List<AttributeChange> attributeChanges;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttributeChange {
        private String attributeName;
        private String oldValue;
        private String newValue;
    }
}
