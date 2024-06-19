package com.daniela.classes;

/**
 * Clase que representa un grupo
 */
public class Group {
    /**
     * Identificador único del grupo
     */
    private final int ID;

    /**
     * Nombre del grupo
     */
    private final String NAME;

    /**
     * Descripción del grupo
     */
    private final String DESCRIPTION;

    /**
     * Número de miembros en el grupo
     */
    private final int MEMBER_COUNT;

    /**
     * Constructor por defecto de la clase Group
     */
    public Group() {
        this.ID = -1;
        this.NAME = "";
        this.DESCRIPTION = "";
        this.MEMBER_COUNT = 0;
    }

    /**
     * Constructor de la clase Group con nombre y descripción
     *
     * @param name        Nombre del grupo
     * @param description Descripción del grupo
     */
    public Group(String name, String description) {
        this.ID = -1;
        this.NAME = name;
        this.DESCRIPTION = description;
        this.MEMBER_COUNT = 0;
    }

    /**
     * Constructor de la clase Group con ID, nombre y descripción
     *
     * @param id          Identificador único del grupo
     * @param name        Nombre del grupo
     * @param description Descripción del grupo
     */
    public Group(int id, String name, String description) {
        this.ID = id;
        this.NAME = name;
        this.DESCRIPTION = description;
        this.MEMBER_COUNT = 0;
    }

    /**
     * Constructor de la clase Group con ID, nombre, descripción y número de miembros
     *
     * @param id          Identificador único del grupo
     * @param name        Nombre del grupo
     * @param description Descripción del grupo
     * @param memberCount Número de miembros en el grupo
     */
    public Group(int id, String name, String description, int memberCount) {
        this.ID = id;
        this.NAME = name;
        this.DESCRIPTION = description;
        this.MEMBER_COUNT = memberCount;
    }

    /**
     * Método para obtener el identificador único del grupo
     *
     * @return ID del grupo
     */
    public int getId() {
        return ID;
    }

    /**
     * Método para obtener el nombre del grupo
     *
     * @return Nombre del grupo
     */
    public String getName() {
        return NAME;
    }

    /**
     * Método para obtener la descripción del grupo
     *
     * @return Descripción del grupo
     */
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * Método para obtener el número de miembros en el grupo
     *
     * @return Número de miembros del grupo
     */
    public int getMemberCount() {
        return MEMBER_COUNT;
    }

    /**
     * Genera una representación HTML del grupo en formato de tabla
     *
     * @return Representación HTML del grupo
     */
    public String toHTML() {
        return String.format(
                """
                        <tr>
                            <td>%s</td>
                            <td><a href="?id=%d">%s</a></td>
                            <td>%s</td>
                            <td>%d</td>
                        </tr>
                        """, this.ID, this.ID, this.NAME, this.DESCRIPTION, this.MEMBER_COUNT);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + ID +
                ", name='" + NAME + '\'' +
                ", description='" + DESCRIPTION + '\'' +
                ", memberCount='" + MEMBER_COUNT + '\'' +
                '}';
    }
}
