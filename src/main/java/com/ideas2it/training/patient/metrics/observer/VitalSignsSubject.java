package com.ideas2it.training.patient.metrics.observer;

import com.ideas2it.training.patient.metrics.model.VitalSignResponse;

/**
 * Subject interface for the Observer pattern.
 * Defines methods for registering, removing, and notifying observers about
 * vital signs changes.
 */
public interface VitalSignsSubject {
    /**
     * Register an observer to receive vital signs updates
     * 
     * @param observer the observer to register
     */
    void registerObserver(VitalSignsObserver observer);

    /**
     * Remove an observer from receiving vital signs updates
     * 
     * @param observer the observer to remove
     */
    void removeObserver(VitalSignsObserver observer);

    /**
     * Notify all registered observers about vital signs changes
     * 
     * @param vitalSigns the updated vital signs data
     */
    void notifyObservers(VitalSignResponse vitalSigns);
}