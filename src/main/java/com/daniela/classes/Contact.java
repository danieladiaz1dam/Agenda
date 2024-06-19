package com.daniela.classes;

import java.util.Objects;

/**
 * Clase que representa un contacto
 */
public class Contact {
    /**
     * Identificador único del contacto
     */
    private final int ID;

    /**
     * Nombre del contacto
     */
    private final String NAME;

    /**
     * Número de teléfono del contacto
     */
    private final String PHONE;

    /**
     * Correo electrónico del contacto
     */
    private final String EMAIL;

    /**
     * Indica si el contacto es favorito
     */
    private final boolean IS_FAVORITE;

    /**
     * Constructor de la clase Contact
     *
     * @param ID       Id del contacto
     * @param name     Nombre del contacto
     * @param phone    Número de teléfono del contacto
     * @param email    Correo electrónico del contacto
     * @param favorite Indicador de si el contacto es favorito o no
     */
    public Contact(int ID, String name, String phone, String email, boolean favorite) {
        this.ID = ID;
        // IntelliJ me sugirió cambiar mi if/else por esta función
        this.NAME = Objects.requireNonNullElse(name, "");
        this.PHONE = Objects.requireNonNullElse(phone, "");
        this.EMAIL = Objects.requireNonNullElse(email, "");
        this.IS_FAVORITE = favorite;
    }

    /**
     * Retorna el ID del contacto
     *
     * @return El ID del contacto
     */
    public int getID() {
        return ID;
    }

    /**
     * Retorna el nombre del contacto
     *
     * @return El nombre del contacto
     */
    public String getName() {
        return NAME;
    }

    /**
     * Retorna el número de teléfono del contacto
     *
     * @return El número de teléfono del contacto
     */
    public String getPhone() {
        return PHONE;
    }

    /**
     * Retorna el correo electrónico del contacto
     *
     * @return El correo electrónico del contacto
     */
    public String getEmail() {
        return EMAIL;
    }

    /**
     * Retorna si el contacto es favorito
     *
     * @return true si el contacto es favorito, false de lo contrario
     */
    public boolean isFavorite() {
        return IS_FAVORITE;
    }

    /**
     * Representa un contacto en forma de fila de una tabla HTML
     *
     * @return fila para una tabla HTML
     */
    public String toTableRowAsContact() {
        String phoneNumber = "";

        if (this.PHONE.length() > 3)
            phoneNumber = Phone.formatPhoneNumber(this.PHONE.substring(0, 3), this.PHONE.substring(3));

        return String.format(
                """
                    <tr>
                        <td>%d</td>
                        <td><a href="?id=%d">%s</a></td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>
                            <button type="button" onclick="edit(%d)"><span class="material-symbols-rounded"> edit </span></button>
                            <button type="button" onclick="removeContact(%d)"><span class="material-symbols-rounded"> delete </span></button>
                       </td>
                    </tr>
                    """,
                this.ID, this.ID, this.NAME, this.EMAIL,
                phoneNumber,
                this.ID, this.ID
        );
    }

    /**
     * Representa un contacto de un grupo en forma de fila de una tabla HTML
     *
     * @param groupID Id del grupo al que pertenece el contacto
     * @return fila para una tabla HTML
     */
    public String toTableRowAsMember(int groupID) {
        String phoneNumber = "";

        if (this.PHONE.length() > 3)
            phoneNumber = Phone.formatPhoneNumber(this.PHONE.substring(0, 3), this.PHONE.substring(3));

        return String.format(
                """
                <tr>
                    <td>%d</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>
                        <button class="removeMemberBtn" type="button" onclick="removeGroupMember(%d, %d)"><span class="material-symbols-rounded"> delete </span></button>
                   </td>
                </tr>
                """, this.ID, this.NAME, this.EMAIL, phoneNumber, groupID, this.ID
        );
    }

    /**
     * Retorna una representación en forma de cadena de este contacto
     *
     * @return Una cadena que representa el contacto, incluyendo todos sus atributos
     */
    @Override
    public String toString() {
        return "Contact{" +
                "ID=" + ID +
                ", name='" + NAME + '\'' +
                ", phone='" + PHONE + '\'' +
                ", email='" + EMAIL + '\'' +
                '}';
    }
}
