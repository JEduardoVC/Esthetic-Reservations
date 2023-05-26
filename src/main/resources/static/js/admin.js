$(function () {
    let darkMode = sessionStorage.getItem('darkModeAdmin');
    if (darkMode === null) {
        sessionStorage.setItem('darkModeAdmin', 'false');
        darkMode = 'false';
    }
    if(darkMode === 'false'){
        $('.moon').removeClass('d-none no-mostrar');
        $('.sun').addClass('d-none no-mostrar');
    } else {
        $('.sun').removeClass('d-none no-mostrar');
        $('.moon').addClass('d-none no-mostrar');
    }
    if(darkMode === 'false' && $('html').hasClass('dark-mode') === true){
        $('html').removeClass('dark-mode');
    }
    if(darkMode === 'true' && $('html').hasClass('dark-mode') === false){
        $('html').addClass('dark-mode');
    }
    $('.dark-mode-toggler').on('click', toggleDarkMode);
})

$(document).on('click','#btn-cerrar-sesion', function(e){
    sessionStorage.clear();
    document.location = `${BASE_URL}app/cerrar`;
});

const toggleDarkMode = function () {
    $('html').toggleClass('dark-mode');
    sessionStorage.setItem('darkModeAdmin', sessionStorage.getItem('darkModeAdmin') === 'true' ? 'false' : 'true');
    $('.sun').toggleClass('d-none no-mostrar');
    $('.moon').toggleClass('d-none no-mostrar');
};