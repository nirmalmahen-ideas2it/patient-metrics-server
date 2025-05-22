package com.ideas2it.training.patient.metrics.model.patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "patient_info")
public class PatientInfoDocument implements Serializable {

    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String medicalRecordNumber;
    @Field(type = FieldType.Date, format = DateFormat.date, pattern = "yyyy-MM-dd")
    private LocalDate startOfCareDate;
    private String status;
    @Field(type = FieldType.Text)
    private String firstName;
    @Field(type = FieldType.Text)
    private String lastName;
    private String sex;
    @Field(type = FieldType.Date, format = DateFormat.date, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Field(type = FieldType.Text)
    private String referralName;

    @Field(type = FieldType.Text)
    private String primaryDiagnosis;

    private Long physicianId;
    @Field(type = FieldType.Text)
    private String physicianLicenseNumber;
    @Field(type = FieldType.Text)
    private String physicianName;
    private String physicianHospital;
}

