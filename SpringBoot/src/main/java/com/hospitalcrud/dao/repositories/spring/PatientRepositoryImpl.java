package com.hospitalcrud.dao.repositories.spring;

import com.hospitalcrud.dao.mappers.spring.PatientRowMapper;
import com.hospitalcrud.dao.model.MedRecord;
import com.hospitalcrud.dao.model.Patient;
import com.hospitalcrud.dao.repositories.JDBC.common.QuerysSQL;
import com.hospitalcrud.dao.repositories.PatientRepository;
import com.hospitalcrud.domain.errors.ForeignKeyConstraintError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Repository
@Profile("spring")
public class PatientRepositoryImpl implements PatientRepository {

    @Autowired
    private JdbcClient jdbcClient;
    private final PatientRowMapper patientRowMapperSpring;
    private final CredentialRepositoryImpl credentialRepository;
    private final PaymentRepositoryImpl paymentRepository;
    private final MedRecordRepositoryImpl medRecordRepository;


    public PatientRepositoryImpl(PatientRowMapper patientRowMapperSpring, CredentialRepositoryImpl
            credentialRepository, PaymentRepositoryImpl paymentRepository,
                                 MedRecordRepositoryImpl medRecordRepository) {
        this.patientRowMapperSpring = patientRowMapperSpring;
        this.credentialRepository = credentialRepository;
        this.paymentRepository = paymentRepository;
        this.medRecordRepository = medRecordRepository;
    }


    @Override
    public List<Patient> getAll() {
        return jdbcClient.sql(QuerysSQL.SELECT_ALL_PATIENTS)
                .query(patientRowMapperSpring)
                .list();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public int add(Patient patient) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(QuerysSQL.INSERT_INTO_PATIENTS_NAME_DATE_OF_BIRTH_PHONE_VALUES)
                .param(1, patient.getName())
                .param(2, patient.getBirthDate())
                .param(3, patient.getPhone())
                .update(keyHolder);
        int newId = Objects.requireNonNull(keyHolder.getKey(), "Key was not generated").intValue();
        patient.setId(newId);
        patient.getCredential().setPatientId(newId);
        credentialRepository.add(patient.getCredential());
        return newId;
    }

    @Override
    public void update(Patient patient) {
        jdbcClient.sql(QuerysSQL.UPDATE_PATIENT)
                .param(1, patient.getName())
                .param(2, patient.getBirthDate())
                .param(3, patient.getPhone())
                .param(4, patient.getId())
                .update();
    }

    @Override
    @Transactional
    public void delete(int idDelete, boolean confirm) {

        if (confirm) {
            MedRecord medRecord=new MedRecord();
            medRecord.setIdPatient(idDelete);
            medRecordRepository.delete(medRecord.getId());
            paymentRepository.delete(medRecord.getIdPatient());
            credentialRepository.delete(medRecord.getIdPatient());
        }
        try {
            jdbcClient.sql(QuerysSQL.DELETE_PATIENT)
                    .param(1, idDelete)
                    .update();
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyConstraintError("Delete everything related to the patient?");
        }
    }
}
