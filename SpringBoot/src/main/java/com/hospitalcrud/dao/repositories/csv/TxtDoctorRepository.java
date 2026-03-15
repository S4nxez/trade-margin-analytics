package com.hospitalcrud.dao.repositories.csv;

import com.hospitalcrud.config.Configuration;
import com.hospitalcrud.dao.model.Doctor;
import com.hospitalcrud.utils.Constantes;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Log4j2
@Profile("txtFiles")
public class TxtDoctorRepository implements com.hospitalcrud.dao.repositories.DoctorRepository {

    @Override
    public List<Doctor> getAll() {
        List<Doctor> doctors = new ArrayList<>();
        try {
            List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths
                    .get(Configuration.getInstance().getProperty(Constantes.DOCTOR_PATH)));
            for (String line : lines) {
                String[] values = line.split(";");
                Doctor doctor = new Doctor(
                        Integer.parseInt(values[0].trim()),
                        values[1].trim(),
                        values[2].trim()
                );

                doctors.add(doctor);
                updateNextId(doctor.getDoctor_id() + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    private void updateNextId(int nextId) {
        Configuration.getInstance().setProperty(Constantes.NEXT_DOCTOR_ID, String.valueOf(nextId));
    }

}
