let pagina = 1;

const months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
const dateActally = new Date();
let currentDate = new Date();
let currentDay = currentDate.getDate();
let currentMonth = currentDate.getMonth();
let currentYear = currentDate.getFullYear();

let prev = document.querySelector("#prev");
let month = document.querySelector("#month");
let year = document.querySelector("#year");
let next = document.querySelector("#next");

let dates = document.querySelector("#dates");

const carrito = JSON.parse(sessionStorage.getItem("carrito")) ?? {productos: [], servicios: []};

const appointment = {
	date: "",
	time: ""
};

(async () => {
	month.textContent = months[currentMonth];
    year.textContent = currentYear.toString();
    
    prev.addEventListener("click", () => lastMonth());
    next.addEventListener("click", () => nextMonth());
    
    if(sessionStorage.getItem("appointmentId")) {
		const newAppointment = carrito.servicios;
		const appointmentReservation = await getAppointment();
		document.querySelector("#reservation").textContent = "Actualizar";
		document.querySelector(".title-p").textContent = "Actualizar cita";
		writeMonths(appointmentReservation.date.split("-")[1], appointmentReservation.date);
		showTime(`${appointmentReservation.time.split(":")[1]}:${appointmentReservation.time.split(":")[2]}`);
		if(newAppointment.length != 0) {
			carrito.servicios = newAppointment;
			sessionStorage.setItem("carrito", JSON.stringify(carrito));
		}
	} else {
		writeMonths(currentMonth);
	    showTime();
	}
	if(sessionStorage.getItem("saleId")) {
		const newSale = carrito.productos;
		await getSale();
		document.querySelector("#reservation").textContent = "Actualizar";
		document.querySelector(".title-p").textContent = "Actualizar cita";
		if(newSale.length != 0) {
			carrito.productos = newSale;
			sessionStorage.setItem("carrito", JSON.stringify(carrito));
		}
	}
	
    showSeccion();
	
    document.querySelector("#nextPage").addEventListener("click",() => {
		pagina++;
		showSeccion();
		showReview();
	});
	
    document.querySelector("#previusPage").addEventListener("click",() => {
		pagina--;
		showSeccion();
	});
	
	document.querySelector("#reservation").addEventListener("click", async () => {
		if(carrito.productos.length > 0) {
			const respuesta = sessionStorage.getItem("saleId") ? await updateSale() : await saveSale();
			if(respuesta.id) {
				showLoading("Enviando correo...")
				await sendMail(respuesta.id, !sessionStorage.getItem("saleId"), false);
				alerta("success", "Se le envio un correo con su código QR para recoger sus productos en sucursal", `Compra ${sessionStorage.getItem("saleId") ? "Actualizada" : "Exitosa"}`)
			}
		}
		if(carrito.servicios.length > 0) {
			if(appointment.time == "") {
				alerta("error", "Seleccionar fecha y hora para la cita")
				return;
			}
			const respuestaCita = sessionStorage.getItem("appointmentId") ? await updateAppointment() : await saveAppointment();
			if(respuestaCita.date_created) {
				showLoading("Enviando correo...")
				await sendMail(respuestaCita.id, !sessionStorage.getItem("appointmentId"), true);
				alerta("success", "Se le envio un correo con su código QR para hacer valida su reseracion", `Cita ${sessionStorage.getItem("appointmentId") ? "Actualizada" : "Confirmada"}`);
			}
		}
		if(carrito.productos.length == 0 && carrito.servicios.length == 0) alerta("error", "No ha seleccionado servicios o productos para agender su reservación")
//		else setTimeout(() => {
//			location.href = `${BASE_URL}app/client/appointment`;
//		}, 2000);
	})
})();

