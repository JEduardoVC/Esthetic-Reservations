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