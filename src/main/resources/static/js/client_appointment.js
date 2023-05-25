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

var infos = {};

async function showAppointment() {
	await getAppointments();
	citasObj.forEach(cita => {
		const data = {
			appointment: cita.id,
			client: sessionStorage.getItem('userId'),
			service: cita.id_service[0].service_name,
			employee: cita.id_employee.id,
			employeeName: cita.id_employee.name + ' ' + cita.id_employee.lastName
		}
		infos[cita.id] = data;
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
					${cita.id_status.id==1 ? '<button class="btn-review trigger-modal" data-target="'+cita.id+'">Reseña</button>' : ''}
				</div>
			</div>
		`);
	});
}

$('.star-comment').on('click', function(e){
	const rating = $(this).data('value');
	$('#rating').val(rating);
	$('.star-comment').each(function(){
		const value = $(this).data('value');
		if(Number(value) <= Number(rating)){
			$(this).removeClass('bi-star');
			$(this).addClass('bi-star-fill');
		} else {
			$(this).addClass('bi-star');
			$(this).removeClass('bi-star-fill');
		}
	});
});

function removeModal(){
	$('#modal-new-comment').addClass('no-mostrar');
}

$(document).on('click', '.trigger-modal', async function(e){
	$('#btn-action').data('action','add');
	const data = infos[$(this).data('target')]
	$('#btn-action').data('target', $(this).data('target'));
	$('#modal-new-comment').removeClass('no-mostrar');
	$('#emp-name').html(data.employeeName);
	$('#service-name').html(data.service);
	const review = await getReview($(this).data('target'));
	if(review.errorCode === undefined){
		$('#review').val(review.comment);
		$('.star-comment').each(function(){
			const value = $(this).data('value');
			if(Number(value) <= Number(review.rating)){
				$(this).removeClass('bi-star');
				$(this).addClass('bi-star-fill');
			} else {
				$(this).addClass('bi-star');
				$(this).removeClass('bi-star-fill');
			}
		});
		$('#rating').val(review.rating);
		$('#btn-action').data('action','edit');
		$('#btn-action').data('review', review.id);
	}
});

async function getReview(appointmentId){
	const review = await request({
		method: 'GET',
		endpoint: 'api/comment',
		urlParams: {
			by: 'appointment',
			filterTo: appointmentId
		},
		fetch: 'data'
	});
	return review;
}

$('#btn-action').on('click', async function(e){
	const data = infos[$(this).data('target')];
	if($('#review').val() === ''){
		alerta('warning', 'Escribe tu reseña', 'Cuéntanos tu experiencia');
		return;
	}
	const body = {
		rating: $('#rating').val(),
		comment: $('#review').val(),
		clientId: data.client,
		employeeId: data.employee,
		appointmentId: data.appointment
	}
	if($('#rating').val() === '0'){
		const confirmed = await confirmAlert('warning', '¿Dejar en Cero estrellas el servicio?', '¿Estás seguro de dar esta calificación?', 'Sí, confirmar.');
		if(!confirmed){
			return;
		}
	}
	let done = false;
	if($(this).data('action') === 'add'){
		done = await saveComment(body);
	} else {
		done = await updateComment(body, $(this).data('review'));
	}
	if(done)
	alerta('success', 'Comentario enviado. Gracias.')
	removeModal();
});

async function saveComment(body){
	const response = await request({
		method: 'POST',
		endpoint: 'api/comment',
		body: body,
		fetch: 'response'
	});
	if (response.isValid) {
        return true;
    } else {
		alerta('error', 'Ocurrió un error al guardar la reseña.');
    }
}
async function updateComment(body, id){
	const response = await request({
		method: 'PUT',
		endpoint: 'api/comment/' + id,
		body: body,
		fetch: 'response'
	});
	if (response.isValid) {
        return true;
    } else {
		alerta('error', 'Ocurrió un error al actualizar la reseña.');
    }
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