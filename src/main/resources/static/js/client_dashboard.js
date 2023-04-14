(function() {
    const myHeaders = new Headers();
    myHeaders.append("Authorization", `Bearer ${sessionStorage.getItem("token")}` );
    
	const services = document.querySelector("#services");
    services.addEventListener("click", function(){
        location.href = "/app/client/services"
    });
    
	const products = document.querySelector("#products");
    products.addEventListener("click", function(){
        location.href = "/app/client/products"
    });
    
	const employees = document.querySelector("#employees");
    employees.addEventListener("click", function(){
        location.href = "/app/client/employees"
    });
	const reservation = document.querySelector("#reservation");
    reservation.addEventListener("click", function(){
        location.href = "/app/client/reservation"
    });
})();



