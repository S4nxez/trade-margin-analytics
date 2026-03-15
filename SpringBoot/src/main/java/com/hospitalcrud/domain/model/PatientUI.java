package com.hospitalcrud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class PatientUI {
        private int id;
        private String name;
        private LocalDate birthDate;
        private String phone;
        private int paid;
        private String userName;
        private String password;
}

