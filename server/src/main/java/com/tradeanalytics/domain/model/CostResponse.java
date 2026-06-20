package com.tradeanalytics.domain.model;

/**
 * Desglose del coste estimado de una operación internacional, en euros
 * redondeados. {@code total} es la suma de los cuatro componentes.
 */
public record CostResponse(
        long transport,   // transporte (flete × factor incoterm)
        long duty,        // arancel estimado
        long vat,         // IVA de importación
        long fees,        // fees de gestión / despacho
        long total,       // coste total estimado
        String precision  // baja / media / alta
) {}
