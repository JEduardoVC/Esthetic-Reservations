(function() {
	const btn = document.querySelector("#btn-login");
	btn.addEventListener("click", updatePassword)
})();

async function updatePassword() {
	const alerts = [];
	const email = document.querySelector("#email").value;
	const password = document.querySelector("#password").value;
	const replyPassword = document.querySelector("#replyPassword").value;
	if(email == "") alerts.push("Email vacio");
	if(password == "") alerts.push("Contraseña vacia");
	if(replyPassword == "") alerts.push("Repetir Contraseña vacia");
	if(password != replyPassword) alerts.push("Contraseñas no coinciden")
	if(alerts.length == 0) {
		const id = document.location.pathname.split("/")[5];
		console.warn(JSON.stringify({
				"email": email,
				"password": password
			}));
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
		console.info(resultado);
		if(resultado) location.href = `${BASE_URL}app/login`;
	} else {
		mostrarAlerta(alerts);
	}
}

function mostrarAlerta(alertas) {
	const div = document.querySelector("#alertas");
	while(div.childNodes.length != 0) div.removeChild(div.firstChild);
	alertas.forEach(message => {
		div.innerHTML += `<p class="error">${message}</p>`
	});
}