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
				if(cameras.length > 0) {
					scanner.start(cameras[0])
				} else {
					alert("No se encontraron camaras")
				}
			}).catch(function(e) {
				alert(e);
			});
			scanner.addListener("scan", function(respuesta) {
				sessionStorage.setItem("qr", respuesta);
				window.location = `${BASE_URL}app/owner`;
			})
		}
	} catch(DOMException) {
		console.log(DOMException)
		alert("Denego acceso a la camara");
	}
}