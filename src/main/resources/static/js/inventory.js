(function() {	
	obtenerInventory();
	
	let inventarioObj = [];

	function mostrarInventory() {
		const div = document.querySelector("#mostrar-productos");
		const titulos = document.createElement("DIV");
		titulos.style = `display: grid;grid-template-columns: repeat(5, 1fr);`
		titulos.innerHTML = `
			<div style="text-align: center;">
				<p>Nombre</p>
			</div>
			<div style="text-align: center;">
				<p>Imagen</p>
			</div>
			<div style="text-align: center;">
				<p>Precio</p>
			</div>
			<div style="text-align: center;">
				<p>En Almacen</p>
			</div>
			<div style="text-align: center;">
				<p>Acciones</p>
			</div>
		`;
		div.appendChild(titulos);
		inventarioObj.forEach(producto => {
			const productos = document.createElement("DIV");
			productos.style = `display: grid;grid-template-columns: repeat(5, 1fr);`;
			productos.innerHTML = `
				<div>
					<p>${producto.inventory_name}</p>
				</div>
				<div>
					<img src="C:/Esthetic-Reservation/Inventario/${producto.imagen}.jpg">
				</div>
				<div style="text-align: center;">
					<p>${producto.price}</p>
				</div>
				<div style="text-align: center;">
					<p>${producto.store}</p>
				</div>
				<div>
					<button id="btn-editar" value="${producto.id}">Actualizar</button>
					<button id="btn-eliminar" value="${producto.id}">Eliminar</button>
				</div>
			`;
			div.appendChild(productos);
		})
	}
	
	async function obtenerInventory() {
		const resultado = await fetch(BASE_URL + `api/owner/inventario/branch/${sessionStorage.getItem("branchId")}`, {
			method: "GET",
			headers: {
		            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
				},
			redirect: 'follow'
		})
		const respuesta = await resultado.json();
		inventarioObj = respuesta.content;
		mostrarInventory();
	}
	
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
	if(imagen.files[0] == null) alertas.push("Falta seleccionar imagen");
	if(store == "") alertas.push("Cantidad de producto vacio");
	if(alertas.length == 0) {
		const id_branch = await obtenerBranch(sessionStorage.getItem("branchId")).then(data => data.id);
		var formdata = new FormData();
		formdata.append("inventory_name", nombre);
		formdata.append("price", precio);
		formdata.append("store", store);
		formdata.append("imagen", null);
		formdata.append("file", imagen.files[0]);
		formdata.append("id_branch", id_branch);
		await fetch(BASE_URL + "api/owner/inventario/agregar", {
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
			document.location = BASE_URL + "app/owner/inventario";
		})
	} 
	else mostrarAlerta(alertas);
}

async function obtenerBranch(id) {
	const resultado = await fetch(BASE_URL + `api/branch/${id}`, {
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