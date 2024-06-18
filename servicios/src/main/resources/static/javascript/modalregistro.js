$('#exampleModal').on('show.bs.modal', function (e) {
    let $modal = $(this);
    // Cargar el contenido del formulario desde la otra página
    $modal.find('#modal-body-content').load('formularioCliente.html');
});