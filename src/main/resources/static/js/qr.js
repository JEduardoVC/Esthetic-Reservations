(function() {
	const readQR = document.querySelector("#readQr");
	readQR.addEventListener("click", leerCodigo)
})();

async function leerCodigo() {
	try {
		const respuesta = await navigator.mediaDevices.getUserMedia({video: true});
		if(respuesta.active) {
			var scanner = new Instascan.Scanner({
				video: document.querySelector("#webcam"),
				scanPeriod: 1,
				mirror: false
			});
			Instascan.Camera.getCameras().then(function(cameras) {
				console.info(cameras.length);
				if(cameras.length > 0) {
					scanner.start(cameras[0])
				} else {
					alert("No se encontraron camaras")
				}
			}).catch(function(e) {
				alert(e);
			});
			scanner.addListener("scan", async function(respuesta) {
				respuesta = JSON.parse(respuesta);
				if(respuesta.service == 2) {
					const sale = await getSales(respuesta.id_item); 
					confirmAlert("success", "Recoge tu compra", `Puedes recoger tu compra ${getProducts(sale.productsList)}`, "Hecho");
				} else {
					sessionStorage.setItem("qr_item", respuesta.id_item);
					sessionStorage.setItem("qr_branch", respuesta.id_branch);
					window.location = `${BASE_URL}app/owner`;
				}
			})
		}
	} catch(DOMException) {
		console.log(DOMException)
		alert("Denego acceso a la camara");
	}
}

function getProducts(products) {
	let info = "\n";
	products.forEach(product => {
		info += `${product.quantity} de ${product.product.inventory_name}\n`
		info += "\n";
	})
	return info;
}

async function getSales(id) {
	console.info(`${BASE_URL}api/client/sale/${id}`);
	const resultado = await fetch(`${BASE_URL}api/sale/${id}`, {
		method: "GET",
		headers: {
			"Authorization": `Bearer ${sessionStorage.getItem("token")}`,
			"Content-Type": "application/json"
		}
	});
	const respuesta = await resultado.json();
	return respuesta;
}