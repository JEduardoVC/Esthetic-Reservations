(async function() {
	if(sessionStorage.getItem("token") == null) window.location = `${BASE_URL}app/login`
	const servicio = await obtenerServicio();
	document.querySelector("#nombre").value = servicio.service_name;
	document.querySelector("#duracion").value = servicio.duration;
	document.querySelector("#precio").value = servicio.price;
	const btn = document.querySelector("#btn-actualizar");
	btn.addEventListener("click", actualizarServicio)
})();

async function obtenerServicio() {
	const resultado = await fetch(`${BASE_URL}api/owner/servicios/${sessionStorage.getItem("serviceId")}`, {
		method: 'GET',
			headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
			redirect: "follow"
	})
	const respuesta = await resultado.json();
	return respuesta;
}

async function actualizarServicio() {
	let alertas = [];
	const nombre = document.querySelector("#nombre").value;
	let duracion = document.querySelector("#duracion").value;
	if(duracion.split(':').length == 2){
		duracion += ':00';
	}
	const precio = document.querySelector("#precio").value;
	if(nombre == "") alertas.push("Nombre del Servicio Vacio");
	if(duracion == "") alertas.push("Duracion del Servicio Vacio");
	if(precio == "") alertas.push("Precio del Servicio Vacio");
	if(alertas.length == 0) {
		const url = `${BASE_URL}api/owner/servicios/actualizar/${sessionStorage.getItem("serviceId")}`;
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
		if(respuesta.id) {
			document.location = `${BASE_URL}app/owner/servicios`;
		}
	} else {
		mostrarAlerta(alertas);
	}
}

function mostrarAlerta(alertas) {
	const div = document.querySelector("#alertas");
	while(div.childNodes.length != 0) div.removeChild(div.firstChild);
	alertas.forEach(message => {
		div.innerHTML += `<p class="error">${message}</p>`
	});
}