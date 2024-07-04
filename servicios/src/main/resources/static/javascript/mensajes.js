document.addEventListener('DOMContentLoaded', function() {
    // Espera 3 segundos antes de ocultar el mensaje de error
    setTimeout(function() {
        // Añade la clase 'hidden' para iniciar la transición de opacidad
        document.getElementById('error-message').classList.add('hidden');
    }, 1500); // 3000 milisegundos = 3 segundos
});

document.addEventListener('DOMContentLoaded', function() {
    // Espera 3 segundos antes de ocultar el mensaje de error
    setTimeout(function() {
        // Añade la clase 'hidden' para iniciar la transición de opacidad
        document.getElementById('exito-message').classList.add('hidden');
    }, 1500); // 3000 milisegundos = 3 segundos
});