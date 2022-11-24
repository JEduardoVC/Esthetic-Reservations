(function() {
	const btn_login = document.querySelector("#btn-login");
	btn_login.addEventListener("click", presionarBoton)
})();

async function presionarBoton() {
	const username = document.querySelector("#username").value;
	const password = document.querySelector("#password").value;
	await fetch("http://localhost:5500/api/auth/user/login",  {
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
		} else {
			document.cookie = `token=${data.token}; samesite=lax`;
			document.cookie = `userId=${data.userId}; samesite=lax`;
			document.cookie = `rol=${data.userRoles[0].id}; samesite=lax`;
			switch(data.userRoles[0].id) {
				case 1:
					document.location = "http://localhost:5500/app/admin";
				case 2:
					obtenerBranch(data.userId)
					.then(data => {						
						document.cookie = `branchId=${data}; samesite=lax`;
						document.location = "http://localhost:5500/app/owner";
					})
				case 3:
					ocument.location = "http://localhost:5500/app/employee";
				case 4:
					document.location = "http://localhost:5500/app";
			}
		}
		mostrarAlerta(alertas);
	})
}

async function obtenerBranch(id_owner) {
	const resultado = await fetch(`http://localhost:5500/api/branch/all/filter?filterBy=owner&filterTo=${id_owner}`, {
		method: "GET",
		headers: {
			'Accept': 'application/json',
            'Content-Type': 'application/json'
		},
		redirect: "follow"
	});
	const branch = await resultado.json();
	const id = branch.content[0].id;
	return id;
}

function mostrarAlerta(alertas) {
	const div = document.querySelector("#alertas");
	while(div.childNodes.length != 0) div.removeChild(div.firstChild);
	alertas.forEach(message => {
		div.innerHTML += `<p class="error">${message}</p>`
	});
}