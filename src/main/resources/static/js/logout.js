(function() {
	const btn = document.querySelector("#btn-cerrar-sesion");
	btn.addEventListener("click", function() {
		sessionStorage.clear();
		document.location = BASE_URL + "app/cerrar";
	})
})();