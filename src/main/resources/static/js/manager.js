// Promedio de puntuacion de acuerdo a su tabla
const score = 4;
const stars = document.querySelectorAll("#star");
stars.forEach(function (){
    for(let i=0; i<score; i++){
        stars[i].classList.add("checked");
    }
});

//Ultima puntuacion dada a este empleado
const score_comment = 2;
const stars_comment = document.querySelectorAll("#star-comment");
stars_comment.forEach(function (){
    for(let i=0; i<score_comment; i++){
        stars_comment[i].classList.add("checked");
    }
});