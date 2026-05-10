package com.tradeanalytics.domain.services;

import com.tradeanalytics.dao.repositories.CountryRepository;
import com.tradeanalytics.domain.model.CountryUI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public List<CountryUI> get(){
        List<CountryUI> countriesUI = new ArrayList<>();

        return countriesUI;
    }

}
