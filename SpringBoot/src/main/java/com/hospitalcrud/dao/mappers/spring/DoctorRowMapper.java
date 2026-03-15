package com.hospitalcrud.dao.mappers.spring;

import com.hospitalcrud.dao.model.Doctor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DoctorRowMapper implements RowMapper<Doctor> {
    @Override
    public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Doctor doctor = new Doctor();

        doctor.setDoctor_id(rs.getInt("doctor_id"));
        doctor.setName(rs.getString("name"));
        doctor.setSpecialization(rs.getString("specialization"));
        return doctor;
    }
}
