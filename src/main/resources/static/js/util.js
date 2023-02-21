const BASE_URL = 'http://localhost:5500/';

async function enviarSimpleCorreo(email) {
	var formdata = new FormData();
	formdata.append("mail", email);
	var requestOptions = {
	  method: 'POST',
	  body: formdata,
	  redirect: 'follow'
	};
	let respuesta;
	await fetch("http://localhost:5500/api/auth/sendSimpleMail", requestOptions)
	  .then(response => response.json())
	  .then(data => {
		respuesta = data;
	});
	return respuesta;
}

async function enviarMultimediaCorreo(email, qr, branch, appointment) {
	var formdata = new FormData();
	formdata.append("mail", email);
	formdata.append("qr", qr);
	formdata.append("branch", branch);
	formdata.append("appointment", appointment)
	var requestOptions = {
	  method: 'POST',
	  body: formdata,
	  redirect: 'follow'
	};
	let respuesta;
	await fetch("http://localhost:5500/api/appointment/sendMultiMail", requestOptions)
	  .then(response => response.json())
	  .then(data => {
		respuesta = data;
	});
	return respuesta;
}