let currentLayer;

// ======================
// MOCK DATA (rutas)
// ======================
const routes = [
  { id: 1, name: "China → España (Directo)", coords: [[31.23,121.47],[40.41,-3.70]], cost: 1200, time: 25, risk: "medium" },
  { id: 2, name: "China → Países Bajos → España", coords: [[31.23,121.47],[52.36,4.90],[40.41,-3.70]], cost: 1050, time: 30, risk: "low" },
  { id: 3, name: "USA → España", coords: [[40.71,-74.00],[40.41,-3.70]], cost: 1400, time: 18, risk: "low" },
  { id: 4, name: "USA → Portugal", coords: [[40.71,-74.00],[38.72,-9.13]], cost: 1300, time: 20, risk: "medium" },
];

// ======================
// RENDER RUTAS
// ======================
const routesContainer = document.getElementById("routes");
function getRiskClass(risk){ return risk==="low"?"good":risk==="medium"?"medium":"bad"; }
function renderRoutes() {
  routesContainer.innerHTML = "";
  routes.forEach((route)=>{
    const card = document.createElement("div");
    card.className = "route-card";
    card.innerHTML = `<div class="route-title">${route.name}</div>
      <div class="metric">💰 Coste: ${route.cost}€</div>
      <div class="metric">⏱ Tiempo: ${route.time} días</div>
      <div class="metric ${getRiskClass(route.risk)}">⚠ Riesgo: ${route.risk}</div>`;
    card.addEventListener("click",()=>drawRoute(route));
    routesContainer.appendChild(card);
  });
}

// ======================
// MEJOR RUTA
// ======================
function calculateBestRoute(){
  return routes.reduce((best,current)=>{
    const score=current.cost*0.5+current.time*10+(current.risk==="low"?0:current.risk==="medium"?200:500);
    const bestScore=best.cost*0.5+best.time*10+(best.risk==="low"?0:best.risk==="medium"?200:500);
    return score<bestScore?current:best;
  });
}
function renderBestRoute(){
  const best=calculateBestRoute();
  document.getElementById("best-route").innerText=best.name;
}

// ======================
// INIT
// ======================
renderRoutes();
renderBestRoute();