package com.daniela.classes;

/**
 * Clase que representa una dirección
 */
public class Address {
    /**
     * Id del objeto en la base de datos
     */
    private final int ID;

    /**
     * Calle de la dirección
     */
    private final String STREET;

    /**
     * Número de la casa
     */
    private final String HOUSE_NUMBER;

    /**
     * Código postal
     */
    private final String ZIP;

    /**
     * Ciudad
     */
    private final String CITY;

    /**
     * Categoría de la dirección
     */
    private final String CATEGORY;


    /**
     * Constructor de la clase Address
     *
     * @param ID       Id del objeto en la base de datos
     * @param street   Calle de la dirección
     * @param number   Número de la casa
     * @param zip      Código postal
     * @param city     Ciudad
     * @param category Categoría de la dirección
     */
    public Address(int ID, String street, String number, String zip, String city, String category) {
        this.ID = ID;
        this.STREET = street;
        this.HOUSE_NUMBER = number;
        this.ZIP = zip;
        this.CITY = city;
        this.CATEGORY = category;
    }

    /**
     * Constructor de la clase Address
     *
     * @param street   Calle de la dirección
     * @param number   Número de la casa
     * @param zip      Código postal
     * @param city     Ciudad
     * @param category Categoría de la dirección
     */
    public Address(String street, String number, String zip, String city, String category) {
        this.ID = -1;
        this.STREET = street;
        this.HOUSE_NUMBER = number;
        this.ZIP = zip;
        this.CITY = city;
        this.CATEGORY = category;
    }

    /**
     * Devuelve el identificador de la dirección
     *
     * @return El identificador de la dirección
     */
    public int getID() {
        return ID;
    }

    /**
     * Devuelve la calle de la dirección
     *
     * @return La calle de la dirección
     */
    public String getStreet() {
        return STREET;
    }

    /**
     * Devuelve el número de la casa
     *
     * @return El número de la casa
     */
    public String getHouseNumber() {
        return HOUSE_NUMBER;
    }

    /**
     * Devuelve el código postal de la dirección
     *
     * @return El código postal de la dirección
     */
    public String getZip() {
        return ZIP;
    }

    /**
     * Devuelve la ciudad de la dirección
     *
     * @return La ciudad de la dirección
     */
    public String getCity() {
        return CITY;
    }

    /**
     * Devuelve la categoría de la dirección
     *
     * @return La categoría de la dirección
     */
    public String getCategory() {
        return CATEGORY;
    }

    /**
     * Genera y devuelve un bloque de HTML que representa los campos de formulario de una dirección
     * Este método incluye valores rellenados de los atributos de la dirección
     *
     * @return Un bloque de HTML con campos de formulario para la dirección
     */
    public String toFormField() {
        return String.format("""
                <div class="field">
                    <input type="hidden" name="addressIDs" value="%d">
                    <p class="field">
                        <input type="text" name="streets" placeholder="Calle" value="%s" required>
                        <span class="gap10"></span>
                        <input class="small-field" type="number" name="houseNumbers" placeholder="Num."
                               pattern="\\d*" min="1" value="%s" required>
                        <button class="remove-field" type="button"
                                onclick="removeField(this.parentNode.parentNode, 'address', %d)">
                            <span class="material-symbols-rounded"> close </span>
                        </button>
                    </p>
                    <p class="field">
                        <input type="text" name="cities" placeholder="Ciudad" value="%s" required>
                        <span class="gap10"></span>
                        <input class="small-field" type="text" name="zipCodes" placeholder="CP"
                               pattern="\\d*"
                               min="10000" max="99999" value="%s" required>
                    </p>
                    <p class="field">
                        <input type="text" name="addressCategories" placeholder="Category" value="%s" required>
                    </p>
                </div>
                """, this.ID, this.STREET, this.HOUSE_NUMBER, this.ID,  this.CITY, this.ZIP, this.CATEGORY);
    }

    /**
     * Retorna una representación en forma de cadena de esta dirección
     *
     * @return Una cadena que representa la dirección, incluyendo todos sus atributos
     */
    @Override
    public String toString() {
        return String.format("C. %s Nº %s, %s %s · %s", this.STREET, this.HOUSE_NUMBER, this.CITY, this.ZIP, this.CATEGORY);
    }
}
