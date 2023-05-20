document.addEventListener('DOMContentLoaded', function () {
    darkMode();
    darkModePhone();
    menu();
    menuPhone();
    isDarkMode();
    isDarkModeNew();
});

// $(function () {
//     let darkMode = sessionStorage.getItem('darkMode');
//     if (darkMode === null) {
//         sessionStorage.setItem('darkMode', 'false');
//         darkMode = 'false';
//     }
//     if(darkMode === 'false'){
//         $('.luna').removeClass('d-none no-mostrar');
//         $('.sol').addClass('d-none no-mostrar');
//     } else {
//         $('.sol').removeClass('d-none no-mostrar');
//         $('.luna').addClass('d-none no-mostrar');
//     }
//     if(darkMode === 'false' && $('html').hasClass('dark-mode') === true){
//         $('html').removeClass('dark-mode');
//     }
//     if(darkMode === 'true' && $('html').hasClass('dark-mode') === false){
//         $('html').addClass('dark-mode');
//     }
//     $('.dark-mode-boton').on('click', toggleDarkMode);
//     $('.ligth-mode-boton').on('click', toggleDarkMode);
// })

// const toggleDarkMode = function () {
//     $('html').toggleClass('dark-mode');
//     sessionStorage.setItem('darkMode', sessionStorage.getItem('darkMode') === 'true' ? 'false' : 'true');
//     $('.sol').toggleClass('d-none no-mostrar');
//     $('.luna').toggleClass('d-none no-mostrar');
// };

function darkMode() {
    const botonDarkMode = document.querySelector(".dark-mode-boton");
    const botonLigthMode = document.querySelector(".ligth-mode-boton");
    if(!botonDarkMode) return;
    if(!botonLigthMode) return;
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