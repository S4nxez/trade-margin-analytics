package com.tradeanalytics.domain.services;

import com.tradeanalytics.domain.model.CostRequest;
import com.tradeanalytics.domain.model.CostResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Estima el coste total de una operación internacional (transporte, arancel,
 * IVA y fees) a partir de los datos de la mercancía y la ruta. Concentra las
 * reglas de negocio que antes vivían en el cliente.
 */
@Service
public class CostEstimatorService {

    private static final BigDecimal VAT_RATE = new BigDecimal("0.21");      // IVA de importación (España)
    private static final BigDecimal FEE_FLAT = new BigDecimal("25");        // gestión / despacho base (€)
    private static final BigDecimal FEE_RATE = new BigDecimal("0.005");     // 0,5% sobre el valor
    private static final BigDecimal FREIGHT_PER_KG = new BigDecimal("2.5"); // flete estimado si no hay ruta (€/kg)
    private static final BigDecimal DEFAULT_DUTY = new BigDecimal("0.05");

    // Factor de transporte asumido por el importador según incoterm
    private static final Map<String, BigDecimal> INCOTERM_FACTOR = Map.of(
            "EXW", new BigDecimal("1.15"),
            "FOB", BigDecimal.ONE,
            "CIF", new BigDecimal("0.9")
    );

    // Arancel orientativo por categoría de producto (palabra clave -> tipo medio)
    private static final Map<String, BigDecimal> DUTY_RATES = Map.ofEntries(
            Map.entry("textil", new BigDecimal("0.12")),
            Map.entry("ropa", new BigDecimal("0.12")),
            Map.entry("moda", new BigDecimal("0.12")),
            Map.entry("calzado", new BigDecimal("0.12")),
            Map.entry("electronica", new BigDecimal("0.04")),
            Map.entry("electrónica", new BigDecimal("0.04")),
            Map.entry("movil", new BigDecimal("0.04")),
            Map.entry("ordenador", new BigDecimal("0.04")),
            Map.entry("juguete", new BigDecimal("0.047")),
            Map.entry("alimentacion", new BigDecimal("0.08")),
            Map.entry("alimentación", new BigDecimal("0.08")),
            Map.entry("comida", new BigDecimal("0.08")),
            Map.entry("maquinaria", new BigDecimal("0.02")),
            Map.entry("herramienta", new BigDecimal("0.027")),
            Map.entry("mueble", new BigDecimal("0.056")),
            Map.entry("muebles", new BigDecimal("0.056")),
            Map.entry("madera", new BigDecimal("0.04")),
            Map.entry("quimico", new BigDecimal("0.065")),
            Map.entry("químico", new BigDecimal("0.065")),
            Map.entry("cosmetica", new BigDecimal("0.065")),
            Map.entry("cosmética", new BigDecimal("0.065"))
    );

    public CostResponse estimate(CostRequest req) {
        BigDecimal value = nonNegative(req.value());
        double weight = req.weight() == null ? 0 : Math.max(0, req.weight());
        String incoterm = req.incoterm() == null ? "FOB" : req.incoterm().toUpperCase();

        DutyMatch duty = lookupDuty(req.productType());
        BigDecimal incotermFactor = INCOTERM_FACTOR.getOrDefault(incoterm, BigDecimal.ONE);

        // Transporte base: flete de la ruta si llega; si no, estimación por peso
        boolean fromRoute = req.freight() != null && req.freight().signum() > 0;
        BigDecimal baseFreight = fromRoute
                ? req.freight()
                : FREIGHT_PER_KG.multiply(BigDecimal.valueOf(weight));

        BigDecimal transport = baseFreight.multiply(incotermFactor);
        BigDecimal dutyAmount = value.multiply(duty.rate());
        // Base de aduana española: valor + transporte + arancel
        BigDecimal vat = VAT_RATE.multiply(value.add(transport).add(dutyAmount));
        BigDecimal fees = value.signum() > 0
                ? FEE_FLAT.add(value.multiply(FEE_RATE))
                : BigDecimal.ZERO;
        BigDecimal total = transport.add(dutyAmount).add(vat).add(fees);

        // Precisión: cuantos más datos fiables, mayor confianza
        int score = 0;
        if (value.signum() > 0) score++;
        if (weight > 0) score++;
        if (fromRoute) score++;
        if (duty.known()) score++;
        String precision = score >= 4 ? "alta" : score >= 2 ? "media" : "baja";

        return new CostResponse(
                round(transport),
                round(dutyAmount),
                round(vat),
                round(fees),
                round(total),
                precision
        );
    }

    // Busca el tipo arancelario por palabra clave del producto.
    private DutyMatch lookupDuty(String product) {
        if (product == null || product.isBlank()) return new DutyMatch(DEFAULT_DUTY, false);
        String text = product.toLowerCase().trim();
        for (Map.Entry<String, BigDecimal> entry : DUTY_RATES.entrySet()) {
            if (text.contains(entry.getKey())) return new DutyMatch(entry.getValue(), true);
        }
        return new DutyMatch(DEFAULT_DUTY, false);
    }

    private static BigDecimal nonNegative(BigDecimal v) {
        return (v == null || v.signum() < 0) ? BigDecimal.ZERO : v;
    }

    private static long round(BigDecimal v) {
        return v.setScale(0, RoundingMode.HALF_UP).longValue();
    }

    private record DutyMatch(BigDecimal rate, boolean known) {}
}
