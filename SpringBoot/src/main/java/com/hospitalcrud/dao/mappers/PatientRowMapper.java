package com.hospitalcrud.dao.mappers;

import com.hospitalcrud.dao.model.Patient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Profile("txtFiles")
@Component
public class PatientRowMapper {

    public Patient mapRow(String line) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String[] values = line.split(";");

        return new Patient(
                LocalDate.parse(values[2].trim(), formatter),
        null,
                Integer.parseInt(values[0]),
                values[1],
                values[3]
                );
    }

    public String toStringTextFile(Patient patient) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return  String.format("%d;%s;%s;%s",
                patient.getId(),
                patient.getName(),
                patient.getBirthDate().format(formatter),
                patient.getPhone());
    }
}
