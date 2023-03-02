(async function() {	
	const btn = document.querySelector("#btn-agregar");
	if(sessionStorage.getItem("productoId") == null && btn != null) {
		document.querySelector("#accion-producto").textContent = "Agregar Producto";
		btn.addEventListener("click", agregarInventory)
	}
	else if(sessionStorage.getItem("productoId") != null && btn != null) {
		document.querySelector("#accion-producto").textContent = "Actualizar Producto";
		const inventario = await obtenerInventario();
		document.querySelector("#nombre").value = inventario.inventory_name;
		const img = document.createElement("img");
		img.src = `/Inventario/${inventario.imagen}.jpg`;
		const store = document.querySelector("#store");
		const parent = store.parentNode;
		parent.insertBefore(img, store);
		document.querySelector("#precio").value = inventario.price
		document.querySelector("#cantidad").value = inventario.store;
		btn.textContent = "Actualizar"
		btn.addEventListener("click", actualizarInventory);
	}
	else obtenerInventarios();
	if(location.pathname == "/app/owner/inventario" && sessionStorage.getItem("productoId")) sessionStorage.removeItem("productoId") 
	
})();

async function obtenerInventario() {
	const resultado = await fetch(`http://localhost:5500/api/owner/inventario/${sessionStorage.getItem("productoId")}`, {
		method: "GET",
		headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
		redirect: "follow"
	});
	const respuesta = await resultado.json();
	return respuesta;
}

async function obtenerInventarios() {
	const resultado = await fetch(`http://localhost:5500/api/owner/inventario/branch/${sessionStorage.getItem("branchId")}`, {
		method: 'GET',
		headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
		redirect: "follow"
	})
	const respuesta = await resultado.json();
	const inventario = respuesta.content;
	if(inventario == undefined) {
		inventario = [];
	}
	mostrarInventario(inventario)
}

function mostrarInventario(inventario) {
	const mostrarInventory = document.querySelector("#mostrar-productos")
	const divTitulos = document.createElement("div");
	divTitulos.innerHTML = `
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
	divTitulos.style = "display: grid;grid-template-columns: repeat(5, 1fr);font-weight: bold;";
	mostrarInventory.appendChild(divTitulos);
	inventario.forEach(producto => {
		const divContenido = document.createElement("div");
		divContenido.style = "display: grid;grid-template-columns: repeat(5, 1fr);";
		divContenido.innerHTML = `
			<div style="text-align: center;">
				<p>${producto.inventory_name}</p>
			</div>
			<div style="text-align: center;">
				<img src="/Inventario/${producto.imagen}.jpg">
			</div>
			<div style="text-align: center;">
				<p>${producto.price}</p>
			</div>
			<div style="text-align: center;">
				<p>${producto.store}</p>
			</div>
			<div>
				<button id="btn-update">Actualizar</button>
				<button id="btn_eliminar">Eliminar</button>
			</div>
		`;
		mostrarInventory.appendChild(divContenido);
		const btnUpdate = document.querySelector("#btn-update");
		btnUpdate.addEventListener("click", function() {
			sessionStorage.setItem("productoId", producto.id);
			window.location = "http://localhost:5500/app/owner/inventario/actualizar";
		})
		const btnDelete = document.querySelector("#btn_eliminar");
		btnDelete.addEventListener("click", async function() {
			await fetch(`http://localhost:5500/api/owner/inventario/eliminar/${producto.id}`, {
				method: "delete",
				headers: {
	            	"Authorization": `Bearer ${sessionStorage.getItem("token")}`
				},
				redirect: "follow"
			});
			window.location = "http://localhost:5500/app/owner/inventario";
		})
	})
}

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
	console.info(imagen.files[0]);
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
			window.location = "http://localhost:5500/app/owner/inventario";
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
		await fetch(`http://localhost:5500/api/owner/inventario/actualizar/${sessionStorage.getItem("productoId")}`, {
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
			window.location = "http://localhost:5500/app/owner/inventario";
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