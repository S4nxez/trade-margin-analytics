package com.hospitalcrud.dao.mappers.JDBC;


import com.hospitalcrud.dao.model.Credential;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CredentialRowMapperJDBC {

    public List<Credential> mapRow(ResultSet rs) {
        List<Credential> lista = new ArrayList<>();

        try {
            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                int credential_id = rs.getInt("user_id");
                int patient_id = rs.getInt("patient_id");
                int doctor_id = rs.getInt("doctor_id");

                Credential credential = new Credential(credential_id, username,password, patient_id, doctor_id);
                lista.add(credential);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
