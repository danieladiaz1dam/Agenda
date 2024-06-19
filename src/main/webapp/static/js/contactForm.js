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
        console.log(e.target.value)
        if (e.target.value === 'add_new_tag') {
            document.getElementById('newTagContainer').style.display = 'block';
        } else {
            addNewTag(e.target.value);
            document.getElementById('newTagContainer').style.display = 'none';
        }
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
    currentTags.shift(); // Quitar el primer elemento, ya que no es válido
    return currentTags;
}

/**
 * Comprueba si una tag existe
 * @param name Nombre de la Tag
 * @returns {boolean} true si existe, false en caso contrario
 */
function checkIfTagExists(name) {
    let currentTags = getCurrentTags();

    let i = 0;
    let found = false;

    while (!found && i < currentTags.length) {
        if (currentTags[i].innerHTML === name)
            found = true;
        i++;
    }

    return found;
}

/**
 * Añade una tag personalizada si no existe.
 */
function createNewTag() {
    let newTagInput = document.getElementById('newTag');
    const tags = document.getElementById('tagSelect');

    if (!checkIfTagExists(newTagInput.value)) {
        addNewTag(newTagInput.value);
    }

    newTagInput.value = "";

    tags.selectedIndex = 0;
    tags.dispatchEvent(new Event('change'));
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
 */
function removeField(elem) {
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
        <input type="text" name="emails" placeholder="Email" required>
        <span class="gap10"></span>
        <input type="text" name="emailCategories" placeholder="Categoría" required>

        <button class="remove-field" type="button" onclick="removeField(this.parentNode)">
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
        <input class="small-field" type="number" name="countryCodes" placeholder="+34" pattern="\\d*" min="1" required>
        <span class="gap10"></span>
        <input type="number" name="phones" placeholder="Telephone" pattern="\\d*" min="100000000" max="999999999" required>
        <span class="gap10"></span>
        <input type="text" name="phoneCategories" placeholder="Categoría" required>

        <button class="remove-field" type="button" onclick="removeField(this.parentNode)">
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
            <input type="text" name="streets" placeholder="Calle" required>
            <span class="gap10"></span>
            <input class="small-field" type="text" name="houseNumbers" placeholder="Num." pattern="\\d*" min="1" required>
            <button class="remove-field" type="button" onclick="removeField(this.parentNode.parentNode)">
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