package com.daniela.classes;

public class Email {
    private final int ID;
    private String email;
    private String category;

    public Email(int ID, String email, String category) {
        this.ID = ID;
        this.email = email;
        this.category = category;
    }

    public Email(String email, String category) {
        this.ID = -1;
        this.email = email;
        this.category = category;
    }

    public String getEmail() {
        return email;
    }

    public String getCategory() {
        return category;
    }

    public String toHTML() {
        return "<p>Email ID: " + ID + ", Email: " + email + ", Category: " + category + "</p>";
    }

    @Override
    public String toString() {
        return String.format("%s · %s", email, category);
    }
}
