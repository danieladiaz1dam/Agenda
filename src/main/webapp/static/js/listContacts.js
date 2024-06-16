function edit(id) {
    //alert(`Editar contacto: ${id}`);
    location.href = "SvEditarContacto?id=" + id;
}

function remove(id) {
    location.href = "SvEliminarContacto?id=" + id;
}