package com.daniela.classes;

public class Email {
    /**
     * Identificador único del email
     */
    private final int ID;

    /**
     * Dirección de correo electrónico
     */
    private final String EMAIL;

    /**
     * Categoría del email
     */
    private final String CATEGORY;

    /**
     * Constructor de la clase Email con ID
     *
     * @param ID       Identificador único del email
     * @param email    Dirección de correo electrónico
     * @param category Categoría del email
     */
    public Email(int ID, String email, String category) {
        this.ID = ID;
        this.EMAIL = email;
        this.CATEGORY = category;
    }

    /**
     * Constructor de la clase Email sin ID
     *
     * @param email    Dirección de correo electrónico
     * @param category Categoría del email
     */
    public Email(String email, String category) {
        this.ID = -1;
        this.EMAIL = email;
        this.CATEGORY = category;
    }

    public int getID() {
        return ID;
    }

    public String getEmail() {
        return EMAIL;
    }

    public String getCategory() {
        return CATEGORY;
    }

    /**
     * Convierte el email a un campo de formulario HTML
     *
     * @return el email en formato de campo de formulario HTML
     */
    public String toFormField() {
        return String.format("""
                <p class="field">
                    <input type="hidden" name="emailIDs" value="%d">
                    <input type="email" name="emails" placeholder="Email" value="%s" required>
                    <span class="gap10"></span>
                    <input type="text" name="emailCategories" placeholder="Category" value="%s" required>
                
                    <button class="remove-field" type="button" onclick="removeField(this.parentNode, 'email', %d)">
                        <span class="material-symbols-rounded"> close </span>
                    </button>
                </p>
                """, this.ID, this.EMAIL, this.CATEGORY, this.ID);
    }

    /**
     * Retorna una representación en forma de cadena de este email
     *
     * @return Una cadena que representa el email, incluyendo todos sus atributos
     */
    @Override
    public String toString() {
        return String.format("%s · %s", EMAIL, CATEGORY);
    }
}
