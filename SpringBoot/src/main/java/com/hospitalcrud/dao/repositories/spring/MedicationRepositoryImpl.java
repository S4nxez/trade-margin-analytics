package com.hospitalcrud.dao.repositories.spring;

import com.hospitalcrud.dao.mappers.spring.MedicationRowMapper;
import com.hospitalcrud.dao.model.MedRecord;
import com.hospitalcrud.dao.model.Medication;
import com.hospitalcrud.dao.repositories.JDBC.common.QuerysSQL;
import com.hospitalcrud.dao.repositories.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("spring")
public class MedicationRepositoryImpl implements MedicationRepository {

    @Autowired
    JdbcClient jdbcClient;

    @Autowired
    private MedicationRowMapper medicationRowMapperSpring;

    @Override
    public List<Medication> getAll() {
        return jdbcClient.sql(QuerysSQL.GET_ALL_MEDICATIONS)
                .query(medicationRowMapperSpring)
                .list();
    }

    public void add(Medication medication) {
        jdbcClient.sql(QuerysSQL.INSERT_INTO_MEDICATIONS)
                .param(1, medication.getMedRecordId())
                .param(2, medication.getMedicationName())
                .update();
    }

    public void delete(int medRecordId) {
        if (medRecordId != 0)
            jdbcClient.sql(QuerysSQL.DELETE_PRESCRIBEDMEDICATION).param(1, medRecordId).update();
        else
            jdbcClient.sql(QuerysSQL.DELETE_MEDICATIONS).param(1, medRecordId).update();
    }

}
