(function() {	
	const btn = document.querySelector("#btn-agregar");
	btn.addEventListener("click", agregarInventory);
})();

async function agregarInventory() {
	let alertas = [];
	const nombre = document.querySelector("#nombre").value;
	const precio = document.querySelector("#precio").value;
	const store = document.querySelector("#cantidad").value;
	const imagen = document.querySelector("#imagen");
	if(nombre == "") alertas.push("Nombre vacio");
	if(precio == "") alertas.push("Precio vacio");
	if(imagen == null) alertas.push("Falta seleccionar imagen");
	if(store == "") alertas.push("Cantidad de producto vacio");
	if(alertas.length == 0) {
		const id_branch = await obtenerBranch(readCookie("branchId")).then(data => data.id);
		var formdata = new FormData();
		formdata.append("inventory_name", nombre);
		formdata.append("price", precio);
		formdata.append("store", store);
		formdata.append("imagen", null);
		formdata.append("file", imagen.files[0]);
		formdata.append("id_branch", id_branch);
		console.info(formdata);
		await fetch("http://localhost:5500/api/owner/inventario/agregar", {
			method: "POST",
			headers: {
				'Accept': 'application/json',
	            "Authorization": `Bearer ${readCookie("token")}`
			},
			body: formdata,
			redirect: 'follow'
		})
		.then(response => response.json())
		.then(data => {
			document.location = "http://localhost:5500/app/owner/inventario";
		})
	} 
	else mostrarAlerta(alertas);
}

async function obtenerBranch(id) {
	const resultado = await fetch(`http://localhost:5500/api/branch/${id}`, {
		method: "GET",
		headers: {
			'Accept': 'application/json',
            'Content-Type': 'application/json'
		},
		redirect: "follow"
	});
	const branch = await resultado.json();
	return branch;
}

function mostrarAlerta(alertas) {
	const div = document.querySelector("#alertas");
	while(div.childNodes.length != 0) div.removeChild(div.firstChild);
	alertas.forEach(message => {
		div.innerHTML += `<p class="error">${message}</p>`
	});
}

function readCookie(name) {
  return decodeURIComponent(document.cookie.replace(new RegExp("(?:(?:^|.*;)\\s*" + name.replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*([^;]*).*$)|^.*$"), "$1")) || null;

}