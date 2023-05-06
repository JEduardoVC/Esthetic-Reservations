'use strict'

DataTable.Buttons.defaults.dom.button.className = 'btn';

var employeesTable;

$(async function () {
    const branches = await getUserBranches();
    const employees = await getBranchEmployees();
    refreshTable('info-employees', employees);
});

$('#select-branch').on('change', async function (e) {
    sessionStorage.setItem('branchId', $(this).val());
    refreshTable('info-employees', await getBranchEmployees());
});

function refreshTable(tableId, html) {
    const table = $(`#${tableId}`);
    table.html('');
    table.html(html);
}

async function inflateForm(action, id = -1) {
    cleanForm();
    refreshTable(branchesTable, await getBranches());
    if (action === 'add') {
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
                if (employee === undefined) {
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

$(document).on('click', '#add-employee', function (e) {
    location.href = `/app/owner/personal/agregar`;
});

$(document).on('click', '.btn-update', function (e) {
    const id = $(this).data('target');
    sessionStorage.setItem('targetEmployee', id);
    location.href = `/app/owner/personal/editar`;
});

$(document).on('click', '.btn-remove', async function (e) {
    const id = $(this).data('target');
    await deleteUser(id);
});

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
    select.append($('<option>', {
        disabled: 'disabled',
        value: '',
        text: '--Seleccione--'
    }))
    $(branches).each(function () {
        const branch = this;
        select.append($('<option>', {
            value: branch.id,
            text: branch.branchName
        }))
    });
    select.val(sessionStorage.getItem('branchId'));
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
                        <button type="button" class="btn-update" data-target="${employee.user.id}">Editar</button> 
                    </div>
                </div>`;
    });
    if(html === ''){
        html += `<div id="show-employee" class="employee">
                    <div class="center"><p>No tienes empleados en esta sucursal</p></div>
                </div>`;
    }
    return html;
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
        refreshTable('info-employees', await getBranchEmployees());
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
