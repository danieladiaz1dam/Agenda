package com.daniela.classes;

public class Address {
    private final int ID;
    private String street;
    private String houseNumber;
    private String zip;
    private String city;
    private String category;

    public Address(int ID, String street, String number, String zip, String city, String category) {
        this.ID = ID;
        this.street = street;
        this.houseNumber = number;
        this.zip = zip;
        this.city = city;
        this.category = category;
    }

    public Address(String street, String number, String zip, String city, String category) {
        this.ID = -1;
        this.street = street;
        this.houseNumber = number;
        this.zip = zip;
        this.city = city;
        this.category = category;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getCategory() {
        return category;
    }

    public String toHTML() {
        return "<p>Address ID: " + ID + ", Street: " + street + ", House Number: " + houseNumber + ", City: " + city + ", ZIP: " + zip + ", Category: " + category + "</p>";
    }

    @Override
    public String toString() {
        return String.format("C. %s, %s %s, %s · %s", this.street, this.houseNumber, this.zip, this.city, this.category);
    }
}
