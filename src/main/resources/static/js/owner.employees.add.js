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
}

$(document).on('click', '#btnAction', async function (e) {
    let data = {};
    $('form#usersForm :input').each(function () {
        const aria = $(this).attr('aria-label');
        data[aria] = $(this).val();
    });
    let workingBranchesIds = new Set();
    $('.branch-checkbox').each(function(){
        if($(this).prop('checked')){
            workingBranchesIds.add(Number($(this).text()));
        }
    });
    let rolesIds = new Set();
    rolesIds.add(3);
    const done = await addUser(data, Array.from(rolesIds), Array.from(workingBranchesIds));
    if (done) {
        alerta('success', 'Se creó el usuario.');
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
            alerta('error', createResponse.status + '\n' + JSON.stringify(createResponse.data.message));
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
