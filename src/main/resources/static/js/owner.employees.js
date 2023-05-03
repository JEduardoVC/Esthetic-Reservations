'use strict'

DataTable.Buttons.defaults.dom.button.className = 'btn';

var employeesTable;

$(async function () {
    const branches = await getUserBranches();
    const employees = await getBranchEmployees();
    employeesTable = $('#employeesTable').DataTable({
        paging: true,
        data: employees,
        rowId: 'user.id',
        columns: [
            { "name": "username", "data": "user.username", "targets": 0 },
            { "name": "nombre", "data": "user.name", "targets": 1 },
            { "name": "direccion", "data": "user.address", "targets": 2 },
            { "name": "correo", "data": "user.email", "targets": 3 },
            { "name": "telefono", "data": "user.phoneNumber", "targets": 4 },
            {
                "searchable": false, "orderable": false,
                "name": "acciones", "render": function (data, type, row) {
                    let rowId = Number(data.user.id);
                    return `
                            <button type="button" class="btn-update" data-target="${rowId}">Editar</button>
                            <button type="button" class="btn-remove" data-target="${rowId}">Eliminar</button>`;
                }, "data": null, "targets": [6]
            }
        ],
        language: languageMX
    });
});

$('#select-branch').on('change', async function(e){
    sessionStorage.setItem('branchId', $(this).val());
    refreshTable(employeesTable, await getBranchEmployees());
});

function refreshTable(table, data) {
    table.clear();
    table.rows.add(data).draw();
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

async function getUserBranches(){
    const branches = await request({
        method: 'GET',
        endpoint: 'api/branch/all/filter',
        urlParams:{
            filterBy: 'owner',
            filterTo: sessionStorage.getItem('userId')
        },
        alertOnError: true
    });
    const select = $('#select-branch');
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
    return employees;
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
        refreshTable(employeesTable, await getBranchEmployees());
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
