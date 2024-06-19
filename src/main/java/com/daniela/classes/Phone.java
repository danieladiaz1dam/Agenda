package com.daniela.classes;

/**
 * Clase que representa un Teléfono
 */
public class Phone {
    /**
     * Identificador único del teléfono
     */
    private final int ID;

    /**
     * Código de país del número de teléfono
     */
    private final String COUNTRY_CODE;

    /**
     * Número de teléfono
     */
    private final String NUMBER;

    /**
     * Categoría del teléfono (por ejemplo, móvil, trabajo, casa)
     */
    private final String CATEGORY;

    /**
     * Constructor con ID
     *
     * @param ID          Identificador único del teléfono
     * @param countryCode Código de país del número de teléfono
     * @param number      Número de teléfono
     * @param category    Categoría del teléfono (por ejemplo, móvil, trabajo, casa)
     */
    public Phone(int ID, String countryCode, String number, String category) {
        this.ID = ID;
        this.COUNTRY_CODE = countryCode;
        this.NUMBER = number;
        this.CATEGORY = category;
    }

    /**
     * Constructor sin ID
     *
     * @param countryCode Código de país del número de teléfono
     * @param number      Número de teléfono
     * @param category    Categoría del teléfono (por ejemplo, móvil, trabajo, casa)
     */
    public Phone(String countryCode, String number, String category) {
        this.ID = -1;
        this.COUNTRY_CODE = countryCode;
        this.NUMBER = number;
        this.CATEGORY = category;
    }

    /**
     * Obtiene el identificador del número de teléfono
     *
     * @return el identificador del número de teléfono
     */
    public int getID() {
        return ID;
    }

    /**
     * Obtiene el código de país del número de teléfono
     *
     * @return el código de país del número de teléfono
     */
    public String getCountryCode() {
        return COUNTRY_CODE;
    }

    /**
     * Obtiene el número de teléfono
     *
     * @return el número de teléfono
     */
    public String getNumber() {
        return NUMBER;
    }

    /**
     * Obtiene la categoría del teléfono
     *
     * @return la categoría del teléfono
     */
    public String getCategory() {
        return CATEGORY;
    }

    /**
     * Formatea un número de teléfono con el código de país
     *
     * @param countryCode el código de país del número de teléfono
     * @param number      el número de teléfono
     * @return el número de teléfono formateado con el código de país
     */
    public static String formatPhoneNumber(String countryCode, String number) {
        return "+" + countryCode + formatPhoneNumber(number);
    }

    /**
     * Formatea un número de teléfono
     *
     * @param number el número de teléfono
     * @return el número de teléfono formateado
     */
    public static String formatPhoneNumber(String number) {
        return String.format("%s %s %s %s", number.substring(0, 3), number.substring(3, 5), number.substring(5, 7), number.substring(7));
    }

    /**
     * Convierte el número de teléfono a un campo de formulario HTML
     *
     * @return el número de teléfono en formato de campo de formulario HTML
     */
    public String toFormField() {
        return String.format("""
                <p class="field">
                    <input type="hidden" name="phoneIDs" value="%d">
                     <input class="small-field" type="number" name="countryCodes" value="%s" placeholder="+34"
                            pattern="\\d*"
                            min="1" required>
                     <span class="gap10"></span>
                     <input type="number" name="phones" placeholder="Telephone" pattern="d*" min="100000000"
                            max="999999999" value="%s" required>
                     <span class="gap10"></span>
                     <input type="text" name="phoneCategories" placeholder="Category" value="%s" required>

                     <button class="remove-field" type="button" onclick="removeField(this.parentNode, 'phone', %d)">
                         <span class="material-symbols-rounded"> close </span>
                     </button>
                </p>
                """, this.ID, this.COUNTRY_CODE, this.NUMBER, this.CATEGORY, this.ID);
    }

    /**
     * Retorna una representación en forma de cadena de este teléfono
     *
     * @return Una cadena que representa el teléfono, incluyendo todos sus atributos
     */
    @Override
    public String toString() {
        return String.format("+%s %s · %s", this.COUNTRY_CODE, formatPhoneNumber(NUMBER), this.CATEGORY);
    }
}
