package com.ideas2it.training.patient.metrics.repository;

import com.ideas2it.training.patient.metrics.model.VitalSignResponse;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface VitalSignsRepository extends MongoRepository<VitalSignResponse, String> {
}
