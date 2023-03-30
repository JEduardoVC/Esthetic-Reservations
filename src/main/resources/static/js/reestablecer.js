(function () {
	const btn = document.querySelector("#btn-password");
	btn.addEventListener("click", enviarCorreo);
		
})();

async function enviarCorreo() {
	const email = document.querySelector("#email").value;
	if(email == "") {
		alerta("error", "Correo Vacio");
		return;
	}
	showLoading("Enviando correo...")
	const data = await enviarSimpleCorreo(email);
	if(data.errorCode == 404) {
		alerta("error", "Correo no registrado");
	} else {
		alerta("success", data.message)
	}
}