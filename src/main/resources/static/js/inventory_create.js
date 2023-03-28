(function() {
	if(sessionStorage.getItem("token") == null) window.location = `${BASE_URL}app/login`
	const btn = document.querySelector("#btn-agregar");
	btn.addEventListener("click", agregarInventory)
})();

async function agregarInventory() {
	let alertas = [];
	const nombre = document.querySelector("#nombre").value;
	const precio = document.querySelector("#precio").value;
	const store = document.querySelector("#cantidad").value;
	const imagen = document.querySelector("#imagen");
	if(nombre == "") alertas.push("Nombre vacio");
	if(precio == "") alertas.push("Precio vacio");
	if(imagen.files[0] == null) alertas.push("Falta seleccionar imagen");
	if(store == "") alertas.push("Cantidad de producto vacio");
	if(alertas.length == 0) {
		var formdata = new FormData();
		formdata.append("inventory_name", nombre);
		formdata.append("price", precio);
		formdata.append("store", store);
		formdata.append("imagen", null);
		formdata.append("file", imagen.files[0]);
		formdata.append("id_branch", sessionStorage.getItem("branchId"));
		await fetch(`${BASE_URL}api/owner/inventario/agregar`, {
			method: "POST",
			headers: {
				'Accept': 'application/json',
	            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
			body: formdata,
			redirect: 'follow'
		})
		.then(response => response.json())
		.then(data => {
			window.location = `${BASE_URL}app/owner/inventario`;
		})
	} 
	else mostrarAlerta(alertas);
}

function mostrarAlerta(alertas) {
	const div = document.querySelector("#alertas");
	while(div.childNodes.length != 0) div.removeChild(div.firstChild);
	alertas.forEach(message => {
		div.innerHTML += `<p class="error">${message}</p>`
	});
}