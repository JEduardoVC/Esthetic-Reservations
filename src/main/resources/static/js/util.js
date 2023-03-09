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
	const myHeaders = new Headers();
	myHeaders.append("Accept", 'application/json');
	myHeaders.append("Authorization", `Bearer ${sessionStorage.getItem("token")}`);
	formdata.append("mail", email);
	formdata.append("qr", qr.item);
	formdata.append("branch", branch);
	formdata.append("appointment", appointment)
	var requestOptions = {
	  method: 'POST',
	  headers: myHeaders,
	  body: formdata,
	  redirect: 'follow'
	};
	const resultado = await fetch("http://localhost:5500/api/appointment/sendMultiMail", requestOptions)
	const respuesta = await resultado.json();
	return respuesta;
}