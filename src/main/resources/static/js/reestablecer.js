(function () {
	const btn = document.querySelector("#btn-password");
	btn.addEventListener("click", enviarCorreo);
})();

async function enviarCorreo() {
	const email = document.querySelector("#email").value;
	var formdata = new FormData();
	formdata.append("mail", email);
	var requestOptions = {
	  method: 'POST',
	  body: formdata,
	  redirect: 'follow'
	};
	
	await fetch("http://localhost:5500/api/auth/sendMail", requestOptions)
	  .then(response => response.json())
	  .then(data => {
		let alertas = []
		alertas.push(data.message)
		if(data.errorCode == 404) mostrarAlerta(alertas, "error");
		else {
			mostrarAlerta(alertas, "successful");
		}
	});
}

function mostrarAlerta(alertas, tipo) {
	const div = document.querySelector("#alertas");
	while(div.childNodes.length != 0) div.removeChild(div.firstChild);
	alertas.forEach(message => {
		div.innerHTML += `<p class="${tipo}">${message}</p>`
	});
}