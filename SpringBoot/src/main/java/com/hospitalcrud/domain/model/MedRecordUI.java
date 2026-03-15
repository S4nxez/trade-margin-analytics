package com.hospitalcrud.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedRecordUI {
        private int id;
        private String description;
        private String date;
        private int idPatient;
        private int idDoctor;
        private List<String> medications;
}

