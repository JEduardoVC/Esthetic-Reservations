const userId = sessionStorage.getItem("userId");
const branchId = sessionStorage.getItem("branchId");
const myHeaders = new Headers();

myHeaders.append("Authorization", `Bearer ${sessionStorage.getItem("token")}` );

var map;

document.addEventListener('DOMContentLoaded',  function() {
	initMap();
	showBranchs();
	document.querySelector("#selectionBranch").addEventListener("click", seleccionar);
});


async function initMap(){
  const resultado = await fetch('/api/branch/all',{method: 'GET'});
  const sucursales = await resultado.json();
  const branchs = sucursales["content"];
  var map = L.map('map').setView([21.1419707351680, -100.31784830972968], 8);

  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
  }).addTo(map);
  
  branchs.forEach(branch => {
    const {id, branchName} = branch;
    L.marker([21.1419707351680+id, -100.31784830972968+id]).addTo(map)
        .bindPopup(branchName)
        .openPopup();
  });
}

async function showBranchs(){
    const resultado = await fetch('/api/branch/all',{method: 'GET'});
    const sucursales = await resultado.json();
    const branchs = sucursales["content"];
    let select =  document.querySelector("#branchs");
    branchs.forEach(sucursal => {
		const option = document.createElement("option");
		option.innerHTML = `<option name="branchId" value="${sucursal.id}">${sucursal.branchName}</option>`
		select.appendChild(option)
	});
}

function seleccionar() {
	console.info(document.querySelector("#branchs"))
	console.warn("Entrando");
}