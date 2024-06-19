// Esperar a que la página termine de cargar
// noinspection JSUnresolvedReference

document.addEventListener('DOMContentLoaded', function () {
    const favoriteCheckbox = document.getElementById('favorite');
    const favoriteLabel = document.getElementById('favorite-label');
    const favoriteSpan = favoriteLabel.querySelector('.material-symbols-rounded');

    const tags = document.getElementById('tagSelect');
    const tagInput = document.getElementById('tagList');

    const form = document.getElementById('form');

    // Lógica del checkbox <3
    favoriteCheckbox.addEventListener('change', () => {
        if (favoriteCheckbox.checked) {
            favoriteSpan.classList.add('checked');
        } else {
            favoriteSpan.classList.remove('checked');
        }
    });

    // Actualizar el listado de tags
    tags.addEventListener('change', (e) => {
        addNewTag(e.target.value);
    });

    // Guardar las tags antes de enviar el form
    form.addEventListener('submit', () => {
        let currentTags = getCurrentTags();
        let str = "";

        currentTags.forEach(tag => {
            str += tag.innerHTML + ","
        });

        tagInput.value = str.slice(0, -1);

        return true;
    })
});

/**
 * Añade una nueva tag al listado de tags seleccionadas
 * @param name Nombre de la tag
 */
function addNewTag(name) {
    const tags = document.getElementById('tagSelect');
    const tagList = document.getElementById('tagListContainer');

    if (name !== "") {
        tagList.innerHTML += `<span class="tag-item" onclick="removeTag(this)">${name}</span>`;

        for (let i = 0; i < tags.length; i++) {
            if (tags.options[i].value === name) {
                tags.removeChild(tags.options[i]);
            }
        }
    }

    tags.selectedIndex = 0;
}

/**
 * Devuelve un array de las tags seleccionadas actualmente
 * @returns {ChildNode[]}
 */
function getCurrentTags() {
    // Devuelve un NodeList, el cual es más difícil de operar, así que lo paso a Array
    let currentTags = Array.from(document.getElementById("tagListContainer").childNodes);
    // Quitar saltos de línea
    currentTags.shift();
    currentTags.pop()
    return currentTags;
}

/**
 * Elimina una tag de la lista de seleccionadas, la añade de vuelta al la lista de tags disponibles
 * @param elem Elemento HTML de la Tag a eliminar
 */
function removeTag(elem) {
    let name = elem.innerHTML;
    const tags = document.getElementById('tagSelect');

    tags.options[tags.options.length] = new Option(name, name);
    elem.remove();
}

/**
 * Elimina un elemento HTML
 * (Usado para eliminar manualmente los campos)
 * @param elem Elemento a eliminar
 * @param type Tipo de elemento que hemos borrado
 * @param id Id del elemento borrado, blank si es nulo
 */
function removeField(elem, type, id) {
    elem.remove()
    let deletedEmails = document.getElementById("deletedEmails");
    let deletedPhones = document.getElementById("deletedPhones");
    let deletedAddresses = document.getElementById("deletedAddresses");

    switch (type) {
        case "email":
            deletedEmails.value += id + ',';
            break;
        case "phone":
            deletedPhones.value += id + ',';
            break;
        case "address":
            deletedAddresses.value += id + ',';
            break;
    }
}

/**
 * Elimina un elemento HTML
 * (Usado para eliminar manualmente los campos)
 * @param elem Elemento a eliminar
 */
function removeNewField(elem) {
    elem.remove()
}

/**
 * Añade un nuevo campo de tipo Email
 */
function addEmail() {
    const emails = document.getElementById("emailList");

    let p = document.createElement('p');
    p.className = 'field';
    p.innerHTML = `
        <input type="hidden" name="emailIDs" value="-1">
        <input type="text" name="emails" placeholder="Email" required>
        <span class="gap10"></span>
        <input type="text" name="emailCategories" placeholder="Categoría" required>

        <button class="remove-field" type="button" onclick="removeNewField(this.parentNode)">
            <span class="material-symbols-rounded"> close </span>
        </button>
    `;

    emails.append(p);
}

/**
 * Añade un nuevo campo de tipo Phone
 */
function addPhone() {
    const phones = document.getElementById("phoneList");

    let p = document.createElement('p');
    p.className = 'field';
    p.innerHTML = `
        <input type="hidden" name="phoneIDs" value="-1">
        <input class="small-field" type="number" name="countryCodes" placeholder="+34" pattern="\\d*" min="1" required>
        <span class="gap10"></span>
        <input type="number" name="phones" placeholder="Telephone" pattern="\\d*" min="100000000" max="999999999" required>
        <span class="gap10"></span>
        <input type="text" name="phoneCategories" placeholder="Categoría" required>

        <button class="remove-field" type="button" onclick="removeNewField(this.parentNode)">
            <span class="material-symbols-rounded"> close </span>
        </button>
    `;

    phones.append(p);
}

/**
 * Añade un nuevo campo de tipo Address
 */
function addAddress() {
    const addresses = document.getElementById("AddressList");

    let div = document.createElement('div');
    div.className = 'field';

    div.innerHTML = `
        <p class="field">
            <input type="hidden" name="addressIDs" value="-1">
            <input type="text" name="streets" placeholder="Calle" required>
            <span class="gap10"></span>
            <input class="small-field" type="text" name="houseNumbers" placeholder="Num." pattern="\\d*" min="1" required>
            <button class="remove-field" type="button" onclick="removeNewField(this.parentNode.parentNode)">
                <span class="material-symbols-rounded"> close </span>
            </button>
        </p>
        <p class="field">
            <input type="text" name="cities" placeholder="Ciudad" required>
            <span class="gap10"></span>
            <input class="small-field" type="number" name="zipCodes" placeholder="CP" pattern="\\d*" min="10000" max="99999" required>
        </p>
        <p class="field">
            <input type="text" name="addressCategories" placeholder="Categoría" required>
        </p>
    `;

    addresses.append(div);
}