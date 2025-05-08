package com.ideas2it.training.patient.metrics;

import com.ideas2it.training.patient.metrics.config.AsyncSyncConfiguration;
import com.ideas2it.training.patient.metrics.config.EmbeddedMongo;
import com.ideas2it.training.patient.metrics.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { PatientmetricsApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedMongo
public @interface IntegrationTest {
}
