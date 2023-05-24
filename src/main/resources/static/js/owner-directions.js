(function() {
	const appointment = document.querySelector("#appointment");
  appointment.addEventListener("click", function() {
    sessionStorage.removeItem("qr");
    location.href = "/app/owner";
  });
	const services = document.querySelector("#services");
  services.addEventListener("click", function(){
		sessionStorage.removeItem("qr");
		location.href = "/app/owner/servicios";
  });
	const inventory = document.querySelector("#inventory");
  inventory.addEventListener("click", function(){
		sessionStorage.removeItem("qr");
		location.href = "/app/owner/inventario";
  });
	const employees = document.querySelector("#employees");
  employees.addEventListener("click", function(){
		sessionStorage.removeItem("qr");
		location.href = "/app/owner/personal";
  });
	const validate_appointment = document.querySelector("#validate_appointment");
  validate_appointment.addEventListener("click", function(){
		sessionStorage.removeItem("qr");
		location.href = "/app/owner/validar/citas";
  });
	const appointment_phone = document.querySelector("#appointment-phone");
  appointment_phone.addEventListener("click", function() {
    sessionStorage.removeItem("qr");
    location.href = "/app/owner/";
  })
	const services_phone = document.querySelector("#services-phone");
  services_phone.addEventListener("click", function(){
		sessionStorage.removeItem("qr");
		location.href = "/app/owner/servicios";
  });
	const inventory_phone = document.querySelector("#inventory-phone");
  inventory_phone.addEventListener("click", function(){
		sessionStorage.removeItem("qr");
		location.href = "/app/owner/inventario";
  });
	const employees_phone = document.querySelector("#employees-phone");
  employees_phone.addEventListener("click", function(){
		sessionStorage.removeItem("qr");
		location.href = "/app/owner/personal";
  });
	const validate_appointment_phone = document.querySelector("#validate_appointment-phone");
  validate_appointment_phone.addEventListener("click", function(){
	  	sessionStorage.removeItem("qr");
		location.href = "/app/owner/validar/citas";
  });
  
  // View user
	const viewUser = document.querySelector("#btn-editar-usuario");
  viewUser.addEventListener("click", function(){
	  	sessionStorage.removeItem("qr");
		location.href = "/app/user/";
	});
	const viewUserPhone = document.querySelector("#btn-editar-usuario-phone");
  viewUserPhone.addEventListener("click", function(){
	  sessionStorage.removeItem("qr");
		location.href = "/app/user/";
  });
})();