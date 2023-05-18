(function() {
	if(!sessionStorage.getItem("token")) location.href = `${BASE_URL}app/login`;
	showServices();
	$(".btn-principal").on("click", showModal);
}());

let servicesObj = [];

async function showModal(id = null) {
	const service = Object.is(typeof(id), "number") ? await getService(id) : null;
	$("body").append(`
		<div class="modal-overlay" id="modal-new-service">
	        <div class="modal-form">
	            <div class="volver-img">
	                <button class="btn-volver-img">
	                    <img class="filter" src="/img/volver.webp" onclick="removeModal()">
	                </button>
	            </div>
	            <h1>Nuevo servicio</h1>
	            <form>
	                <div class="field">
	                    <label class="filter" for="name">Nombre</label>
	                    <input type="text" name="name" id="name" value="${service ? service.service_name : ""}">
	                </div>
	                <div class="field">
	                    <label class="filter" for="time">Duración</label>
	                    <input type="time" name="time" id="time" min="00:00" value="${service ? service.duration : ""}">
	                </div>
	                <div class="field">
	                    <label class="filter" for="price">Precio</label>
	                    <input type="number" name="price" id="price" min="0" value="${service ? service.price : ""}">
	                </div>
	                <button id="${service ? service.id : ""}" class="btn-principal" type="button" onclick="${service ? "updateService(id)" : "addService()"}">${service ? "Actualizar" : "Añadir"}</button>
	            </form>
	        </div>
	    </div>
    `);
}

async function showServices() {
	await getServices();
	$("#mostrar-servicios").empty();
	servicesObj.forEach(service => {
		$("#mostrar-servicios").append(`
			<div class="service-registered">
                <p>${service.service_name}</p>
                <p>${service.duration}</p>
                <p><span>$</span> ${service.price}</p>
                <div class="actions-services">
                    <button type="button" class="btn-update" onclick="showModal(${service.id})">Actualizar</button>
                    <button type="button" class="btn-remove" onclick="deleteService(${service.id})">Eliminar</button>
                </div>
            </div>
		`);
	});
}

async function addService() {
	if(validate()) {
		const resultado = await fetch(`${BASE_URL}api/owner/services/agregar`, {
			method: "POST",
			body: JSON.stringify({
				"service_name": $("#name").val(),
				"duration": `${$("#time").val()}:00`,
				"price": $("#price").val(),
				"id_branch": sessionStorage.getItem("branchId")
			}),
			headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`,
				"Content-Type": "application/json"
			}
		})
		const respuesta = await resultado.json();
		servicesObj = [...servicesObj, {
			"id": respuesta.id,
			"service_name": respuesta.service_name,
			"duration": respuesta.duration,
			"price": respuesta.price
		}];
		removeModal();
		alerta("success", "Se ha agregado el servicio correctamente", "Servicio Agregado");
		showServices();
	}
}

async function updateService(id) {
	if(validate()) {
		const resultado = await fetch(`${BASE_URL}api/owner/services/actualizar/${id}`, {
			method: "PUT",
			body: JSON.stringify({
				"service_name": $("#name").val(),
				"duration": `${$("#time").val()}:00`,
				"price": $("#price").val(),
				"id_branch": sessionStorage.getItem("branchId")
			}),
			headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`,
				"Content-Type": "application/json"
			}
		})
		const respuesta = await resultado.json();
		const index = servicesObj.findIndex(e => e.id == id);
		servicesObj[index] = respuesta;
		removeModal();
		alerta("success", "Se ha actualizado el servicio correctamente", "Servicio Actualizado");
		showServices();
	}
}

async function deleteService(id) {
	const accion = await confirmAlert("warning", "Eliminar Servicio", "¿Esta seguro de eliminar el servicio?", "Eliminar");
	if(accion) {
		const resultado = await fetch(`${BASE_URL}api/owner/services/eliminar/${id}`, {
			method: "DELETE",
			headers: {
				Accept: "application/json",
				Authorization: `Bearer ${sessionStorage.getItem("token")}`
			},
			redirect: "follow"
		})
		const respuesta = await resultado.json();
		if(respuesta.error) {
			alerta("error", "No se puede eliminar servicio, porque hay una reservación con este servicio", "Error al eliminar servicio")
		} else {
			alerta("success", "Su servicio fue elminado exitosamete!", "Servicio Eliminado")
			servicesObj = servicesObj.find(service => service.id != id);
			showServices();
		}
	}
}

async function getServices() {
	const resultado = await fetch(`${BASE_URL}api/owner/services/branch/${sessionStorage.getItem("branchId")}`, {
		method: "GET",
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`,
			"Content-Type": "application/json"
		}
	})
	const respuesta = await resultado.json();
	servicesObj = respuesta.content ?? []; 
}

async function getService(id) {
	const resultado = await fetch(`${BASE_URL}api/owner/services/${id}`, {
		method: "GET",
		headers: {
			Accept: "application/json",
			Authorization: `Bearer ${sessionStorage.getItem("token")}`
		},
		redirect: "follow"
	})
	const respuesta = await resultado.json();
	respuesta.duration = `${respuesta.duration.split(":")[0]}:${respuesta.duration.split(":")[1]}`
	return respuesta;
}

function validate() {
	if($("#name").val() == "") {
		alerta("warning", "El nombre del servicio esta vacio", "Campo Vacio");
		return;
	}
	if($("#time").val() == "") {
		alerta("warning", "La duración del servicio esta vacio", "Campo Vacio");
		return;
	}
	if($("#price").val() == "") {
		alerta("warning", "El precio del servicio esta vacio", "Campo Vacio");
		return;
	}
	return true;
}

function removeModal() {	
	$("body").children().last().remove();
}