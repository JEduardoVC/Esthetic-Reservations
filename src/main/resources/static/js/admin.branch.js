'use strict'
let branches = [];
let table = $('#branchesTable').DataTable({
    paging: true,
    language: {
        url: '//cdn.datatables.net/plug-ins/1.13.1/i18n/es-ES.json'
    }
});

window.onload = inflate();

$(function () {
    $('.timepicker').timepicker({
        'step': 15,
        'timeFormat': 'h:i A',
        'minTime': '0:00am',
        'maxTime': '11:45pm',
        'scrollDefault': '10:00am',
        'disableTextInput': true,
        'disableTouchKeyboard': true
    });
});


$('#branch-open').on('changeTime', function () {
    $('#branch-close').timepicker('option', 'minTime', $(this).val());
    if ($(this).val() !== '12:00 AM') {
        $('#branch-close').timepicker('option', 'maxTime', '12:00 AM');
    } else {
        $('#branch-close').timepicker('option', 'maxTime', '11:45 PM');
    }
});

$('#branch-close').on('changeTime', function () {
    $('#branch-open').timepicker('option', 'maxTime', $(this).val());
    if ($(this).val() === '12:00 AM') {
        $('#branch-open').timepicker('option', 'minTime', '12:00 AM');
        $('#branch-open').timepicker('option', 'maxTime', '11:45 PM');
    }
});

function inflate() {
    loadBranches();
}

function loadBranches() {
    table.clear();
    const url = BASE_URL + 'api/branch/all';
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
                    alerta('warning', 'Sin sucursales.');
                } else {
                    alerta('error', JSON.stringify(data));
                }
            } else {
                branches = data.content;
                const html = branches.map(branch => {
                    return `<tr>
                        <td class="text-dark">${branch.id}</td>
                        <td class="text-dark">${branch.branchName}</td>
                        <td class="text-dark">${branch.location + ', ' + branch.municipality + ', ' + branch.state}</td>
                        <td class="text-dark">${branch.owner.name + ' (' + branch.owner.id + ')'}</td>
                        <td class="text-dark">${branch.scheduleOpen}</td>
                        <td class="text-dark">${branch.scheduleClose}</td>
                        <td>
                            <button id="deletebranch${branch.id}" class="btn btn-outline-danger" onclick="deleteBranch(${branch.id});"><i class="bi bi-trash3-fill"></i>Eliminar</button>
                            <button id="editbranch${branch.id}" class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#modalBranchesForm" onclick="GetInfo('edit',${branch.id});"><i class="bi bi-pencil-square"></i>Editar</button>
                        </td>
                    </tr>`
                }).join('');
                table.rows.add($(html)).draw();
            }
        })
}

async function deleteBranch(id) {
    const confirmed = await confirmAlert('warning', 'Eliminar sucursal', '¿Estás seguro de eliminar la sucursal? No se puede deshacer.', 'Sí, eliminar.');
    if (confirmed) {
        actionDeleteBranch(id);
    }
}

async function actionDeleteBranch(id) {
    const response = await deleteBranchRequest(id);
    if (!isValidResponse(response)) {
        alerta('error', response.data.message);
        return;
    }
    alerta('success', 'Sucursal eliminada.');
    loadBranches();
}


