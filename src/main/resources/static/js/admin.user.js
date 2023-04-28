'use strict'

$(async function () {
    const branches = await getBranches();
    const users = await getUsers();
    let lang = languageMX;
    lang.select = {
        rows: {
            _: 'Seleccionadas %d sucursales.',
            0: 'Selecciona una sucursal dando clic.',
            1: 'Una sucursal seleccionada.'
        },
        cols: {

        }
    }
    $('#branchesTable').DataTable({
        paging: true,
        data: branches,
        select: {
            style: 'multi'
        },
        dom: 'ilfprtlp',
        columns: [
            { "name": "id", "data": "id", "targets": 0 },
            { "name": "nombre", "data": "branchName", "targets": 1 },
            { "name": "dueño", "data": "owner.username", "targets": 2 },
        ],
        language: lang
    });
    $('#usersTable').DataTable({
        paging: true,
        data: users,
        columns: [
            { "name": "id", "data": "id", "targets": 0 },
            { "name": "username", "data": "username", "targets": 1 },
            { "name": "nombre", "data": "name", "targets": 2 },
            { "name": "direccion", "data": "address", "targets": 3 },
            { "name": "correo", "data": "email", "targets": 4 },
            { "name": "telefono", "data": "phoneNumber", "targets": 5 },
        ],
        language: languageMX
    });

})

async function getBranches() {
    const response = await branchesRequest();
    if (!isValidResponse(response)) {
        alerta('error', JSON.stringify(response.data));
        return null;
    }
    return response.data.content;
}

async function branchesRequest() {
    const url = BASE_URL + 'api/branch/all';
    const response = await fetch(url + new URLSearchParams({

    }), {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        }
    });
    const json = await response.json();
    return {
        'data': json,
        'status': response.status
    }
}

async function getUsers() {
    const response = await usersRequest();
    if (!isValidResponse(response)) {
        alerta('error', JSON.stringify(response.data));
        return;
    }
    return response.data.content;
}

async function usersRequest() {
    const url = BASE_URL + 'api/user/all';
    const response = await fetch(url + new URLSearchParams({

    }), {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        }
    })
    const json = await response.json();
    return {
        'data': json,
        'status': response.status
    }
}

function GetInfo(action, id = 0) {
    let userId = 0;
    document.getElementById('alertas').innerHTML = '';
    document.getElementById("user-password").value = '';
    document.getElementById("user-cpassword").value = '';
    if (action == 'add') {
        document.getElementById("user-username").value = '';
        document.getElementById("user-name").value = '';
        document.getElementById("user-lastname").value = '';
        document.getElementById("user-address").value = '';
        document.getElementById("user-email").value = '';
        document.getElementById("user-phone").value = '';
        getRoles();
    } else {
        const url = BASE_URL + `api/user/${id}`;
        fetch(url + new URLSearchParams({

        }), {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                "Authorization": `Bearer ${sessionStorage.getItem("token")}`
            }
        })
            .then((response) => response.json())
            .then(user => {
                if (typeof user.errorCode !== 'undefined') {
                    if (user.errorCode == 404) {
                        alerta('error', 'No existe ese usuario.');
                        $('#modalUsersForm').modal('hide');
                    }
                } else {
                    userId = user.id;
                    document.getElementById("user-username").value = user.username;
                    document.getElementById("user-name").value = user.name
                    document.getElementById("user-lastname").value = user.lastName;
                    document.getElementById("user-address").value = user.address;
                    document.getElementById("user-email").value = user.email;
                    document.getElementById("user-phone").value = user.phoneNumber;
                    getRoles(user.userRoles);
                }
            })
    }
    $('#btnModal').on('click', function(e){
        let branchesIds = [];
        let selectedBranches = $('#branchesTable').DataTable().rows({ selected: true })
        selectedBranches.every(function (rowIdx, tableLoop, rowLoop) {
            const branch = this.data();
            branchesIds.push(branch.id);
        });
        alert(branchesIds);
        actionUser(action, userId, branchesIds);
    });
}

function getRoles(userRoles = []) {
    const url = BASE_URL + `api/role/all`;
    fetch(url + new URLSearchParams({

    }), {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
    })
        .then((response) => response.json())
        .then(data => {
            if (typeof data.errorCode !== 'undefined') {
                alerta('error', data.message);
            } else {
                const roles = data.content;
                let userRolesIds = [];
                userRoles.forEach(userRole => {
                    userRolesIds.push(userRole.id);
                });
                const html = roles.map(role => {
                    let hasRole = userRolesIds.includes(role.id);
                    if (role.name === 'ROLE_EMPLOYEE') {

                    }
                    return `<div id="switch${role.id}" class="form-check form-switch">
                                <input id="role-${role.id}" class="form-check-input" type="checkbox" id="flexSwitchCheckDefault" ${hasRole ? 'checked' : ''}>
                                <label class="form-check-label" for="flexSwitchCheckDefault">${role.name}</label>
                            </div>`
                }).join('');
                $('')
                document.getElementById('roles-sw').innerHTML = html;
                $(`#switch3`).on('change', function (e) {
                    $('#branchesdiv').toggleClass('no-mostrar d-none');
                });
            }
        })
}

