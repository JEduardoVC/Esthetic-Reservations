(function() {
	obtenerServios();
	
	obtenerInventario();
})();

async function obtenerServios() {
	const resultado = await fetch("http://localhost:5500/api/servicios/obtener", {
		method: "POST"
	});
	const servicios = await resultado.json();
	mostrarServicios(servicios.servicio);
}

function mostrarServicios(servicios) {
	const servicio = document.querySelector("#servicios");
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
	servicio.appendChild(div);
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