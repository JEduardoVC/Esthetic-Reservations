(function() {
	obtenerServios();
	
	//obtenerInventario();
})();

async function obtenerServios() {
	var formdata = new FormData();
	formdata.append("id_branch", "1");
	
	var requestOptions = {
	  method: 'POST',
	  body: formdata,
	  redirect: 'follow'
	};
	await fetch("http://localhost:5500/api/client/obtener/sevicios", requestOptions)
	.then(response => response.json())
	.then(data => {
		mostrarServicios(data);
	})
}

function mostrarServicios(servicios) {
	const service = document.querySelector("#servicios");
	const div = document.createElement("DIV");
	servicios.forEach(servicio => {
		const divServicio = document.createElement("DIV");
		divServicio.innerHTML = `
		<div>
    		<h3>${servicio.service_name}</h3>   		
			<p>${servicio.price}</p>
    		<span>${servicio.duration}</span>
    	</div>`;
    	div.appendChild(divServicio);
	})
	service.appendChild(div);
}

async function obtenerInventario() {
	const resultado = await fetch("http://localhost:5500/api/inventario/obtener", {
		method: "POST"
	});
	const inventarios = await resultado.json();
	mostrarInventario(inventarios.inventario);
}

function mostrarInventario(inventarios) {
	const servicio = document.querySelector("#inventario");
	const div = document.createElement("DIV");
	inventarios.forEach(inventario => {
		const divServicio = document.createElement("DIV");
		divServicio.innerHTML = `
		<div>
    		<h3>${servicio.service_name}</h3>        		
			<p>${servicio.price}</p>
    		<span>${servicio.duration}</span>
    	</div>`;
    	div.appendChild(divServicio);
	})
	servicio.appendChild(div);
}