package com.daniela.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hace referencia a un contacto de la base de datos pero obteniendo todos sus datos de las demás tablas
 */
public class ContactDetails {
    /**
     * Id del contacto en la base de datos
     */
    private final int ID;

    /**
     * Nombre del contacto
     */
    private final String NAME;

    /**
     * Fecha de cumpleaños del contacto
     */
    private final String BIRTHDAY;

    /**
     * Fecha de creación del contacto en la base de datos
     */
    private final String CREATION_DATE;

    /**
     * Indica si el contacto es favorito
     */
    private final boolean IS_FAVORITE;

    /**
     * Etiquetas asociadas al contacto
     */
    private final String TAGS;

    /**
     * Lista de correos electrónicos del contacto
     */
    private final List<Email> EMAILS;

    /**
     * Lista de números de teléfono del contacto
     */
    private final List<Phone> PHONES;

    /**
     * Lista de direcciones del contacto
     */
    private final List<Address> ADDRESSES;

    /**
     * Constructor para crear un objeto ContactDetails vacío
     */
    public ContactDetails() {
        this.ID = -1;
        this.NAME = "";
        this.BIRTHDAY = "";
        this.CREATION_DATE = "";
        this.IS_FAVORITE = false;
        this.TAGS = "";
        this.EMAILS = new ArrayList<>();
        this.PHONES = new ArrayList<>();
        this.ADDRESSES = new ArrayList<>();
    }

    /**
     * Constructor para crear un objeto ContactDetails
     *
     * @param ID           Id del contacto en la base de datos
     * @param name         Nombre del contacto
     * @param birthday     Fecha de cumpleaños del contacto
     * @param creationDate Fecha de creación del contacto en la base de datos
     * @param isFavorite   Indica si el contacto es favorito
     * @param tags         Etiquetas asociadas al contacto
     */
    public ContactDetails(int ID, String name, String birthday, String creationDate, boolean isFavorite, String tags) {
        this.ID = ID;
        this.NAME = name;
        this.BIRTHDAY = birthday;
        this.CREATION_DATE = creationDate;
        this.IS_FAVORITE = isFavorite;
        this.TAGS = tags;
        this.EMAILS = new ArrayList<>();
        this.PHONES = new ArrayList<>();
        this.ADDRESSES = new ArrayList<>();
    }

    /**
     * Constructor para crear un objeto ContactDetails con listas de emails, teléfonos y direcciones
     *
     * @param name       Nombre del contacto
     * @param birthday   Fecha de cumpleaños del contacto
     * @param isFavorite Indica si el contacto es favorito
     * @param tags       Etiquetas asociadas al contacto
     * @param emails     Lista de correos electrónicos del contacto
     * @param phones     Lista de números de teléfono del contacto
     * @param addresses  Lista de direcciones del contacto
     */
    public ContactDetails(String name, String birthday, boolean isFavorite, String tags, ArrayList<Email> emails, ArrayList<Phone> phones, ArrayList<Address> addresses) {
        this.ID = -1;
        this.NAME = name;
        this.BIRTHDAY = birthday;
        this.IS_FAVORITE = isFavorite;
        this.TAGS = tags;
        this.CREATION_DATE = null;
        this.EMAILS = emails;
        this.PHONES = phones;
        this.ADDRESSES = addresses;
    }

    /**
     * Constructor para crear un objeto ContactDetails con listas de emails, teléfonos y direcciones
     *
     * @param ID         Id del contacto en la base de datos
     * @param name       Nombre del contacto
     * @param birthday   Fecha de cumpleaños del contacto
     * @param isFavorite Indica si el contacto es favorito
     * @param tags       Etiquetas asociadas al contacto
     * @param emails     Lista de correos electrónicos del contacto
     * @param phones     Lista de números de teléfono del contacto
     * @param addresses  Lista de direcciones del contacto
     */
    public ContactDetails(int ID, String name, String birthday, boolean isFavorite, String tags, ArrayList<Email> emails, ArrayList<Phone> phones, ArrayList<Address> addresses) {
        this.ID = ID;
        this.NAME = name;
        this.BIRTHDAY = birthday;
        this.IS_FAVORITE = isFavorite;
        this.TAGS = tags;
        this.CREATION_DATE = null;
        this.EMAILS = emails;
        this.PHONES = phones;
        this.ADDRESSES = addresses;
    }

    /**
     * Obtiene el ID del contacto
     *
     * @return el ID del contacto
     */
    public int getID() {
        return ID;
    }

    /**
     * Obtiene el nombre del contacto
     *
     * @return el nombre del contacto
     */
    public String getName() {
        return NAME;
    }

    /**
     * Obtiene la fecha de cumpleaños del contacto
     *
     * @return la fecha de cumpleaños del contacto
     */
    public String getBirthday() {
        return BIRTHDAY;
    }

    /**
     * Obtiene la fecha de creación del contacto
     *
     * @return la fecha de creación del contacto
     */
    public String getCreationDate() {
        return CREATION_DATE;
    }

    /**
     * Verifica si el contacto es favorito
     *
     * @return true si el contacto es favorito, false en caso contrario
     */
    public boolean isFavorite() {
        return IS_FAVORITE;
    }

    /**
     * Obtiene las etiquetas asociadas al contacto
     *
     * @return las etiquetas asociadas al contacto
     */
    public String getTags() {
        return TAGS;
    }

    /**
     * Obtiene la lista de correos electrónicos del contacto
     *
     * @return la lista de correos electrónicos del contacto
     */
    public List<Email> getEmails() {
        return EMAILS;
    }

    /**
     * Obtiene la lista de números de teléfono del contacto
     *
     * @return la lista de números de teléfono del contacto
     */
    public List<Phone> getPhones() {
        return PHONES;
    }

    /**
     * Obtiene la lista de direcciones del contacto
     *
     * @return la lista de direcciones del contacto
     */
    public List<Address> getAddresses() {
        return ADDRESSES;
    }

    /**
     * Retorna una representación en forma de cadena de este contacto
     *
     * @return Una cadena que representa la dirección, incluyendo todos sus atributos
     */
    @Override
    public String toString() {
        String[] tagList = {""};

        if (this.TAGS != null && !this.TAGS.isEmpty())
            tagList = TAGS.split(",");

        return "ContactDetails{" +
                "ID=" + ID +
                ", name='" + NAME + '\'' +
                ", birthday=" + BIRTHDAY +
                ", creationDate=" + CREATION_DATE +
                ", isFavorite=" + IS_FAVORITE +
                ", tags=" + Arrays.toString(tagList) +
                ", phones=" + PHONES +
                ", addresses=" + ADDRESSES +
                ", emails=" + EMAILS +
                '}';
    }
}
