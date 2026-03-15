package com.hospitalcrud.dao.repositories.staticDAO;

import com.hospitalcrud.dao.model.MedRecord;
import com.hospitalcrud.dao.model.Medication;
import com.hospitalcrud.dao.repositories.MedRecordRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("static")
public class StaticMedRecordRepository implements MedRecordRepository {

    private static List<MedRecord> medRecordList = new ArrayList<>();

    public StaticMedRecordRepository() {
        ArrayList<Medication> medicationsList = new ArrayList<>();
        medicationsList.add(new Medication(1, "Paracetamos", 1));
        medRecordList.add(new MedRecord(1, 1, 1, "Diagnosis1", LocalDate.of(2021, 5, 23), new ArrayList<Medication>()));
        medRecordList.add(new MedRecord(2, 3, 2, "Diagnosis2", LocalDate.of(2021, 5, 23), medicationsList));
        medRecordList.add(new MedRecord(3, 1, 3, "Diagnosis3", LocalDate.of(2021, 5, 23), medicationsList));
        medRecordList.add(new MedRecord(4, 1, 1, "Cuentitis Aguditis", LocalDate.now(), medicationsList));
    }

    public List<MedRecord> getAll(int idPatient) {
        return medRecordList.stream().filter(medRecord -> medRecord.getIdPatient() == idPatient).toList();
    }

    public int add(MedRecord medRecord) {
        return medRecordList.add(medRecord) ? 1 : 0;
    }

    public void delete(int id) {
        medRecordList.removeIf(medRecord -> medRecord.getId() == id);

    }

    public void update(MedRecord medRecord) {
        delete(medRecord.getId());
        add(medRecord);
    }
}
