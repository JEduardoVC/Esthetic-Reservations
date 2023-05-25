$(async function () {
    await getAllProducts();
});

$(document).on('click', '.trigger-modal', function (e) {
    cleanForm();
    const action = $(this).data('action');
    const target = $(this).data('target');
    showModal(action, target);
});

async function inflateModal(action, target) {
    let product = {
        id: -1,
        inventory_name: '',
        imagen: '',
        description: '',
        capacibility: 0,
        price: 0,
        store: 0
    };
    if (action === 'edit') {
        product = await getProduct(target);
    }
    $('#modal-header').html(`<h1>${action === 'edit' ? 'Editar' : 'Nuevo'} producto</h1>`);
    $('#modal-content').html(`<form id="inventory-form" method="post">
                                <div class="field">
                                    <label class="filter" for="name">Nombre</label>
                                    <input class="inventory-field" type="text" name="name" id="name" aria-label="inventory_name" placeholder="Nombre del producto" value="${product.inventory_name}">
                                </div>
                                <div class="field">
                                    <label class="filter" for="img">${action === 'edit' ? 'Actualizar ' : ''} Imagen</label>
                                    <input class="filter inventory-field" type="file" name="img" id="img" aria-label="imagen" placeholder="Imagen del producto" accept="image/*">
                                </div>
                                <div class="field">
                                    <label class="filter" for="price">Precio</label>
                                    <input class="inventory-field" type="number" name="price" id="price" min="0" aria-label="price" placeholder="Precio del producto" value="${product.price}">
                                </div>
                                <div class="field">
                                    <label class="filter" for="units">Unidades</label>
                                    <input class="inventory-field" type="number" name="units" id="store" min="0" aria-label="store" placeholder="Cantidad del producto" value="${product.store}">
                                </div>
                                <div class="field">
                                    <label class="filter" for="description">Descripción</label>
                                    <textarea class="inventory-field" name="description" id="description" aria-label="description" placeholder="Descripción del producto" value="${product.description}"></textarea>
                                </div>
                                <div class="field">
                                    <label class="filter" for="capacibility">Capacidad</label>
                                    <input class="inventory-field" type="text" name="capacibility" id="capacibility" aria-label="capacibility" placeholder="Capacidad del producto" value="${product.capacibility}">
                                </div>
                                <button id="btn-action" class="btn-principal" data-action="${action}" data-target="${product.id}" type="button">${action === 'edit' ? 'Actualizar' : 'Añadir'}</button>
                            </form>`);
}

$(document).on('click', '#btn-action', async function (e) {
    if (validateForm($(this).data('action'))) {
        if ($(this).data('action') === 'add') {
            await agregarInventory();
        } else {
            await actualizarInventory($(this).data('target'));
        }
        await getAllProducts();
        removeModal();
    }
});

async function agregarInventory() {
    const nombre = document.querySelector("#name").value;
    const precio = document.querySelector("#price").value;
    const store = document.querySelector("#store").value;
    const description = document.querySelector("#description").value;
    const capacibility = document.querySelector("#capacibility").value;
    const imagen = document.querySelector("#img");
    var formdata = new FormData();
    formdata.append("inventory_name", nombre);
    formdata.append("price", precio);
    formdata.append("store", store);
    formdata.append("description", description);
    formdata.append("capacibility", capacibility);
    formdata.append("imagen", null);
    formdata.append("file", imagen.files[0]);
    formdata.append("id_branch", sessionStorage.getItem("branchId"));
    await fetch(`${BASE_URL}api/owner/inventario/agregar`, {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: formdata,
        redirect: 'follow'
    })
        .then(response => response.json())
        .then(data => {
            alerta('success', 'Se creó el producto.', 'Éxito');
        })
}

function cleanForm() {
    $('input').each(function () {
        if ($(this).prop('type') === 'number') {
            $(this).val('0');
        } else {
            $(this).val('');
        }
    });
    $('#alertas').addClass('no-mostrar d-none');
}


function validateForm(action) {
    let errors = [];
    let data = [];
    $('.inventory-field').each(function () {
        const aria = $(this).attr('aria-label');
        const ph = $(this).attr('placeholder');
        data[aria] = $(this).val();
        if ($(this).val() === '' || $(this).val() === '0') {
            if ($(this).prop('type') === 'file' && $(this).val() === '' && action === 'edit') {
                data[aria] = null;
                return;
            }
            errors.push(`${ph} es requerido.`);
        }
    });
    if (errors.length > 0) {
        showAlerts(errors);
        return false;
    }
    return true;
}

