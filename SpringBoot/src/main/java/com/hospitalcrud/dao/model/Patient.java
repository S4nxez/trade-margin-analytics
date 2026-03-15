package com.hospitalcrud.dao.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class Patient {
    private int id;
    private String name;
    private LocalDate birthDate;
    private String phone;
    private Credential credential;

    public Patient(LocalDate birthDate, Credential credential, int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.credential = credential;
        this.phone = phone;
    }
}
