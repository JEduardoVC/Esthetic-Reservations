(function () {
	if (sessionStorage.getItem("token") == null) window.location = `${BASE_URL}app/login`
	if (location.pathname == "/app/owner/inventario" && sessionStorage.getItem("productoId")) sessionStorage.removeItem("productoId")
	obtenerInventarios();
})();

async function obtenerInventarios() {
	const resultado = await fetch(`${BASE_URL}api/owner/inventario/branch/${sessionStorage.getItem("branchId")}`, {
		method: 'GET',
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`
		},
		redirect: "follow"
	})
	const respuesta = await resultado.json();
	const inventario = respuesta.content;
	if (inventario == undefined) {
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
	// divTitulos.style = "display: grid;grid-template-columns: repeat(5, 1fr);font-weight: bold;";
	// mostrarInventory.appendChild(divTitulos);
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
				<button type="button" id="btn-update-${producto.id}" name=${producto.id}>Actualizar</button>
                <button type="button" id="btn-cancel-${producto.id}" name=${producto.id}>Eliminar</button>
			</div>
		`;
		// mostrarInventory.appendChild(divContenido);
		document.querySelector(`#btn-update-${producto.id}`).addEventListener("click", update);
        document.querySelector(`#btn-cancel-${producto.id}`).addEventListener("click", deleteProduct);
	})
}

function update(event) {
	sessionStorage.setItem("productoId", event.target.name);
	window.location = `${BASE_URL}app/owner/inventario/actualizar`;
}

function deleteProduct(event) {
	Swal.fire({
		title: '¿Estas seguro?',
		text: "No se podra reevertir los cambios!!",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Eliminar'
	}).then(async (result) => {
		if (result.isConfirmed) {
			const resultado = await fetch(`${BASE_URL}api/owner/inventario/eliminar/${event.target.name}`, {
				method: 'DELETE',
				headers: {
					"Authorization": `Bearer ${sessionStorage.getItem("token")}`
				},
				redirect: "follow"
			})
			const respuesta = await resultado.json();
			if(respuesta.errorCode == 404) {
				alerta("Error", respuesta.message)
				return;
			} else {
				document.location = `${BASE_URL}app/owner/inventario`;
			}
		}
	})
}