async function deleteBranchRequest(id) {
    const url = BASE_URL + `api/branch/${id}`;
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

function GetInfo(action, id) {
    let ownerId = 0;
    let branchId = 0;
    document.getElementById('alertas').innerHTML = '';
    if (action == 'add') {
        document.getElementById("branch-name").value = '';
        document.getElementById("branch-address").value = '';
        document.getElementById("branch-state").value = '';
        document.getElementById("branch-municipality").value = '';
        document.getElementById("branch-open").value = '';
        document.getElementById("branch-close").value = '';
        getUsers();
    } else {
        const url = BASE_URL + `api/branch/${id}`;
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
            .then(async branch => {
                if (typeof branch.errorCode !== 'undefined') {
                    if (branch.errorCode == 404) {
                        alerta('error', 'No existe esa sucursal.');
                    }
                } else {
                    ownerId = branch.owner.id;
                    branchId = branch.id;
                    document.getElementById("branch-name").value = branch.branchName;
                    document.getElementById("branch-address").value = branch.location;
                    document.getElementById("branch-state").value = branch.state;
                    document.getElementById("branch-municipality").value = branch.municipality;
                    await getUsers(branch.owner.id);
                    setTpDate(branch.scheduleOpen, 'branch-open');
                    setTpDate(branch.scheduleClose, 'branch-close');
                    // document.getElementById("branch-open").value = $('#branch-open').timepicker('getTime', new Date()).toString().slice(16, 24);
                    // document.getElementById("branch-close").value = $('#branch-close').timepicker('getTime', new Date()).toString().slice(16, 24);
                }
            })
    }
    document.getElementById("btnModal").onclick = function () {
        actionBranch(action, branchId, ownerId);
    };
}

async function getUsers(ownerId = -1) {
    $("#select-owner").empty();
    const response = await getUsersRequest();
    if (!isValidResponse(response)) {
        if (response.status === 404) {
            alerta('error', 'No hay ningún dueño.');
        } else {
            alerta('error', JSON.stringify(response.data));
        }
        $('#modalBranchesForm').modal('hide');
        return;
    }

    const users = response.data.content;
    const select = document.getElementById('select-owner');

    if (ownerId == -1) {
        let opt = document.createElement('option');
        opt.selected = true;
        opt.value = -1;
        opt.innerHTML = 'Selecciona un usuario';
        select.appendChild(opt);
    }

    users.forEach(user => {
        let opt = document.createElement('option');
        opt.selected = user.id == ownerId;
        opt.value = user.id;
        opt.innerHTML = user.username;
        select.appendChild(opt);
    });

}

async function getUsersRequest() {
    const url = BASE_URL + 'api/user/all?';
    const response = await fetch(url + new URLSearchParams({
        'by': 'role',
        'filterTo': 'OWNER'
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
    };
}

function actionBranch(action, id, ownerId = -1) {
    let errors = [];
    let error = false;
    const name = document.getElementById("branch-name").value;
    const address = document.getElementById("branch-address").value;
    const estate = document.getElementById("branch-state").value;
    const municipality = document.getElementById("branch-municipality").value;
    ownerId = document.getElementById("select-owner").value;
    if (name == '') {
        errors.push('Debes introducir el nombre de la sucursal');
        error = true;
    }
    if (address == 0) {
        errors.push('Debes introducir la dirección');
        error = true;
    }
    if (estate == '') {
        errors.push('Debes introducir el estado');
        error = true;
    }
    if (municipality == '') {
        errors.push('Debes introducir la ciudad');
        error = true;
    }
    if (ownerId == -1) {
        errors.push('Debes seleccionar el dueño');
        error = true;
    }
    let open = '';
    let close = '';
    if ($('#branch-open').val() == '') {
        errors.push('Debes introducir la hora de apertura');
        error = true;
    } else {
        open = $('#branch-open').timepicker('getTime', new Date()).toString().slice(16, 24);
    }
    if ($('#branch-close').val() == '') {
        errors.push('Debes introducir la hora de cierre');
        error = true;
    } else {
        close = $('#branch-close').timepicker('getTime', new Date()).toString().slice(16, 24);
    }
    if (error) {
        showAlerts(errors);
    } else if (action == 'add') {
        AddBranch(name, address, estate, municipality, open, close, ownerId);
    } else {
        UpdateBranch(name, address, estate, municipality, open, close, id, ownerId);
    }
}

async function AddBranch(name, address, branchEstate, branchMunicipality, branchOpen, branchClose, ownerId) {
    const addResponse = await addBranchRequest(name, address, branchEstate, branchMunicipality, branchOpen, branchClose, ownerId);
    if (isValidResponse(addResponse)) {
        alerta('success', 'Se creó la sucursal');
        $('#modalBranchesForm').modal('hide');
        loadBranches();
    } else {
        if (addResponse.errorCode === 400) {
            showObjectAlerts(addResponse.message, 'error');
        } else if (addResponse.errorCode === 500 || addResponse.errorCode === 409) {
            let errors = [];
            errors.push(addResponse.message);
            showAlerts(errors, 'error');
        } else {
            alerta('error', addResponse.errorCode + '\n' + JSON.stringify(addResponse.message));
        }
    }
}

async function addBranchRequest(name, address, branchEstate, branchMunicipality, branchOpen, branchClose, ownerId) {
    const url = BASE_URL + `api/branch`;
    const response = await fetch(url + new URLSearchParams({

    }), {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: JSON.stringify({
            branchName: name,
            location: address,
            state: branchEstate,
            ownerId: ownerId,
            municipality: branchMunicipality,
            scheduleOpen: branchOpen,
            scheduleClose: branchClose
        })
    });

    const json = await response.json();
    return {
        'data': json,
        'status': response.status
    };
}

async function UpdateBranch(name, address, branchEstate, branchMunicipality, branchOpen, branchClose, id, curOwnerId) {
    const updateResponse = await updateBranchRequest(name, address, branchEstate, branchMunicipality, branchOpen, branchClose, id, curOwnerId);
    if (isValidResponse(updateResponse)) {
        alerta('success', 'Se modificó la sucursal');
        $('#modalBranchesForm').modal('hide');
        loadBranches();
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

async function updateBranchRequest(name, address, branchEstate, branchMunicipality, branchOpen, branchClose, id, curOwnerId) {
    const url = BASE_URL + `api/branch/${id}`;
    const response = await fetch(url + new URLSearchParams({

    }), {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: JSON.stringify({
            branchName: name,
            location: address,
            state: branchEstate,
            ownerId: curOwnerId,
            municipality: branchMunicipality,
            scheduleOpen: branchOpen,
            scheduleClose: branchClose
        })
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

function strToDate(str) {
    const times = str.split(':');
    let date = new Date();
    date.setHours(times[0]);
    date.setMinutes(times[1]);
    date.setSeconds(times[2]);
    return Promise.resolve(date);
}

async function setTpDate(str, tpId) {
    strToDate(str).then(date => {
        $('#' + tpId).timepicker('setTime', date);
        if (tpId == 'branch-close') {
            $('#branch-close').timepicker('option', 'minTime', $('#branch-open').val());
        } else {
            if ($('#' + tpId).val() !== '12:00 AM') {
                $('#branch-close').timepicker('option', 'maxTime', '12:00 AM');
            }
        }
    })
}