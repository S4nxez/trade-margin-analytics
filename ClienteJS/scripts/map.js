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

// ======================
// DIBUJAR RUTA
// ======================
function drawRoute(route){
  if(currentLayer) map.removeLayer(currentLayer);
  currentLayer = L.polyline(route.coords,{ color:"#22c55e", weight:4 }).addTo(map);
  map.fitBounds(currentLayer.getBounds());
}