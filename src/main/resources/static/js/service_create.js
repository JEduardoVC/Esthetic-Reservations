(function() {
	if(sessionStorage.getItem("token") == null) window.location = `${BASE_URL}app/login`
	const btn = document.querySelector("#btn-agregar");
	btn.addEventListener("click", agregarServicio);
})();

async function agregarServicio() {
	let alertas = [];
	const nombre = document.querySelector("#nombre").value;
	const duracion = document.querySelector("#duracion").value + ':00';
	alert(duracion);
	const precio = document.querySelector("#precio").value;
	if(nombre == "") alertas.push("Nombre del Servicio Vacio");
	if(duracion == "00:00:00") alertas.push("Duracion del Servicio Vacio");
	if(precio == "") alertas.push("Precio del Servicio Vacio");
	if(alertas.length == 0) {
		const resultado = await fetch(`${BASE_URL}api/owner/servicios/agregar`, {
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