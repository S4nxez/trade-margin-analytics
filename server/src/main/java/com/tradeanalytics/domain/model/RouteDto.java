package com.tradeanalytics.domain.model;

import java.util.List;

public record RouteDto(
            Long id,
            String name,
            List<List<Double>> coords,
            Integer cost,
            Integer time,
            String risk
    ) {}