async function getSale() {
	const resultado = await fetch(`${BASE_URL}api/client/sale/${sessionStorage.getItem("saleId")}`, {
		method: "GET",
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`,
			"Content-Type": "application/json"
		}
	})
	const respuesta = await resultado.json();
	let saleArray = [];
	respuesta.productsList.forEach(producto => {
		saleArray = [...saleArray, {"id": parseInt(producto.product.id), "cantidad": `${producto.quantity}`}]
	})
	carrito.productos = saleArray;
	sessionStorage.setItem("carrito", JSON.stringify(carrito));
}

async function getAppointment() {
	const resultado = await fetch(`${BASE_URL}api/client/appointment/${sessionStorage.getItem("appointmentId")}`, {
		method: "GET",
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`,
			"Content-Type": "application/json"
		}
	})
	const respuesta = await resultado.json();
	carrito.servicios = agruparItems(respuesta.id_service)
	sessionStorage.setItem("carrito", JSON.stringify(carrito));
	return {
		date: respuesta.appointment_date,
		time: respuesta.appointmnet_time
	}
}

function agruparItems(items) {
	let itemArray = [];
	let itemObj = {};
	items.forEach(el => (itemObj[el.id] = itemObj[el.id] + 1 || 1))
	Object.entries(itemObj).forEach(([key, value]) => {
		itemArray = [...itemArray, {"id": parseInt(key), "cantidad": `${value}`}]
	})
	return itemArray;
}

async function saveSale() {
	const obj = new Object();
	const products = carrito.productos.flatMap(producto => obj.constructor({productId: producto.id, quantity: parseInt(producto.cantidad)}))
	const respuesta = await fetch(`${BASE_URL}api/sale`, {
		method:"POST",
		body: JSON.stringify({
			branchId: parseInt(sessionStorage.getItem("branchId")),
			clientId: parseInt(sessionStorage.getItem("userId")),
			products: products
		}),
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`,
			"Content-Type": "application/json"
		}
	})
	const resultado = await respuesta.json();
	if(resultado.errorCode) {
		alerta("error", resultado.message, "Error al comprar un producto");
		return {"id": null};
	}
	return resultado;
}

async function updateSale() {
	const obj = new Object();
	const products = carrito.productos.flatMap(producto => obj.constructor({productId: producto.id, quantity: parseInt(producto.cantidad)}))
	const respuesta = await fetch(`${BASE_URL}api/sale/${sessionStorage.getItem("saleId")}`, {
		method:"PUT",
		body: JSON.stringify({
			branchId: parseInt(sessionStorage.getItem("branchId")),
			clientId: parseInt(sessionStorage.getItem("userId")),
			products: products
		}),
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`,
			"Content-Type": "application/json"
		}
	})
	const resultado = await respuesta.json();
	if(resultado.errorCode) {
		alerta("error", resultado.message, "Error al comprar un producto");
		return {"id": null};
	}
	return resultado;
}

async function saveAppointment() {
	const respuesta = await fetch(`${BASE_URL}api/appointment/guardar`, {
		method:"POST",
		body: JSON.stringify({
			id_branch: sessionStorage.getItem("branchId"),
			appointment_date: appointment.date,
			appointment_time: `${appointment.time}`,
			id_service: carrito.servicios.flatMap(servicio => servicio.id),
			cantidad: carrito.servicios.flatMap(servicio => parseInt(servicio.cantidad)),
			id_client: sessionStorage.getItem("userId"),
			id_employee: sessionStorage.getItem("employeeId"),
			id_status: 2
		}),
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`,
			"Content-Type": "application/json"
		}
	})
	const resultado = await respuesta.json();
	return resultado;
}

async function updateAppointment() {
	const respuesta = await fetch(`${BASE_URL}api/appointment/actualizar/${sessionStorage.getItem("appointmentId")}`, {
		method:"PUT",
		body: JSON.stringify({
			id_branch: sessionStorage.getItem("branchId"),
			appointment_date: appointment.date,
			appointment_time: appointment.time,
			id_service: carrito.servicios.flatMap(servicio => servicio.id),
			cantidad: carrito.servicios.flatMap(servicio => parseInt(servicio.cantidad)),
			id_client: sessionStorage.getItem("userId")
		}),
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`,
			"Content-Type": "application/json"
		}
	})
	const resultado = await respuesta.json();
	return resultado;
}

