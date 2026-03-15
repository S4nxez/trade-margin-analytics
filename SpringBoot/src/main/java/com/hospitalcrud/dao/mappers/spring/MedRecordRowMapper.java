package com.hospitalcrud.dao.mappers.spring;

import com.hospitalcrud.dao.model.MedRecord;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MedRecordRowMapper implements RowMapper<MedRecord> {
    @Override
    public MedRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        MedRecord medRecord = new MedRecord();

        medRecord.setId(rs.getInt("record_id"));
        medRecord.setIdPatient(rs.getInt("patient_id"));
        medRecord.setIdDoctor(rs.getInt("doctor_id"));
        medRecord.setDiagnosis(rs.getString("diagnosis"));
        medRecord.setDate(rs.getDate("admission_date").toLocalDate());

        return medRecord;
    }
}
