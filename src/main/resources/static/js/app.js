window.addEventListener('load', async function () {
    await isAllowed();
}, false);
document.addEventListener('DOMContentLoaded', function() {
    darkMode();
    menu();
    isDarkMode();
});

function darkMode(){
    const botonDarkMode = document.querySelector(".dark-mode-boton");
    const botonLigthMode = document.querySelector(".ligth-mode-boton");
    if(botonDarkMode){
        if(botonLigthMode){
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
    }
}

function menu(){
    const menu = document.querySelector(".options-animation");
    if(menu){
        menu.addEventListener("click", function(){
            const acciones = document.querySelector("#actions");
            acciones.classList.toggle("mostrar");
            acciones.classList.toggle("menu-buttons");
            acciones.classList.toggle("actions");
        });
    }
}

function isDarkMode() {
    if (sessionStorage.getItem("darkMode")) {
        document.body.classList.add("dark-mode");
        document.querySelector(".luna").classList.add("no-mostrar");
        document.querySelector(".sol").classList.remove("no-mostrar");
    }
}

async function showInfoClient(userId) {
    const resultadoUsuario = await fetch(`/api/user/${userId}`, { method: 'GET', headers: myHeaders, redirect: 'follow' });
    const client = await resultadoUsuario.json();
    const nameUser = document.querySelector("#nombre-usuario");
    console.log(nameUser);
    const { name, lastName } = client;
    nameUser.textContent = `${name} ${lastName}`;
}

async function showInfoBranch(branchId) {
    const resultadoBranch = await fetch(`/api/branch/${branchId}`, { method: 'GET', headers: myHeaders, redirect: 'follow' });
    const branch = await resultadoBranch.json();
    const { branchName } = branch;
    const nameBranch = document.querySelector("#nombre-branch");
    nameBranch.textContent = `${branchName}`;
}

async function isAllowed() {
    const response = await allowedRequest();
    // if(response.status === 403){
    //     console.log('Forbidden ' +  '/' + window.location.href.replace(BASE_URL, ''));
    //     location.href = '/app/forbidden';
    // } else if(response.status === 401){
    //     console.log('Unauthorized ' +  '/' + window.location.href.replace(BASE_URL, ''));
    //     location.href = '/app/unauthorized';
    // } else {
        console.log('Allowed ' +  '/' + window.location.href.replace(BASE_URL, ''));
    // }
    console.log(`${sessionStorage.getItem("token")}`);
}

async function allowedRequest() {
    const url = BASE_URL + 'api/auth/allowed';
    const uri = '/' + window.location.href.replace(BASE_URL, '');
    const response = await fetch(url + new URLSearchParams({

    }), {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: JSON.stringify({
            url: uri
        }),
        redirect: 'follow'
    });
    if(response.redirected){
        location.href = response.url;
    } else {
        const status = response.status;
        const json = await response.json();
        return { 'status': status, 'msg': json.message };
    }
}
