'use strict'

DataTable.Buttons.defaults.dom.button.className = 'btn';

var employeesTable;
var branchesTable;

$(async function () {
    const branches = await getBranches();
    branchesTable = $('#branchesTable').DataTable({
        paging: true,
        data: branches,
        columns: [
            { "name": "id", "data": "id", "targets": 0 },
            { "name": "nombre", "data": "branchName", "targets": 1 },
            { "name": "direccion", "data": "location", "targets": 2 },
            {
                "name": "dueño", "render": function (data, type, row) {
                    return `${data.owner.name} ${data.owner.lastName} (${data.owner.id})`;
                }, 'data': null, "targets": 3
            },
            { "name": "apertura", "data": "scheduleOpen", "targets": 4 },
            { "name": "cierre", "data": "scheduleClose", "targets": 5 },
            {
                "searchable": false, "orderable": false,
                "name": "acciones", "render": function (data, type, row) {
                    let rowId = Number(data.id);
                    return `
                            <button type="button" class="btn btn-warning rounded-pill modal-trigger" data-bs-toggle="modal" data-bs-target="#modalBranchesForm" data-action="edit" data-target="${rowId}"><span class="bi bi-pencil"></span></button>
                            <button type="button" class="btn btn-danger rounded-pill" data-target="${rowId}"><span class="bi bi-trash text-white"></span></button>`;
                }, "data": null, "targets": 6
            }
        ],
        language: languageMX
    });

    let lang = languageMX;
    lang.select = {
        rows: {
            _: 'Seleccionados %d empleados.',
            0: 'Selecciona un empleado dando clic.',
            1: 'Un empleado seleccionado.'
        },
        cols: {

        }
    }
    employeesTable = $('#employeesTable').DataTable({
        paging: true,
        data: [],
        select: {
            style: 'multi'
        },
        dom: 'iBlfprtlp',
        rowId: 'id',
        columns: [
            { "name": "id", "data": "user.id", "targets": 0 },
            {
                "name": "nombre", "render": function (data, type, row) {
                    return `${data.user.name} ${data.user.lastName}`;
                }, "data": null, "targets": 1
            },
            { "name": "username", "data": "user.username", "targets": 2 },
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
    employeesTable.on('buttons-action', function (e, buttonApi, dataTable, node, config) {
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

$(document).on('click', '.btn-danger', async function (e) {
    const id = $(this).data('target');
    await deleteBranch(id);
});

$(document).on('click', '.modal-trigger', function (e) {
    const action = $(this).data('action');
    const id = $(this).data('target');
    inflateForm(action, id !== undefined ? id : -1);
    $('#btnModal').data('action', action);
    $('#btnModal').data('target', id);
});

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
        $(this).removeClass('is-invalid is-valid');
        hideFeedback($(this).attr('aria-label'));
    });
    $('select').each(function () {
        $(this).val('');
        $(this).removeClass('is-invalid is-valid');
        hideFeedback($(this).attr('aria-label'));
    });
    // $('#branchesdiv').addClass('no-mostrar d-none');
    $('#employeesTable').DataTable().rows().deselect();
    $('#alertas').addClass('no-mostrar d-none');
    $('#select-owner option').remove();
    // hideFeedback('roles');
    hideFeedback('form');
}

function refreshTable(table, data) {
    table.clear();
    table.rows.add(data).draw(true);
}

async function inflateForm(action, id = -1) {
    cleanForm();
    const owners = await getOwners();
    const select = $('#select-owner');
    if (id == -1) {
        select.append($('<option>', {
            value: '',
            text: 'Selecciona un dueño.'
        }))
    }
    $(owners).each(function () {
        let owner = this;
        select.append($('<option>', {
            value: owner.id,
            text: owner.username
        }))
    });
    refreshTable(employeesTable, await getAllEmployees());
    if (action === 'add') {
    }
    if (action === 'edit') {
        const branch = await getBranch(id);
        const employees = await getBranchEmployees(branch.id);
        let employeesIds = [];
        $(employees).each(function () {
            employeesIds.push('#' + this.id);
        });
        employeesTable.rows(employeesIds).select();
        const ownerId = branch.owner.id;
        document.getElementById("branch-name").value = branch.branchName;
        document.getElementById("branch-location").value = branch.location;
        document.getElementById("branch-state").value = branch.state;
        document.getElementById("branch-municipality").value = branch.municipality;
        $('#select-owner option').each(function () {
            if (Number($(this).val()) === ownerId) {
                select.val($(this).val());
                return;
            }
        });
        setTpDate(branch.scheduleOpen, 'branch-open');
        setTpDate(branch.scheduleClose, 'branch-close');
    }
}

function validate() {

}

async function extraValidation(valid = true) {
    const selectedEmployees = employeesTable.rows({ selected: true });
    let employeesIds = [];
    selectedEmployees.every(function (rowIdx, tableLoop, rowLoop) {
        const employee = this.data();
        employeesIds.push(employee.id);
    });
    let cont = 0;
    if (!valid) {
        $('#formFeedback').html('Revisa el formulario.');
        showFeedback('form', ['Revisa el formulario']);
        return cont;
    } else {
        hideFeedback('form');
        const branchId = $('#btnModal').data('target');
        const action = $('#btnModal').data('action');
        const done = await actionBranch(action, branchId, employeesIds);
        if (done) {
            refreshTable(branchesTable, await getBranches());
            alerta('success', action === 'add' ? 'Se creó la sucursal.' : 'Se editó la sucursal.');
            $('#modalBranchesForm').modal('hide');
        }
    }
}

$(document).on('click', '#btnModal', function (e) {
    $('#branchesForm').validate().destroy();
    $('#branchesForm').validate({
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
            'branch-name': 'required',
            'select-owner': 'required',
            'branch-location': 'required',
            'branch-municipality': 'required',
            'branch-state': 'required',
            'branch-open': 'required',
            'branch-close': 'required'
        },
        messages: {
            'branch-name': 'Ingresa el nombre de la sucursal',
            'select-owner': 'Selecciona el dueño de la sucursal',
            'branch-location': 'Ingresa la ubicación de la sucursal',
            'branch-municipality': 'Ingresa el municipio',
            'branch-state': 'Ingresa el estado',
            'branch-open': 'Selecciona la hora de apertura',
            'branch-close': 'Selecciona la hora de cierre'

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

async function getBranches() {
    const branches = await request({
        method: 'GET',
        endpoint: 'api/branch/all',
        alertOnError: true,
    });
    return branches;
}

async function getBranch(id) {
    const branches = await request({
        method: 'GET',
        endpoint: `api/branch/${id}`,
        alertOnError: true,
        fetch: 'data'
    });
    return branches;
}

async function getOwners() {
    const owners = await request({
        method: 'GET',
        endpoint: 'api/user/all',
        urlParams: {
            by: 'role',
            filterTo: 'OWNER'
        },
        alertOnError: true,
    });
    if (owners.length === 0) {
        alert('no hay dueños');
    }
    return owners;
}

async function getAllEmployees() {
    const employees = await request({
        method: 'GET',
        endpoint: 'api/employee/all',
        alertOnError: true
    });
    return employees;
}

async function getBranchEmployees(id) {
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

async function actionBranch(action, id, selectedEmployees) {
    let data = {};
    $('form#branchesForm :input').each(function () {
        const aria = $(this).attr('aria-label');
        data[aria] = $(this).val();
    });
    data['scheduleOpen'] = $('#branch-open').timepicker('getTime', new Date()).toString().slice(16, 24);
    data['scheduleClose'] = $('#branch-close').timepicker('getTime', new Date()).toString().slice(16, 24);
    data['ownerId'] = $('#select-owner').val();
    data['employees'] = selectedEmployees;
    if (action === 'add') {
        return addBranch(data);
    } else {
        return updateBranch(id, data);
    }
}

async function addBranch(data) {
    let body = {
        branchName: data.branchName,
        location: data.location,
        state: data.state,
        ownerId: data.ownerId,
        municipality: data.municipality,
        scheduleOpen: data.scheduleOpen,
        scheduleClose: data.scheduleClose
    }
    if (data.employees.length > 0) {
        body['employeesIds'] = data.employees;
    }
    const addResponse = await request({
        method: 'POST',
        endpoint: 'api/branch',
        body: body,
        alertOnError: false,
        fetch: 'response'
    });
    if (addResponse.isValid === true) {
        return true;
    } else {
        if (addResponse.status === 400) {
            showObjectAlerts(addResponse.data.message, 'error');
        } else if (addResponse.status === 500 || addResponse.status === 409) {
            let errors = [];
            errors.push(addResponse.data.message);
            showAlerts(errors, 'error');
        } else {
            alerta('error', addResponse.status + '\n' + JSON.stringify(addResponse.data.message));
        }
    }
    return false;
}

async function updateBranch(id, data) {
    let body = {
        branchName: data.branchName,
        location: data.location,
        state: data.state,
        ownerId: data.ownerId,
        municipality: data.municipality,
        scheduleOpen: data.scheduleOpen,
        scheduleClose: data.scheduleClose,
        employeesIds: data.employees
    }
    const addResponse = await request({
        method: 'PUT',
        endpoint: 'api/branch/' + id,
        body: body,
        alertOnError: false,
        fetch: 'response'
    });
    if (addResponse.isValid === true) {
        return true;
    } else {
        if (addResponse.status === 400) {
            showObjectAlerts(addResponse.data.message, 'error');
        } else if (addResponse.status === 500 || addResponse.status === 409) {
            let errors = [];
            errors.push(addResponse.data.message);
            showAlerts(errors, 'error');
        } else {
            alerta('error', addResponse.status + '\n' + JSON.stringify(addResponse.data.message));
        }
    }
    return false;
}

async function deleteBranch(id) {
    const confirmed = await confirmAlert('warning', 'Eliminar sucursal', '¿Estás seguro de eliminar la sucursal? No se puede deshacer.', 'Sí, eliminar.');
    if (confirmed) {
        const response = await request({
            method: 'DELETE',
            endpoint: `api/branch/${id}`,
            fetch: 'response'
        });
        if(!response.isValid){
            alerta('error', response.data.message);
            return;
        }
        refreshTable(branchesTable, await getBranches());
        alerta('success', 'Sucursal eliminada.');
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