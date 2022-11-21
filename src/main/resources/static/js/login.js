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
		console.log(data);
		if(data.errorCode == 400) {
			if(data.message.username != null) alertas.push(`Username ${data.message.username}`);
			if(data.message.password != null) alertas.push(`Password ${data.message.password}`);
		} else if(data.errorCode == 401) {
			if(data.message != null) alertas.push(`${data.message}`);
		} else {
			document.cookie = `token=${data.token}; samesite=lax`;
			document.cookie = `userId=${data.userId}; samesite=lax`;
			document.cookie = `rol=${data.userRoles[0].id}; samesite=lax`;
			console.info(document.cookie);
			switch(data.userRoles[0].id) {
				case 1:
					document.location = "http://localhost:5500/app/admin";
				case 2:
					document.location = "http://localhost:5500/app/owner";
				case 3:
					document.location = "http://localhost:5500/app/employee";
				case 4:
					document.location = "http://localhost:5500/app/client";
			}
		}
		mostrarAlerta(alertas);
	})
}

function mostrarAlerta(alertas) {
	const div = document.querySelector("#alertas");
	while(div.childNodes.length != 0) div.removeChild(div.firstChild);
	alertas.forEach(message => {
		div.innerHTML += `<p class="error">${message}</p>`
	});
}