const BASE_URL = 'http://localhost:5500/';
// const BASE_URL = 'http://192.168.100.5:5500/';
//const BASE_URL = 'http://192.168.1.76:5500/';
//const BASE_URL = 'http://192.168.1.70:5500/';
//const BASE_URL = 'http://172.15.41.174:5500/';

async function enviarSimpleCorreo(email) {
	var formdata = new FormData();
	formdata.append("mail", email);
	var requestOptions = {
		method: 'POST',
		body: formdata,
		redirect: 'follow'
	};
	let respuesta;
	console.info(`${BASE_URL}api/mail/reestablecer`)
	await fetch(`${BASE_URL}api/mail/reestablecer`, requestOptions)
		.then(response => response.json())
		.then(data => {
			respuesta = data;
		});
	return respuesta;
}

async function sendMail(id, created, appointment) {
	const url = `${BASE_URL}api/mail/${created ? "new" : "update"}/${appointment ? "appointment" : "sale"}`;
	console.warn(url);
	const newFormData = new FormData();
	newFormData.append("mail", sessionStorage.getItem("userId"));
	newFormData.append("branch", sessionStorage.getItem("branchId"));
	newFormData.append("id", id);
	console.warn(newFormData);
	console.error(created ? "POST" : "PUT");
	const resultado = await fetch(url, {
		method: created ? "POST" : "PUT",
		body: newFormData,
		headers: {
			Accept: "application/json",
			Authorization: `Bearer ${sessionStorage.getItem("token")}`
		}
	});
	const respuesta = await resultado.json();
	return respuesta;
}

function alerta(tipo, message, title = '') {
	let icon = tipo;
	if (title === '') {
		switch (tipo) {
			case 'error':
				title = 'Oops...';
				break;
			case 'warning':
				title = 'Cuidado';
				break;
			case 'info':
				title = 'Atención';
				break;
			case 'question':
				title = '¿Seguro?';
				break;
			default:
				title = 'Correcto';
				icon = 'success';
				break;
		}
	}
	Swal.fire({
		icon: icon,
		title: title,
		text: message
	})
}

function alertaHtml(tipo, htmlContent, title = '') {
	const icon = tipo;
	if (title === '') {
		switch (tipo) {
			case 'error':
				title = 'Oops...';
				break;
			case 'warning':
				title = 'Cuidado';
				break;
			case 'info':
				title = 'Atención';
				break;
			case 'question':
				title = '¿Seguro?';
				break;
			default:
				title = 'Correcto';
				icon = 'success';
				break;
		}
	}
	Swal.fire({
		icon: icon,
		title: title,
		html: htmlContent,
	})
}

async function confirmAlert(tipo = 'warning', title = '¿Estás seguro?', text = 'Esta acción podría ser irreversible.', confirmText = 'Sí, confirmar.') {
	const result = await Swal.fire({
		title: title,
		text: text,
		icon: tipo,
		showCancelButton: true,
		confirmButtonColor: '#d33',
		cancelButtonColor: '#9623c8',
		confirmButtonText: confirmText
	});
	return result.isConfirmed;
}

function showLoading(message) {
	Swal.fire({
		title: message,
		timer: 3000,
		showConfirmButton: false,
		didOpen: function () {
			Swal.showLoading()
		}
	})
}