async function showReview() {
	const servicios = await obtenerServicios();
	const divPadre = document.querySelector(".container-show-summary");
	while(divPadre.firstChild) divPadre.removeChild(divPadre.lastChild);
	let timeTotal = "00:00:00";
	let priceTotal = 0;
	servicios.forEach(servicio => {
		const serv = carrito.servicios.find(element => element.id == servicio.id) ?? null;
		if(serv != null && serv.id == servicio.id) {
			const div = document.createElement("div");
			div.classList.add("container-summary")
			div.innerHTML = `
				<div class="product-service" id="product-service">
	                <img class="cancel" src="/img/cancelar.webp" onclick="cancelarSeccion(${serv.id}, 'servicios')">
	                <img class="product" src="/img/Productos-venta.webp">
	                <div class="description">
	                    <p>${servicio.service_name}</p>
	                    <div class="description-service">
	                        <span>${appointment.date}</span>
	                        <span>${appointment.time ? changeFormatTime(appointment.time) : "00:00"}</span>
	                    </div>
	                </div>
	            </div>
	            <div class="price" id="price">
	                <span>$ ${servicio.price}</span>
	            </div>
	            <div class="quantity" id="quantity">
	                <p>${serv.cantidad}</p>
	            </div>
	            <div class="subtotal" id="subtotal">
	                <span>$ ${parseInt(serv.cantidad) * servicio.price}</span>
	            </div>
			`;
			document.querySelector(".container-show-summary").appendChild(div);
			timeTotal = `00:${changeTime(servicio.duration, 1) + changeTime(timeTotal, 1)}:${changeTime(servicio.duration, 2) + changeTime(timeTotal, 2)}`
			priceTotal += parseInt(serv.cantidad) * servicio.price;
		}
	})
	const productos = await obtenerProductos();
	productos.forEach(producto => {
		const prod = carrito.productos.find(element => element.id == producto.id) ?? null;
		if(prod != null && prod.id == producto.id) {
			const div = document.createElement("div");
			div.classList.add("container-summary")
			div.innerHTML = `
	            <div class="product-service" id="product-service">
	                    <img class="cancel" src="/img/cancelar.webp" onclick="cancelarSeccion(${prod.id}, 'productos')">
	                    <img class="product" src="/img/Productos-venta.webp">
	                    <div class="description">
	                        <p>${producto.inventory_name}</p>
	                        <span>${producto.capacibility}</span>
	                    </div>
	                </div>
	                <div class="price" id="price">
	                    <span>$ ${producto.price}</span>
	                </div>
	                <div class="quantity" id="quantitu">
	                    <p>${prod.cantidad}</p>
	                </div>
	                <div class="subtotal" id="subtotal">
	                    <span>$ ${prod.cantidad * producto.price}</span>
	                </div>
	            </div>
			`;
			document.querySelector(".container-show-summary").appendChild(div);
			priceTotal += parseInt(prod.cantidad) * producto.price;
		}
	})
	const hr = parseInt(changeTime(timeTotal, 1) / 60);
	const min = parseInt(changeTime(timeTotal, 1) % 60);
	document.querySelector("#time-service").textContent = `${hr} hr ${min} min`;
	document.querySelector("#price-total").textContent = `$ ${priceTotal}`;	
}

