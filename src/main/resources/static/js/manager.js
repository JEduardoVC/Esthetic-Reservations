// Promedio de puntuacion de acuerdo a su tabla
const score = 4;
const stars = document.querySelectorAll("#star_id");
stars.forEach(function (){
    for(let i=0; i<score; i++){
        stars[i].classList.add("checked");
    }
});

//Ultima puntuacion dada a este empleado
const score_comment = 2;
const stars_comment = document.querySelectorAll("#star-comment_id");
stars_comment.forEach(function (){
    for(let i=0; i<score_comment; i++){
        stars_comment[i].classList.add("checked");
    }
});


////////// Esta parte es igual a la de arriba, solo es para saber que se cambia el id por el ID que viene de la base   /////////


// Promedio de puntuacion de acuerdo a su tabla
const score2 = 3;
const stars2 = document.querySelectorAll("#star_id2");
stars2.forEach(function (){
    for(let i=0; i<score2; i++){
        stars2[i].classList.add("checked");
    }
});

//Ultima puntuacion dada a este empleado
const score_comment2 = 5;
const stars_comment2 = document.querySelectorAll("#star-comment_id2");
stars_comment2.forEach(function (){
    for(let i=0; i<score_comment2; i++){
        stars_comment2[i].classList.add("checked");
    }
});