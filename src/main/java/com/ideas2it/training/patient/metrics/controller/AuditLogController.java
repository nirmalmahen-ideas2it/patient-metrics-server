package com.ideas2it.training.patient.metrics.controller;

import com.ideas2it.training.patient.metrics.model.AuditPayload;
import com.ideas2it.training.patient.metrics.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/by-user")
    public List<AuditPayload> getLogsByUserIdAndDateRange(
        @RequestParam Long userId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return auditLogService.getLogsByUserIdAndDateRange(userId, from, to);
    }

    @GetMapping("/by-patient")
    public List<AuditPayload> getLogsByPatientIdAndDateRange(
        @RequestParam Long patientId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return auditLogService.getLogsByPatientIdAndDateRange(patientId, from, to);
    }

    @GetMapping("/by-entity-type")
    public List<AuditPayload> getLogsByEntityTypeAndDateRange(
        @RequestParam String entityType,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return auditLogService.getLogsByEntityTypeAndDateRange(entityType, from, to);
    }

    @GetMapping("/by-entity-id/{entityId}")
    public List<AuditPayload> getLogsByEntityId(@PathVariable Long entityId) {
        return auditLogService.getLogsByEntityId(entityId);
    }

    @GetMapping("/filter")
    public List<AuditPayload> filterAuditLogs(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) Long patientId,
        @RequestParam(required = false) String entityType,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return auditLogService.getFilteredAuditLogs(userId, patientId, entityType, from, to);
    }
}
