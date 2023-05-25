'use strict'

DataTable.Buttons.defaults.dom.button.className = 'btn';

var usersTable;
var branchesTable;

$(async function () {
    const users = await getUsers();
    usersTable = $('#usersTable').DataTable({
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
                            <button type="button" class="btn btn-warning rounded-pill" data-bs-toggle="modal" data-bs-target="#modalUsersForm"  data-target="${rowId}"><span class="bi bi-pencil"></span></button>
                            <button type="button" class="btn btn-danger rounded-pill" data-target="${rowId}"><span class="bi bi-trash text-white"></span></button>`;
                }, "data": null, "targets": [6]
            }
        ],
        language: languageMX
    });

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
    branchesTable = $('#branchesTable').DataTable({
        paging: true,
        data: [],
        rowId: 'id',
        select: {
            style: 'multi'
        },
        dom: 'iBlfprtlp',
        columns: [
            { "name": "id", "data": "id", "targets": 0 },
            { "name": "nombre", "data": "branchName", "targets": 1 },
            { "name": "dueño", "data": "owner.username", "targets": 2 },
        ],
        buttons: [
            {
                text: 'Seleccionar filtrados.',
                action: function () {
                    this.rows({ search: 'applied' }).select();
                },
                className: 'btn-outline-primary m-2 rounded-pill'
            },
            {
                text: 'Deseleccionar filtrados.',
                action: function () {
                    this.rows({ search: 'applied' }).deselect();
                },
                className: 'btn-outline-secondary m-2 rounded-pill'
            },
            {
                text: 'Deseleccionar todos',
                action: function () {
                    this.rows().deselect();
                },
                className: 'btn-outline-warning m-2 rounded-pill'
            },
            {
                extend: 'showSelected',
                text: `Mostrar selección`,
                className: 'btn-outline-success m-2 rounded-pill'
            },
        ],
        language: lang
    });
    branchesTable.on('buttons-action', function (e, buttonApi, dataTable, node, config) {
        console.log(node.prop('text'));
        if (buttonApi.text() === 'Mostrar selección' || buttonApi.text() === 'Mostrar todos') {
            dataTable
                .search('')
                .draw()
            if (buttonApi.text() === 'Mostrar selección') {
                buttonApi.text('Mostrar todos');
            } else {
                buttonApi.text('Mostrar selección');
            }

        }
    });

});

function refreshTable(table, data) {
    table.clear();
    table.rows.add(data).draw();
}

function hideFeedback(aria) {
    let feedback = $(`#${aria}Feedback`);
    feedback.addClass('d-none');
    feedback.removeClass('d-block');
    feedback.html('');
}

function showFeedback(aria, errors) {
    let feedback = $(`#${aria}Feedback`);
    if (Array.isArray(errors)) {
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

function cleanForm() {
    $('input').each(function () {
        $(this).val('');
        hideFeedback($(this).attr('aria-label'));
    });
    $('input').removeClass('is-invalid');
    $('input').removeClass('is-valid');
    $('#branchesdiv').addClass('no-mostrar d-none');
    $('#branchesTable').DataTable().rows().deselect();
    $('#alertas').addClass('no-mostrar d-none');
    hideFeedback('roles');
    hideFeedback('form');
}

async function inflateForm(action, id = -1) {
    cleanForm();
    refreshTable(branchesTable, await getBranches());
    if(action === 'add'){
        await getRoles();
    }
    if (action === 'edit') {
        const user = await getUserInfo(id);
        document.getElementById("user-username").value = user.username;
        document.getElementById("user-name").value = user.name
        document.getElementById("user-lastname").value = user.lastName;
        document.getElementById("user-address").value = user.address;
        document.getElementById("user-email").value = user.email;
        document.getElementById("user-phone").value = user.phoneNumber;
        user.userRoles.forEach(async role => {
            if (role.id === 3) {
                $('#branchesdiv').removeClass('no-mostrar d-none');
                const employee = await getEmployeeInfo(user.id);
                // console.log(employee);
                if(employee === undefined){
                    refreshTable(branchesTable, await getBranches(false));
                }
                let branchesIds = [];
                employee.workingBranches.forEach(branch => {
                    branchesIds.push('#' + branch.id);
                });
                $('#branchesTable').DataTable().rows(branchesIds).select();
            }
        });
        getRoles(user.userRoles);
    }
}

$(document).on('click', '.btn-warning', function (e) {
    const id = $(this).data('target');
    $('#btnModal').data('action', 'edit');
    $('#btnModal').data('target', id);
    inflateForm('edit', id);
});

$(document).on('click', '.btn-danger', async function (e) {
    const id = $(this).data('target');
    await deleteUser(id);
});

$(document).on('click', '.modal-trigger', function (e) {
    const action = $(this).data('action');
    const id = $(this).data('target');
    inflateForm(action);
    $('#btnModal').data('action', action);
    $('#btnModal').data('target', id);
});




function validate() {

}

async function extraValidation(valid = true) {
    let branchesIds = [];
    let selectedBranches = $('#branchesTable').DataTable().rows({ selected: true })
    selectedBranches.every(function (rowIdx, tableLoop, rowLoop) {
        const branch = this.data();
        branchesIds.push(branch.id);
    });
    let cont = 0;
    if (selectedBranches.count() === 0 && $('#role-3').prop('checked') === true) {
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
        showFeedback('form', ['Revisa el formulario']);
        return cont;
    } else {
        hideFeedback('form');
        const userId = $('#btnModal').data('target');
        const action = $('#btnModal').data('action');
        const done = await actionUser(action, userId, selectedRoles, branchesIds);
        if (done) {
            refreshTable(usersTable, await getUsers());
            alerta('success', action === 'add' ? 'Se creó el usuario.' : 'Se editó el usuario.');
            $('#modalUsersForm').modal('hide');
        }
    }
}

$(document).on('click', '#btnModal', function (e) {
    const action = $(this).data('action');
    $('#usersForm').validate().destroy();
    $('#usersForm').validate({
        invalidHandler: async function (event, validator) {
            // 'this' refers to the form
            const extra = await extraValidation(false);
            var errors = validator.numberOfInvalids() + extra;
            if (errors) {
                var message = errors == 1
                    ? 'Error en 1 campo. Revisa lo resaltado en rojo.'
                    : 'Error en ' + errors + ' campos. Han sido resaltados en rojo.';
                showFeedback('form', [message]);
            } else {
                hideFeedback('form');
            }
        },
        submitHandler: async function (form) {
            await extraValidation(true);
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
    if ($(this).hasClass('is-invalid') && $(this).prop('type') === 'email' || $(this).prop('type') !== 'email') {
        $('#usersForm').validate().element($(this));
    }
});

async function getBranches(disableEmployee = true) {
    const branches = await request({
        method: 'GET',
        endpoint: 'api/branch/all',
        alertOnError: true
    }, 'branches');
    if (branches.length === 0) {
        $('#role-3').prop('disabled', disableEmployee);
        Swal.fire({
            title: 'Sin sucursales',
            text: 'No podrás agregar empleados',
            icon: 'warning',
            showCancelButton: true,
            cancelButtonText: 'Sucursales',
            confirmButtonText: 'Continuar',
            customClass: {
                confirmButton: 'btn m-2 btn-primary',
                cancelButton: 'btn m-2 btn-secondary'
            },
            buttonsStyling: false,
            reverseButtons: true
        }).then((result) => {
            console.log(result);
            if (result.dismiss === Swal.DismissReason.cancel) {
                location.href = '/app/admin/sucursales';
            } else {
                return [];
            }
        });
    }
    $('#switch3').removeProp('disabled');
    return branches;
}

async function getUsers() {
    const users = await request({
        method: 'GET',
        endpoint: 'api/user/all',
        alertOnError: true
    },'users');
    return users;
}

async function getEmployeeInfo(id) {
    const response = await request({
        method: 'GET',
        endpoint: 'api/employee',
        urlParams: {
            by: 'userid',
            filterTo: id
        },
        fetch: 'response',
        alertOnError: true
    }, 'empleado');
    if(!response.isValid && response.status === 409){
        await Swal.fire({
            title: 'Atención',
            text: 'El usuario tiene el rol de empleado, sin embargo, no tiene sucursales asignadas.',
            icon: 'warning',
            confirmButtonText: 'Entiendo',
            customClass: {
                confirmButton: 'btn m-2 btn-primary',
            },
            buttonsStyling: false,
        });
        return;
    }
    return response.data;
}

async function getUserInfo(id) {
    const user = await request({
        method: 'GET',
        endpoint: `api/user/${id}`,
        alertOnError: true,
        fetch: 'data'
    }, 'get user');
    return user;
}

async function getRoles(userRoles = []) {
    const roles = await request({
        method: 'GET',
        endpoint: 'api/role/all',
        alertOnError: true
    },'get roles');
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
    $(`#role-3`).on('change', function (e) {
        // this.checked = !this.checked;
        $('#branchesdiv').toggleClass('no-mostrar d-none');
    });
}

async function actionUser(action, id, selectedRoles, branchesIds) {
    let data = {};
    $('form#usersForm :input').each(function () {
        const aria = $(this).attr('aria-label');
        data[aria] = $(this).val();
    });
    if (action == 'add') {
        return await addUser(data, id, selectedRoles, branchesIds);
    } else {
        return await updateUser(data, id, selectedRoles, branchesIds);
    }
}

async function addUser(data, id, selectedRoles, branchesIds) {
    const createResponse = await request({
        method: 'POST',
        endpoint: 'api/auth/register/roles',
        body: {
            username: data.username,
            name: data.name,
            lastName: data.lastName,
            address: data.address,
            phoneNumber: data.phoneNumber,
            email: data.email,
            password: data.password,
            rolesIds: selectedRoles,
            workingBranchesIds: branchesIds
        },
        fetch: 'response'
    }, 'adduser');
    if (createResponse.isValid) {
        return true;
    } else {
        if (createResponse.status === 400) {
            showObjectAlerts(createResponse.data.message, 'error');
        } else if (createResponse.status === 500 || createResponse.status === 409) {
            let errors = [];
            errors.push(createResponse.data.message);
            showAlerts(errors, 'error');
        } else {
            alerta('error', createResponse.status + '\n' + JSON.stringify(createResponse.data.message));
        }
    }
}

async function updateUser(data, id, selectedRoles, branchesIds) {
    let body = {
        username: data.username,
        name: data.name,
        lastName: data.lastName,
        address: data.address,
        phoneNumber: data.phoneNumber,
        email: data.email,
        rolesIds: selectedRoles,
        workingBranchesIds: branchesIds
    };
    if (data.password !== '') {
        body['password'] = data.password;
    }
    const updateResponse = await request({
        method: 'PUT',
        endpoint: `api/user/${id}/roles`,
        body: body,
        fetch: 'response'
    });
    if (updateResponse.isValid) {
        return true;
    } else {
        if (updateResponse.status === 400) {
            showObjectAlerts(updateResponse.message, 'error');
        } else if (updateResponse.status === 500 || updateResponse.status === 409) {
            let errors = [];
            errors.push(updateResponse.data.message);
            showAlerts(errors, 'error');
            showFeedback('form', ['Revisa los datos.']);
        } else {
            alerta('error', updateResponse.status + '\n' + JSON.stringify(updateResponse.data.message));
        }
    }

}

async function deleteUser(id) {
    const confirmed = await confirmAlert('warning', 'Eliminar usuario', '¿Estás seguro de eliminar el usuario? No se puede deshacer.', 'Sí, eliminar.');
    if (confirmed) {
        const response = await request({
            method: 'DELETE',
            endpoint: `api/user/${id}`,
            fetch: 'response'
        }, 'delete user');
        if(!response.isValid){
            alerta('error', response.data.message);
            return;
        }
        refreshTable(usersTable, await getUsers());
        alerta('success', 'Usuario eliminado.');
        return true;
    }
}

$('.password-toggler').on('click', function(){
    const input = $(this).siblings('.form-floating').find('input.form-control');
    const icon = $(this).children('.bi');
    console.log(icon)
    const type = input.prop('type');
    input.prop('type', type === 'password' ? 'text' : 'password');
    icon.toggleClass('bi-eye');
});

function showAlerts(alerts, type = 'error') {
    let html = '<div>\n'
    alerts.forEach(alert => {
        html += `<p class="${type}">${alert}</p>\n`;
    })
    html += '</div>';
    document.getElementById('alertas').innerHTML = html;
    $('#alertas').removeClass('d-none');
    document.getElementById('alertas').scrollIntoView();
}

function showObjectAlerts(alerts, type) {
    let html = '<div>\n'
    for (const alert in alerts) {
        html += `<p class="${type}">${alerts[alert]}</p>\n`;
    }
    html += '</div>';
    document.getElementById('alertas').innerHTML = html;
    $('#alertas').removeClass('d-none');
    document.getElementById('alertas').scrollIntoView();
}
