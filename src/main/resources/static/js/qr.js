(function() {
	const readQR = document.querySelector("#readQr");
	readQR.addEventListener("click", () => {
		leerCodigo();
	});
})();

async function leerCodigo() {
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
			window.location = "http://localhost:5500/app/owner";
		})
	} else {
		alert("Denego acceso a la camara");
	}
}