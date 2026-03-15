package com.hospitalcrud.dao.repositories.staticDAO;

import com.hospitalcrud.dao.model.Credential;
import com.hospitalcrud.dao.model.Patient;
import com.hospitalcrud.dao.repositories.PatientRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("static")
public class StaticPatientRepository implements PatientRepository {

    private static List<Patient> patients = new ArrayList<>();

    public StaticPatientRepository() {
        patients.add(new Patient(
                LocalDate.of(2003, 5, 23),
                new Credential(1, "john_smith", "password123", 1, 1),
                1,
                "John Smith",
                "333-222-111"
        ));
        patients.add(new Patient(
                LocalDate.of(1990, 8, 15),
                null,
                2,
                "Jane Doe",
                "444-555-666"
        ));
        patients.add(new Patient(
                LocalDate.of(1985, 12, 5),
                null,
                3,
                "Mike Jones",
                "777-888-999"
        ));
        patients.add(new Patient(
                LocalDate.of(1978, 3, 30),
                new Credential(4, "susan_lee", "password321", 4, 4),
                4,
                "Susan Lee",
                "111-222-333"
        ));

    }

    @Override
    public List<Patient> getAll() {
        return patients;
    }

    public int add(Patient patient) {
        patients.add(patient);
        return 4;
    }

    public void update(Patient patientDatabase) {
    patients.replaceAll(p -> p.getId() == patientDatabase.getId() ? patientDatabase : p);
    }

    public void delete(int idDelete, boolean confirm) {
        patients.removeIf(p -> p.getId() == idDelete && confirm);
    }
}
