(function() {
	if(!sessionStorage.getItem("token")) location = `${BASE_URL}app/login`;
	mostrarServicios();
//	sessionStorage.removeItem("carrito");
})();

let carrito = JSON.parse(sessionStorage.getItem("carrito")) ?? {servicios: [], productos: []};

async function mostrarServicios() {
	const serviciosObj = await obtenerServicios();
	if(!serviciosObj) return;
	serviciosObj.forEach(servicio => {
		const divServicio = document.createElement("div");
		divServicio.classList.add("one-service");
		divServicio.innerHTML = `
	        <div class="description-oneservice">
	            <h4>${servicio.service_name}</h4>
	        </div>
		`;
		const div = document.querySelector(".more-services");
		divServicio.addEventListener("click", () => {
			sessionStorage.setItem("serviceId", servicio.id);
			mostrarServicio();
		});
		(servicio.id != sessionStorage.getItem("serviceId")) ? divServicio.classList.remove("selected") : divServicio.classList.add("selected");
		div.appendChild(divServicio);
	});
}

async function mostrarServicio() {
	const servicio = await obtenerServicio();
	if(!servicio) return;
	console.info(carrito.servicios)
	const value = carrito.servicios.find(serv => serv.id == servicio.id) ?? {cantidad: 0};
	const divServicio = document.createElement("div");
	divServicio.innerHTML = `
		<div class="container">
			<div>
	            <button class="btn-secundario" onclick="volver()">Volver</button>
	        </div>
			<div class="time-cost-service" id="info-service">
				<div class="title-service">
					<h4>${servicio.service_name}</h4>
				</div>	
				<div class="circular-time" id="circular-time">
					<div class="value-time" id="value-time"></div>
				</div>
				<div class="form-service">
					<p class="price-service">$${servicio.price}</p>
					<div class="input-service">
						<button class="decrement" id="decrement" type="button" onclick="stepper(this)">-</button>
							<input id="quantity-services" type="number" min="0" value="${value.cantidad}" step="1">
						<button class="increment" id="increment" type="button" onclick="stepper(this)">+</button> 
					</div>
					<button class="btn-principal" id="btn-añadir">Añadir</button>
				</div>
			</div>
		</div>
    `;
    const div = document.querySelector(".show-service"); 
    if(div.childNodes.length == 1) div.removeChild(div.lastElementChild)
    div.appendChild(divServicio);
	mostrarTiempo(parseInt(servicio.duration.split(':')[0]) * 60 + parseInt(servicio.duration.split(':')[1]));
	document.querySelector("#btn-añadir").addEventListener("click", () => {
		const cantidad = document.querySelector("#quantity-services").getAttribute("value");
		const index = carrito.servicios.findIndex(serv => serv.id == servicio.id);	
		if(index !== -1) {
			const carritoObj = JSON.parse(sessionStorage.getItem("carrito"));
			carritoObj.servicios[index] = {id: servicio.id, cantidad: cantidad}
			carrito = carritoObj;
			alerta("success", "Servicio actualizado correctamente", "Hecho");	
		}
		else {
			carrito.servicios = [...carrito.servicios, {id: servicio.id, cantidad:cantidad}]
			alerta("success", "Producto agregado correctamente", "Hecho");
		}
		sessionStorage.setItem("carrito", JSON.stringify(carrito));
	})
}

async function obtenerServicios() {
	const resultado = await fetch(`${BASE_URL}api/client/service/branch/${sessionStorage.getItem("branchId")}`, {
		method: 'GET',
		headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
		redirect: "follow"
	})
	const respuesta = await resultado.json();
	const serviciosObj = respuesta.content;
	if(serviciosObj == undefined) {
		serviciosObj = [];
	}
	return serviciosObj;
}

async function obtenerServicio() {
	const resultado = await fetch(`${BASE_URL}api/client/service/${sessionStorage.getItem("serviceId")}`, {
		method: 'GET',
			headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
			redirect: "follow"
	})
	const respuesta = await resultado.json();
	return respuesta;
}

function mostrarTiempo(time) {
	let circularTime = document.querySelector("#circular-time");
	let valueTime = document.querySelector("#value-time");
	let progressValue = 0;
	let progressValueEnd = time;
	let speed = 15;
	let newValor = 0;
	let progress = setInterval(() => {
	    progressValue++;
	    valueTime.textContent = `${progressValue} Min`;
	    circularTime.style.background = `
	        conic-gradient(
	            #7218C6 ${progressValue * 6}deg,
	            #7218c627 ${progressValue * 6}deg
	        )`;
	    if(progressValue > 60){
	        
	        newValor++;
	        circularTime.style.background = `
	        conic-gradient(
	            #7218C6 ${newValor * 6}deg,
	            #7218c627 ${newValor * 6}deg
	        )`;
	    }
	    if(progressValue == progressValueEnd){
	        clearInterval(progress)
	    }
	}, speed)
}

function stepper(btn){
	const input = document.querySelector("#quantity-services");
    let id = btn.getAttribute("id");
    let value = input.getAttribute("value");
    let step = input.getAttribute("step");
    let min = input.getAttribute("min");
    let calStep = (id == "increment") ? (step*1): (step*-1);
    let newValue = parseInt(calStep) + parseInt(value);
    if(newValue >= min){
        input.setAttribute("value", newValue);
    }
}

function volver() {
	sessionStorage.removeItem("serviceId");
	location = `${BASE_URL}app/client/dashboard`;
}