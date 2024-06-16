package com.daniela.classes;

public class Tag {
    private final int ID;
    private String name;

    public Tag() {
        this.ID = -1;
        this.name = "";
    }

    public Tag(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                '}';
    }
}