function actionUser(action, id, branchesIds) {
    const username = document.getElementById("user-username").value;
    const name = document.getElementById("user-name").value;
    const lastname = document.getElementById("user-lastname").value;
    const address = document.getElementById("user-address").value;
    const email = document.getElementById("user-email").value;
    const phone = document.getElementById("user-phone").value;
    const password = document.getElementById("user-password").value;
    const cpassword = document.getElementById("user-cpassword").value;
    const rolesSwitches = document.querySelectorAll('[id^="role-"]');
    let selectedRoles = [];
    let unselectedRoles = [];
    rolesSwitches.forEach(roleSwitch => {
        if (roleSwitch.checked) {
            selectedRoles.push('' + roleSwitch.id.replace('role-', ''));
        } else {
            unselectedRoles.push('' + roleSwitch.id.replace('role-', ''));
        }
    });
    let error = false;
    let errors = [];
    if (selectedRoles.length == 0) {
        errors.push('Debes seleccionar al menos un rol para el usuario.');
        error = true;
    }
    if (username == '') {
        errors.push('Debes introducir el nombre de usuario');
        error = true;
    }
    if (name == '') {
        errors.push('Debes introducir el nombre del usuario');
        error = true;
    }
    if (lastname == '') {
        errors.push('Debes introducir el apellido del usuario');
        error = true;
    }
    if (address == 0) {
        errors.push('Debes introducir la dirección');
        error = true;
    }
    if (email == '') {
        errors.push('Debes introducir el correo');
        error = true;
    }
    if (phone == '') {
        errors.push('Debes introducir el teléfono');
        error = true;
    }
    if (action == 'add') {
        if (password == '' || cpassword == '') {
            errors.push('Debes ingresar una contraseña y confirmarla.');
            error = true;
        }
        if (password !== cpassword) {
            errors.push('Las contraseñas no coinciden.');
            error = true;
        }
    } else if (password !== '') { // Edita y cambia contraseña
        if (cpassword == '') {
            errors.push('Debes ingresar una contraseña y confirmarla.');
            error = true;
        }
        if (password !== cpassword) {
            errors.push('Las contraseñas no coinciden.');
            error = true;
        }
    }
    if (error) {
        showAlerts(errors);
    } else if (action == 'add') {
        AddUser(username, name, lastname, address, email, phone, selectedRoles, unselectedRoles, password, branchesIds);
    } else {
        UpdateUser(username, name, lastname, address, email, phone, selectedRoles, unselectedRoles, id, password, branchesIds);
    }
}

async function AddUser(userUsername, userName, lastname, userAddress, userEmail, phone, selectedRoles, unselectedRoles, password, branchesIds) {
    const roleId = selectedRoles[0];
    const createResponse = await addUserRequest(userUsername, userName, lastname, userAddress, userEmail, phone, roleId, password, selectedRoles, branchesIds);
    if (isValidResponse(createResponse)) {
        id = createResponse.id;
        alerta('success', 'Se creó el usuario.');
        $('#modalUsersForm').modal('hide');
    } else {
        if (createResponse.errorCode === 400) {
            showObjectAlerts(createResponse.message, 'error');
        } else if (createResponse.errorCode === 500 || createResponse.errorCode === 409) {
            let errors = [];
            errors.push(createResponse.message);
            showAlerts(errors, 'error');
        } else {
            alerta('error', createResponse.errorCode + '\n' + JSON.stringify(createResponse.message));
        }
    }

}

async function UpdateUser(userUsername, userName, lastname, userAddress, userEmail, phone, selectedRoles, unselectedRoles, id, password, branchesIds) {
    const updateResponse = await editUserRequest(userUsername, userName, lastname, userAddress, userEmail, phone, id, password);
    alert(branchesIds);
    for (const idRole of selectedRoles) {
        const response = await grantRoleRequest(id, idRole, branchesIds);
        // if (!isValidResponse(response)) {
        //     console.log(response.message);
        // }
    }
    for (const idRole of unselectedRoles) {
        const response = await revokeRoleRequest(id, idRole);
        // if (!isValidResponse(response)) {
        //     console.log(response.message);
        // }
    }
    if (isValidResponse(updateResponse)) {
        alerta('success', 'Se modificó el usuario.');
        $('#modalUsersForm').modal('hide');
    } else {
        if (updateResponse.errorCode === 400) {
            showObjectAlerts(updateResponse.message, 'error');
        } else if (updateResponse.errorCode === 500 || updateResponse.errorCode === 409) {
            let errors = [];
            errors.push(updateResponse.message);
            showAlerts(errors, 'error');
        } else {
            alerta('error', updateResponse.errorCode + '\n' + JSON.stringify(updateResponse.message));
        }
    }

}

