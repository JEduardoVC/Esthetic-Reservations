const carrito = {
    userId:'',
    branchId:'',
    cantidad_productos:[],
    total_venta: '',
    productos: []
}

let contador = 0;
let page = 1;

const userId = sessionStorage.getItem("userId");
const branchId = sessionStorage.getItem("branchId");
const myHeaders = new Headers();
myHeaders.append("Authorization", `Bearer ${sessionStorage.getItem("token")}` );

document.addEventListener('DOMContentLoaded', function() {
    // Show info for nav
    showInfoClient();
    showInfoBranch();
    
    // Show Products
    showInventory();

    // Show seccion 
    mostrarSeccion()
    
    // Buttons
    nextPage();
    previousPage();

    // Paginator
    pagerButtons();
});
async function showInfoClient(){
    const resultadoUsuario = await fetch(`/api/user/${userId}`,{method: 'GET', headers: myHeaders, redirect: 'follow'});
    const client = await resultadoUsuario.json();
    const nameUser = document.querySelector("#nombre-usuario");
    const { name, lastName } = client;
    nameUser.textContent = `${name} ${lastName}`;
}

async function showInfoBranch(){
    const resultadoBranch = await fetch(`/api/branch/${branchId}`,{method: 'GET', headers: myHeaders, redirect: 'follow'});
    const branch = await resultadoBranch.json();
    const { branchName } = branch;
    const nameBranch = document.querySelector("#nombre-branch");
    nameBranch.textContent = `${branchName}`;
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

function pagerButtons(){
    const nextPage = document.querySelector("#siguiente");
    const previosPage = document.querySelector("#anterior");
    if (page === 1) {
        previosPage.classList.add('ocultar');
        nextPage.classList.remove('ocultar');
    } else if(page === 2){
        selectQuantity();
        nextPage.classList.remove('ocultar');
        previosPage.classList.remove('ocultar');
    } else {
        nextPage.classList.add('ocultar');
        previosPage.classList.remove('ocultar');
        mostrarResumen();
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

function selectQuantity(){
    const select = document.querySelector("#num_products");
    // Clean the display for the new array
    while (select.firstChild) {
        select.removeChild(select.firstChild);
    }
    let cont = 1;
    carrito["productos"].forEach(product =>{
        const {name} = product;
        const layout = document.createElement("div");
        layout.classList.add("list-product");
        const text = document.createElement("p");
        text.textContent = name;
        const input = document.createElement("input");
        input.type = "number";
        input.id = "quantity_product_" + cont;
        input.value = carrito.cantidad_productos[cont-1];
        layout.appendChild(text);
        layout.appendChild(input);
        select.appendChild(layout);
        cont++;
    });
}

async function mostrarResumen() {
    carrito.userId = userId;
    carrito.branchId = branchId;
    // Destructuring
    const {productos} = carrito;

    // Seleccionar el resumen
    const resumenDiv = document.querySelector('#resumen');
    
    // Limpia el HTML previo
    while (resumenDiv.firstChild) {
        resumenDiv.removeChild(resumenDiv.firstChild);
    }
    // validación de objeto
    if (carrito["productos"].length == 0) {
        const noProductos = document.createElement('P');
        noProductos.textContent = 'Falta seleccionar algun producto';
        noProductos.classList.add('invalidar-carrito');

        // agregar a resumen Div
        resumenDiv.appendChild(noProductos);
        /*Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Faltan datos de Productos, hora, fecha!',
        })*/
        return;
    }

    const headingCarrito = document.createElement('H1');
    headingCarrito.textContent = 'Resumen del Carrito';

    // Mostrar el resumen
    const productosCarrito = document.createElement('DIV');
    productosCarrito.classList.add('resumen-Productos');

    let cantidad = 0;
    let cont = 1;
    carrito.cantidad_productos = [];
    productos.forEach(async producto => {
        const {id} = producto;

        const resultProductSelected = await fetch(`/api/owner/inventario/${id}`,{method: 'GET', headers: myHeaders, redirect: 'follow'});
        const productSelected = await resultProductSelected.json();

        const {inventory_name, price} = productSelected;
    
        const contenedorproducto = document.createElement('DIV');
        contenedorproducto.classList.add('contenedor-producto');

        const textoproducto = document.createElement('P');
        textoproducto.textContent = inventory_name;
        
        const precioproducto = document.createElement('P');
        precioproducto.textContent = "$" + price;
        precioproducto.classList.add('precio');
        
        contenedorproducto.appendChild(textoproducto);
        contenedorproducto.appendChild(precioproducto);

        const contenedorPrecios = document.createElement("div");
        contenedorPrecios.classList.add("contenedor-precio");

        const input = document.querySelector("#quantity_product_" + cont);
        
        const quantity = document.createElement("p");
        quantity.textContent = "Cantidad seleccionada: " + input.value;
        carrito.cantidad_productos = [...carrito.cantidad_productos, input.value];
        
        const subtotal = document.createElement("p");
        subtotal.textContent ="Subtotal: " + price*input.value;
        cantidad += price*input.value;
        carrito.total_venta = price*input.value;

        contenedorPrecios.appendChild(quantity);
        contenedorPrecios.appendChild(subtotal);
        
        productosCarrito.appendChild(contenedorproducto);
        productosCarrito.appendChild(contenedorPrecios);
        cont++;
    });
    
    resumenDiv.appendChild(headingCarrito);
    resumenDiv.appendChild(productosCarrito);
    
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
    btnReserve.textContent = "Realizar compra";
    btnReserve.onclick = makeAPurcharse;

    resumenDiv.appendChild(btnReserve);
}