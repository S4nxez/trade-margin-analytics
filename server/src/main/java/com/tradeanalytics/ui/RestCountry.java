package com.tradeanalytics.ui;

import com.tradeanalytics.domain.model.CountryUI;
import com.tradeanalytics.domain.services.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestCountry {
    private final CountryService countryService;

    public RestCountry(CountryService countryService){
        this.countryService=countryService;
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/countries")
    public List<CountryUI> get(){
        return countryService.get();
    }
}
