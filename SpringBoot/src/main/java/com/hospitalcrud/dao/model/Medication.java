package com.hospitalcrud.dao.model;

import lombok.*;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "medication")
public class Medication {
    @XmlTransient
    private int id;
    @XmlValue
    private String medicationName;
    @XmlTransient
    private int medRecordId;
}
