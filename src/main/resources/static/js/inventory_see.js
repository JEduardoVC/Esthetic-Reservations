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
		`;
		const divAcciones = document.createElement("DIV");
		const btn_update = document.createElement("BUTTON");
		btn_update.textContent = "Actualizar"
		btn_update.addEventListener("click", function () {
			sessionStorage.setItem("productoId", producto.id);
			window.location = `${BASE_URL}app/owner/inventario/actualizar`;
		})
		divAcciones.appendChild(btn_update);
		const btn_delete = document.createElement("BUTTON");
		btn_delete.textContent = "Eliminar"
		btn_delete.addEventListener("click", async function () {
			const resultado = await fetch(`${BASE_URL}api/owner/inventario/eliminar/${producto.id}`, {
				method: 'DELETE',
				headers: {
					"Authorization": `Bearer ${sessionStorage.getItem("token")}`
				},
				redirect: "follow"
			})
			const respuesta = await resultado.json();
			if (respuesta.errorCode == 404) {
				let alertas = [respuesta.message];
				mostrarAlerta(alertas);
			} else {
				document.location = `${BASE_URL}app/owner/inventario`;
			}
		})
		divAcciones.appendChild(btn_delete);
		divContenido.appendChild(divAcciones);
		mostrarInventory.appendChild(divContenido);
	})
}