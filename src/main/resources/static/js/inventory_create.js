(function() {
	if(sessionStorage.getItem("token") == null) window.location = `${BASE_URL}app/login`
	const btn = document.querySelector("#btn-agregar");
	btn.addEventListener("click", agregarInventory)
})();

async function agregarInventory() {
	const nombre = document.querySelector("#nombre").value;
	const precio = document.querySelector("#precio").value;
	const store = document.querySelector("#cantidad").value;
	const imagen = document.querySelector("#imagen");
	if(nombre == "") {
		alerta("error", "Nombre Vacio");
		return;
	}
	if(precio == "") {
		alerta("error", "Precio Vacio");
		return;
	}
	if(imagen.files[0] == null) {
		alerta("error", "Falta seleccionar archivo");
		return;
	}
	if(store == "") {
		alerta("error", "Cantidad de producto Vacia")
		return;
	}
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