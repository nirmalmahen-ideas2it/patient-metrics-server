package com.ideas2it.training.patient.metrics.observer;

import com.ideas2it.training.patient.metrics.model.VitalSignResponse;

/**
 * Observer interface for the Observer pattern.
 * Defines the method that will be called when vital signs are updated.
 */
public interface VitalSignsObserver {
    /**
     * Update method called when vital signs change
     * @param vitalSigns the updated vital signs data
     */
    void update(VitalSignResponse vitalSigns);
} 