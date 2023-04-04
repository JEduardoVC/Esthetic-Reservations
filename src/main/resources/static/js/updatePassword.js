(function() {
	const btn = document.querySelector("#btn-login");
	btn.addEventListener("click", updatePassword)
})();

async function updatePassword() {
	const email = document.querySelector("#email").value;
	const password = document.querySelector("#password").value;
	const replyPassword = document.querySelector("#replyPassword").value;
	if(email == "") {
		alerta("error", "Correo Vacio");
		return;
	}
	if(password == "") {
		alerta("error", "Contraseña vacia");
		return;
	}
	if(replyPassword == "") {
		alerta("error", "Repetir Contraseña vacia");
		return;
	}
	if(password != replyPassword) {
		alerta("error", "Contraseñas no coinciden")
		return;
	}
	const id = document.location.pathname.split("/")[5];
	const respuesta = await fetch(`${BASE_URL}api/auth/update/password/${id}`, {
		method: "PUT",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify({
			"email": email,
			"password": password
		}),
		redirect: "follow"
	})
	const resultado = await respuesta.json();
	if(resultado) location.href = `${BASE_URL}app/login`;
}