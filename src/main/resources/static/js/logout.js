(function() {
	const btn = document.querySelector("#btn-cerrar-sesion");
	if(btn){
		btn.addEventListener("click", function() {
			sessionStorage.clear();
			document.location = `${BASE_URL}app/cerrar`;
		})
	}
	const btn_phone = document.querySelector("#btn-cerrar-sesion-phone");
	if(btn_phone){
		btn_phone.addEventListener("click", function() {
			sessionStorage.clear();
			document.location = `${BASE_URL}app/cerrar`;
		})
	}
})();