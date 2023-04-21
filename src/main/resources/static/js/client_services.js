(function() {
	if(!sessionStorage.getItem("token")) location = `${BASE_URL}app/login`;
	sessionStorage.setItem("serviceId", "1");
	mostrarServicios();
	mostrarServicio();
})();

async function mostrarServicios() {
	const servicios = await obtenerServicios();
	if(!servicios) return;
	servicios.forEach(servicio => {
		const divServicio = document.createElement("div");
		divServicio.classList.add("one-product");
		divServicio.innerHTML = `
	        <div class="description-oneproduct">
	            <h4>${servicio.service_name}</h4>
	        </div>
		`;
		const div = document.querySelector(".more-products");
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
	const divServicio = document.createElement("div");
	divServicio.innerHTML = `
		<div class="container">
	        <div>
                <button class="btn-secundario" onclick="volver()">Volver</button>
	        </div>
            <div class="header-products">
                <h4>${servicio.service_name}</h4>
            </div>
	        <div class="time-cost-service">
	            <div class="circular-time" id="circular-time">
	                <div class="value-time" id="value-time"></div>
	            </div>
	        </div>
        	<p class="price-product">$ ${servicio.price}</p>
	        <div class="form-product">
	            <div class="input-product">
	                <button class="decrement" id="decrement" type="button" onclick="stepper(this)">-</button>
	                <input id="quantity-products" type="number" min="0" value="0" step="1">
	                <button class="increment" id="increment" type="button" onclick="stepper(this)">+</button> 
	            </div>
	            <button class="btn-principal" id="btn-aÃ±adir">Añadir</button>
	        </div>
		</div>	
    `;
    const div = document.querySelector(".show-product");
    div.appendChild(divServicio);
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
	const servicios = respuesta.content;
	if(servicios == undefined) {
		servicios = [];
	}
	return servicios;
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
	}, speed);
}

function stepper(btn){
	const input = document.querySelector("#quantity-products");
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