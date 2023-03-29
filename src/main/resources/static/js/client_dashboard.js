(function() {
    const userId = sessionStorage.getItem("userId");
    const branchId = sessionStorage.getItem("branchId");
    const myHeaders = new Headers();
    myHeaders.append("Authorization", `Bearer ${sessionStorage.getItem("token")}` );

    document.addEventListener('DOMContentLoaded', function() {
        showInfoClient(userId);
        showInfoBranch(branchId);
    });

	const services = document.querySelector("#only-services");
    services.addEventListener("click", function(){
        location.href = "/app/client/services"
    });
    
	const products = document.querySelector("#only-products");
    products.addEventListener("click", function(){
        location.href = "/app/client/products"
    });
    
	const services_products = document.querySelector("#services-products");
    services_products.addEventListener("click", function(){
        location.href = "/app/client/services/products"
    });
})();



