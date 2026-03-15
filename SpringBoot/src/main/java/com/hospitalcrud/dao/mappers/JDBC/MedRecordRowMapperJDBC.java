package com.hospitalcrud.dao.mappers.JDBC;

import com.hospitalcrud.dao.model.MedRecord;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class MedRecordRowMapperJDBC {
    public List<MedRecord> mapRow(ResultSet rs) {
        List<MedRecord> lista = new ArrayList<>();

        try {
            while (rs.next()) {

                int id = rs.getInt("record_id");
                int patientId = rs.getInt("patient_id");
                int doctorId = rs.getInt("doctor_id");
                String diagnosis = rs.getString("diagnosis");
                LocalDate admission_date = rs.getDate("admission_date").toLocalDate();
                lista.add(new MedRecord(id, patientId, doctorId, diagnosis, admission_date, null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}
