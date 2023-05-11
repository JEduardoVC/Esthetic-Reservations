'use strict'
'use strict'

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
    let errors = [];
    $('.employee-field').each(function () {
        const aria = $(this).attr('aria-label');
        const ph = $(this).attr('placeholder');
        data[aria] = $(this).val();
        if ($(this).val() === '') {
            errors.push(`${ph} es requerido.`);
        }
    });
    if(!validPassword($('#user-password').val())){
        errors.push('La contraseña debe contener al menos 8 caracteres, un número, una mayúsucula y un símbolo especial (@#$%^&+=!)');
    }else if ($('#user-password').val() !== $('#user-cpassword').val()) {
        errors.push('Las contraseñas no coinciden');
    } 
    if(!validEmail($('#user-email').val())){
        errors.push('Introduce un email válido.');
    }
    if(!validPhoneNumber($('#user-phone').val())){
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
    let rolesIds = new Set();
    rolesIds.add(3);
    if (errors.length === 0) {
        const done = await addUser(data, Array.from(rolesIds), Array.from(workingBranchesIds));
        if (done) {
            await swal('success', 'Se creó el usuario.');
            location.href = '/app/owner/personal';
        }
    } else {
        showAlerts(errors);
    }
});

function validEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function validPassword(password){
    var re = /^(?=^.{8,}$)(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#\$%\^&\+=!]).*/;
    return re.test(password);
}

function validPhoneNumber(phoneNumber){
    let re  =/^\d{10}$/;
    return re.test(phoneNumber);
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

function showAlerts(alerts, type = 'error') {
    let html = '<div>\n'
    alerts.forEach(alert => {
        html += `<p class="${type}">${alert}</p>\n`;
    })
    html += '</div>';
    document.getElementById('alertas').innerHTML = html;
    $('#alertas').removeClass('no-mostrar d-none');
    document.getElementById('alertas').scrollIntoView();
}

function showObjectAlerts(alerts, type) {
    let html = '<div>\n'
    for (const alert in alerts) {
        html += `<p class="${type}">${alerts[alert]}</p>\n`;
    }
    html += '</div>';
    document.getElementById('alertas').innerHTML = html;
    $('#alertas').removeClass('no-mostrar d-none');
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