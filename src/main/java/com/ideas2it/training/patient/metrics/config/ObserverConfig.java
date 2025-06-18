package com.ideas2it.training.patient.metrics.config;

import com.ideas2it.training.patient.metrics.observer.CriticalVitalSignsAlert;
import com.ideas2it.training.patient.metrics.observer.VitalSignsMonitor;
import com.ideas2it.training.patient.metrics.observer.VitalSignsTrendAnalyzer;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to wire up the Observer pattern components.
 * This ensures all observers are properly registered with the subject.
 */
@Configuration
public class ObserverConfig {

    private final VitalSignsMonitor vitalSignsMonitor;
    private final CriticalVitalSignsAlert criticalVitalSignsAlert;
    private final VitalSignsTrendAnalyzer trendAnalyzer;

    public ObserverConfig(VitalSignsMonitor vitalSignsMonitor,
            CriticalVitalSignsAlert criticalVitalSignsAlert,
            VitalSignsTrendAnalyzer trendAnalyzer) {
        this.vitalSignsMonitor = vitalSignsMonitor;
        this.criticalVitalSignsAlert = criticalVitalSignsAlert;
        this.trendAnalyzer = trendAnalyzer;
    }

    @PostConstruct
    public void registerObservers() {
        // Register all observers with the subject
        vitalSignsMonitor.registerObserver(criticalVitalSignsAlert);
        vitalSignsMonitor.registerObserver(trendAnalyzer);
    }
}