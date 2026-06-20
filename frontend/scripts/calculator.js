// ==========================================================
// Calculadora de costes de operación internacional (MVP)
// Recoge los datos del panel y delega el cálculo en el backend
// (POST /routes/cost). Las reglas de negocio viven en el servidor;
// el cliente solo aporta la base de transporte de la ruta elegida.
// ==========================================================

const eur = new Intl.NumberFormat("es-ES", {
  style: "currency",
  currency: "EUR",
  maximumFractionDigits: 0,
});

// Ruta seleccionada por el usuario (integración opcional con rutas)
let selectedRoute = null;
let debounceTimer = null;

const calcEls = {};

function cacheCalcEls() {
  calcEls.value = document.getElementById("calcValue");
  calcEls.weight = document.getElementById("calcWeight");
  calcEls.incoterm = document.getElementById("calcIncoterm");
  calcEls.product = document.getElementById("calcProduct");
  calcEls.btn = document.getElementById("calcBtn");
  calcEls.total = document.getElementById("calcTotal");
  calcEls.transport = document.getElementById("calcTransport");
  calcEls.duty = document.getElementById("calcDuty");
  calcEls.vat = document.getElementById("calcVat");
  calcEls.fees = document.getElementById("calcFees");
  calcEls.precision = document.getElementById("calcPrecision");
}

// Flete base de la ruta: seleccionada > mejor ruta > ninguno (lo estima el backend).
function currentFreight() {
  if (selectedRoute && typeof selectedRoute.cost === "number") {
    return selectedRoute.cost;
  }
  if (typeof calculateBestRoute === "function") {
    try {
      const best = calculateBestRoute();
      if (best && typeof best.cost === "number") return best.cost;
    } catch (_) {
      /* sin rutas disponibles */
    }
  }
  return null;
}

function setResult({ transport, duty, vat, fees, total, precision }) {
  calcEls.total.textContent = eur.format(total);
  calcEls.transport.textContent = eur.format(transport);
  calcEls.duty.textContent = eur.format(duty);
  calcEls.vat.textContent = eur.format(vat);
  calcEls.fees.textContent = eur.format(fees);
  calcEls.precision.dataset.level = precision;
  calcEls.precision.textContent = `Precisión ${precision}`;
}

function setEmpty(level = "baja", label) {
  for (const el of [
    calcEls.total,
    calcEls.transport,
    calcEls.duty,
    calcEls.vat,
    calcEls.fees,
  ]) {
    el.textContent = "—";
  }
  calcEls.precision.dataset.level = level;
  calcEls.precision.textContent = label ?? `Precisión ${level}`;
}

async function estimate() {
  const value = Math.max(0, parseFloat(calcEls.value.value) || 0);
  const weight = Math.max(0, parseFloat(calcEls.weight.value) || 0);

  // Sin datos mínimos, mantenemos el bloque visible en estado vacío.
  if (value <= 0 && weight <= 0) {
    setEmpty();
    return;
  }

  const body = {
    value,
    weight,
    incoterm: calcEls.incoterm.value,
    productType: calcEls.product.value.trim(),
    freight: currentFreight(),
  };

  try {
    const res = await fetch(`${window.API_BASE}/routes/cost`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    setResult(await res.json());
  } catch (error) {
    console.error("Error calculando costes:", error);
    setEmpty("baja", "Sin conexión");
  }
}

// Actualización automática con debounce; el botón calcula al instante.
function scheduleEstimate() {
  clearTimeout(debounceTimer);
  debounceTimer = setTimeout(estimate, 250);
}

function initCalculator() {
  cacheCalcEls();
  if (!calcEls.value) return; // markup no presente

  calcEls.btn.addEventListener("click", estimate);
  [calcEls.value, calcEls.weight, calcEls.incoterm, calcEls.product].forEach(
    (el) => el.addEventListener("input", scheduleEstimate),
  );

  // Integración opcional con el sistema de rutas
  document.addEventListener("route:selected", (e) => {
    selectedRoute = e.detail || null;
    estimate();
  });
  document.addEventListener("routes:updated", () => {
    selectedRoute = null; // las rutas cambiaron: pasamos a usar la mejor ruta
    estimate();
  });

  setEmpty(); // estado inicial siempre visible
}

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initCalculator);
} else {
  initCalculator();
}
