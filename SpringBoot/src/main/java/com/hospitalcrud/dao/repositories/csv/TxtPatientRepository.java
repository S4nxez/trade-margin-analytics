package com.hospitalcrud.dao.repositories.csv;

import com.hospitalcrud.config.Configuration;
import com.hospitalcrud.dao.mappers.PatientRowMapper;
import com.hospitalcrud.dao.model.Patient;
import com.hospitalcrud.dao.repositories.PatientRepository;
import com.hospitalcrud.utils.Constantes;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository
@Log4j2
@Profile("txtFiles")
public class TxtPatientRepository implements PatientRepository {
    private final PatientRowMapper patientRowMapper;

    public TxtPatientRepository( PatientRowMapper patientRowMapper) {
        this.patientRowMapper = patientRowMapper;
    }

    @Override
    public List<Patient> getAll() {
        List<Patient> patients = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(Configuration.getInstance().getProperty(Constantes.PATH_PATIENTS)))) {
            reader.lines().toList().forEach(line -> {
                Patient patient = patientRowMapper.mapRow(line);
                patients.add(patient);
                updateNextId(patient.getId() + 1);
            });
        } catch (IOException e) {
            log.error("Error reading patients file: " + e.getMessage());
        }

        return patients;
    }

    @Override
    public int add(Patient patient) {
        List<Patient> patients = getAll();
        boolean ret;

        patient.setId(getNextId());
        patients.add(patient);
        ret = savePatients(patients);
        updateNextId(patient.getId() + 1);
        return ret ? 1 : 0;
    }

    private boolean savePatients(List<Patient> patients){
        boolean ret = true;
        try {
            List<String> lines = patients.stream()
                    .map(patientRowMapper::toStringTextFile).toList();
            Files.write(Paths.get(Configuration.getInstance().getProperty(Constantes.PATH_PATIENTS)), lines);
        } catch (IOException e) {
            log.error("Error writing patients file: " + e.getMessage());
            ret = false;
        }
        return ret;
    }

    @Override
    public void update(Patient patientDatabase) {
        List<Patient> patients = getAll();

        patients.replaceAll(p -> p.getId() == patientDatabase.getId() ? patientDatabase : p);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Configuration.getInstance()
                .getProperty(Constantes.PATH_PATIENTS)))) {
            for (Patient p : patients) {
                String line = patientRowMapper.toStringTextFile(p);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            log.error("Error updating patient: " + e.getMessage(), e);
        }
    }

    private int getNextId() {
        try {
            return Integer.parseInt(Configuration.getInstance().getProperty(Constantes.LAST_ID_PATIENT));
        } catch (NumberFormatException e) {
            log.error("Error parsing last patient ID: " + e.getMessage());
            return 1;
        }
    }

    private void updateNextId(int nextId) {
        Configuration.getInstance().setProperty(Constantes.LAST_ID_PATIENT, String.valueOf(nextId));
    }

    @Override
    public void delete(int idDelete, boolean confirm) {
        if (!confirm) {
            log.info("Deletion not confirmed for patient ID: " + idDelete);
            return;
        }

        List<Patient> patients = getAll();
        patients.removeIf(p -> p.getId() == idDelete);
        savePatients(patients);
    }
}
