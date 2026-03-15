package com.hospitalcrud.dao.mappers.JDBC;

import com.hospitalcrud.dao.model.Patient;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PatientRowMapperJDBC {
    public List<Patient> mapRow(ResultSet rs) {
        List<Patient> listaPatients = new ArrayList<>();

        try {
            while (rs.next()) {
                int id = rs.getInt("patient_id");
                String name = rs.getString("name");
                LocalDate birthDate = rs.getDate("date_of_birth").toLocalDate();
                String phone = rs.getString("phone");
                Patient patient = new Patient(birthDate, null, id, name, phone);
                listaPatients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaPatients;
    }
}
