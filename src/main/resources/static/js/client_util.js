async function showServices() {
    const resultadoServicios = await fetch(`${BASE_URL}api/owner/servicios/branch/${branchId}`,{method: 'GET', headers: myHeaders, redirect: 'follow'});
    const servicios = await resultadoServicios.json();
    if(servicios["content"] == 0){
        const div_servicios = document.querySelector("#servicios");
        const sinServicios = document.createElement("P");
        sinServicios.textContent = "No hay servicios registrados"
        sinServicios.className = "sin-contenidso";
        div_servicios.appendChild(sinServicios)
    }
    servicios["content"].forEach(servicio => {
        const { id, service_name, duration, price } = servicio;
        const div_servicios = document.querySelector("#servicios");
        const div_servicio = document.createElement("div");
        div_servicio.className = "servicio";
        div_servicio.innerHTML =
        `
            <p class="titulo">${service_name}</p>
            <p>Duracion aproximada: ${duration} min</p>
            <p>Precio: <span>$${price}</span></p>
        `;
        div_servicios.appendChild(div_servicio);
        cita.servicios.forEach(element => {
			if(id == element.id) div_servicio.classList.add("servicio-seleccionado");
		})
        div_servicio.addEventListener("click",function(){
            if(div_servicio.classList.contains("servicio-seleccionado")){
                div_servicio.classList.remove("servicio-seleccionado");
                removeService(id);
            } else {
                div_servicio.classList.add("servicio-seleccionado");
                const servicioObj = {
                    id: parseInt(id),
                    name: service_name,
                    precio: price
                }
                addService(servicioObj);
            }
        });
    });
}

async function showInventory() {
    const resultInventory = await fetch(`${BASE_URL}api/owner/inventario/branch/${branchId}`,{method: 'GET', headers: myHeaders, redirect: 'follow'});
    const inventory = await resultInventory.json();
    if(inventory["content"] == 0){
        const div_productos = document.createElement("div");
        const noInventory = document.createElement("P");
        noInventory.textContent = "No hay inventario en esta sucursal"
        noInventory.className = "sin-contenido";
        div_productos.appendChild(noInventory)
    } else {
        const inventario = document.querySelector("#products");
        const showProducts = document.createElement("div");
        showProducts.classList.add("show-inventario");
        inventory["content"].forEach(product => {
            const {id, imagen, inventory_name, price, store} = product;
            const div_product = document.createElement("div");
            div_product.className = "producto";
            div_product.innerHTML = `
                <p class="img-producto">${imagen}</p>
                <p class="titulo">Producto: ${inventory_name}</p>
                <p>Precio: <span>$${price}</span></p>
                <p>Cantidad: <span>${store}</span></p>
            `;
            showProducts.appendChild(div_product);
            div_product.addEventListener("click",function(){
                if(div_product.classList.contains("producto-seleccionado")){
                    div_product.classList.remove("producto-seleccionado");
                    removeProduct(id);
                } else {
                    div_product.classList.add("producto-seleccionado");
                    const servicioObj = {
                        id: parseInt(id),
                        name: inventory_name,
                        precio: price
                    }
                    addProduct(servicioObj);
                }
            });
        })
        inventario.appendChild(showProducts);
    }
}

function removeService(id) {
    const { servicios } = cita;
    cita.servicios = servicios.filter(servicio => servicio.id !== id);
}

function addService(servicioObj) {
    const { servicios } = cita;
    cita.servicios = [...servicios, servicioObj];
}

function removeProduct(id) {
    const { productos } = carrito;
    carrito.productos = productos.filter(producto => producto.id !== id);
}

function addProduct(productoObj) {
    const { productos } = carrito;
    carrito.productos = [...productos, productoObj];
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

	inputFecha.value = cita.fecha;
    inputFecha.min = fechaDeshabilitar;
}

async function horaCita() {
    const resultadoBranch = await fetch(`/api/branch/${branchId}`,{method: 'GET', headers: myHeaders, redirect: 'follow'});
    const branch = await resultadoBranch.json();
    const inputHora = document.querySelector('#hora');
    inputHora.value = cita.hora;
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

async function obtenerCita() {
	const respuesta = await fetch(`${BASE_URL}api/appointment/${sessionStorage.getItem("citaId")}`, {method: 'GET', headers: myHeaders, redirect: 'follow'});
	const resultado = await respuesta.json();
	return resultado;
}

function objToArrayId(obj) {
	let array = [];
	obj.forEach(element => {
		array = [...array, {
			id: element.id,
			name: element.service_name,
			precio: element.price
		}]
	})
	return array;
}