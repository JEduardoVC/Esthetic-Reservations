(function() {
	if(!sessionStorage.getItem("token")) location = `${BASE_URL}app/login`;
	mostrarProductos();
	mostrarProducto();
//	sessionStorage.removeItem("carrito");
})();

let carrito = JSON.parse(sessionStorage.getItem("carrito")) ?? {productos: []};

async function mostrarProducto() {
	const producto = await obtenerProducto();
	if(!producto) return;
	console.info(carrito);
	const value = carrito.productos.find(prod => prod.id == producto.id) ?? {cantidad: 0};
	const divProducto = document.createElement("div");
	divProducto.innerHTML = `
		<div class="container">
	        <div>
                <button class="btn-secundario" onclick="volver()">Volver</button>
	        </div>
	        <div class="header-products">
	            <h1>${producto.inventory_name}</h1>
	            <span>${producto.capacibility}</span>
	        </div>
	        <div class="product-description">
	            <img src="/Inventario/${producto.imagen}.jpg">
	            <p>${producto.description}</p>
	        </div>
	        <p class="price-product">$ ${producto.price}</p>
	        <div class="form-product">
	            <div class="input-product">
	                <button class="decrement" id="decrement" type="button" onclick="stepper(this)">-</button>
	                <input id="quantity-products" type="number" min="0" value="${value.cantidad}" step="1">
	                <button class="increment" id="increment" type="button" onclick="stepper(this)">+</button> 
	            </div>
	            <button class="btn-principal" id="btn-añadir">Añadir</button>
	        </div>
	    </div>
	`;
	if(document.querySelector(".show-product").childNodes.length == 1) document.querySelector(".show-product").removeChild(document.querySelector(".show-product").lastElementChild)
	document.querySelector(".show-product").appendChild(divProducto);
	document.querySelector("#btn-añadir").addEventListener("click", () => {
		const cantidad = document.querySelector("#quantity-products").getAttribute("value");
		const index = carrito.productos.findIndex(prod => prod.id == producto.id);
		if(index !== -1) {
			const carritoObj = JSON.parse(sessionStorage.getItem("carrito"));
			carritoObj.productos[index] = {id: producto.id, cantidad: cantidad}
			console.info(carritoObj);
			alerta("success", "Producto actualizado correctamente", "Hecho");	
		}
		else {
			carrito.productos = [...carrito.productos, {id: producto.id, cantidad:cantidad}]
			alerta("success", "Producto agregado correctamente", "Hecho");
		}
			sessionStorage.setItem("carrito", JSON.stringify(carrito));
	});
}

function isProduct(producto) {
	console.info(producto);
	return producto.id == sessionStorage.getItem("productId");
}

async function mostrarProductos() {
	const productos = await obtenerProductos();
	if(!productos) return;
	productos.forEach(producto => {
		const prod = document.createElement("div");
		prod.classList.add("one-product");
		prod.innerHTML = `
	        <img class="img-product" <img src="/Inventario/${producto.imagen}.jpg">
	        <div class="description-oneproduct">
	            <h4>${producto.inventory_name}</h4>
	            <p>${producto.description}</p>
	            <div class="price-capacity">
	                <p>$ ${producto.price}</p>
	                <span>${producto.capacibility}</span>
	            </div>
	        </div>
		`;
		const div = document.querySelector(".more-products");
		prod.addEventListener("click", () => {
			sessionStorage.setItem("productId", producto.id);
			mostrarProducto();
		});
		(producto.id != sessionStorage.getItem("productId")) ? prod.classList.remove("selected") : prod.classList.add("selected");
		div.appendChild(prod);
	})
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

async function obtenerProducto() {
	if(!sessionStorage.getItem("productId")) return null;
	const resultado = await fetch(`${BASE_URL}api/client/product/${sessionStorage.getItem("productId")}`, {
		method: "GET",
		headers: {
				"Authorization": `Bearer ${sessionStorage.getItem("token")}`
			},
		redirect: "follow"
	});
	const respuesta = await resultado.json();
	return respuesta;
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
	sessionStorage.removeItem("productId");
	location = `${BASE_URL}app/client/dashboard`;
}