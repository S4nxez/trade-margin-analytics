package com.hospitalcrud.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credential {
    private int user_id;
    private String username;
    private String password;
    private int patientId;
    private int doctorId;

    public Credential(String username, String password, int patientId) {
        this.username = username;
        this.password = password;
        this.patientId = patientId;
    }
}
