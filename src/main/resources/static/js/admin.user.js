'use strict'

$(async function () {
    const branches = await getBranches();
    const users = await getUsers();
    await getRoles();
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
            {
                "searchable": false, "orderable": false,
                "name": "acciones", "render": function (data, type, row) {
                    let rowId = Number(data.id);
                    return `
                            <button type="button" class="btn btn-warning rounded-pill" data-target="${rowId}"><span class="bi bi-pencil"></span></button>
                            <button type="button" class="btn btn-danger rounded-pill modal-trigger" data-bs-toggle="modal" data-bs-target="#modalUsersForm" data-target="${rowId}"><span class="bi bi-trash text-white"></span></button>`;
                }, "data": null, "targets": [6]
            }
        ],
        language: languageMX
    });

});

$(document).on('click', '.btn-warning', function (e) {
    const id = $(this).data('target');
    $('#btnModal').data('action', 'edit');
    $('#btnModal').data('target', id);
    inflateForm('edit', id);
    $('#modalUsersForm').modal('show');
});

$(document).on('click', '.modal-trigger', function (e) {
    const action = $(this).data('action');
    const id = $(this).data('target');
    inflateForm(action);
    $('#btnModal').data('action', action);
    $('#btnModal').data('target', id);
});

function extraValidation(valid = true) {
    let branchesIds = [];
    let selectedBranches = $('#branchesTable').DataTable().rows({ selected: true })
    selectedBranches.every(function (rowIdx, tableLoop, rowLoop) {
        const branch = this.data();
        branchesIds.push(branch.id);
    });
    let cont = 0;
    if (selectedBranches.count() === 0 && $('#switch3').prop('checked') === true) {
        valid = false;
        let errors = ['Es necesario al menos una sucursal de trabajo para el empleado.'];
        cont += errors.length;
        showFeedback('branches', errors);
    } else {
        hideFeedback('branches');
    }
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
    if (selectedRoles.length === 0) {
        valid = false;
        let errors = ['Selecciona al menos un rol para el usuario.'];
        cont += errors.length;
        showFeedback('roles', errors);
    } else {
        hideFeedback('roles');
    }
    if (!valid) {
        $('#formFeedback').html('Revisa el formulario.');
        showFeedback('form',['Revisa el formulario']);
        return cont;
    } else {
        hideFeedback('form');
        const userId = $('#btnModal').data('target');
        const action = $('#btnModal').data('action');
        actionUser(action, userId, selectedRoles, branchesIds);
    }
}

function hideFeedback(aria){
    let feedback = $(`#${aria}Feedback`);
    feedback.addClass('d-none');
    feedback.removeClass('d-block');
    feedback.html('');
}

function showFeedback(aria, errors) {
    let feedback = $(`#${aria}Feedback`);
    if(Array.isArray(errors)){
        let html = `<ul>`;
        errors.forEach(error => {
            html += `<li>${error}</li>`;
        });
        html += `</ul>`;
        feedback.html(html);
    } else {
        feedback.html(errors);
    }
    feedback.addClass('d-block');
    feedback.removeClass('d-none');
}

function validate() {

}

$(document).on('click', '#btnModal', function (e) {
    const action = $(this).data('action');
    $('#usersForm').validate().destroy();
    $('#usersForm').validate({
        invalidHandler: function (event, validator) {
            // 'this' refers to the form
            var errors = validator.numberOfInvalids() + extraValidation(false);
            if (errors) {
                var message = errors == 1
                    ? 'Error en 1 campo. Revisa lo resaltado en rojo.'
                    : 'Error en ' + errors + ' campos. Han sido resaltados en rojo.';
                showFeedback('form',[message]);
            } else {
                hideFeedback('form');
            }
        },
        submitHandler: function (form) {
            extraValidation(true);
        },
        rules: {
            'user-username': 'required',
            'user-email': {
                required: true,
                email: true
            },
            'user-name': 'required',
            'user-lastname': 'required',
            'user-address': 'required',
            'user-phone': {
                required: true,
                minlength: 10,
                maxlength: 10,
                digits: true
            },
            'user-password': {
                required: action !== 'edit',
                minlength: 8,
            },
            'user-cpassword': {
                required: action !== 'edit',
                minlength: 8,
                equalTo: "#user-password"
            }
        },
        messages: {
            'user-username': 'El nombre de usario es requerido.',
            'user-email': {
                required: 'Ingresa el correo electrónico.',
                email: 'El correo debe ser algo como usuario@dominio.co'
            },
            'user-name': 'Ingresa el nombre.',
            'user-lastname': 'Ingresa el apellido.',
            'user-address': 'Ingresa la dirección',
            'user-phone': 'Ingresa un número a 10 dígitos.',
            'user-password': {
                required: 'Ingresa la contraseña',
                minlength: 'La contraseña debe tener 8 caracteres mínimo.'
            },
            'user-cpassword': {
                required: 'Confirma la contraseña',
                minlength: 'La contraseña debe tener 8 caracteres mínimo.',
                equalTo: 'Las contraseñas no coinciden.'
            }

        },
        errorClass: 'is-invalid',
        validClass: 'is-valid',
        errorElement: 'li',
        wrapper: 'ul',
        errorPlacement: function (error, element) {
            const aria = element.attr('aria-label');
            error.appendTo($(`#${aria}Feedback`));
            showFeedback(aria, error);
        }
    });
});

