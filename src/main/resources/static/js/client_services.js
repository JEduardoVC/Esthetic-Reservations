const cita = {
    userId: '',
    branchId: '',
    fecha: '',
    hora: '',
    servicios: []
}

let contador = 0;

let page = 1;

const userId = sessionStorage.getItem("userId");
const branchId = sessionStorage.getItem("branchId");
const myHeaders = new Headers();
myHeaders.append("Authorization", `Bearer ${sessionStorage.getItem("token")}` );

document.addEventListener('DOMContentLoaded', function() {
    // Show info for nav
//    showInfoClient();
//    showInfoBranch();
    
    // Show services
    showServices();

    // Show seccion 
    mostrarSeccion()
    
    // Buttons
    nextPage();
    previousPage();

    // Paginator
    pagerButtons();

    // Date and hour
    backDateDisabled();
    horaCita();
    fechaCita();

    //Show summary of the cita
    //mostrarResumen();
});

function removeService(id) {
    const { servicios } = cita;
    cita.servicios = servicios.filter(servicio => servicio.id !== id);
}

function addService(servicioObj) {
    const { servicios } = cita;
    cita.servicios = [...servicios, servicioObj];
}

function nextPage() {
    const nextPage = document.querySelector(`#siguiente`);
    if(nextPage != null){
        nextPage.addEventListener("click", () =>{
            page++;
            pagerButtons();
        })
    }
}

function previousPage() {
    const previousPage = document.querySelector(`#anterior`);
    if(previousPage != null){
        previousPage.addEventListener("click", () =>{
            page--;
            pagerButtons();
        })
    }
}

function pagerButtons(){
    const nextPage = document.querySelector("#siguiente");
    const previosPage = document.querySelector("#anterior");
    if (page === 1) {
        previosPage.classList.add('ocultar');
    }else if (page === 2) {
        previosPage.classList.remove('ocultar');
        nextPage.classList.remove('ocultar');
    } else {
        nextPage.classList.add('ocultar');
        previosPage.classList.remove('ocultar');
        mostrarResumen(); // Estamos en la página 3, carga el resumen de la cita
    }
    mostrarSeccion(); // Cambia la sección que se muestra por la de la página
}

function mostrarSeccion() {
    // Eliminar mostrar-seccion de la sección anterior
    const seccionAnterior = document.querySelector('.mostrar-seccion');
    if (seccionAnterior) {
        seccionAnterior.classList.remove('mostrar-seccion');
    }
    const seccionActual = document.querySelector(`#paso-${page}`);
    seccionActual.classList.add('mostrar-seccion');
}

function backDateDisabled() {
    const inputFecha = document.querySelector('#fecha');

    const fechaAhora = new Date();
    const year = fechaAhora.getFullYear();
    const mes = fechaAhora.getMonth() + 1;
    const dia = fechaAhora.getDate() + 1;
    const fechaDeshabilitar = `${year}-${mes}-${dia}`;

    inputFecha.min = fechaDeshabilitar;
}

async function horaCita() {
    const resultadoBranch = await fetch(`/api/branch/${branchId}`,{method: 'GET', headers: myHeaders, redirect: 'follow'});
    const branch = await resultadoBranch.json();
    const inputHora = document.querySelector('#hora');
    inputHora.addEventListener('input', e => {
        const horaCita = e.target.value;
        const hora = horaCita.split(':');
        const { scheduleOpen, scheduleClose } = branch;
        if(hora[0] >= scheduleOpen.split(":")[0] && hora[0] <= scheduleClose.split(":")[0]) {
            cita.hora = horaCita;
        } else {
            console.error("Es muy temprano");
            setTimeout(() => {
                inputHora.value = '';
            }, 3000);
        }
    });
}

function fechaCita() {
    const fechaInput = document.querySelector('#fecha');
    fechaInput.addEventListener('input', e => {
        const dia = new Date(e.target.value).getUTCDay();
        if ([0, 6].includes(dia)) {
            e.preventDefault();
            fechaInput.value = '';
            mostrarAlerta('Fines de Semana no son permitidos', 'error');
        } else {
            cita.fecha = fechaInput.value;
        }
    })
}