async function obtenerProductos() {
	const resultado = await fetch(`${BASE_URL}api/client/products/branch/${sessionStorage.getItem("branchId")}`, {
		method: 'GET',
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`
		},
		redirect: "follow"
	})
	const respuesta = await resultado.json();
	const inventario = respuesta.content;
	if (inventario == undefined) {
		inventario = [];
	}
	return inventario;
}

async function cancelarSeccion(id, seccion) {
	if(await confirmAlert()) {
		if(seccion == "servicios") {
			carrito.servicios = carrito.servicios.filter(servicio => servicio.id != id);
		} else {
			carrito.productos = carrito.productos.filter(producto => producto.id != id);
		}
		sessionStorage.setItem("carrito", JSON.stringify(carrito));
		showReview();
	}	
}

async function newAppointment() {
	const time = `00:${document.querySelector("#time-appointment").value}`;
	if(document.querySelector("#time-appointment").value == "") {
		alerta("error", "Seleccionar una hora");
		return;
	}
	if(!sessionStorage.getItem("employeeId")) {
		alerta("error", "Selecciona un empleado")
		return;
	}
	const citas = await obtenerCitas();
	const {resultado, tiempoTotal} = validarHora(citas, time);
	if(resultado == 1) {
		alerta("error", "Ya se encuentra una cita a esa hora", "Hora no disponible");
		return;
	}
	if(validarTiempoCitas(citas, tiempoTotal, time) == 1) {
		alerta("error", "Hora ocupada por servicio");
		return;
	}
	appointment.time = time;
	alerta("success", "Fecha y hora registrada");
}

function validarTiempoCitas(citas, tiempoTotal, hora) {
	let resultado = 0;
	citas.forEach(cita => {
		let hr = parseInt(0);
		let min = parseInt(0);
		tiempoTotal[cita.id].forEach(time => {
			hr += parseInt(time.split(":")[0]);
			min += parseInt(time.split(":")[1]);
		})
		hr += parseInt(min / 60);
		min = min % 60;
		const citaHr = parseInt(changeTime(cita.appointmnet_time, 1) + hr) + parseInt((changeTime(cita.appointmnet_time, 2) + min) / 60);
		const citaMin = parseInt(changeTime(cita.appointmnet_time, 2) + min) % 60;
		const newTime = `00:${citaHr}:${(citaMin < 10) ? "0" + citaMin : citaMin}`;
		if(validarHoraServicio(cita.appointmnet_time, newTime, hora) == 1) {
			resultado = 1;
		}
	})
	return resultado;
}

function validarHoraServicio(init, finish, time) {
	const timeInt = [parseInt(time.split(":")[1]), parseInt(time.split(":")[2])]
	const initInt = [parseInt(init.split(":")[1]), parseInt(init.split(":")[2])];
	const finishInt = [parseInt(finish.split(":")[1]), parseInt(finish.split(":")[2])]
	if(timeInt[0] > initInt[0] && timeInt[0] < finishInt[0]) {
		return 1;
	} else if(timeInt[0] == finishInt[0]) {		
		if(timeInt[1] < finishInt[1]) {
			return 1;
		}
	}
}

function validarHora(citas, hora) {
	let resultado;
	let tiempoTotal = [];
	citas.forEach(cita => {
		if(cita.appointmnet_time == hora) resultado = 1;
		tiempoTotal[cita.id] = [];
		cita.id_service.forEach(servicio => {
			tiempoTotal[cita.id] = [...tiempoTotal[cita.id], servicio.duration];
		})
	})
	return {resultado, tiempoTotal};
}

async function obtenerCitas() {
	console.info(`${BASE_URL}api/client/appointment/date/branch/employee/${sessionStorage.getItem("branchId")}/${appointment.date}/${sessionStorage.getItem("employeeId")}`)
	const resultado = await	fetch(`${BASE_URL}api/client/appointment/date/branch/employee/${sessionStorage.getItem("branchId")}/${appointment.date}/${sessionStorage.getItem("employeeId")}`, {
		method: "GET",
		headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
		redirect: "follow"
	});
	const citas = await resultado.json();
	console.info(citas);
	return (citas == undefined) ? [] : citas.content
}

function showTime(timeReservation = null) {
	const time = document.querySelector(".time");
	const date = new Date();
	time.innerHTML = `
        <p>¡Llegó la hora de agendar tu cita!</p>
        <div class="model-time">
            <div class="date-saved">
                <h1>${date.getDate()}</h1>
                <h4>${months[date.getMonth()]} ${date.getFullYear()}</h4>
            </div>
            <div class="select-time">
                <div class="input-time">
                    <label>Selecciona la hora</label>
                    <input type="time" pattern="[1-12]{2}:[0-59] (am|pm|)" id="time-appointment" value="${timeReservation ?? ""}">
                </div>
                <button class="btn-principal button" id="date-times">Confirmar cita</button>
            </div>
        </div>
	`;
	if(timeReservation) appointment.time = timeReservation;
	document.querySelector("#date-times").addEventListener("click", newAppointment);
}

function writeMonths(month, dateReservation = null){
	const date = dateReservation ? new Date(dateReservation.split("-")[0], dateReservation.split("-")[1], dateReservation.split("-")[2]) : new Date();
	let index = months.findIndex(month => month == document.querySelector("#month").textContent);
    for(let i= startDay(); i>0;i--){
        dates.innerHTML += `<div class="date item color-darken">${getTotalDays(currentMonth-1)-(i-1)}</div>`;
    }
    for(let i=1;i<=getTotalDays(month); i++){
        dates.innerHTML += `<div class="date item ${date.getDate() == i && date.getMonth() == month ? "selected" : ""}" id="date_${i}" onclick="selectDay(${date.getDate() > i && month == date.getMonth() ? "null" : "this"})">${i}</div>`;
        (date.getDate() == i && date.getMonth() == month) ? appointment.date = `${document.querySelector("#year").textContent}-${index + 1}-${i}` : "";
    }
}

function getTotalDays(month){
    if(month === -1) month == 11;
    if(month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9 ||month == 11){
        return 31;
    } else if(month == 3 || month == 5 || month == 8 || month == 10){
        return 30;
    } else {
        return isLeap() ? 29 : 28;
    }
}

function isLeap(){
    return (currentYear % 100 !== 0 && currentYear % 4 === 0 || currentYear % 400 === 0) ? true : false;  
}

function startDay(){
    let start = new Date(currentYear, currentMonth, 1);
    return ((start.getDay()-1) === -1) ? 6 : start.getDay();
}

function lastMonth(){
    if(currentMonth !== 0){
        currentMonth--;
    } else {
        currentMonth = 11;
        currentYear--;
    }
    setNewDate();
}

function nextMonth(){
    if(currentMonth !== 11){
        currentMonth++;
    } else {
        currentMonth = 0;
        currentYear++;
    }
    setNewDate();
}

function setNewDate(){
    currentDate.setFullYear(currentYear,currentMonth,currentDay);
    month.textContent = months[currentMonth];
    year.textContent = currentYear.toString();
    dates.textContent = ``;
    writeMonths(currentMonth);
}

function selectDay(div){
	if(!div) return;
	document.querySelector("#dates").childNodes.forEach(element => {
		if(element.classList.contains("selected")) element.classList.remove("selected")
	});
    div.classList.toggle("selected");
	const index = months.findIndex(month => month == document.querySelector("#month").textContent);
    appointment.date = `${document.querySelector("#year").textContent}-${index+1}-${div.textContent}`;
}

function showSeccion(){
	console.log(pagina);
    const seccionAnterior = document.querySelector('.mostrar');
	console.log(seccionAnterior);
    if (seccionAnterior) {
        seccionAnterior.classList.remove("mostrar");
        seccionAnterior.style = "display: none";
    }
    const seccionActual = document.querySelector(`#paso-${pagina}`);
	seccionActual.classList.add("mostrar");
	if(screen.width > 1024) seccionActual.style = "display: grid";
	if(screen.width <= 480) seccionActual.style = "display: flex";
	

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

function changeTime(time, position) {
	return parseInt(time.split(":")[position]);
}

function volver() {
	location = `${BASE_URL}app/client/dashboard`;
}