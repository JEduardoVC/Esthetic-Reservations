(function() {
	
    if(sessionStorage.getItem("appointmentId")) appointment.textContent = "Cancelar Editar Cita";
    if(sessionStorage.getItem("saleId")) appointment.textContent = "Cancelar Editar Compra";
	
    document.querySelector("#services").addEventListener("click", function(){
		if(!sessionStorage.getItem("saleId")) location.href = "/app/client/services";
        else alerta("warning", "Esta actualizando una compra, no puede agregar servicios", "No disponible");
    });
    
    document.querySelector("#products").addEventListener("click", function(){
        if(!sessionStorage.getItem("appointmentId")) location.href = "/app/client/products";
        else alerta("warning", "Esta actualizando una reservación, no puede agregar productos", "No disponible");
    });
    
    document.querySelector("#employees").addEventListener("click", function(){
        location.href = "/app/client/employees"
    });
    
    document.querySelector("#reservation").addEventListener("click", function(){
        location.href = sessionStorage.getItem("appointmentId") ? "/app/client/update/reservation" : "/app/client/reservation";
    });
       
    document.querySelector("#appointment").addEventListener("click", function(){
		if(sessionStorage.getItem("appointmentId")) {
			sessionStorage.removeItem("appointmentId");
			sessionStorage.removeItem("carrito");
		}
		location.href = "/app/client/appointment";
    });
    document.querySelector("#appointment-phone").addEventListener("click", function(){
		if(sessionStorage.getItem("appointmentId")) {
			sessionStorage.removeItem("appointmentId");
			sessionStorage.removeItem("carrito");
		}
		location.href = "/app/client/appointment";
    });
    
    $("#btn-volver-dashboard").on("click", () => {
		location.href = `${BASE_URL}app/client/location`;
	})
    
})();