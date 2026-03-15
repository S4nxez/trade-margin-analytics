package com.hospitalcrud.dao.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "medRecords")
@XmlAccessorType(XmlAccessType.FIELD)
public class MedRecordListWrapper {
    @XmlElement(name = "medRecord")
    private List<MedRecord> medRecords;
}
