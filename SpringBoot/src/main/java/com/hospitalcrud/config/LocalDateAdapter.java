package com.hospitalcrud.config;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v, dateFormat);
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        return v.format(dateFormat);
    }
}
