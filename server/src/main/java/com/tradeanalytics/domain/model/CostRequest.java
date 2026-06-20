package com.tradeanalytics.domain.model;

import java.math.BigDecimal;

/**
 * Datos de entrada de la calculadora de costes de una operación internacional.
 * {@code freight} es el coste de transporte base de la ruta elegida (opcional);
 * si no se aporta, el backend lo estima a partir del peso.
 */
public record CostRequest(
        BigDecimal value,      // €
        Double weight,         // kg
        String incoterm,       // EXW / FOB / CIF
        String productType,
        BigDecimal freight
) {}
