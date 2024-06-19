function edit(id) {
    //alert(`Editar contacto: ${id}`);
    location.href = "editContact?id=" + id;
}

/**
 * Función para las peticiones xhr
 * @param xhr Objeto xhr sobre el que cargar la función
 */
function xhrOnloadFunction(xhr) {
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log(xhr.responseText);
            location.reload();

        } else {
            console.log(`Error: ${xhr.status} - ${xhr.statusText}`);
        }
    };
}

/**
 * Eliminar un contacto
 * @param id id del contacto a eliminar
 */
function removeContact(id) {
    const xhr = new XMLHttpRequest();
    xhr.open("DELETE", "/AgendaDaniela_war_exploded/deleteContact?id=" + id);

    xhrOnloadFunction(xhr);

    xhr.send();
}

/**
 * Eliminar un grupo
 * @param id id del grupo a eliminar
 */
function removeGroup(id) {
    const xhr = new XMLHttpRequest();
    xhr.open("DELETE", "/AgendaDaniela_war_exploded/deleteGroup?id=" + id);

    xhrOnloadFunction(xhr);

    xhr.send();
}

/**
 * Eliminar un miembro de un grupo
 * @param memberId Id del contacto
 * @param groupId Id del grupo
 */
function removeGroupMember(groupId, memberId) {
    const xhr = new XMLHttpRequest();
    xhr.open("DELETE", "/AgendaDaniela_war_exploded/removeGroupMember?groupID=" + groupId + "&memberID=" + memberId);

    xhrOnloadFunction(xhr);

    xhr.send();
    //location.reload();
}
