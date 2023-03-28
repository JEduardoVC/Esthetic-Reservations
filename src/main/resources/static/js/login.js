(function() {
	const btn_login = document.querySelector("#btn-login");
	btn_login.addEventListener("click", presionarBoton)
})();

async function presionarBoton() {
	const username = document.querySelector("#username").value;
	const password = document.querySelector("#password").value;
	await fetch(`${BASE_URL}api/auth/user/login`,  {
		method: 'POST',
		headers: {
			'Accept': 'application/json',
            'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			"username": username,
			"password": password
		}),
		redirect: 'follow'
	})
	.then(response => response.json())
	.then(data => {
		let alertas = []
		if(data.errorCode == 400) {
			if(data.message.username != null) alertas.push(`Username ${data.message.username}`);
			if(data.message.password != null) alertas.push(`Password ${data.message.password}`);
		} else if(data.errorCode == 401) {
			if(data.message != null) alertas.push(`${data.message}`);
		} else if(data.userRoles[0].id != obtenerRol()) {
			alertas.push("Rol de usuario incorrecto");
		} else {
			sessionStorage.setItem("token", data.token);
			sessionStorage.setItem("userId", data.userId);
			sessionStorage.setItem("rol", data.userRoles[0].id);
			switch(data.userRoles[0].id) {
				case 1:
					document.location = "http://localhost:5500/app/admin";
					break;
				case 2:
					obtenerBranch(data.userId)
					.then(data => {
						if(data == 0) {
							alertas.push("Encargado no tiene una sucursal asignada");
							mostrarAlerta(alertas);
						} else {
							sessionStorage.setItem("branchId", data);
							location = `${BASE_URL}app/owner`;
						}
					})
					break;
				case 3:
					location = `${BASE_URL}app/employee`;
					break;
				case 4:
					location = `${BASE_URL}app`;
					break;
			}
		}
		mostrarAlerta(alertas);
	})
}

async function obtenerBranch(id_owner) {
	const resultado = await fetch(`${BASE_URL}api/branch/all/filter?filterBy=owner&filterTo=${id_owner}`, {
		method: "GET",
		headers: {
			'Accept': 'application/json',
            'Content-Type': 'application/json'
		},
		redirect: "follow"
	});
	const branch = await resultado.json();
	if(branch.content.length == 0) return 0;
	const id = branch.content[0].id;
	return id;
}

function obtenerRol() {
	if(document.querySelector("#admin").checked) return 1;
	if(document.querySelector("#owner").checked) return 2;
	if(document.querySelector("#employee").checked) return 3;
	if(document.querySelector("#cliente").checked) return 4;
}

function mostrarAlerta(alertas) {
	const div = document.querySelector("#alertas");
	while(div.childNodes.length != 0) div.removeChild(div.firstChild);
	alertas.forEach(message => {
		div.innerHTML += `<p class="error">${message}</p>`
	});
}