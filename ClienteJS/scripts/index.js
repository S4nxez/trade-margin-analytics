// ======================
// MAPA CON LÍMITES DE ZOOM
// ======================
const map = L.map("map", {
  worldCopyJump: false, // evita bucle horizontal
  maxBounds: [[-85, -180],[85,180]], // límites vertical y horizontal
  minZoom: 2,
  maxZoom: 6
}).setView([40, 0], 2);

L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", { attribution: "&copy; OpenStreetMap" }).addTo(map);
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
// DIBUJAR RUTA
// ======================
function drawRoute(route){
  if(currentLayer) map.removeLayer(currentLayer);
  currentLayer = L.polyline(route.coords,{ color:"#22c55e", weight:4 }).addTo(map);
  map.fitBounds(currentLayer.getBounds());
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
// DASHBOARD MOCK MEJORADO
// ======================
const ctx = document.getElementById('dashboardChart').getContext('2d');
const dashboardChart = new Chart(ctx, {
  type: 'line',
  data: {
    labels: ['Semana 1', 'Semana 2', 'Semana 3', 'Semana 4'],
    datasets: [
      { label: 'Coste medio por ruta (€)', data: [1200, 1150, 1300, 1250], borderColor: '#22c55e', backgroundColor: 'rgba(34,197,94,0.3)', tension:0.4, fill:true },
      { label: 'Tiempo medio (días)', data: [25, 28, 22, 26], borderColor: '#f59e0b', backgroundColor: 'rgba(245,158,11,0.3)', tension:0.4, fill:true },
      { label: 'Riesgo medio (%)', data: [50, 30, 60, 40], borderColor: '#ef4444', backgroundColor: 'rgba(239,68,68,0.3)', tension:0.4, fill:true }
    ]
  },
  options: {
    responsive: true,
    maintainAspectRatio: false,
    plugins: { legend:{labels:{color:"white"}} },
    scales:{
      x:{ ticks:{color:"white"} },
      y:{ ticks:{color:"white"} }
    }
  }
});

// ======================
// INIT
// ======================
renderRoutes();
renderBestRoute();