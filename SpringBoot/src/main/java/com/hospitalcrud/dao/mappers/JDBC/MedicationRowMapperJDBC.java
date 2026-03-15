package com.hospitalcrud.dao.mappers.JDBC;

import com.hospitalcrud.dao.model.Medication;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MedicationRowMapperJDBC {
    public List<Medication> mapRow(ResultSet rs) throws SQLException {
        List<Medication> lista = new ArrayList<>();

        while (rs.next()) {
            String medicationName = rs.getString("medication_name");
            int id = rs.getInt("prescription_id");
            int record = rs.getInt("record_id");
            lista.add(new Medication(id, medicationName, record));
        }
        return lista;
    }
}
