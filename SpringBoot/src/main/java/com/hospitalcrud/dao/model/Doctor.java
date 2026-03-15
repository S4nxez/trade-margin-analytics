package com.hospitalcrud.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    private int doctor_id;
    private String name;
    private String specialization;
}
