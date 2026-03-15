package com.hospitalcrud.dao.repositories;

import com.hospitalcrud.dao.model.MedRecord;

import java.util.List;

public interface MedRecordRepository {

    List<MedRecord> getAll(int idPatient);

    int add(MedRecord medRecord);

    void delete(int id);

    void update(MedRecord medRecord);
}
