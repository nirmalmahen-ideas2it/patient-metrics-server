package com.ideas2it.training.patient.metrics.observer;

import com.ideas2it.training.patient.metrics.model.VitalSignResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of VitalSignsSubject that manages observers and
 * notifies them of vital signs changes.
 */
@Slf4j
@Component
public class VitalSignsMonitor implements VitalSignsSubject {
    private final List<VitalSignsObserver> observers = new ArrayList<>();

    @Override
    public void registerObserver(VitalSignsObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            log.info("Registered new observer: {}", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void removeObserver(VitalSignsObserver observer) {
        if (observers.remove(observer)) {
            log.info("Removed observer: {}", observer.getClass().getSimpleName());
        }
    }

    @Override
    public void notifyObservers(VitalSignResponse vitalSigns) {
        log.info("Notifying {} observers about vital signs update for patient: {}",
                observers.size(), vitalSigns.getPatient().getId());

        for (VitalSignsObserver observer : observers) {
            try {
                observer.update(vitalSigns);
            } catch (Exception e) {
                log.error("Error notifying observer {}: {}",
                        observer.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
}