async function editUserRequest(userUsername, userName, lastname, userAddress, userEmail, phone, id, password) {
    const url = BASE_URL + `api/user/`;
    const response = await fetch(url + `${id}` + new URLSearchParams({

    }), {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: JSON.stringify({
            username: userUsername,
            name: userName,
            lastName: lastname,
            address: userAddress,
            phoneNumber: phone,
            email: userEmail,
            password: password
        })
    });
    const json = await response.json();
    return json;
}
async function addUserRequest(userUsername, userName, lastname, userAddress, userEmail, phone, roleId, password, selectedRoles, branchesIds) {
    const url = BASE_URL + `api/auth/${roleId}/register`;
    const response = await fetch(url + new URLSearchParams({

    }), {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: JSON.stringify({
            username: userUsername,
            name: userName,
            lastName: lastname,
            address: userAddress,
            phoneNumber: phone,
            email: userEmail,
            password: password,
            rolesIds: selectedRoles,
            workingBranchesIds: branchesIds
        })
    });
    const json = await response.json();
    return json;
}

async function grantRoleRequest(id, idRole, branchesIds) {
    const url = BASE_URL + `api/user/`;

    const response = await fetch(url + `${id}/grant/${idRole}` + new URLSearchParams({
        branchesIds: branchesIds
    }), {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        }
    });

    const json = await response.json();
    return json;
}

async function revokeRoleRequest(id, idRole) {
    const url = BASE_URL + `api/user/`;

    const response = await fetch(url + `${id}/revoke/${idRole}` + new URLSearchParams({

    }), {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        }
    });

    const json = await response.json();
    return json;
}

async function deleteUser(id) {
    const confirmed = await confirmAlert('warning', 'Eliminar usuario', '¿Estás seguro de eliminar el usuario? No se puede deshacer.', 'Sí, eliminar.');
    if (confirmed) {
        actionDeleteUser(id);
    }
}

async function actionDeleteUser(id) {
    const response = await deleteUserRequest(id);
    if (!isValidResponse(response)) {
        alerta('error', response.message);
    }
    alerta('success', 'Usuario eliminado.');
}

async function deleteUserRequest(id) {
    const url = BASE_URL + `api/user/${id}`;
    const response = await fetch(url + new URLSearchParams({

    }), {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        }
    });
    const json = await response.json();
    return json;
}

function isValidResponse(response) {
    return typeof response.errorCode === 'undefined';
}

const togglePassword = document.querySelector('#togglePassword');
const toggleCPassword = document.querySelector('#toggleCPassword');

const togglePasswordIcon = document.querySelector('#togglePasswordIcon');
const toggleCPasswordIcon = document.querySelector('#toggleCPasswordIcon');

const password = document.querySelector('#user-password');
const cpassword = document.querySelector('#user-cpassword');

togglePassword.addEventListener('click', () => {

    // Toggle the type attribute using
    // getAttribure() method
    const type = password.getAttribute('type') === 'password' ? 'text' : 'password';

    password.setAttribute('type', type);

    // Toggle the eye and bi-eye icon
    togglePasswordIcon.classList.toggle('bi-eye');
});

toggleCPassword.addEventListener('click', () => {

    // Toggle the type attribute using
    // getAttribure() method
    const type = cpassword.getAttribute('type') === 'password' ? 'text' : 'password';

    cpassword.setAttribute('type', type);

    // Toggle the eye and bi-eye icon
    toggleCPasswordIcon.classList.toggle('bi-eye');
});

function showAlerts(alerts, type = 'error') {
    let html = '<div>\n'
    alerts.forEach(alert => {
        html += `<p class="${type}">${alert}</p>\n`;
    })
    html += '</div>';
    document.getElementById('alertas').innerHTML = html;
    document.getElementById('alertas').scrollIntoView();
}

function showObjectAlerts(alerts, type) {
    let html = '<div>\n'
    for (const alert in alerts) {
        html += `<p class="${type}">${alerts[alert]}</p>\n`;
    }
    html += '</div>';
    document.getElementById('alertas').innerHTML = html;
    document.getElementById('alertas').scrollIntoView();
}
