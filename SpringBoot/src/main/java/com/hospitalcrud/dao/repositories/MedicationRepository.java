package com.hospitalcrud.dao.repositories;

import com.hospitalcrud.dao.model.Medication;

import java.util.List;

public interface MedicationRepository {
    List<Medication> getAll();
}

