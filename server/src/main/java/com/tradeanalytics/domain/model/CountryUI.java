package com.tradeanalytics.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class CountryUI {
        private int id;
        private String name;
        private LocalDate birthDate;
        private String phone;
        private int paid;
        private String userName;
        private String password;
}

