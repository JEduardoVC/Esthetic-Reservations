(function() {
	showItems();
	$("#filterTodo").on("change", () => {
		showItems();
	})
	$("#filterAppointment").on("change", () => {
		$("#appointment-services").empty();
		showAppointment();
	})
	$("#filterSale").on("change", () => {
		$("#appointment-services").empty();
		showSale();
	})
	sessionStorage.removeItem("carrito");
	sessionStorage.removeItem("appointmentId");
})();

let citasObj = [];
let saleObj = [];

function showItems() {
	$("#appointment-services").empty();
	showAppointment();
	showSale();
}

async function showAppointment() {
	await getAppointments();
	citasObj.forEach(cita => {
		$("#appointment-services").append(`
			<div class="appointment-container">
				<div class="info-appointment">
				    <p>Sucursal: <span>${cita.id_branch.name}</span></p>
				    <p>Encargada: <span>${cita.id_employee.name} ${cita.id_employee.lastName}</span></p>
				    <p>Ubicacion del local: <span>${cita.id_branch.location}</span></p>
				    <p>Fecha y hora de la cita: <span>${cita.appointment_date} a las ${changeFormatTime(cita.appointmnet_time)}</span></p>
				    <div class="products-services-appointment">
				        <p>Servicios: </p>
				        <div class="services-appointment">${getItems(cita.id_service, true)}</div>
				    </div>
				</div>
				<div class="actions-appointment">
				    <h4>Acciones</h4>
				    <button class="btn-update" onclick="updateAppointment(${cita.id})">Editar reservación</button>
				    <button class="btn-remove" onclick="deleteAppointment(${cita.id})">Cancelar reservación</button>
				</div>
			</div>
		`);
	});
}

function updateAppointment(id) {
	sessionStorage.setItem("appointmentId", id);
	location = `${BASE_URL}app/client/update/reservation`;
}

async function deleteAppointment(id) {
	const accion = await confirmAlert("warning", "Cancelar Reservación", "¿Esta seguro de cancelar su reservación?", "Aceptar");
	if(accion) {
		showLoading("Enviando correo...")
		const respuestaMail = await sendMailCancelAppointment(id, true);
		if(respuestaMail.statusCodeValue == 202) alerta("success", "Se le envio un correo al cliente para informarle la cancelación de su cita", "Cita Cancelada")
		const resultado = await fetch(`${BASE_URL}api/appointment/eliminar/${id}`, {
			method: "DELETE",
			headers: {
				Accept: "application/json",
				Authorization: `Bearer ${sessionStorage.getItem("token")}`
			}
		})
		const respuesta = await resultado.json();
		if(respuesta.code == 200) {
			citasObj = citasObj.find(cita => cita.id != id);
			setTimeout(() => {
				alerta("success", "Su reservación fue cancelada exitosamete!", "Reservación cancelada")
				showItems();
			},1500);
		}
	}
}

function getItems(items, isService = false) {
	let div = "";
	items.forEach(item => {
		const span = document.createElement("span");
		span.textContent = isService ? item.service_name : item.product.inventory_name;
		div += span.outerHTML;
	})
	return div;
}

async function getAppointments() {
	const respuesta = await fetch(`${BASE_URL}api/client/appointment/branch/${sessionStorage.getItem("userId")}/${sessionStorage.getItem("branchId")}`, {
		method: "GET",
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`,
			"Content-Type": "application/json"
		}
	});
	const resultado = await respuesta.json();
	citasObj = resultado.content ?? [];
}

async function showSale() {
	await getSales();
	comprasObj.forEach(sale => {
		$("#appointment-services").append(`
			<div class="appointment-container">
				<div class="info-appointment">
				    <p>Sucursal: <span>${sale.branch.branchName}</span></p>
				    <p></p>
				    <p>Ubicacion del local: <span>${sale.branch.location}</span></p>
				    <div class="products-services-appointment">
				        <p>Productos: </p>
				        <div class="services-appointment">${getItems(sale.productsList, false)}</div>
				    </div>
				</div>
				<div class="actions-appointment">
				    <h4>Acciones</h4>
				    <button class="btn-update" onclick="updateSale(${sale.id})">Editar compra</button>
				    <button class="btn-remove" onclick="deleteSale(${sale.id})">Cancelar compra</button>
				</div>
			</div>
		`);
	});
}

async function deleteSale(id) {
	const accion = await confirmAlert("warning", "Cancelar Compra", "¿Esta seguro de cancelar su compra?", "Aceptar");
	if(accion) {
		const resultado = await fetch(`${BASE_URL}api/sale/${id}`, {
			method: "DELETE",
			headers: {
				Accept: "application/json",
				Authorization: `Bearer ${sessionStorage.getItem("token")}`
			}
		});
		const respuesta = await resultado.json();
		if(respuesta) {
			comprasObj = comprasObj.find(compra => compra.id != id);
			setTimeout(() => {
				alerta("success", "Su compra fue cancelada exitosamete!", "Compra cancelada")
				showItems();
			}, 1500);
		}
	}
}

function updateSale(id) {
	sessionStorage.setItem("saleId", id);
	location.href = `${BASE_URL}app/client/reservation`
}

async function getSales() {
	const resultado = await fetch(`${BASE_URL}api/client/sales/branch/${sessionStorage.getItem("userId")}/${sessionStorage.getItem("branchId")}`, {
		method: "GET",
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`,
			"Content-Type": "application/json"
		}
	});
	const respuesta = await resultado.json();
	comprasObj = respuesta.content ?? [];
}

function volver() {
	location = `${BASE_URL}app/client/dashboard`;
}