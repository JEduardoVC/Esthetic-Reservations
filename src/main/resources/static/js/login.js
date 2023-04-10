(function() {
	const btn_login = document.querySelector("#btn-login");
	btn_login.addEventListener("click", presionarBoton)
})();

async function presionarBoton() {
	const username = document.querySelector("#username").value;
	const password = document.querySelector("#password").value;
	if(username == "") {
		alerta("error", "Username Vacio");
		return;
	}
	if(password == "") {
		alerta("error", "Password Vacio");
		return;
	}
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
		if(data.errorCode == 401) {
			alerta("error", data.message)
		} else {
			sessionStorage.setItem("token", data.token);
			sessionStorage.setItem("userId", data.userId);
			sessionStorage.setItem("rol", data.userRoles[0].id);
			switch(data.userRoles[0].id) {
				case 1:
					document.location = `${BASE_URL}app/admin`;
					break;
				case 2:
					obtenerBranch(data.userId)
					.then(data => {
						if(data == 0) {
							alerta("error", "Encargado no tiene una sucursal asignada")
							return;
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
					location = `${BASE_URL}app/client/location`;
					break;
			}
		}
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