$(document).on('input', 'input.form-control', function () {
    if($(this).hasClass('is-invalid') && $(this).prop('type') === 'email' || $(this).hasClass('is-invalid') && $(this).prop('type')!=='email'){
        $('#usersForm').validate().element($(this));
    }
});

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

function cleanForm() {
    $('input').each(function () {
        $(this).val('');
        hideFeedback($(this).attr('aria-label'));
    });
    $('input').removeClass('is-invalid');
    $('input').removeClass('is-valid');
    hideFeedback('roles');
    hideFeedback('form');
}

async function inflateForm(action, id = -1) {
    cleanForm();
    if (action === 'add') {
        await getRoles();
    } else {
        const response = await getUserInfoRequest(id);
        if (!isValidResponse(response)) {
            alerta('error', JSON.stringify(response.data));
            return;
        }
        const user = response.data;
        document.getElementById("user-username").value = user.username;
        document.getElementById("user-name").value = user.name
        document.getElementById("user-lastname").value = user.lastName;
        document.getElementById("user-address").value = user.address;
        document.getElementById("user-email").value = user.email;
        document.getElementById("user-phone").value = user.phoneNumber;
        getRoles(user.userRoles);
    }

}

async function getUserInfoRequest(id) {
    const url = BASE_URL + `api/user/${id}`;
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

async function getRoles(userRoles = []) {
    const response = await rolesRequest();
    if (!isValidResponse(response)) {
        alerta('error', JSON.stringify(response.data));
        return;
    }
    const roles = response.data.content;
    let userRolesIds = [];
    userRoles.forEach(userRole => {
        userRolesIds.push(userRole.id);
    });
    const html = roles.map(role => {
        let hasRole = userRolesIds.includes(role.id);
        return `<div id="switch${role.id}" class="form-check form-switch">
                                <input id="role-${role.id}" class="form-check-input" type="checkbox" ${hasRole ? 'checked' : ''}>
                                <label class="form-check-label" for="flexSwitchCheckDefault">${role.name}</label>
                            </div>`
    }).join('');
    document.getElementById('roles-sw').innerHTML = html;
    $(`#switch3`).on('change', function (e) {
        this.checked = !this.checked;
        $('#branchesdiv').toggleClass('no-mostrar d-none');
    });
}

async function rolesRequest() {
    const url = BASE_URL + `api/role/all`;
    const response = await fetch(url + new URLSearchParams({

    }), {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
    });
    const json = await response.json();
    return {
        'data': json,
        'status': response.status
    }

}

function actionUser(action, id, selectedRoles, branchesIds) {
    let data = {};
    $('form#usersForm :input').each(function(){
        const aria = $(this).attr('aria-label');
        data[aria] = $(this).val();
    });
    if (action == 'add') {
        addUser(data, id, selectedRoles, branchesIds);
    } else {
        updateUser(data, id, selectedRoles, branchesIds);
    }
}

async function addUser(data, id, selectedRoles, branchesIds) {
    const createResponse = await addUserRequest(data, id, selectedRoles, branchesIds);
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

async function updateUser(data, id, selectedRoles, branchesIds) {
    const updateResponse = await editUserRequest(data, id, selectedRoles, branchesIds);
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

async function editUserRequest(data, id, selectedRoles, branchesIds) {
    const url = BASE_URL + `api/user/${id}`;
    const response = await fetch(url + new URLSearchParams({

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
    return {
        'data': json,
        'status': response.status
    };
}
async function addUserRequest(data, id, selectedRoles, branchesIds) {
    const url = BASE_URL + `api/auth/register/roles`;
    const response = await fetch(url + new URLSearchParams({

    }), {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: JSON.stringify({
            username: data.username,
            name: data.name,
            lastName: data.lastName,
            address: data.address,
            phoneNumber: data.phoneNumber,
            email: data.email,
            password: data.password,
            rolesIds: selectedRoles,
            workingBranchesIds: branchesIds
        })
    });
    const json = await response.json();
    return {
        'data': json,
        'status': response.status
    };
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
    return {
        'data': json,
        'status': response.status
    };
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
    return {
        'data': json,
        'status': response.status
    };
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
        alerta('error', response.data.message);
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
    return {
        'data': json,
        'status': response.status
    };
}

function isValidResponse(response) {
    return typeof response.data.errorCode === 'undefined' && response.status <= 399;
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
