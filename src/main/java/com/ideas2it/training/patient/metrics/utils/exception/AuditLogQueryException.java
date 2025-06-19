package com.ideas2it.training.patient.metrics.utils.exception;

/**
 * Exception thrown when audit log query fails.
 */
public class AuditLogQueryException extends RuntimeException {
    public AuditLogQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}