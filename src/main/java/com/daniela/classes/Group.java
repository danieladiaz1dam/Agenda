package com.daniela.classes;

public class Group {
    private final int id;
    private String name;
    private String description;
    private int memberCount = 0;

    public Group() {
        this.id = -1;
    }

    public Group(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Group(int id, String name, String description, int memberCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.memberCount = memberCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public String toHTML() {
        return String.format(
        """
            <tr>
                <td>%s</td>
                <td><a href="?id=%d">%s</a></td>
                <td>%s</td>
                <td>%d</td>
            </tr>
        """, this.id, this.id, this.name, this.description, this.memberCount);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", memberCount='" + memberCount + '\'' +
                '}';
    }
}
