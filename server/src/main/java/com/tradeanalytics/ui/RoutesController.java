package com.tradeanalytics.ui;

import com.tradeanalytics.common.Constants;
import com.tradeanalytics.dao.model.Country;
import com.tradeanalytics.domain.model.RoutesResponse;
import com.tradeanalytics.domain.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constants.ROUTES_PATH)
@RequiredArgsConstructor
public class RoutesController {

    private final CountryService countryService;

    public List<Country> get(){
        return countryService.get();
    }

    @GetMapping
    public RoutesResponse getRoutes(@RequestParam String origin,
                                    @RequestParam String destination) {
        return new RoutesResponse(countryService.getRoutes(origin, destination));
    }



}
