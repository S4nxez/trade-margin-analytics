package com.hospitalcrud.dao.model;

import com.hospitalcrud.config.LocalDateAdapter;
import javax.xml.bind.annotation.*;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "medRecord")
public class MedRecord {
        private int id;
        private int idPatient;
        private int idDoctor;
        private String diagnosis;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate date;
        @XmlElementWrapper(name="medications")
        @XmlElement(name = "medication")
        private List<Medication> medications;
}

