let pagina = 1;

const months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
const dateActally = new Date();
let currentDate = new Date();
let currentDay = currentDate.getDate();
let currentMonth = currentDate.getMonth();
let currentYear = currentDate.getFullYear();

let prev = document.querySelector("#prev");
let month = document.querySelector("#month");
let year = document.querySelector("#year");
let next = document.querySelector("#next");

let dates = document.querySelector("#dates");

document.addEventListener('DOMContentLoaded', function(){
    month.textContent = months[currentMonth];
    year.textContent = currentYear.toString();
    
    prev.addEventListener("click", () => lastMonth());
    next.addEventListener("click", () => nextMonth());
    
    writeMonths(currentMonth);

    showSeccion();

    changeSeccion();
});



function writeMonths(month){
    for(let i= startDay(); i>0;i--){
        dates.innerHTML += `<div class="date item color-darken">${getTotalDays(currentMonth-1)-(i-1)}</div>`;
    }
    for(let i=1;i<=getTotalDays(month); i++){
        dates.innerHTML += `<div class="date item" id="date_${i}" onclick="selectDay(this)">${i}</div>`;
    }
}

function getTotalDays(month){
    if(month === -1) month == 11;
    if(month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9 ||month == 11){
        return 31;
    } else if(month == 3 || month == 5 || month == 8 || month == 10){
        return 30;
    } else {
        return isLeap() ? 29:28;
    }
}

function isLeap(){
    return (currentYear % 100 !== 0 && currentYear % 4 === 0 || currentYear % 400 === 0) ? true : false;  
}

function startDay(){
    let start = new Date(currentYear, currentMonth, 1);
    return ((start.getDay()-1) === -1) ? 6 : start.getDay();
}

function lastMonth(){
    if(currentMonth !== 0){
        currentMonth--;
    } else {
        currentMonth = 11;
        currentYear--;
    }
    setNewDate();
}

function nextMonth(){
    if(currentMonth !== 11){
        currentMonth++;
    } else {
        currentMonth = 0;
        currentYear++;
    }
    setNewDate();
}

function setNewDate(){
    currentDate.setFullYear(currentYear,currentMonth,currentDay);
    month.textContent = months[currentMonth];
    year.textContent = currentYear.toString();
    dates.textContent = ``;
    writeMonths(currentMonth);
}

function selectDay(div){
    div.classList.toggle("selected");
}

function showSeccion(){
    const seccionAnterior = document.querySelector('.mostrar-seccion');
    if (seccionAnterior) {
        seccionAnterior.classList.remove('mostrar-seccion');
    }
    
    const seccionActual = document.querySelector(`#paso-${pagina}`);
    seccionActual.classList.add('mostrar-seccion');
}

function changeSeccion() {
    const enlaces = document.querySelectorAll('.button');
    enlaces.forEach(enlace => {
        enlace.addEventListener('click', e => {
            e.preventDefault();
            pagina = parseInt(e.target.dataset.paso);
            showSeccion();

            // botonesPaginador();
        })
    })
}

function PaginatorButtons(){
    if (pagina === 1) {
        paginaAnterior.classList.add('ocultar');
    } else if (pagina === 3) {
        paginaSiguiente.classList.add('ocultar');
        paginaAnterior.classList.remove('ocultar');

        mostrarResumen(); // Estamos en la página 3, carga el resumen de la cita
    } else {
        paginaAnterior.classList.remove('ocultar');
        paginaSiguiente.classList.remove('ocultar');
    }
}