document.addEventListener('DOMContentLoaded', function() {
    //darkMode();
    menu();
    isDarkMode();
});

function darkMode(){
    const botonDarkMode = document.querySelector(".dark-mode-boton");
    const botonLigthMode = document.querySelector(".ligth-mode-boton");
    botonDarkMode.addEventListener("click", function() {
        if(!document.querySelector(".dark-mode")){
            document.body.classList.add("dark-mode");
            document.querySelector(".luna").classList.add("no-mostrar");
            document.querySelector(".sol").classList.remove("no-mostrar");
            sessionStorage.setItem("darkMode", "true");
        }
    });
    botonLigthMode.addEventListener("click", function() {
        if(document.querySelector(".dark-mode")){
            document.body.classList.remove("dark-mode");
            document.querySelector(".luna").classList.remove("no-mostrar");
            document.querySelector(".sol").classList.add("no-mostrar");
            sessionStorage.removeItem("darkMode");
        }
    });
}

function menu(){
    const menu = document.querySelector(".animacion-menu");
    if(menu){
        menu.addEventListener("click", function(){
            const acciones = document.querySelector("#acciones");
            acciones.classList.toggle("mostrar");
            acciones.classList.toggle("botones-menu");
            acciones.classList.toggle("acciones");
        });
    }
}

function isDarkMode(){
    if(sessionStorage.getItem("darkMode")){
        document.body.classList.add("dark-mode");
        document.querySelector(".luna").classList.add("no-mostrar");
        document.querySelector(".sol").classList.remove("no-mostrar");
    }
}

async function showInfoClient(userId){
    const resultadoUsuario = await fetch(`/api/user/${userId}`,{method: 'GET', headers: myHeaders, redirect: 'follow'});
    const client = await resultadoUsuario.json();
    const nameUser = document.querySelector("#nombre-usuario");
    console.log(nameUser);
    const { name, lastName } = client;
    nameUser.textContent = `${name} ${lastName}`;
}

async function showInfoBranch(branchId){
    const resultadoBranch = await fetch(`/api/branch/${branchId}`,{method: 'GET', headers: myHeaders, redirect: 'follow'});
    const branch = await resultadoBranch.json();
    const { branchName } = branch;
    const nameBranch = document.querySelector("#nombre-branch");
    nameBranch.textContent = `${branchName}`;
}