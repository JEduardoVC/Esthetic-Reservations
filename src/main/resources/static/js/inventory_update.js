(async function() {
	if(sessionStorage.getItem("token") == null) window.location = `${BASE_URL}app/login`
	const inventario = await obtenerInventario();
	document.querySelector("#nombre").value = inventario.inventory_name;
	document.querySelector("#precio").value = inventario.price
	document.querySelector("#cantidad").value = inventario.store;
	const img = document.createElement("img");
	img.src = `/Inventario/${inventario.imagen}.jpg`;
	const store = document.querySelector("#store");
	const parent = store.parentNode;
	parent.insertBefore(img, store);
	const btn = document.querySelector("#btn-actualizar");
	btn.addEventListener("click", actualizarInventory)
})();

async function obtenerInventario() {
	const resultado = await fetch(`${BASE_URL}api/owner/inventario/${sessionStorage.getItem("productoId")}`, {
		method: "GET",
		headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
		redirect: "follow"
	});
	const respuesta = await resultado.json();
	return respuesta;
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
		await fetch(`${BASE_URL}api/owner/inventario/actualizar/${sessionStorage.getItem("productoId")}`, {
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