package com.daniela.classes;

public class Phone {
    private final int ID;
    private String countryCode;
    private String number;
    private String category;

    public Phone(int ID, String countryCode, String number, String category) {
        this.ID = ID;
        this.countryCode = countryCode;
        this.number = number;
        this.category = category;
    }

    public Phone(String countryCode, String number, String category) {
        this.ID = -1;
        this.countryCode = countryCode;
        this.number = number;
        this.category = category;
    }

    public Phone(String phone) {
        this.ID = -1;
        this.countryCode = phone.substring(0, 3);
        this.number = phone.substring(3);
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getNumber() {
        return number;
    }

    public String getCategory() {
        return category;
    }

    public static String formatPhoneNumber(String countryCode, String number) {
        return "+" + countryCode + formatPhoneNumber(number);
    }

    public static String formatPhoneNumber(String number) {
        return String.format("%s %s %s %s", number.substring(0, 3), number.substring(3, 5), number.substring(5, 7), number.substring(7));
    }

    public String toHTML() {
        return "<p>Phone ID: " + ID + ", Country Code: " + countryCode + ", Number: " + formatPhoneNumber(number) + ", Category: " + category + "</p>";
    }

    @Override
    public String toString() {
        return String.format("+%s %s · %s", this.countryCode, formatPhoneNumber(number), this.category);
    }
}
