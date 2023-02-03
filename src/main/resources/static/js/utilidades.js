function viewInventory() {
	window.location = `http://localhost:5500/app/owner/inventario/${sessionStorage.getItem("branchId")}/${sessionStorage.getItem("userId")}`;
}

function actualizar(e) {
	sessionStorage.setItem("inventoryId", e.value);
	window.location = `http://localhost:5500/app/owner/inventario/actualizar/${sessionStorage.getItem("branchId")}/${sessionStorage.getItem("userId")}/${e.value}`;
}

async function eliminar(e) {
	const resultado = await fetch(`http://localhost:5500/api/owner/inventario/eliminar/${e.value}`, {
		method: 'DELETE',
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`
		},
		redirect: "follow"
	});
	const respuesta = await resultado.json();
	viewInventory();
}

function regresar() {
	viewInventory();
}