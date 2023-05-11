(function() {
	showSale();
	showAppointment();
	sessionStorage.removeItem("carrito");
	sessionStorage.removeItem("appointmentId");
})();

async function showAppointment() {
	const citas = await getAppointments();
	const divPadre = document.querySelector("#appointment-services");
	citas.forEach(cita => {
		const div = document.createElement("div");
		div.classList.add("appointment-container")
		div.innerHTML = `
			<div class="info-appointment">
			    <p>Sucursal: <span>${cita.id_branch.name}</span></p>
			    <p>Encargada: <span>AQUÍ VA EL EMPLEADO</span></p>
			    <p>Ubicacion del local: <span>${cita.id_branch.location}</span></p>
			    <p>Fecha y hora de la cita: <span>${cita.appointment_date} a las ${cita.appointmnet_time} horas</span></p>
			    <div class="products-services-appointment">
			        <p>Servicios: </p>
			        <div class="services-appointment">${getItems(cita.id_service)}</div>
			    </div>
			</div>
			<div class="actions-appointment">
			    <h4>Acciones</h4>
			    <button class="btn-update" onclick="updateAppointment(${cita.id})">Editar reservación</button>
			    <button class="btn-remove" onclick="deleteAppointment(${cita.id})">Cancelar reservación</button>
			</div>
		`;
		divPadre.appendChild(div);
	});
}

function updateAppointment(id) {
	sessionStorage.setItem("appointmentId", id);
	location = `${BASE_URL}app/client/update/reservation`;
}

async function deleteAppointment(id) {
	const accion = await confirmAlert("warning", "Cancelar Reservación", "¿Esta seguro de cancelar su reservación?", "Aceptar");
	if(accion) {
		const resultado = await fetch(`${BASE_URL}api/appointment/eliminar/${id}`, {
			method: "DELETE",
			headers: {
				Accept: "application/json",
				Authorization: `Bearer ${sessionStorage.getItem("token")}`
			}
		})
		const respuesta = await resultado.text();
		if(respuesta) {
			alerta("success", "Su reservación fue cancelada exitosamete!", "Reservación cancelada")
			setTimeout(() => {
				location.reload();
			},1500);
		}
	}
}

async function showSale() {
	const sales = await getSales();
	const divPadre = document.querySelector("#appointment-services");
	sales.forEach(sale => {
		const div = document.createElement("div");
		div.classList.add("appointment-container")
		div.innerHTML = `
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
		`;
		divPadre.appendChild(div);
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
			alerta("success", "Su compra fue cancelada exitosamete!", "Compra cancelada")
			setTimeout(() => {
				location.reload();
			}, 1500);
		}
	}
}

function updateSale(id) {
	sessionStorage.setItem("saleId", id);
	location.href = `${BASE_URL}app/client/reservation`
}

function getItems(items, isServices = true) {
	let div = "";
	items.forEach(item => {
		const span = document.createElement("span");
		span.textContent = isServices ? item.service_name : item.product.inventory_name;
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
	const citasObj = resultado.content;
	if(citasObj == undefined) citasObj = [];
	return citasObj;
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
	const saleObj = respuesta.content;
	if(saleObj == undefined) saleObj = [];
	return saleObj;
}