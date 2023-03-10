(() => {
	obtenerCitas();
})();

async function obtenerCitas() {
	const resultado = await	fetch(`http://localhost:5500/api/appointment/sucursal/${sessionStorage.getItem("branchId")}`, {
		method: "GET",
		headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
		redirect: "follow"
	});
	const citas = await resultado.json();
	mostrarCitas(citas == undefined ? [] : citas.content)
}

function mostrarCitas(citas) {
	const padre = document.querySelector("#mostrar-citas");
	const divTitulos = document.createElement("div");
	divTitulos.innerHTML = `
		<div style="text-align: center;">
			<p>Usuario</p>
		</div>
		<div style="text-align: center;">
			<p>Servicios</p>
		</div>
		<div style="text-align: center;">
			<p>Fecha</p>
		</div>
		<div style="text-align: center;">
			<p>Hora</p>
		</div>
		<div style="text-align: center;">
			<p>Acciones</p>
		</div>
	`;
	divTitulos.style = "display: grid;grid-template-columns: repeat(5, 1fr);font-weight: bold;";
	padre.appendChild(divTitulos);
	citas.forEach(cita => {
		const divContenido = document.createElement("div");
		divContenido.style = "display: grid;grid-template-columns: repeat(5, 1fr);";
		divContenido.innerHTML = `
			<div style="display: flex; align-items: center; justify-content: center;">
				<p>${cita.id_client.name} ${cita.id_client.lastName}</p>
			</div>
			<div style="display: flex; align-items: center; justify-content: center;">
				${obtenerServicios(cita.id_service)}
			</div>
			<div style="display: flex; align-items: center; justify-content: center;">
				<p>${cita.appointment_date}</p>
			</div>
			<div style="display: flex; align-items: center; justify-content: center;">
				<p>${cita.appointmnet_time}</p>
			</div>
			<div style="display: flex; align-items: center; justify-content: center;">
				<button id="btn_eliminar">Cancelar</button>
			</div>
		`;
		padre.appendChild(divContenido)
	})
	
	function obtenerServicios(servicios) {
		const services = document.createElement("div");
		servicios.forEach(servicio => {
			const divContenido = document.createElement("div");
			divContenido.style = `display: grid;grid-template-row: repeat(${servicios.length}, 1fr`;
			divContenido.innerHTML = `
				<div style="text-align: center;">
					<p>${servicio.service_name}</p>
				</div>
			`;
			services.appendChild(divContenido);
		})
		return services.outerHTML;
	}
}