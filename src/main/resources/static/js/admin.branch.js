var branches = [];
let table = $('#branchesTable').DataTable({
    paging: true,
    language: {
        url: '//cdn.datatables.net/plug-ins/1.13.1/i18n/es-ES.json'
    }
});

window.onload = loadBranches();

function loadBranches() {
    table.clear();
    var url = BASE_URL + 'api/branch/all';
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
                    alert('Sin sucursales.');
                }
            } else {
                branches = data.content;
                const html = branches.map(branch => {
                    return `<tr>
                        <td class="text-dark">${branch.id}</td>
                        <td class="text-dark">${branch.branchName}</td>
                        <td class="text-dark">${branch.location + ', ' + branch.state + ', ' + branch.municipality}</td>
                        <td class="text-dark">${branch.owner.name + ' (' + branch.owner.id + ')'}</td>
                        <td class="text-dark">${branch.scheduleOpen}</td>
                        <td class="text-dark">${branch.scheduleClose}</td>
                        <td>
                            <button id="deletebranch${branch.id}" class="btn btn-outline-danger fs-4" onclick="deleteBranch(${branch.id});">Eliminar</button>
                            <button id="editbranch${branch.id}" class="btn btn-warning fs-4" data-bs-toggle="modal" data-bs-target="#modalBranchesForm" onclick="GetInfo('edit',${branch.id});">Editar</button>
                        </td>
                    </tr>`
                }).join('');
                table.rows.add($(html)).draw();
            }
        })
}

function deleteBranch(id) {
    if (confirm('¿Seguro que desea eliminar la sucursal ' + id + '?')) {
        actionDeleteBranch(id);
    } else {
        location.href = '/app/admin/sucursales';
    }
}

function actionDeleteBranch(id) {
    var url = BASE_URL + `api/branch/${id}`;
    fetch(url + new URLSearchParams({

    }), {
        method: 'DELETE',
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
                    alert('No existe esa sucursal.');
                }
            } else {
                alert(data.message);
                loadBranches();
            }
        })
}

function GetInfo(action, id) {
    let ownerId = 0;
    let branchId = 0;
    if (action == 'add') {
        document.getElementById("branch-name").value = '';
        document.getElementById("branch-address").value = '';
        document.getElementById("branch-state").value = '';
        document.getElementById("branch-municipality").value = '';
        document.getElementById("branch-open").value = '';
        document.getElementById("branch-close").value = '';
        getUsers();
    } else {
        var url = BASE_URL + `api/branch/${id}`;
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
            .then(branch => {
                if (typeof branch.errorCode !== 'undefined') {
                    if (branch.errorCode == 404) {
                        alert('No existe esa sucursal.');
                    }
                } else {
                    ownerId = branch.owner.id;
                    branchId = branch.id;
                    document.getElementById("branch-name").value = branch.branchName;
                    document.getElementById("branch-address").value = branch.location;
                    document.getElementById("branch-state").value = branch.state;
                    document.getElementById("branch-municipality").value = branch.municipality;
                    document.getElementById("branch-open").value = branch.scheduleOpen;
                    document.getElementById("branch-close").value = branch.scheduleClose;
                    getUsers(branch.owner.id);
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
        alert(JSON.stringify(response));
        return;
    }
    const users = response.content;
    const select = document.getElementById('select-owner');

    if (ownerId == -1) {
        var opt = document.createElement('option');
        opt.selected = true;
        opt.value = -1;
        opt.innerHTML = 'Selecciona un usuario';
        opt.classList.add('fs-4')
        select.appendChild(opt);
    }

    users.forEach(user => {
        var opt = document.createElement('option');
        opt.selected = user.id == ownerId;
        opt.value = user.id;
        opt.innerHTML = user.username;
        opt.classList.add('fs-4')
        select.appendChild(opt);
    });

}

async function getUsersRequest() {
    var url = BASE_URL + 'api/user/all';
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
    return json;
}
function actionBranch(action, id, ownerId = -1) {
    const name = document.getElementById("branch-name").value;
    const address = document.getElementById("branch-address").value;
    const estate = document.getElementById("branch-state").value;
    const municipality = document.getElementById("branch-municipality").value;
    const open = document.getElementById("branch-open").value;
    const close = document.getElementById("branch-close").value;
    ownerId = document.getElementById("select-owner").value;
    var error = false;
    var errors = '';
    if (name == '') {
        errors += 'Debes introducir el nombre de la sucursal\n';
        error = true;
    }
    if (address == 0) {
        errors += 'Debes introducir la dirección\n';
        error = true;
    }
    if (estate == '') {
        errors += 'Debes introducir el estado\n';
        error = true;
    }
    if (municipality == '') {
        errors += 'Debes introducir la ciudad\n';
        error = true;
    }
    if (open == '') {
        errors += 'Debes introducir la hora de apertura\n';
        error = true;
    }
    if (close == '') {
        errors += 'Debes introducir la hora de cierre\n';
        error = true;
    }
    if (ownerId == -1) {
        errors += 'Debes seleccionar el dueño\n';
        error = true;
    }
    if (error) {
        alert(errors);
    } else if (action == 'add') {
        AddBranch(name, address, estate, municipality, open, close, ownerId);
    } else {
        UpdateBranch(name, address, estate, municipality, open, close, id, ownerId);
    }
}

function UpdateBranch(name, address, branchEstate, branchMunicipality, branchOpen, branchClose, id, curOwnerId) {
    var url = BASE_URL + `api/branch/${id}`;
    fetch(url + new URLSearchParams({

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
    })
        .then((response) => response.json())
        .then(branch => {
            if (typeof branch.errorCode !== 'undefined') {
                if (branch.errorCode == 404) {
                    alert('No existe esa sucursal.');
                }
            } else {
                alert('Se modificó la sucursal');
                $('#modalBranchesForm').modal('hide');
                loadBranches();
            }
        })
}

function AddBranch(name, address, branchEstate, branchMunicipality, branchOpen, branchClose, ownerId) {
    var url = BASE_URL + `api/branch`;
    fetch(url + new URLSearchParams({

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
    })
        .then((response) => response.json())
        .then(branch => {
            if (typeof branch.errorCode !== 'undefined') {
                if (branch.errorCode == 404) {
                    alert(JSON.stringify(branch));
                }
            } else {
                alert('Se creó la sucursal');
                $('#modalBranchesForm').modal('hide');
                loadBranches();
            }
        })
}

function isValidResponse(response) {
    return typeof response.errorCode === 'undefined';
}
