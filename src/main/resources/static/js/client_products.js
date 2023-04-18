(function() {
	if(!sessionStorage.getItem("token")) {
		location = `${BASE_URL}app/login`
	}
	mostrarProductos();
	mostrarProducto();
})();

async function mostrarProducto() {
	const producto = await obtenerProducto();
	const divProducto = document.createElement("div");
	divProducto.innerHTML = `
		<div class="container">
	        <div>
	            <form action="/app/client/dashboard">
	                <button class="btn-secundario">Volver</button>
	            </form>
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
	                <input id="quantity-products" type="number" min="0" value="0" step="1"> 
	                <button class="increment" id="increment" type="button" onclick="stepper(this)">+</button> 
	            </div>
	            <button class="btn-principal">Añadir</button>
	        </div>
	    </div>
	`;
	document.querySelector(".show-product").appendChild(divProducto);
}

async function mostrarProductos() {
	const productos = await obtenerProductos();
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
			location.reload();
		});
		(producto.id != sessionStorage.getItem("productId")) ? prod.classList.remove("selected") : prod.classList.add("selected");
		div.appendChild(prod);
		if(!sessionStorage.getItem("productId")) sessionStorage.setItem("productId", producto.id);
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

const input = document.querySelector("#quantity-products");

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