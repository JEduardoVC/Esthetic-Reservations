var useres = [];
let table = $('#usersTable').DataTable({
    paging: true,
    language: {
        url: '//cdn.datatables.net/plug-ins/1.13.1/i18n/es-ES.json'
    }
});

window.onload = inflate();

function inflate() {
    loadUsers();
}

function loadUsers() {
    table.clear();
    var url = BASE_URL + 'api/user/all';
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
        .then(data => {
            if (typeof data.errorCode !== 'undefined') {
                if (data.errorCode == 404) {
                    alert('Sin usuarios.');
                }
            } else {
                users = data.content;
                const html = users.map(user => {
                    return `<tr>
                        <td class="text-dark">${user.id}</td>
                        <td class="text-dark">${user.username}</td>
                        <td class="text-dark">${user.name + ' ' + user.lastName}</td>
                        <td class="text-dark">${user.address}</td>
                        <td class="text-dark">${user.email}</td>
                        <td class="text-dark">${user.phoneNumber}</td>
                        <td>
                            <button id="deleteuser${user.id}" class="btn btn-outline-danger fs-4" onclick="deleteUser(${user.id});">Eliminar</button>
                            <button id="edituser${user.id}" class="btn btn-warning fs-4" data-bs-toggle="modal" data-bs-target="#modalUsersForm" onclick="GetInfo('edit',${user.id});">Editar</button>
                        </td>
                    </tr>`
                }).join('');
                table.rows.add($(html)).draw();
            }
        })
}

function deleteUser(id) {
    if (confirm('¿Seguro que desea eliminar la usuario ' + id + '?')) {
        actionDeleteUser(id);
    } else {
        location.href = '/app/admin/usuarios';
    }
}

function actionDeleteUser(id) {
    var url = BASE_URL + `api/user/${id}`;
    fetch(url + new URLSearchParams({

    }), {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        }
    })
        .then((response) => response.json())
        .then(data => {
            if (typeof data.errorCode !== 'undefined') {
                if (data.errorCode == 404) {
                    alert('No existe esa usuario.');
                }
            } else {
                alert(data.message);
                loadUsers();
            }
        })
}

function GetInfo(action, id = 0) {
    let userId = 0;
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
        var url = BASE_URL + `api/user/${id}`;
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
                        alert('No existe esa usuario.');
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
    document.getElementById("btnModal").onclick = function () {
        actionUser(action, userId);
    };
}

function getRoles(userRoles = []) {
    var url = BASE_URL + `api/role/all`;
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
                if (data.errorCode == 404) {
                    alert('No existe ese usuario.');
                }
            } else {
                if (typeof data.errorCode !== 'undefined') {
                    if (data.errorCode == 404) {
                        alert('No hay roles.');
                    }
                } else {
                    roles = data.content;
                    userRolesIds = [];
                    userRoles.forEach(userRole => {
                        userRolesIds.push(userRole.id);
                    });
                    const html = roles.map(role => {
                        var hasRole = userRolesIds.includes(role.id);
                        return `<div class="form-check form-switch">
                                    <input id="role-${role.id}" class="form-check-input" type="checkbox" id="flexSwitchCheckDefault" ${hasRole ? 'checked' : ''}>
                                    <label class="form-check-label" for="flexSwitchCheckDefault">${role.name}</label>
                                </div>`
                    }).join('');
                    document.getElementById('roles-sw').innerHTML = html;
                }
            }
        })
}

function actionUser(action, id) {
    const username = document.getElementById("user-username").value;
    const name = document.getElementById("user-name").value;
    const lastname = document.getElementById("user-lastname").value;
    const address = document.getElementById("user-address").value;
    const email = document.getElementById("user-email").value;
    const phone = document.getElementById("user-phone").value;
    const password = document.getElementById("user-password").value;
    const cpassword = document.getElementById("user-cpassword").value;
    const rolesSwitches = document.querySelectorAll('[id^="role-"]');
    var selectedRoles = [];
    var unselectedRoles = [];
    rolesSwitches.forEach(roleSwitch => {
        if (roleSwitch.checked) {
            selectedRoles.push('' + roleSwitch.id.replace('role-', ''));
        } else {
            unselectedRoles.push('' + roleSwitch.id.replace('role-', ''));
        }
    });
    var error = false;
    var errors = '';
    if (selectedRoles.length == 0) {
        errors += 'Debes seleccionar al menos un rol para el usuario.\n';
        error = true;
    }
    if (username == '') {
        errors += 'Debes introducir el nombre de usuario\n';
        error = true;
    }
    if (name == '') {
        errors += 'Debes introducir el nombre del usuario\n';
        error = true;
    }
    if (lastname == '') {
        errors += 'Debes introducir el apellido de la usuario\n';
        error = true;
    }
    if (address == 0) {
        errors += 'Debes introducir la dirección\n';
        error = true;
    }
    if (email == '') {
        errors += 'Debes introducir el correo\n';
        error = true;
    }
    if (phone == '') {
        errors += 'Debes introducir el teléfono\n';
        error = true;
    }
    if (action == 'add') {
        if (password == '' || cpassword == '') {
            errors += 'Debes ingresar una contraseña y confirmarla.\n';
            error = true;
        }
        if (password !== cpassword) {
            errors += 'Las contraseñas no coinciden.\n';
            error = true;
        }
    } else if (password !== '') { // Edita y cambia contraseña
        if (cpassword == '') {
            errors += 'Debes ingresar una contraseña y confirmarla.\n';
            error = true;
        }
        if (password !== cpassword) {
            errors += 'Las contraseñas no coinciden.\n';
            error = true;
        }
    }
    if (error) {
        alert(errors);
    } else if (action == 'add') {
        AddUser(username, name, lastname, address, email, phone, selectedRoles, unselectedRoles, password);
    } else {
        UpdateUser(username, name, lastname, address, email, phone, selectedRoles, unselectedRoles, id, password);
    }
}

