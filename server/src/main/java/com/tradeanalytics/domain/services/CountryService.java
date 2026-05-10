package com.tradeanalytics.domain.services;

import com.tradeanalytics.dao.model.Country;
import com.tradeanalytics.dao.model.Location;
import com.tradeanalytics.dao.model.RouteEdge;
import com.tradeanalytics.dao.repositories.CountryRepository;
import com.tradeanalytics.dao.repositories.LocationRepository;
import com.tradeanalytics.dao.repositories.RouteEdgeRepository;
import com.tradeanalytics.domain.model.RouteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    private final LocationRepository locationRepository;

    private final RouteEdgeRepository routeEdgeRepository;


    public List<Country> get() {
        return countryRepository.findAll();
    }

    public List<RouteDto> getRoutes(String originCountry, String destinationCountry) {

        Location origin = locationRepository.findByCountry_NameIgnoreCase(originCountry)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Origin not found"));

        Location destination = locationRepository.findByCountry_NameIgnoreCase(destinationCountry)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Destination not found"));

       // List<RouteEdge> directRoutes =  routeEdgeRepository.findByOriginLocationAndDestinationLocationAndActiveTrue(origin, destination);

        List<RouteEdge> indirectRoutes =
                new ArrayList<>(routeEdgeRepository.findAll().stream()
                        .filter(RouteEdge::getActive)
                        .filter(r -> r.getOriginLocation().equals(origin)
                                || r.getDestinationLocation().equals(destination))
                        .toList());

        return indirectRoutes.stream()
                .map(r -> mapRoute(r, origin, destination))
                .sorted(Comparator.comparingInt(this::score))
                .limit(7)
                .toList();
    }

    private RouteDto mapRoute(RouteEdge edge, Location origin, Location destination) {

        double[] originCoords = {
                origin.getLatitude(),
                origin.getLongitude()
        };

        double[] destCoords = {
                destination.getLatitude(),
                destination.getLongitude()
        };

        return new RouteDto(
                edge.getId(),
                origin.getName() + " → " + destination.getName(),
                List.of(
                        List.of(originCoords[0], originCoords[1]),
                        List.of(destCoords[0], destCoords[1])
                ),
                edge.getBaseCost().intValue(),
                edge.getEstimatedHours(),
                risk(edge.getBaseCost(), edge.getEstimatedHours())
        );
    }

    private String risk(BigDecimal cost, Integer hours) {
        int score = cost.intValue() + (hours * 10);

        if (score < 1100) return "low";
        if (score < 1400) return "medium";
        return "high";
    }

    private int score(RouteDto dto) {
        return dto.cost() * 5 + dto.time() * 10;
    }


}
