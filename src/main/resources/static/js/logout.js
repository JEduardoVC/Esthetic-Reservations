(function() {
	const btn = document.querySelector("#btn-cerrar-sesion");
	btn.addEventListener("click", function() {
		sessionStorage.clear();
		document.location = "http://localhost:5500/app/cerrar";
	})
})();