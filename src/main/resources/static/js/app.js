document.addEventListener('DOMContentLoaded', async function () {
    await isAllowed();
    darkMode();
    darkModePhone();
    menu();
    menuPhone();
    isDarkMode();
    isDarkModeNew();
});

function darkMode() {
    const botonDarkMode = document.querySelector(".dark-mode-boton");
    const botonLigthMode = document.querySelector(".ligth-mode-boton");
    botonDarkMode.addEventListener("click", function () {
        if (!document.querySelector(".dark-mode")) {
            document.body.classList.add("dark-mode");
            document.querySelector(".luna").classList.add("no-mostrar");
            document.querySelector(".sol").classList.remove("no-mostrar");
            document.querySelector(".luna").classList.add("d-none");
            document.querySelector(".sol").classList.remove("d-none");
            sessionStorage.setItem("darkMode", "true");
        }
    });
    botonLigthMode.addEventListener("click", function () {
        if (document.querySelector(".dark-mode")) {
            document.body.classList.remove("dark-mode");
            document.querySelector(".luna").classList.remove("no-mostrar");
            document.querySelector(".sol").classList.add("no-mostrar");
            document.querySelector(".luna").classList.remove("d-none");
            document.querySelector(".sol").classList.add("d-none");
            sessionStorage.removeItem("darkMode");
            isDarkMode();
        }
    });
}
function darkModePhone(){
    const botonDarkMode = document.querySelector(".dark-mode-boton-phone");
    const botonLigthMode = document.querySelector(".ligth-mode-boton-phone");
    if(botonDarkMode){
        if(botonLigthMode){
            botonDarkMode.addEventListener("click", function() {
                if(!document.querySelector(".dark-mode")){
                    document.body.classList.add("dark-mode");
                    document.querySelector(".luna-phone").classList.add("no-mostrar");
                    document.querySelector(".sol-phone").classList.remove("no-mostrar");
                    sessionStorage.setItem("darkMode", "true");
                }
            });
            botonLigthMode.addEventListener("click", function() {
                if(document.querySelector(".dark-mode")){
                    document.body.classList.remove("dark-mode");
                    document.querySelector(".luna-phone").classList.remove("no-mostrar");
                    document.querySelector(".sol-phone").classList.add("no-mostrar");
                    sessionStorage.removeItem("darkMode");
                }
            });
        }
    }
}

function menu() {
    const menu = document.querySelector(".options-animation");
    if (menu) {
        menu.addEventListener("click", function () {
            const acciones = document.querySelector("#actions");
            acciones.classList.toggle("mostrar");
            acciones.classList.toggle("menu-buttons");
            acciones.classList.toggle("actions");
        });
    }
}
function menuPhone(){
    const menu = document.querySelector("#menuPhone");
    if(menu){
        menu.addEventListener("click", function(){
            const shortCuts = document.querySelector("#menuShortCuts");
            shortCuts.classList.toggle("mostrar");
            shortCuts.classList.toggle("no-mostrar");
        });
    }
}

function isDarkModeNew(){
    const lunaDesktop = document.querySelector(".luna");
    const solDesktop = document.querySelector(".sol");
    const lunaDesktopPhone = document.querySelector(".luna-phone");
    const solDesktopPhone = document.querySelector(".sol-phone");
    if(lunaDesktop){
        if(solDesktop){
            if(lunaDesktopPhone){
                if(solDesktopPhone){
                    if(sessionStorage.getItem("darkMode")){
                        document.body.classList.add("dark-mode");
                            lunaDesktop.classList.add("no-mostrar");
                            lunaDesktopPhone.classList.add("no-mostrar");
                            solDesktop.classList.remove("no-mostrar");
                            solDesktopPhone.classList.remove("no-mostrar");
                        }
                    else{
                        lunaDesktop.classList.remove("no-mostrar");
                        lunaDesktopPhone.classList.remove("no-mostrar");
                        solDesktop.classList.add("no-mostrar");
                        solDesktopPhone.classList.add("no-mostrar");
                    }
                }
            }
        }
    }
}

function isDarkMode() {
    if (sessionStorage.getItem("darkMode")) {
        var root = document.getElementsByTagName('html')[0]; // '0' to assign the first (and only `HTML` tag)
        root.setAttribute('class', 'dark-mode');
        document.body.classList.add("dark-mode");
        document.querySelector("header").classList.add("dark-mode");
        document.querySelector("nav").classList.add("dark-mode");
        document.querySelector(".luna").classList.add("no-mostrar");
        document.querySelector(".sol").classList.remove("no-mostrar");
        document.querySelector(".luna").classList.add("d-none");
        document.querySelector(".sol").classList.remove("d-none");
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
