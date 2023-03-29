const userId = sessionStorage.getItem("userId");
const branchId = sessionStorage.getItem("branchId");
const myHeaders = new Headers();

myHeaders.append("Authorization", `Bearer ${sessionStorage.getItem("token")}` );

document.addEventListener('DOMContentLoaded', function() {
    getAppointment();
});

async function getAppointment() {
    const resultGetAppointment = await fetch(`/api/appointment/usuario/${userId}`, {method: 'GET', headers: myHeaders, redirect: 'follow'});
    const appointments = await resultGetAppointment.json();
    const show_appointment = document.querySelector("#appointment");
    appointments["content"].forEach(appointment => {
        const {id_branch, id_service, appointment_date, appointmnet_time, id} = appointment;
        const {location,  branchName, owner} = id_branch;
        const {name} = owner;
        let services = "";
        id_service.forEach(service => {
            let {service_name} = service;
            services += `<br><span>&nbsp;&nbsp;${service_name}</span>`
        })
        const newAppointment = document.createElement("div");
        newAppointment.innerHTML = `
            <p>Sucursal:<span> ${branchName}</span></p>
            <p>Encargada:<span> ${name}</span></p>
            <p>Ubicación:<span> ${location}</span></p>
            <p>Fecha y hora:<span> ${appointment_date} : ${appointmnet_time}</span></p>
            <p>Servicios: ${services}</p>
            <p>Status:<span> Pendiente</span></p>
            <div>
                <button type="button" id="btn-update-${id}" name=${id}>Editar reservacion</button>
                <button type="button" id="btn-cancel-${id}" name=${id}>Cancelar</button>
            </div>
            <hr>
        `;
        show_appointment.appendChild(newAppointment);
        document.querySelector(`#btn-update-${id}`).addEventListener("click", update);
        document.querySelector(`#btn-cancel-${id}`).addEventListener("click", cancel);
    });
}

function update(event){
    id = event.target.name;
    sessionStorage.setItem("citaId", id);
    location.href = `${BASE_URL}app/client/appointment/update`;
}
function cancel(event){
    id = event.target.name;
}