package com.hospitalcrud.dao.mappers.spring;

import com.hospitalcrud.dao.model.Medication;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MedicationRowMapper implements RowMapper<Medication> {
    @Override
    public Medication mapRow(ResultSet rs, int rowNum) throws SQLException {
        Medication medication = new Medication();
        medication.setId(rs.getInt("prescription_id"));
        medication.setMedicationName(rs.getString("medication_name"));
        medication.setMedRecordId(rs.getInt("record_id"));
        return medication;
    }
}
