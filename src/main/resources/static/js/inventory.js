(function() {	
	const btn = document.querySelector("#btn-agregar");
	const btn_actualizar = document.querySelector("#btn-actualizar");
	if(btn != null) btn.addEventListener("click", agregarInventory);
	if(sessionStorage.getItem("inventoryId") != null && btn_actualizar == null) sessionStorage.removeItem("inventoryId")
	if(btn_actualizar != null) btn_actualizar.addEventListener("click", actualizarInventory);
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
		await fetch("http://localhost:5500/api/owner/inventario/agregar", {
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
			document.location = `http://localhost:5500/app/owner/inventario/${sessionStorage.getItem("branchId")}`;
		})
	} 
	else mostrarAlerta(alertas);
}

async function actualizarInventory() {
	let alertas = [];
	const nombre = document.querySelector("#nombre").value;
	const precio = document.querySelector("#precio").value;
	const store = document.querySelector("#cantidad").value;
	const imagen = document.querySelector("#imagen");
	if(nombre == "") alertas.push("Nombre vacio");
	if(precio == "") alertas.push("Precio vacio");
	if(store == "") alertas.push("Cantidad de producto vacio");
	if(alertas.length == 0) {
		var formdata = new FormData();
		formdata.append("inventory_name", nombre);
		formdata.append("price", precio);
		formdata.append("store", store);
		formdata.append("imagen", null);
		formdata.append("file", imagen.files[0]);
		formdata.append("id_branch", sessionStorage.getItem("branchId"));
		await fetch(`http://localhost:5500/api/owner/inventario/actualizar/${sessionStorage.getItem("inventoryId")}`, {
			method: "PUT",
			headers: {
				'Accept': 'application/json',
	            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
			body: formdata,
			redirect: 'follow'
		})
		.then(response => response.json())
		.then(data => {
			document.location = `http://localhost:5500/app/owner/inventario/${sessionStorage.getItem("branchId")}`;
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