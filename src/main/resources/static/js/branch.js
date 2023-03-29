var map;
document.addEventListener('DOMContentLoaded', function() {
    mostrarBranch();
});

async function mostrarBranch(){
    try{
        const resultado = await fetch('/api/branch/all',{method: 'GET'});
        const sucursales = await resultado.json();
        sucursales["content"].forEach(sucursal => {
            let { id, location, branchName  } = sucursal;
            let { email, name, lastName } = sucursal["owner"];
            const sucursal_mostrar = document.querySelector("#sucursal");
            const contenido = document.createElement("div");
            contenido.className = "sucursal-contenido";
            const titulo = document.createElement("div");
            titulo.className = "nombre";
            const info = document.createElement("div");
            info.id = "info";
            info.className = "info";

            // Titulo
            const h1 = document.createElement("h1");
            h1.textContent = branchName;
            titulo.appendChild(h1);
            contenido.appendChild(titulo);
            info.innerHTML = 
            `
                <p>Nombre de la encargada: <span>${name} ${lastName}</span></p>
                <p>Email: <span>${email}</span></p>
                <p>Direccion: <span>${location}</span></p>
            `;
            const div_btn = document.createElement("div");
            div_btn.id = "seleccionar";
            div_btn.className = "seleccionar-no-sucursal";
            const form = document.createElement("form");
            form.action = "/app/client";
            form.method = "GET";
            const btn = document.createElement("button");
            btn.className = "btn-secundario";
            btn.textContent = "Seleccionar";
            btn.type = "button";
            btn.id = "seleccion-sucursal";
            btn.name = id;

            form.appendChild(btn);
            div_btn.appendChild(form);
            info.appendChild(div_btn);
            contenido.appendChild(info);
            
            // Mapa
            const mapa = document.createElement("div");
            mapa.className = "mapa";
            map = new google.maps.Map(mapa, {
                center: {lat: 21.1419707351680, lng: -100.31784830972968},
                zoom: 15
              });
            var marker = new google.maps.Marker({
                position: {lat: 21.14197073516805, lng: -100.31784830972968},
                map: map,
            title: branchName
              });
            contenido.appendChild(mapa);
            sucursal_mostrar.appendChild(contenido);
            btn.addEventListener("click", seleccionarBranch)
        });
        if(isUser()){
          const userId = sessionStorage.getItem("userId");
          const token = sessionStorage.getItem("token");
          const header = new Headers();
          header.append("Authorization", `Bearer ${token}`);
          const btn = document.querySelector("#seleccionar");
          const resultado = await fetch(`/api/user/${userId}`,{method: 'GET', headers: header, redirect: "follow"});
          const usuario = await resultado.json();
          const { name, lastName } = usuario;
          const nombreUsuario = document.querySelector("#nombreUsuario");
          btn.classList.remove("seleccion-no-sucursal");
          btn.classList.add("seleccion-sucursal")
          nombreUsuario.textContent = `${name} ${lastName}`;
          cambiarBtn();
        } else {
          const btn = document.querySelector("#seleccionar");
          btn.classList.add("seleccion-no-sucursal");
          btn.classList.remove("seleccion-sucursal")
        }
    }
    catch(e){
        console.error(e);
    }
}

function seleccionarBranch(e) {
  window.sessionStorage.setItem("branchId",e.target.name);
  document.location = "http://localhost:5500/app/client/dashboard";
}

function isUser(){
  
  if(sessionStorage.getItem("rol") == 4){
    return true;
  } else {
    return false;
  }
}

function cambiarBtn(){
  const btn_login = document.querySelector("#login");
  btn_login.classList.toggle("log-out");
  const btn_logout = document.querySelector("#logout")
  btn_logout.classList.toggle("login");
}