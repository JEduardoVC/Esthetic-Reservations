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

document.addEventListener('DOMContentLoaded', function(){
    month.textContent = months[currentMonth];
    year.textContent = currentYear.toString();
    
    prev.addEventListener("click", () => lastMonth());
    next.addEventListener("click", () => nextMonth());
    
    writeMonths(currentMonth);
    
    showTime();
    
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
	
	document.querySelector("#reservation").addEventListener("click", () => {
		if(appointment.time == "") {
			alerta("error", "Seleccionar fecha y hora para la cita")
			return;
		}
	})
});

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
	                <img class="cancel" src="/img/cancelar.webp" onclick="cancelarServicio(${serv.id})">
	                <img class="product" src="/img/Productos-venta.webp">
	                <div class="description">
	                    <p>${servicio.service_name}</p>
	                    <div class="description-service">
	                        <span>${appointment.date}</span>
	                        <span>${appointment.time}</span>
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
	const hr = parseInt(changeTime(timeTotal, 1) / 60);
	const min = parseInt(changeTime(timeTotal, 1) % 60);
	document.querySelector("#time-service").textContent = `${hr} hr ${min} min`;
	document.querySelector("#price-total").textContent = `$ ${priceTotal}`;	
}

async function cancelarServicio(id) {
	if(await confirmAlert()) {		
		carrito.servicios = carrito.servicios.filter(servicio => servicio.id != id);
		sessionStorage.setItem("carrito", JSON.stringify(carrito));
		showReview();
	}	
}

async function newAppointment() {
	const time = `00:${document.querySelector("#time-appointment").value}`;
	if(time == "00:") {
		alerta("error", "Seleccionar una hora");
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
	pagina++;
	showSeccion();
	showReview();
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
	const resultado = await	fetch(`${BASE_URL}api/client/appointment/date/branch/${sessionStorage.getItem("branchId")}/${appointment.date}`, {
		method: "GET",
		headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
		redirect: "follow"
	});
	const citas = await resultado.json();
	return (citas == undefined) ? [] : citas.content
}

function showTime() {
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
                    <input type="time" pattern="[1-12]{2}:[0-59] (am|pm|)" id="time-appointment" value="19:19">
                </div>
                <button class="btn-principal button" id="date-times">Confirmar cita</button>
            </div>
        </div>
	`;
	document.querySelector("#date-times").addEventListener("click", newAppointment);
}

function writeMonths(month){
	const date = new Date();
	let index = months.findIndex(month => month == document.querySelector("#month").textContent);
    for(let i= startDay(); i>0;i--){
		const day = getTotalDays(currentMonth-1)-(i-1);
        dates.innerHTML += `<div class="date item color-darken>${day}</div>`;
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
	console.info(div);
	if(!div) return;
	document.querySelector("#dates").childNodes.forEach(element => {
		if(element.classList.contains("selected")) element.classList.remove("selected")
	});
    div.classList.toggle("selected");
	const index = months.findIndex(month => month == document.querySelector("#month").textContent);
	console.info(index);
    appointment.date = `${document.querySelector("#year").textContent}-${index}-${div.textContent}`;
}

function showSeccion(){
    const seccionAnterior = document.querySelector('.mostrar-seccion');
    if (seccionAnterior) {
        seccionAnterior.classList.remove('mostrar-seccion');
//        seccionAnterior.classList.add("ocultar");
        seccionAnterior.style = "display: none";
    }
    const seccionActual = document.querySelector(`#paso-${pagina}`);
    seccionActual.classList.add("mostrar-seccion")
    seccionActual.style = "display: grid";
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