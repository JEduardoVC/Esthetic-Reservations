(function(){
	if(!sessionStorage.getItem("token")) location.href = `${BASE_URL}app/login`;
	showAppointment();
})();

let citasObj = [];

async function getAppointments() {
	const resultado = await	fetch(`${BASE_URL}api/appointment/sucursal/${sessionStorage.getItem("branchId")}`, {
		method: "GET",
		headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
		redirect: "follow"
	});
	const citas = await resultado.json();
	citasObj = citas.content ?? [];
}

async function showAppointment() {
	await getAppointments();
	$("#show-appointment").empty();
	citasObj.forEach(async appointment => {
		if(sessionStorage.getItem("qr") == appointment.id) appointment = await changeStatus(appointment.id);
		console.info(appointment.appointmnet_time);
		$("#show-appointment").append(`
			<div class="appointment-confirmed">
		        <p>${appointment.id_client.name} ${appointment.id_client.lastName}</p>
		        ${showService(appointment.id_service)}
		        <div class="date-time-appointment">
		            <p>${appointment.appointment_date}</p>
		            <p>${changeFormatTime(appointment.appointmnet_time)}</p>
		        </div>				
		        <div class="status-appointment">
		        	<button class="btn-status-${appointment.id_status.id == 1 ? "success" : "pending"}">${appointment.id_status.status_name}</button>
		        </div>
		        <div class="cancel-appointment">
		            <button class="btn-remove" onclick="deleteAppointment(${appointment.id})">Cancelar</button>
		        </div>
		    </div>
		`);
	})
}

async function changeStatus(id) {
	const resultado = await fetch(`${BASE_URL}api/appointment/actualizar/status/${id}/1`, {
	method: "PUT",
	headers: {
		Accept: "application/json",
		Authorization: `Bearer ${sessionStorage.getItem("token")}`
	},
	redirect: "follow"
	})	
	const respuesta = await resultado.json();
	const index = citasObj.findIndex(e => e.id == id);
	citasObj[index] = respuesta;
	return respuesta;
}

async function deleteAppointment(id) {
	const accion = await confirmAlert("warning", "Cancelar la cita", "¿Esta seguro de cancelar la cita?", "Aceptar");
	if(accion) {
		showLoading("Enviando correo...")
		const respuestaMail = await sendMailCancelAppointment(id);
		if(respuestaMail.statusCodeValue == 202) alerta("success", "Se le envio un correo al cliente para informarle la cancelación de su cita", "Cita Cancelada")
		const resultado = await fetch(`${BASE_URL}api/appointment/eliminar/${id}`, {
			method: "DELETE",
			headers: {
				Accept: "application/json",
				Authorization: `Bearer ${sessionStorage.getItem("token")}`
			},
			redirect: "follow"
		})
		const respuesta = await resultado.json();
		if(respuesta.code == 200) {
			setTimeout(() => {
				alerta("success", "La cita fue cancelada exitosamente!", "Cita Cancelada")
				servicesObj = citasObj.find(cita => cita.id != id);
				showAppointment();
			}, 1500)()
		}
	}
}

function showService(listServices) {
	const div = document.createElement("div");
	div.classList.add("services-appointment");
	listServices.forEach(service => {
		div.innerHTML += `
        	<p>${service.service_name}</p>
		`
	})
	return div.outerHTML;
}