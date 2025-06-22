package com.ideas2it.training.patient.metrics.service;

/**
 * Exception thrown when patient search fails due to repository or database
 * errors.
 */
public class PatientSearchException extends RuntimeException {
    public PatientSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PatientSearchException(String message) {
        super(message);
    }
}