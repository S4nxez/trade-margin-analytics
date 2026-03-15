package com.hospitalcrud.domain.model;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicationUI {

    private int id;
    private String medicationName;
    private int medRecordId;

}




