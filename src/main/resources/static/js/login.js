(function() {
	const btn_login = document.querySelector("#btn-login");
	btn_login.addEventListener("click", presionarBoton)
})();

async function presionarBoton() {
	const username = document.querySelector("#username").value;
	const password = document.querySelector("#password").value;
	validarVacios(username, password);
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
		if(data.length == 3) {
			console.log(data);
			const token = data[0].token;
			const rol = data[1].id;
			document.cookie = `token=${token}`;
			document.cookie = `id_usuario=${data[2]}`;
			if(rol == 1) window.location.href = "http://localhost:5500/app/admin";
			if(rol == 4) window.location.href = "http://localhost:5500/app/client";
		} else {			
			if(data.errorCode == 500) {
				mostrarAlerta(data.message);
				return;
			}
		}
	})
}

function validarVacios(username, password) {
	let alertas = [];
	if(username == "") alertas = [...alertas, "Username Vacio"];
	if(password == "") alertas = [...alertas, "Passwoard Vacio"];
	const div = document.querySelector("#alertas");
	let alert = "";
	alertas.forEach(alerta => {
		alert += `		
	    <div>
	        <p class="error">${alerta}</p>
	    </div>
	    `;
	})
	div.innerHTML = alert;
}

function mostrarAlerta(message) {
	const div = document.querySelector("#alertas");
	div.innerHTML = `
    <div>
        <p class="error">${message}</p>
    </div>
    `;
}