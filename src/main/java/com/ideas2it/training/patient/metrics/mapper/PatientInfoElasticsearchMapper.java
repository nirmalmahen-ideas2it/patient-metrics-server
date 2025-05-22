package com.ideas2it.training.patient.metrics.mapper;

import com.ideas2it.training.patient.metrics.model.patient.PatientInfo;
import com.ideas2it.training.patient.metrics.model.patient.PatientInfoDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PatientInfoElasticsearchMapper {

    PatientInfoElasticsearchMapper INSTANCE = Mappers.getMapper(PatientInfoElasticsearchMapper.class);

    @Mapping(source = "referralInfo.referrerName", target = "referralName")
    @Mapping(source = "diagnoses.primaryDiagnosis", target = "primaryDiagnosis")
    @Mapping(source = "primaryPhysician.id", target = "physicianId")
    @Mapping(source = "primaryPhysician.licenseNumber", target = "physicianLicenseNumber")
    @Mapping(source = "primaryPhysician.name", target = "physicianName")
    @Mapping(source = "primaryPhysician.hospital", target = "physicianHospital")
    @Mapping(source = "startOfCareDate", target = "startOfCareDate")
    @Mapping(source = "birthDate", target = "birthDate")
    PatientInfoDocument toDocument(PatientInfo patientInfo);

}
