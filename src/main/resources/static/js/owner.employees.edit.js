'use strict'
'use strict'

DataTable.Buttons.defaults.dom.button.className = 'btn';

var employeesTable;

$(async function () {
    await getUserBranches();
    inflateForm();
});

function cleanForm() {
    $('input').each(function () {
        $(this).val('');
    });
    $('#alertas').addClass('no-mostrar d-none');
}

async function inflateForm() {
    cleanForm();
    const id = sessionStorage.getItem('targetEmployee');
    const employee = await getEmployeeInfo(id);
    const user = employee.user;
    const workingBranches = employee.workingBranches;
    workingBranches.forEach(branch => {
        $(`#cb${branch.id}`).prop('checked',true);
    });
    document.getElementById("user-username").value = user.username;
    document.getElementById("user-name").value = user.name
    document.getElementById("user-lastname").value = user.lastName;
    document.getElementById("user-address").value = user.address;
    document.getElementById("user-email").value = user.email;
    document.getElementById("user-phone").value = user.phoneNumber;
}

$(document).on('click', '#btnAction', async function (e) {
    const id = sessionStorage.getItem('targetEmployee');
    let data = {};
    $('form#usersForm :input').each(function () {
        const aria = $(this).attr('aria-label');
        data[aria] = $(this).val();
    });
    const employee = await getEmployeeInfo(id);
    const employeeRoles = employee.user.userRoles;
    let workingBranchesIds = new Set();
    let rolesIds = new Set();
    $('.branch-checkbox').each(function(){
        if($(this).prop('checked')){
            workingBranchesIds.add(Number($(this).text()));
        }
    });
    employeeRoles.forEach(role => {
        rolesIds.add(role.id);
    });
    rolesIds.add(3);
    const done = await updateUser(data, id, Array.from(rolesIds), Array.from(workingBranchesIds));
    if (done) {
        alerta('success', 'Se editó el usuario.');
    }
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
        checkboxes.append($('<label>',{
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

async function updateUser(data, id, rolesIds, workingBranchesIds) {
    alert(workingBranchesIds);
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
