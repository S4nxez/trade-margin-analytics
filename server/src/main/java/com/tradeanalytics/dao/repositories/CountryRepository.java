package com.tradeanalytics.dao.repositories;

import com.tradeanalytics.dao.model.Country;

import java.util.List;


public interface CountryRepository {
    List<Country> getAll();
}
