const userId = sessionStorage.getItem("userId");
const branchId = sessionStorage.getItem("branchId");
const myHeaders = new Headers();

myHeaders.append("Authorization", `Bearer ${sessionStorage.getItem("token")}`);

var map;
var markers = [];

document.addEventListener('DOMContentLoaded', function () {
	if (!sessionStorage.getItem("token")) location = `${BASE_URL}app/login`;
	if (sessionStorage.getItem("carrito")) sessionStorage.removeItem("carrito");
	initMap();
	showBranchs();
	document.querySelector("#selectionBranch").addEventListener("click", seleccionar);
});

var map;

async function initMap() {
	const resultado = await fetch(`${BASE_URL}api/branch/all`, {
		method: 'GET',
		headers: myHeaders
	});
	const sucursales = await resultado.json();
	const branchs = sucursales["content"];
	map = L.map('map').setView([branchs[0] ? branchs[0].latitude : 20.587313, branchs[0] ? branchs[0].longitude : -100.394397], 13);

	const osm = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
		attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
	});
	osm.addTo(map);
	const rapid = L.tileLayer('https://maptiles.p.rapidapi.com/es/map/v1/{z}/{x}/{y}.png?rapidapi-key=d81a9ea065msh70497032f58f18cp1fc7c8jsnc883ebaa4eb0', {
		attribution: '&copy: <a href="https://www.maptilesapi.com/">MapTiles API</a>, Datos de Mapa &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
	});

	rapid.addTo(map);

	const googleStreets = L.tileLayer('http://{s}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}', {
		maxZoom: 20,
		subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
	});
	googleStreets.addTo(map);

	const googleSatellite = L.tileLayer('http://{s}.google.com/vt/lyrs=s&x={x}&y={y}&z={z}', {
		maxZoom: 20,
		subdomains: ['mt0', 'mt1', 'mt2', 'mt3']
	});
	googleSatellite.addTo(map);

	const baseLayers = {
		'Satelite': googleSatellite,
		'OpenStreetMap': osm,
		'Map Tiles': rapid,
		'Google Maps': googleStreets
	};

	L.control.layers(baseLayers).addTo(map);

	var myIcon = L.icon({
		iconUrl: BASE_URL + 'img/barber_pole.svg',
		iconSize: [49.4, 123.5],
		iconAnchor: [22, 94],
		popupAnchor: [-3, -76],
	});

	let marker;
	branchs.forEach(branch => {
		marker = new L.marker([branch.latitude, branch.longitude], {
			icon: myIcon
		}).addTo(map).bindPopup(branch.branchName);
		markers.push(marker);
		marker.on('click', function(e){
			$('#branchs option').each(function(){
				const value = $(this).prop('value');
				const values = value.split(':');
				if(values[1] == branch.latitude && values[2] == branch.longitude){
					$('#branchs').val(branch.branchName).change();
					return;
				}
			});
		});
	});
}

async function showBranchs() {
	const resultado = await fetch('/api/branch/all', {
		method: 'GET',
		headers: myHeaders
	});
	const sucursales = await resultado.json();
	const branchs = sucursales["content"];
	const select = document.querySelector("#branchs");
	branchs.forEach(sucursal => {
		const option = document.createElement("option");
		option.innerHTML = `<option name="branchId" value="${sucursal.id}:${sucursal.latitude}:${sucursal.longitude}">${sucursal.branchName}</option>`
		select.appendChild(option)
	});
}

$('#branchs').on('change', function (e) {
	const selected = e.target.selectedOptions[0].childNodes[0].value.split(':');
	const latlng = L.latLng(selected[1], selected[2]);
	map.flyTo(latlng, 16);
	for(const m of markers){
		const mLat = m.getLatLng().lat;
		const mLon = m.getLatLng().lng;
		if(latlng.lat === mLat && latlng.lng === mLon){
			m.openPopup();
		}
	}
});

$("#btn-volver-location").on("click", () => {
	sessionStorage.clear();
	location.href = `${BASE_URL}app/login`;
})

function seleccionar() {
	const select = document.querySelector("#branchs");
	const value = select.selectedOptions[0].childNodes[0].value.split(':')[0];
	if (value == undefined) {
		alerta("error", "No ha seleccionado una sucursal");
		return;
	}
	sessionStorage.setItem("branchId", value);
	location = `${BASE_URL}app/client/dashboard`
}