'use strict'

DataTable.Buttons.defaults.dom.button.className = 'btn';

var employeesTable;

$(async function () {
    const branches = await getUserBranches();
    await getBranchEmployees();
});

$('#select-branch').on('change', async function (e) {
    sessionStorage.setItem('branchId', $(this).val());
    await getBranchEmployees();
});

$(document).on('click', '.trigger-modal', function (e) {
    cleanForm();
    const action = $(this).data('action');
    const target = $(this).data('target');
    showModal(action, target);
});

$(document).on('click', '.btn-remove', async function (e) {
    const id = $(this).data('target');
    await deleteUser(id);
});

// $(window).on('click', function (e) {
//     if (e.target === document.getElementById('modal-new-employee')) {
//         removeModal();
//     }
// });

function cleanForm() {
    $('input').each(function () {
        $(this).val('');
    });
    $('.branch-checkbox').each(function () {
        $(this).prop('checked', false);
    });
    $('#alertas').addClass('no-mostrar d-none');
}

function refreshTable(tableId, html) {
    const table = $(`#${tableId}`);
    table.html('');
    table.html(html);
}

async function showModal(action, target) {
    await inflateForm(action, target);
    $('#modal-new-employee').removeClass('no-mostrar');
}

function removeModal() {
    $('#modal-new-employee').addClass('no-mostrar');
}

$(document).on('click', '#btn-action', async function (e) {
    const action = $(this).data('action');
    const id = $(this).data('target');
    if (validateForm(action)) {
        const data = await getData(action, id);
        if ($(this).data('action') === 'add') {
            const done = await addUser(data.values, Array.from(data.rolesIds), Array.from(data.workingBranchesIds));
            if (done) {
                await swal('success', 'Se creó el usuario.');
            }
        } else {
            const done = await updateUser(data.values, id, Array.from(data.rolesIds), Array.from(data.workingBranchesIds));
            if (done) {
                await swal('success', 'Se editó el usuario.');
            }
        }
        await getBranchEmployees();
        removeModal(action);
    }
});

async function addUser(data, rolesIds, workingBranchesIds) {
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
            rolesIds: rolesIds,
            workingBranchesIds: workingBranchesIds
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
            await swal('error', createResponse.status + '\n' + JSON.stringify(createResponse.data.message));
        }
    }
}

async function updateUser(data, id, rolesIds, workingBranchesIds) {
    let body = {
        username: data.username,
        name: data.name,
        lastName: data.lastName,
        address: data.address,
        phoneNumber: data.phoneNumber,
        email: data.email,
        rolesIds: rolesIds,
        workingBranchesIds: workingBranchesIds
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
        } else {
            alerta('error', updateResponse.status + '\n' + JSON.stringify(updateResponse.data.message));
        }
    }

}

async function getData(action, target) {
    const id = sessionStorage.getItem('targetEmployee');
    let data = {};
    $('.employee-field').each(function () {
        const aria = $(this).attr('aria-label');
        data[aria] = $(this).val();
    });
    let workingBranchesIds = new Set();
    let rolesIds = new Set();
    if (action === 'edit') {
        const employee = await getEmployeeInfo(target);
        const employeeRoles = employee.user.userRoles;
        employeeRoles.forEach(role => {
            rolesIds.add(role.id);
        });
    }
    $('.branch-checkbox').each(function () {
        if ($(this).prop('checked')) {
            workingBranchesIds.add(Number($(this).text()));
        }
    });
    rolesIds.add(3);
    let response = {};
    response['values'] = data;
    response['rolesIds'] = rolesIds;
    response['workingBranchesIds'] = workingBranchesIds;
    return response;
}

function validateForm(action) {
    let errors = [];
    $('.employee-field').each(function () {
        const aria = $(this).attr('aria-label');
        const ph = $(this).attr('placeholder');
        if ($(this).prop('type') === 'password' && action === 'edit') {
            return;
        }
        if ($(this).val() === '') {
            errors.push(`${ph} es requerido.`);
        }
    });
    if ($('#user-password').val() !== '') {
        if (!validPassword($('#user-password').val())) {
            errors.push('La contraseña debe contener al menos 8 caracteres, un número, una mayúsucula y un símbolo especial (@#$%^&+=!)');
        } else if ($('#user-password').val() !== '' && $('#user-password').val() !== $('#user-cpassword').val()) {
            errors.push('Las contraseñas no coinciden');
        }
    }
    if (!validEmail($('#user-email').val())) {
        errors.push('Introduce un email válido.');
    }
    if (!validPhoneNumber($('#user-phone').val())) {
        errors.push('El número de teléfono debe tener 10 dígitos');
    }
    let workingBranchesIds = new Set();
    $('.branch-checkbox').each(function () {
        if ($(this).prop('checked')) {
            workingBranchesIds.add(Number($(this).text()));
        }
    });
    if (workingBranchesIds.size === 0) {
        errors.push('Selecciona al menos una sucursal de trabajo.');
    }
    if (errors.length === 0) {
        return true;
    } else {
        showAlerts(errors, 'error');
        return false;
    }
}

function validEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function validPassword(password) {
    var re = /^(?=^.{8,}$)(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#\$%\^&\+=!]).*/;
    return re.test(password);
}

function validPhoneNumber(phoneNumber) {
    let re = /^\d{10}$/;
    return re.test(phoneNumber);
}

async function inflateForm(action, id = -1) {
    cleanForm();
    $('#btn-action').data('action', action);
    $('#btn-action').attr('data-action', action);
    $('#btn-action').data('target', id);
    $('#btn-action').attr('data-target', id);
    if (action === 'add') {
        $('#modal-header').html('Agregar personal');
    }
    if (action === 'edit') {
        $('#modal-header').html('Editar personal');
        const employee = await getEmployeeInfo(id);
        const user = employee.user;
        const workingBranches = employee.workingBranches;
        workingBranches.forEach(branch => {
            $(`#cb${branch.id}`).prop('checked', true);
        });
        document.getElementById("user-username").value = user.username;
        document.getElementById("user-name").value = user.name
        document.getElementById("user-lastname").value = user.lastName;
        document.getElementById("user-address").value = user.address;
        document.getElementById("user-email").value = user.email;
        document.getElementById("user-phone").value = user.phoneNumber;
    }
}

async function getUserBranches() {
    const branches = await request({
        method: 'GET',
        endpoint: 'api/branch/all/filter',
        urlParams: {
            filterBy: 'owner',
            filterTo: sessionStorage.getItem('userId')
        },
        alertOnError: true
    });
    const select = $('#select-branch');
    const checkboxes = $('#branches-checkboxes');
    select.append($('<option>', {
        value: '',
        text: 'Seleccionar sucursal'
    }))
    $(branches).each(function () {
        const branch = this;
        select.append($('<option>', {
            value: branch.id,
            text: branch.branchName
        }))
        checkboxes.append($('<input>', {
            type: 'checkbox',
            text: branch.id,
            class: 'branch-checkbox',
            id: `cb${branch.id}`,
            value: branch.id,
        }))
        checkboxes.append($('<label>', {
            for: `cb${branch.id}`,
            text: branch.branchName
        }))

    });
    select.val(sessionStorage.getItem('branchId'));
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
    if (!response.isValid && response.status === 409) {
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

async function getBranchEmployees() {
    const id = $('#select-branch').val();
    const employees = await request({
        method: 'GET',
        endpoint: 'api/employee/all',
        urlParams: {
            by: 'branch',
            filterTo: id
        }
    });
    let html = '';
    employees.forEach(employee => {
        html += `<div id="show-employee" class="employee">
                    <div class="center"><p>${employee.user.name} ${employee.user.lastName}</p></div>
                    <div class="center"><p>${employee.user.phoneNumber}</p></div>
                    <div class="center"><p>${employee.user.email}</p></div>
                    <div class="actions-employees">
                        <button type="button" class="btn-remove" data-target="${employee.user.id}">Eliminar</button> 
                        <button type="button" class="btn-update trigger-modal" data-action="edit" data-target="${employee.user.id}">Editar</button> 
                    </div>
                </div>`;
    });
    if (html === '') {
        html += `<div id="show-employee" class="employee">
                    <div class="center"><p>No tienes empleados en esta sucursal</p></div>
                </div>`;
    }
    refreshTable('info-employees', html);
}

async function deleteUser(id) {
    const confirmed = await confirmAlert('warning', 'Eliminar usuario', '¿Estás seguro de eliminar el usuario? No se puede deshacer.', 'Sí, eliminar.');
    if (confirmed) {
        const response = await request({
            method: 'DELETE',
            endpoint: `api/user/${id}`,
            fetch: 'response'
        }, 'delete user');
        if (!response.isValid) {
            alerta('error', response.data.message);
            return;
        }
        await getBranchEmployees();
        alerta('success', 'Usuario eliminado.');
        return true;
    }
}

function showAlerts(alerts, type = 'error') {
    let html = '<div>\n'
    alerts.forEach(alert => {
        html += `<p class="${type}">${alert}</p>\n`;
    })
    html += '</div>';
    document.getElementById('alertas').innerHTML = html;
    document.getElementById('alertas').scrollIntoView();
    $('#alertas').removeClass('no-mostrar d-none');
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

async function swal(tipo, message, title = '') {
    let icon = tipo;
    if (title === '') {
        switch (tipo) {
            case 'error':
                title = 'Oops...';
                break;
            case 'warning':
                title = 'Cuidado';
                break;
            case 'info':
                title = 'Atención';
                break;
            case 'question':
                title = '¿Seguro?';
                break;
            default:
                title = 'Correcto';
                icon = 'success';
                break;
        }
    }
    await Swal.fire({
        icon: icon,
        title: title,
        text: message
    })
}