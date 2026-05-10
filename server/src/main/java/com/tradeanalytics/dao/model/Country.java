package com.tradeanalytics.dao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    private Long id;

    private String name;

    private String iso2;

    private String iso3;

}