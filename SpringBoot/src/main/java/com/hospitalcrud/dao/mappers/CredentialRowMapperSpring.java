package com.hospitalcrud.dao.mappers;

import com.hospitalcrud.dao.model.Credential;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CredentialRowMapperSpring implements RowMapper<Credential> {


    @Override
    public Credential mapRow(ResultSet rs, int rowNum) throws SQLException {
        Credential credential = new Credential();
        credential.setUsername(rs.getString("username"));
        credential.setPassword(rs.getString("password"));
        credential.setPatientId(rs.getInt("patient_id"));
        return credential;
    }
}
