(async function() {
	const btn = document.querySelector("#btn-agregar");
	if(window.location.pathname == "/app/owner/servicios" && sessionStorage.getItem("serviceId")) sessionStorage.removeItem("serviceId")
	if(sessionStorage.getItem("serviceId") != null && btn != null) {
		btn.textContent = "Actualizar"
		let servicio = "";
		btn.addEventListener("click", actualizarServicio)
		servicio = await obtenerServicio();
		const nombre = document.querySelector("#nombre").value = servicio.service_name;
		const duracion = document.querySelector("#duracion").value = servicio.duration;
		const precio = document.querySelector("#precio").value = servicio.price;
	} else if(sessionStorage.getItem("serviceId") == null && btn != null) {
		btn.addEventListener("click", agregarServicio);
	} else {
		obtenerServicios();	
	}
})();

async function obtenerServicios() {
	const resultado = await fetch(`http://localhost:5500/api/owner/servicios/branch/${sessionStorage.getItem("branchId")}`, {
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
			window.location = "http://localhost:5500/app/owner/servicios/actualizar";
		})
		divAcciones.appendChild(btn_update);
		const btn_delete = document.createElement("BUTTON");
		btn_delete.textContent = "Eliminar"		
		btn_delete.addEventListener("click", async function() {
			const resultado = await fetch(`http://localhost:5500/api/owner/servicios/eliminar/${servicio.id}`, {
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
				document.location = "http://localhost:5500/app/owner/servicios";		
			}
		})
		divAcciones.appendChild(btn_delete);
		service.appendChild(divAcciones);
		div.appendChild(service);
	})
}

async function agregarServicio() {
	let alertas = [];
	const nombre = document.querySelector("#nombre").value;
	const duracion = document.querySelector("#duracion").valueAsNumber;
	const precio = document.querySelector("#precio").value;
	if(nombre == "") alertas.push("Nombre del Servicio Vacio");
	if(duracion == "") alertas.push("Duracion del Servicio Vacio");
	if(precio == "") alertas.push("Precio del Servicio Vacio");
	if(alertas.length == 0) {
		const url = `http://localhost:5500/api/owner/servicios/agregar`;
		const resultado = await fetch(url, {
			method: 'POST',
			headers: {
				"Content-Type": "application/json",
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
			body: JSON.stringify({
				"service_name": nombre,
				"price": precio,
				"duration": duracion,
				"id_branch": sessionStorage.getItem("branchId")
			}),
			redirect: "follow"
		})
		const respuesta = await resultado.json();
		if(respuesta.id) {
			document.location = "http://localhost:5500/app/owner/servicios";
		}
	} else {
		mostrarAlerta(alertas);
	}
}

async function actualizarServicio() {
	let alertas = [];
	const nombre = document.querySelector("#nombre").value;
	const duracion = document.querySelector("#duracion").valueAsNumber;
	const precio = document.querySelector("#precio").value;
	if(nombre == "") alertas.push("Nombre del Servicio Vacio");
	if(duracion == "") alertas.push("Duracion del Servicio Vacio");
	if(precio == "") alertas.push("Precio del Servicio Vacio");
	if(alertas.length == 0) {
		const url = `http://localhost:5500/api/owner/servicios/actualizar/${sessionStorage.getItem("serviceId")}`;
		const resultado = await fetch(url, {
			method: 'PUT',
			headers: {
				"Content-Type": "application/json",
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
			body: JSON.stringify({
				"service_name": nombre,
				"price": precio,
				"duration": duracion,
				"id_branch": sessionStorage.getItem("branchId")
			}),
			redirect: "follow"
		})
		const respuesta = await resultado.json();
		console.info(respuesta);
		if(respuesta.id) {
			document.location = "http://localhost:5500/app/owner/servicios";
		}
	} else {
		mostrarAlerta(alertas);
	}
}

async function obtenerServicio() {
	const resultado = await fetch(`http://localhost:5500/api/owner/servicios/${sessionStorage.getItem("serviceId")}`, {
		method: 'GET',
			headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
			redirect: "follow"
	})
	const respuesta = await resultado.json();
	return respuesta;
}

function mostrarAlerta(alertas) {
	const div = document.querySelector("#alertas");
	while(div.childNodes.length != 0) div.removeChild(div.firstChild);
	alertas.forEach(message => {
		div.innerHTML += `<p class="error">${message}</p>`
	});
}