async function mostrarResumen() {
    cita.userId = userId;
    cita.branchId = branchId;


    // Destructuring
    const { servicios, fecha, hora } = cita;

    // Seleccionar el resumen
    const resumenDiv = document.querySelector('#resumen');
    
    // Limpia el HTML previo
    while (resumenDiv.firstChild) {
        resumenDiv.removeChild(resumenDiv.firstChild);
    }
    // validación de objeto
    if (Object.values(cita).includes('')) {
        const noServicios = document.createElement('P');
        noServicios.textContent = 'Faltan datos de Servicios, hora, fecha';
        noServicios.classList.add('invalidar-cita');

        // agregar a resumen Div
        resumenDiv.appendChild(noServicios);
        /*Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Faltan datos de Servicios, hora, fecha!',
        })*/
        return;
    }

    const headingCita = document.createElement('H1');
    headingCita.textContent = 'Resumen de cita';

    const resultadoUsuario = await fetch(`/api/user/${userId}`,{method: 'GET', headers: myHeaders, redirect: 'follow'});
    const client = await resultadoUsuario.json();
    const { name, lastName } = client;

    // Mostrar el resumen
    const fechaCita = document.createElement('P');
    fechaCita.innerHTML = `<span>Fecha:</span> ${fecha}`;

    const horaCita = document.createElement('P');
    horaCita.innerHTML = `<span>Hora:</span> ${hora}`;

    const serviciosCita = document.createElement('DIV');
    serviciosCita.classList.add('resumen-servicios');

    const headingServicios = document.createElement('H3');
    headingServicios.textContent = 'Resumen de Servicios';

    serviciosCita.appendChild(headingServicios);

    let cantidad = 0;

    // Iterar sobre el arreglo de servicios
    servicios.forEach(async servicio => {
        const {id} = servicio;
        const resultadoServicios = await fetch(`/api/owner/servicios/${id}`,{method: 'GET', headers: myHeaders, redirect: 'follow'});
        const service = await resultadoServicios.json();
        const {service_name, price} = service;
        const contenedorServicio = document.createElement('DIV');
        contenedorServicio.classList.add('contenedor-servicio');

        const textoServicio = document.createElement('P');
        textoServicio.textContent = service_name;

        const precioServicio = document.createElement('P');
        precioServicio.textContent = "$" + price;
        precioServicio.classList.add('precio');
        cantidad += price;

        // Colocar texto y precio en el div
        contenedorServicio.appendChild(textoServicio);
        contenedorServicio.appendChild(precioServicio);

        serviciosCita.appendChild(contenedorServicio);
    });
    resumenDiv.appendChild(headingCita);
    resumenDiv.appendChild(fechaCita);
    resumenDiv.appendChild(horaCita);

    resumenDiv.appendChild(serviciosCita);

    const cantidadPagar = document.createElement('P');
    cantidadPagar.classList.add('total');
    setTimeout(() => {
        cantidadPagar.innerHTML = `<span>Total a Pagar:  </span> $${cantidad}`;
    }, 50);
    resumenDiv.appendChild(cantidadPagar);

    // Button for reservation
    const btnReserve = document.createElement("button");
    btnReserve.classList.add("btn-principal");
    btnReserve.classList.add("btn-reservar");
    btnReserve.textContent = "Reservar cita";
    btnReserve.onclick = makeAnAppointment;

    resumenDiv.appendChild(btnReserve);
}

async function makeAnAppointment(){
    const { branchId, fecha, hora, servicios, userId} = cita;
    const servicesId = [];
    servicios.forEach(service => {
        servicesId.push(service.id)
    })
    myHeaders.append("Content-Type", "application/json");
    const resultadoCita = await fetch(`/api/appointment/guardar`,{
        method: 'POST',
        headers: myHeaders,
        body: JSON.stringify({
            "id_branch": parseInt(branchId),
            "appointment_date": fecha,
            "appointment_time": `00:${hora}`,
            "id_service": servicesId,
            "id_client": parseInt(userId)
        }),
        redirect: 'follow'
    });
    const respuesta = await resultadoCita.json();
    if(respuesta){
		enviarMultimediaCorreo(userId, branchId, respuesta.id)
    }
}