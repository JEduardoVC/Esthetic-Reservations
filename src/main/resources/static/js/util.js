const BASE_URL = 'http://localhost:5500/';
//const BASE_URL = 'http://192.168.1.76:5500/';

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

function alerta(tipo, message) {
	const icon = (tipo == "error") ? "error" : "success"
	const title = (tipo == "error") ? "Oops..." : "Correcto"
	Swal.fire({
		icon: icon,
	  	title: title,
	  	text: message
	})
}

function showLoading(message) {
	Swal.fire({
        title: message,
        timer: 3000,
        showConfirmButton: false,
        didOpen: function() {
            Swal.showLoading()
        }
    })
}