async function getAllProducts() {
    const productResponse = await request({
        method: 'GET',
        endpoint: `api/owner/inventario/branch/${sessionStorage.getItem('branchId')}`,
        fetch: 'response'
    });
    if (productResponse.data.content.length === 0) {
        alerta('warning', 'No hay productos en esta sucursal', 'Sin productos');
    } else {
        const products = productResponse.data.content;
        let html = '';
        for (const product of products) {
            html += `<div class="product-registered">
                        <p>${product.inventory_name}</p>
                        <div class="img-product-inventory">
                            <img src="/Inventario/${product.imagen}">
                        </div>
                        <p><span>$</span>${product.price}</p>
                        <p>${product.store}</p>
                        <p>${product.capacibility}</p>
                        <p>${product.description}</p>
                        <div class="actions-product">
                            <button type="button" class="btn-update trigger-modal" data-action="edit" data-target="${product.id}">Actualizar</button>
                            <button type="button" class="btn-remove" data-target="${product.id}">Eliminar</button>
                        </div>
                    </div>`;
        }
        if (html === '') {
            html = html += `<div class="product-registered">
                                <p>Esta sucursal no tiene productos.</p>
                            </div>`;
        }
        $('#mostrar-productos').html('');
        $('#mostrar-productos').html(html);
    }
}

$(document).on('click', '.btn-remove', async function (e) {
    const target = $(this).data('target');
    deleteProduct(target);
});

async function deleteProduct(id) {
    const confirmed = await confirmAlert('warning', 'Eliminar producto', '¿Estás seguro de eliminar el producto? No se puede deshacer.', 'Sí, eliminar.');
    if (confirmed) {
        const response = await request({
            method: 'DELETE',
            endpoint: `api/owner/inventario/eliminar/${id}`,
            fetch: 'response'
        });
        if (!response.isValid) {
            alerta('error', response.data.message);
            return;
        }
        await getAllProducts();
        alerta('success', 'Producto eliminado.');
        return true;
    }
}

async function getProduct(id) {
    const product = await request({
        method: 'GET',
        endpoint: `api/owner/inventario/${id}`,
        fetch: 'data'
    });
    return product;
}

async function actualizarInventory(target) {
    const nombre = document.querySelector("#name").value;
    const precio = document.querySelector("#price").value;
    const description = document.querySelector("#description").value;
    const capacibility = document.querySelector("#capacibility").value;
    const store = document.querySelector("#store").value;
    const imagen = document.querySelector("#img");
    var formdata = new FormData();
    formdata.append("inventory_name", nombre);
    formdata.append("price", precio);
    formdata.append("store", store);
    formdata.append("description", description);
    formdata.append("capacibility", capacibility);
    formdata.append("imagen", null);
    formdata.append("file", imagen.files[0]);
    formdata.append("id_branch", sessionStorage.getItem("branchId"));
    await fetch(`${BASE_URL}api/owner/inventario/actualizar/${target}`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: formdata,
        redirect: 'follow'
    })
        .then(response => response.json())
        .then(data => {
            alerta('success', 'Se actualizó el producto.', 'Éxito');
        })
}

// $(window).on('click', function (e) {
//     if (e.target === document.getElementById('modal-new-service')) {
//         removeModal();
//     }
// });

async function showModal(action, target) {
    await inflateModal(action, target);
    $('#modal-new-service').removeClass('no-mostrar');
}

function removeModal() {
    $('#modal-new-service').addClass('no-mostrar');
}

function showAlerts(alerts, type = 'error') {
    let html = '<div>\n'
    alerts.forEach(alert => {
        html += `<p class="${type}">${alert}</p>\n`;
    })
    html += '</div>';
    $('#alertas').removeClass('no-mostrar d-none');
    document.getElementById('alertas').innerHTML = html;
    document.getElementById('alertas').scrollIntoView();
}

function showObjectAlerts(alerts, type) {
    let html = '<div>\n'
    for (const alert in alerts) {
        html += `<p class="${type}">${alerts[alert]}</p>\n`;
    }
    html += '</div>';
    $('#alertas').removeClass('no-mostrar d-none');
    document.getElementById('alertas').innerHTML = html;
    document.getElementById('alertas').scrollIntoView();
}