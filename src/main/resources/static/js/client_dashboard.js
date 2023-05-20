(function() {
	const services = document.querySelector("#services");
    services.addEventListener("click", function(){
		if(!sessionStorage.getItem("saleId")) location.href = "/app/client/services";
        else alerta("warning", "Esta actualizando una compra, no puede agregar servicios", "No disponible");
    });
    
	const products = document.querySelector("#products");
    products.addEventListener("click", function(){
        if(!sessionStorage.getItem("appointmentId")) location.href = "/app/client/products";
        else alerta("warning", "Esta actualizando una reservación, no puede agregar productos", "No disponible");
    });
    
	const employees = document.querySelector("#employees");
    employees.addEventListener("click", function(){
        location.href = "/app/client/employees"
    });
	const reservation = document.querySelector("#reservation");
    reservation.addEventListener("click", function(){
        location.href = sessionStorage.getItem("appointmentId") ? "/app/client/update/reservation" : "/app/client/reservation";
    });
	const appointment = document.querySelector("#appointment");
    if(sessionStorage.getItem("appointmentId")) appointment.textContent = "Cancelar Editar Cita";
    if(sessionStorage.getItem("saleId")) appointment.textContent = "Cancelar Editar Compra";
    appointment.addEventListener("click", function(){
		if(sessionStorage.getItem("appointmentId")) {
			sessionStorage.removeItem("appointmentId");
			sessionStorage.removeItem("carrito");
		}
		if(sessionStorage.getItem("saleId")) {
			sessionStorage.removeItem("saleId");
			sessionStorage.removeItem("carrito");
		}
		console.info("clic");
		location.href = "/app/client/appointment";
    });
})();