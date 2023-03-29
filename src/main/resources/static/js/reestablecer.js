(function () {
	const btn = document.querySelector("#btn-password");
	btn.addEventListener("click", enviarCorreo);
		
})();

async function enviarCorreo() {
	const email = document.querySelector("#email").value;
	let alertas = []
	if(email == "") alertas.push("Email vacio");
	if(alertas.length != 0) {
		mostrarAlerta(alertas, "error");
	}
	else {
		const data = await enviarSimpleCorreo(email);
		if(data.errorCode == 404) {
			alertas.push("Correo no esta registrado")
			mostrarAlerta(alertas, "error");
		} else {
			alertas.push(data.message)
			mostrarAlerta(alertas, "successful");
		}
	}
}

function mostrarAlerta(alertas, tipo) {
	const div = document.querySelector("#alertas");
	while(div.childNodes.length != 0) div.removeChild(div.firstChild);
	alertas.forEach(message => {
		div.innerHTML += `<p class="${tipo}">${message}</p>`
	});
}