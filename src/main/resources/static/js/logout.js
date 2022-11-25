(function() {
	const btn = document.querySelector("#btn-cerrar-sesion");
	btn.addEventListener("click", cerrarSesion)
})();

function cerrarSesion() {
	const hoy = new Date();
	console.info(hoy);
	document.cookie.split(";").forEach(c => {
		document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=" + new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDay(), hoy.getHours(), hoy.getMinutes()));
	})
	//document.location = "http://localhost:5500/app/cerrar";
}