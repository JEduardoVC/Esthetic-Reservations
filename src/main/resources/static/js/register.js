document.getElementById("btn-registrar").addEventListener("click", register, false);

let alertas = [];

function validForm() {
    alertas = [];
    var valid = true;
    if (document.getElementById('username').value == '') {
        alertas.push("Debes introducir tu nombre de usuario.");
        valid = false;
    }
    if (document.getElementById('first_name').value == '') {
        alertas.push("Debes introducir tu nombre.");
        valid = false;
    }
    if (document.getElementById('last_name').value == '') {
        alertas.push("Debes introducir tus apellidos.");
        valid = false;
    }
    if (document.getElementById('phone_number').value == '') {
        alertas.push("Debes introducir tu numero telefónico.");
        valid = false;
    }
    if (document.getElementById('address').value == '') {
        alertas.push("Debes introducir tu dirección.");
        valid = false;
    }
    if (document.getElementById('email').value == '') {
        alertas.push("Debes introducir tu correo electrónico");
        valid = false;
    }
    if (document.getElementById('password').value == '') {
        alertas.push("Tienes que introducir tu contraseña");
        valid = false;
    }
    if (document.getElementById('repeat_password').value == '') {
        alertas.push("Tienes que confirmar tu contraseña");
        valid = false;
    }
    if (document.getElementById('repeat_password').value !== document.getElementById('repeat_password').value) {
        alertas.push("Las contraseñas no coinciden");
        valid = false;
    }
    return valid;
}

function register() {
    if (!validForm()) {
        showAlerts(alertas, 'error');
        return;
    }
    fetch('http://localhost:5500/api/auth/admin/register', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            'username': document.getElementById("username").value,
            'name': document.getElementById("first_name").value,
            'lastName': document.getElementById("last_name").value,
            'address': document.getElementById("address").value,
            'phoneNumber': document.getElementById("phone_number").value,
            'email': document.getElementById("email").value,
            'password': document.getElementById("password").value
        })
    })
        .then((response) => response.json())
        .then(data => {
            if (typeof data.errorCode !== 'undefined') {
                if (data.errorCode == 400) {
                    for (const message in data.message) {
                        alertas.push(`${data.message[message]}`);
                    }
                    showAlerts(alertas, 'error');
                    return;
                }
            }
            alertas.push('Registro exitoso.');
            showAlerts(alertas, 'successful');
        })
}

function showAlerts(alerts, type) {
    html = '<div>\n'
    for (const alert in alerts) {
        html += `<p class="${type}">${alertas[alert]}</p>\n`;
    }
    html += '</div>';
    document.getElementById('alertas').innerHTML = html;
    document.getElementById('alertas').scrollIntoView();
}