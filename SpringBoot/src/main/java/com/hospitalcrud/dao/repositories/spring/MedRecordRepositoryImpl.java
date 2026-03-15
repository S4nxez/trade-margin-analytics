package com.hospitalcrud.dao.repositories.spring;

import com.hospitalcrud.dao.mappers.spring.MedRecordRowMapper;
import com.hospitalcrud.dao.model.MedRecord;
import com.hospitalcrud.dao.repositories.JDBC.common.QuerysSQL;
import com.hospitalcrud.dao.repositories.MedRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Repository
@Profile("spring")
public class MedRecordRepositoryImpl implements MedRecordRepository {
    @Autowired
    JdbcClient jdbcClient;

    @Autowired
    private final MedRecordRowMapper medRecordRowMapperSpring;
    private final MedicationRepositoryImpl medicationRepository;

    public MedRecordRepositoryImpl(MedicationRepositoryImpl medicationRepository,
                                   MedRecordRowMapper medRecordRowMapperSpring) {
        this.medicationRepository = medicationRepository;
        this.medRecordRowMapperSpring = medRecordRowMapperSpring;
    }

    @Override
    public List<MedRecord> getAll(int idPatient) {
        return jdbcClient.sql(QuerysSQL.SELECT_ALL_MEDRECORDS_BY_PATIENT)
                .param(1,idPatient)
                .query(medRecordRowMapperSpring)
                .list();
    }

    @Override
    @Transactional
    public int add(MedRecord medRecord) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(QuerysSQL.INSERT_INTO_MEDICAL_RECORDS)
                .param(1,medRecord.getIdPatient())
                .param(2,medRecord.getIdDoctor())
                .param(3,medRecord.getDiagnosis())
                .param(4,medRecord.getDate())
                .update(keyHolder);
        int newId = Objects.requireNonNull(keyHolder.getKey(), "Key not generated").intValue();
        medRecord.setId(newId);
        medRecord.getMedications().forEach(medication -> medication.setMedRecordId(newId));
        medRecord.getMedications().forEach(medicationRepository::add);
        return newId;
    }

    @Override
    public void update(MedRecord medRecord) {
        jdbcClient.sql(QuerysSQL.UPDATE_MEDRECORD)
                .param(1, medRecord.getDiagnosis())
                .param(2, medRecord.getDate())
                .param(3, medRecord.getId())
                .update();
    }

    @Override
    @Transactional
    public void delete(int medRecord_id) {
        if (medRecord_id != 0){
            medicationRepository.delete(medRecord_id);
            jdbcClient.sql(QuerysSQL.DELETE_FROM_MED_RECORD)
                    .param(1,medRecord_id)
                    .update();
        }else{
            medicationRepository.delete(medRecord_id);
            jdbcClient.sql(QuerysSQL.DELETE_MED_RECORDS_BY_PATIENT)
                    .param(1,medRecord_id)
                    .update();
        }
    }
}
