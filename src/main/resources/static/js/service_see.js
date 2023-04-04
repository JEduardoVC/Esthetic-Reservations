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
			<div>
				<button type="button" id="btn-update-${servicio.id}" name=${servicio.id}>Actualizar</button>
                <button type="button" id="btn-cancel-${servicio.id}" name=${servicio.id}>Eliminar</button>
			</div>
		`;
		div.appendChild(service);
		document.querySelector(`#btn-update-${servicio.id}`).addEventListener("click", update);
        document.querySelector(`#btn-cancel-${servicio.id}`).addEventListener("click", deleteProduct);
	})
}

function update(event) {
	sessionStorage.setItem("serviceId", event.target.name)
	window.location = `${BASE_URL}app/owner/servicios/actualizar`;
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
			const resultado = await fetch(`${BASE_URL}api/owner/servicios/eliminar/${event.target.name}`, {
				method: 'DELETE',
				headers: {
						"Authorization": `Bearer ${sessionStorage.getItem("token")}`
					},
				redirect: "follow"
			})
			const respuesta = await resultado.json();
			if(respuesta.error) {
				alerta("error", "No se puede eliminar el servicio porque ya esta asignado a una cita")
				return;
			} else {
				document.location = `${BASE_URL}app/owner/servicios`;		
			}
		}
	})
}