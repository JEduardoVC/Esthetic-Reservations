$(async function () {

    const empHtml = await getBranchEmployees();
    $('#employees-infos').html(empHtml);

});

$(document).on('click', '.manager-info', function (e) {
    // solo uno
    $('.manager-info').each(function (e) {
        $(this).removeClass('selected');
    });
    $(this).addClass('selected');
    sessionStorage.setItem('employeeId', $(this).data('value'));
});

async function getBranchEmployees() {
    const id = sessionStorage.getItem('branchId');
    const employees = await request({
        method: 'GET',
        endpoint: 'api/employee/all',
        urlParams: {
            by: 'branch',
            filterTo: id
        }
    });
    const branch = await request({
        method: 'GET',
        endpoint: 'api/branch/' + id,
        alertOnError: true,
        fetch: 'data'
    });
    let html = '';
    for (const employee of employees) {
        const avgRating = await request({
            method: 'GET',
            endpoint: `api/comment/average/employee/${employee.id}`,
            fetch: 'data'
        });
        const employeeRating = avgRating.value === null ? 0 : avgRating.value;
        html += `<div class="infos">
                    <div class="manager-info" data-value="${employee.id}">
                    <img class="img-manager" src="/img/avatar-de-usuario.webp">
                        <div class="description">
                            <p>${employee.user.name}</p>
                            <span>${employee.user.email}</span>
                            <span>${branch.location}</span>
                        </div>
                        <div class="stars">
                            <i class="bi bi-star-fill star ${employeeRating >= 1 ? 'checked' : ''}"  id="star_id"></i>
                            <i class="bi bi-star-fill star ${employeeRating >= 2 ? 'checked' : ''}" id="star_id"></i>
                            <i class="bi bi-star-fill star ${employeeRating >= 3 ? 'checked' : ''}" id="star_id"></i>
                            <i class="bi bi-star-fill star ${employeeRating >= 4 ? 'checked' : ''}" id="star_id"></i>
                            <i class="bi bi-star-fill star ${employeeRating >= 5 ? 'checked' : ''}" id="star_id"></i>
                            </div>
                        </div>`;
        const lastCommentResponse = await request({
            method: 'GET',
            endpoint: `api/comment/last/employee/${employee.id}`,
            fetch: 'response'
        });
        if (lastCommentResponse.status != 404) {
            const lastComment = lastCommentResponse.data;
            const commentRating = lastComment.rating;
            html += `<div class="last-comment">
                        <div class="model">
                            <div class="name-rating">
                                <!-- Nombre de la ultima persona que puntuo este empleado  -->
                                <p>Último comentario de: ${lastComment.client.name}</p>
                                <div class="stars-comment">
                                    <i class="bi bi-star-fill star-comment ${commentRating >= 1 ? 'checked' : ''}" id="star-comment_id"></i>
                                    <i class="bi bi-star-fill star-comment ${commentRating >= 2 ? 'checked' : ''}" id="star-comment_id"></i>
                                    <i class="bi bi-star-fill star-comment ${commentRating >= 3 ? 'checked' : ''}" id="star-comment_id"></i>
                                    <i class="bi bi-star-fill star-comment ${commentRating >= 4 ? 'checked' : ''}" id="star-comment_id"></i>
                                    <i class="bi bi-star-fill star-comment ${commentRating >= 5 ? 'checked' : ''}" id="star-comment_id"></i>
                                </div>
                            </div>
                            <!-- La fecha en la que se realizo la puntuacion -->
                            <div class="date-comment">
                                <p>${lastComment.date}</p>
                            </div>
                            <!-- Comentario de la persona -->
                            <div class="comment">
                                <p>${lastComment.comment}</p>
                            </div>
                        </div>
                    </div>`;
        } else {
            html += `<div class="last-comment">
                        <div class="model">
                            <div class="name-rating">
                                <!-- Nombre de la ultima persona que puntuo este empleado  -->
                                <p>Sin comentarios</p>
                                <div class="stars-comment">
                                    <i class="bi bi-star-fill star-comment id="star-comment_id"></i>
                                    <i class="bi bi-star-fill star-comment id="star-comment_id"></i>
                                    <i class="bi bi-star-fill star-comment id="star-comment_id"></i>
                                    <i class="bi bi-star-fill star-comment id="star-comment_id"></i>
                                    <i class="bi bi-star-fill star-comment id="star-comment_id"></i>
                                </div>
                            </div>
                            <!-- La fecha en la que se realizo la puntuacion -->
                            <div class="date-comment">
                                <p></p>
                            </div>
                            <!-- Comentario de la persona -->
                            <div class="comment">
                                <p>Este encargado no tiene opiniones todavía.</p>
                            </div>
                        </div>
                    </div>`;
        }
        html += `</div>`;


    }
    if (html === '') {
        html += `< div id = "show-employee" class="employee" >
            <div class="center"><p>No hay empleados en esta sucursal</p></div>
                </div > `;
    }
    return html;
}

// Promedio de puntuacion de acuerdo a su tabla
const score = 4;
const stars = document.querySelectorAll("#star_id");
stars.forEach(function () {
    for (let i = 0; i < score; i++) {
        stars[i].classList.add("checked");
    }
});

//Ultima puntuacion dada a este empleado
const score_comment = 2;
const stars_comment = document.querySelectorAll("#star-comment_id");
stars_comment.forEach(function () {
    for (let i = 0; i < score_comment; i++) {
        stars_comment[i].classList.add("checked");
    }
});


////////// Esta parte es igual a la de arriba, solo es para saber que se cambia el id por el ID que viene de la base   /////////


// Promedio de puntuacion de acuerdo a su tabla
const score2 = 3;
const stars2 = document.querySelectorAll("#star_id2");
stars2.forEach(function () {
    for (let i = 0; i < score2; i++) {
        stars2[i].classList.add("checked");
    }
});

//Ultima puntuacion dada a este empleado
const score_comment2 = 5;
const stars_comment2 = document.querySelectorAll("#star-comment_id2");
stars_comment2.forEach(function () {
    for (let i = 0; i < score_comment2; i++) {
        stars_comment2[i].classList.add("checked");
    }
});