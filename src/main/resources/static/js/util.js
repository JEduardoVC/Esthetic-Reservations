// const BASE_URL = 'http://localhost:5500/';
const BASE_URL = 'http://192.168.100.5:5500/';
//const BASE_URL = 'http://192.168.1.76:5500/';
//const BASE_URL = 'http://192.168.1.70:5500/';

async function enviarSimpleCorreo(email) {
	var formdata = new FormData();
	formdata.append("mail", email);
	var requestOptions = {
		method: 'POST',
		body: formdata,
		redirect: 'follow'
	};
	let respuesta;
	await fetch(`${BASE_URL}api/auth/sendSimpleMail`, requestOptions)
		.then(response => response.json())
		.then(data => {
			respuesta = data;
		});
	return respuesta;
}

async function enviarMultimediaCorreo(email, branch, appointment, created) {
	var formdata = new FormData();
	const myHeaders = new Headers();
	myHeaders.append("Accept", 'application/json');
	myHeaders.append("Authorization", `Bearer ${sessionStorage.getItem("token")}`);
	formdata.append("mail", email);
	formdata.append("branch", branch);
	formdata.append("appointment", appointment)
	var requestOptions = {
		method: 'POST',
		headers: myHeaders,
		body: formdata,
		redirect: 'follow'
	};
	const url = created ? `${BASE_URL}api/appointment/sendMultiMail` : `${BASE_URL}api/appointment/sendMultiMailUpdate`;
	const resultado = await fetch(url, requestOptions)
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
