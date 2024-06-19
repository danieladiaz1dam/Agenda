package com.daniela.classes;

/**
 * Clase que representa una etiqueta
 */
public class Tag {
    private final int ID;
    private final String NAME;

    /**
     * Constructor por defecto de la etiqueta
     */
    public Tag() {
        this.ID = -1;
        this.NAME = "";
    }

    /**
     * Constructor de la etiqueta con parámetros
     *
     * @param ID   ID de la etiqueta
     * @param name Nombre de la etiqueta
     */
    public Tag(int ID, String name) {
        this.ID = ID;
        this.NAME = name;
    }

    /**
     * Método para obtener el ID de la etiqueta
     *
     * @return ID de la etiqueta
     */
    public int getID() {
        return ID;
    }

    /**
     * Método para obtener el nombre de la etiqueta
     *
     * @return Nombre de la etiqueta
     */
    public String getName() {
        return NAME;
    }

    /**
     * Representación en forma de cadena de la etiqueta
     *
     * @return Cadena que representa la etiqueta
     */
    @Override
    public String toString() {
        return "Tag{" +
                "ID=" + ID +
                ", name='" + NAME + '\'' +
                '}';
    }
}
