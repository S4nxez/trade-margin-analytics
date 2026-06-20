package com.tradeanalytics.domain.services;

import com.tradeanalytics.dao.model.Country;
import com.tradeanalytics.dao.model.Location;
import com.tradeanalytics.dao.model.RouteEdge;
import com.tradeanalytics.dao.repositories.CountryRepository;
import com.tradeanalytics.dao.repositories.LocationRepository;
import com.tradeanalytics.dao.repositories.RouteEdgeRepository;
import com.tradeanalytics.domain.model.RouteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

    private static final int MAX_LEGS = 3;       // profundidad máxima de la ruta (nº de tramos)
    private static final int MAX_ROUTES = 7;     // nº de rutas devueltas
    private static final int HOURS_PER_DAY = 24;

    private final CountryRepository countryRepository;
    private final LocationRepository locationRepository;
    private final RouteEdgeRepository routeEdgeRepository;

    public List<Country> get() {
        return countryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<RouteDto> getRoutes(String originCountry, String destinationCountry) {
        List<Location> origins = locationRepository.findByCountry_NameIgnoreCase(originCountry);
        List<Location> destinations = locationRepository.findByCountry_NameIgnoreCase(destinationCountry);

        if (origins.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Origen no encontrado: " + originCountry);
        }
        if (destinations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino no encontrado: " + destinationCountry);
        }

        Set<Long> destinationIds = destinations.stream()
                .map(Location::getId)
                .collect(Collectors.toSet());

        // Grafo de tramos activos indexado por la localización de origen
        Map<Long, List<RouteEdge>> adjacency = routeEdgeRepository.findByActiveTrue().stream()
                .collect(Collectors.groupingBy(e -> e.getOriginLocation().getId()));

        // Caminos simples origen -> destino (DFS con límite de tramos, sin ciclos)
        List<List<RouteEdge>> paths = new ArrayList<>();
        for (Location origin : origins) {
            findPaths(origin.getId(), destinationIds, adjacency, new ArrayDeque<>(), new HashSet<>(), paths);
        }

        // Mapea, ordena por score, limita y reasigna ids secuenciales estables
        List<RouteDto> ranked = paths.stream()
                .map(this::toRouteDto)
                .sorted(Comparator.comparingInt(this::score))
                .limit(MAX_ROUTES)
                .toList();

        List<RouteDto> result = new ArrayList<>(ranked.size());
        long id = 1;
        for (RouteDto r : ranked) {
            result.add(new RouteDto(id++, r.name(), r.coords(), r.cost(), r.time(), r.risk()));
        }
        return result;
    }

    private void findPaths(Long currentId, Set<Long> destinationIds,
                           Map<Long, List<RouteEdge>> adjacency,
                           Deque<RouteEdge> path, Set<Long> visited,
                           List<List<RouteEdge>> results) {
        if (path.size() >= MAX_LEGS) return;
        visited.add(currentId);
        for (RouteEdge edge : adjacency.getOrDefault(currentId, List.of())) {
            Long nextId = edge.getDestinationLocation().getId();
            if (visited.contains(nextId)) continue; // evita ciclos
            path.addLast(edge);
            if (destinationIds.contains(nextId)) {
                results.add(new ArrayList<>(path));
            } else {
                findPaths(nextId, destinationIds, adjacency, path, visited, results);
            }
            path.removeLast();
        }
        visited.remove(currentId);
    }

    private RouteDto toRouteDto(List<RouteEdge> path) {
        List<Location> locations = new ArrayList<>();
        locations.add(path.get(0).getOriginLocation());
        for (RouteEdge edge : path) {
            locations.add(edge.getDestinationLocation());
        }

        List<List<Double>> coords = locations.stream()
                .map(l -> Arrays.asList(l.getLatitude(), l.getLongitude()))
                .toList();

        String name = locations.stream()
                .map(Location::getName)
                .collect(Collectors.joining(" → "));

        BigDecimal totalCost = path.stream()
                .map(RouteEdge::getBaseCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalHours = path.stream()
                .mapToInt(RouteEdge::getEstimatedHours)
                .sum();
        int days = (int) Math.round((double) totalHours / HOURS_PER_DAY);

        return new RouteDto(0L, name, coords, totalCost.intValue(), days, risk(path.size()));
    }

    // El riesgo crece con los transbordos: directo bajo, 1 escala medio, 2+ alto
    private String risk(int legs) {
        if (legs <= 1) return "low";
        if (legs == 2) return "medium";
        return "high";
    }

    // Menor coste y tiempo = mejor posición en el ranking
    private int score(RouteDto dto) {
        return dto.cost() * 5 + dto.time() * 10;
    }
}
