package com.ideas2it.training.patient.metrics.observer;

import com.ideas2it.training.patient.metrics.model.VitalSignResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Observer that analyzes trends in vital signs over time.
 * Maintains a history of vital signs for each patient and detects significant
 * changes.
 */
@Slf4j
@Component
public class VitalSignsTrendAnalyzer implements VitalSignsObserver {

    private static final double SIGNIFICANT_CHANGE_THRESHOLD = 0.2; // 20% change
    private final Map<Long, VitalSignResponse> lastReadings = new ConcurrentHashMap<>();

    @Override
    public void update(VitalSignResponse vitalSigns) {
        Long patientId = vitalSigns.getPatient().getId();
        VitalSignResponse lastReading = lastReadings.get(patientId);

        if (lastReading != null) {
            analyzeTrends(lastReading, vitalSigns);
        }

        lastReadings.put(patientId, vitalSigns);
    }

    private void analyzeTrends(VitalSignResponse lastReading, VitalSignResponse currentReading) {
        // Analyze pulse trend
        double pulseChange = calculatePercentageChange(lastReading.getPulse(), currentReading.getPulse());
        if (Math.abs(pulseChange) > SIGNIFICANT_CHANGE_THRESHOLD) {
            log.info("Significant pulse change detected for patient {}: {}% change ({} -> {})",
                    currentReading.getPatient().getId(),
                    String.format("%.1f", pulseChange * 100),
                    lastReading.getPulse(),
                    currentReading.getPulse());
        }

        // Analyze temperature trend
        double tempChange = calculatePercentageChange(lastReading.getTemperature(), currentReading.getTemperature());
        if (Math.abs(tempChange) > SIGNIFICANT_CHANGE_THRESHOLD) {
            log.info("Significant temperature change detected for patient {}: {}% change ({} -> {})",
                    currentReading.getPatient().getId(),
                    String.format("%.1f", tempChange * 100),
                    lastReading.getTemperature(),
                    currentReading.getTemperature());
        }
    }

    private double calculatePercentageChange(double oldValue, double newValue) {
        if (oldValue == 0)
            return 0;
        return (newValue - oldValue) / oldValue;
    }
}