async function AddUser(userUsername, userName, lastname, userAddress, userEmail, phone, selectedRoles, unselectedRoles, password) {
    const roleId = selectedRoles[0];
    let id = -1;
    const createResponse = await addUserRequest(userUsername, userName, lastname, userAddress, userEmail, phone, roleId, password);
    if (isValidResponse(createResponse)) {
        id = createResponse.id;
        alert('Se creó el usuario.');
        $('#modalUsersForm').modal('hide');
        loadUsers();
    } else {
        alert(JSON.stringify(createResponse));
    }
    for (const idRole of selectedRoles) {
        const response = await grantRoleRequest(id, idRole);
        if (!isValidResponse(response)) {
            console.log(response.message);
        }
    }
    for (const idRole of unselectedRoles) {
        const response = await revokeRoleRequest(id, idRole);
        if (!isValidResponse(response)) {
            console.log(response.message);
        }
    }
}

async function UpdateUser(userUsername, userName, lastname, userAddress, userEmail, phone, selectedRoles, unselectedRoles, id, password) {
    const updateResponse = await editUserRequest(userUsername, userName, lastname, userAddress, userEmail, phone, id, password);
    if (isValidResponse(updateResponse)) {
        alert('Se modificó el usuario.');
        $('#modalUsersForm').modal('hide');
        loadUsers();
    } else {
        alert(JSON.stringify(updateResponse));
    }
    for (const idRole of selectedRoles) {
        const response = await grantRoleRequest(id, idRole);
        if (!isValidResponse(response)) {
            console.log(response.message);
        }
    }
    for (const idRole of unselectedRoles) {
        const response = await revokeRoleRequest(id, idRole);
        if (!isValidResponse(response)) {
            console.log(response.message);
        }
    }
}

async function editUserRequest(userUsername, userName, lastname, userAddress, userEmail, phone, id, password) {
    var url = BASE_URL + `api/user/`;
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
async function addUserRequest(userUsername, userName, lastname, userAddress, userEmail, phone, roleId, password) {
    var url = BASE_URL + `api/auth/${roleId}/register`;
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
            password: password
        })
    });
    const json = await response.json();
    return json;
}

async function grantRoleRequest(id, idRole) {
    var url = BASE_URL + `api/user/`;

    const response = await fetch(url + `${id}/grant/${idRole}` + new URLSearchParams({

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
    var url = BASE_URL + `api/user/`;

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

function deleteUser(id) {
    if (confirm('¿Seguro que desea eliminar el usuario ' + id + '?')) {
        actionDeleteUser(id);
    } else {
        location.href = '/app/admin/usuarios';
    }
}

async function actionDeleteUser(id) {
    const response = await deleteUserRequest(id);
    if (!isValidResponse(response)) {
        alert(response.message);
    }
    alert('Usuario eliminado.');
    loadUsers();
}

async function deleteUserRequest(id) {
    var url = BASE_URL + `api/user/${id}`;
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

const password = document.querySelector('#user-password');
const cpassword = document.querySelector('#user-cpassword');

togglePassword.addEventListener('click', () => {

    // Toggle the type attribute using
    // getAttribure() method
    const type = password.getAttribute('type') === 'password' ?'text' : 'password';

    password.setAttribute('type', type);

    // Toggle the eye and bi-eye icon
    togglePassword.classList.toggle('bi-eye');
});

toggleCPassword.addEventListener('click', () => {

    // Toggle the type attribute using
    // getAttribure() method
    const type = cpassword.getAttribute('type') === 'password' ?'text' : 'password';

    cpassword.setAttribute('type', type);

    // Toggle the eye and bi-eye icon
    toggleCPassword.classList.toggle('bi-eye');
});