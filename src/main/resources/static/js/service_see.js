(function() {
	if(sessionStorage.getItem("token") == null) window.location = `${BASE_URL}app/login`
	if(window.location.pathname == "/app/owner/servicios" && sessionStorage.getItem("serviceId")) sessionStorage.removeItem("serviceId")
	obtenerServicios();	
})();

async function obtenerServicios() {
	const resultado = await fetch(`${BASE_URL}api/owner/servicios/branch/${sessionStorage.getItem("branchId")}`, {
		method: 'GET',
		headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
		redirect: "follow"
	})
	const respuesta = await resultado.json();
	const servicios = respuesta.content;
	if(servicios == undefined) {
		servicios = [];
	}
	mostrarServicios(servicios)
}

function mostrarServicios(servicios) {
	const div = document.querySelector("#mostrar-servicios");
	const titulos = document.createElement("DIV");
	titulos.style = `display: grid;grid-template-columns: repeat(4, 1fr);font-weight: bold;`;
	titulos.innerHTML = `
		<div style="text-align: center;">
			<p>Nombre</p>
		</div>
		<div style="text-align: center;">
			<p>Duración</p>
		</div>
		<div style="text-align: center;">
			<p>Precio</p>
		</div>
		<div style="text-align: center;">
			<p>Acciones</p>
		</div>
	`;
	div.appendChild(titulos);
	servicios.forEach(servicio => {
		const service = document.createElement("DIV");
		service.style = `display: grid;grid-template-columns: repeat(4, 1fr);`;
		service.innerHTML = `
			<div style="text-align: center;">
				<p>${servicio.service_name}</p>
			</div>
			<div style="text-align: center;">
				<p>${servicio.duration}</p>
			</div>
			<div style="text-align: center;">
				<p>${servicio.price}</p>
			</div>
		`;
		const divAcciones = document.createElement("DIV");
		const btn_update = document.createElement("BUTTON");
		btn_update.textContent = "Actualizar"
		btn_update.addEventListener("click", function() {
			sessionStorage.setItem("serviceId", servicio.id)
			window.location = `${BASE_URL}app/owner/servicios/actualizar`;
		})
		divAcciones.appendChild(btn_update);
		const btn_delete = document.createElement("BUTTON");
		btn_delete.textContent = "Eliminar"		
		btn_delete.addEventListener("click", async function() {
			const resultado = await fetch(`${BASE_URL}api/owner/servicios/eliminar/${servicio.id}`, {
				method: 'DELETE',
				headers: {
						"Authorization": `Bearer ${sessionStorage.getItem("token")}`
					},
				redirect: "follow"
			})
			const respuesta = await resultado.json();
			if(respuesta.errorCode == 404) {
				let alertas = [respuesta.message];
				mostrarAlerta(alertas);
			} else {
				document.location = `${BASE_URL}app/owner/servicios`;		
			}
		})
		divAcciones.appendChild(btn_delete);
		service.appendChild(divAcciones);
		div.appendChild(service);
	})
}