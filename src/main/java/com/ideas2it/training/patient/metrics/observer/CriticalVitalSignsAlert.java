package com.ideas2it.training.patient.metrics.observer;

import com.ideas2it.training.patient.metrics.model.VitalSignResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Observer that monitors vital signs and generates alerts for critical values.
 */
@Slf4j
@Component
public class CriticalVitalSignsAlert implements VitalSignsObserver {
    private static final int CRITICAL_PULSE_MIN = 40;
    private static final int CRITICAL_PULSE_MAX = 120;
    private static final double CRITICAL_TEMPERATURE_MIN = 35.0;
    private static final double CRITICAL_TEMPERATURE_MAX = 39.0;

    @Override
    public void update(VitalSignResponse vitalSigns) {
        checkVitalSigns(vitalSigns);
    }

    private void checkVitalSigns(VitalSignResponse vitalSigns) {
        if (vitalSigns.getPulse() < CRITICAL_PULSE_MIN || vitalSigns.getPulse() > CRITICAL_PULSE_MAX) {
            log.warn("CRITICAL ALERT: Abnormal pulse rate {} for patient {}",
                    vitalSigns.getPulse(), vitalSigns.getPatient().getId());
        }

        if (vitalSigns.getTemperature() < CRITICAL_TEMPERATURE_MIN ||
                vitalSigns.getTemperature() > CRITICAL_TEMPERATURE_MAX) {
            log.warn("CRITICAL ALERT: Abnormal temperature {} for patient {}",
                    vitalSigns.getTemperature(), vitalSigns.getPatient().getId());
        }
    }
}