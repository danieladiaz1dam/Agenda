package com.daniela.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactDetails {
    private final int ID;
    private String name = "";
    private String birthday = "";
    private String creationDate = "";
    private boolean isFavorite = false;
    private String tags = "";
    private List<Email> emails = new ArrayList<>();
    private List<Phone> phones = new ArrayList<>();
    private List<Address> addresses = new ArrayList<>();

    public ContactDetails() {
        this.ID = 0;
    }

    public ContactDetails(int ID, String name, String birthday, String creationDate, boolean isFavorite, String tags) {
        this.ID = ID;
        this.name = name;
        this.birthday = birthday;
        this.creationDate = creationDate;
        this.isFavorite = isFavorite;
        this.tags = tags;
    }

    public ContactDetails(String name, String birthday, boolean isFavorite, String tags, ArrayList<Email> emails, ArrayList<Phone> phones, ArrayList<Address> addresses) {
        this.ID = -1;
        this.name = name;
        this.birthday = birthday;
        this.isFavorite = isFavorite;
        this.tags = tags;
        this.creationDate = null;
        this.emails = emails;
        this.phones = phones;
        this.addresses = addresses;
    }

    public ContactDetails(int ID, String name, String birthday, String creationDate, boolean isFavorite, ArrayList<Email> emails, ArrayList<Phone> phones, ArrayList<Address> addresses) {
        this.ID = ID;
        this.name = name;
        this.birthday = birthday;
        this.creationDate = creationDate;
        this.isFavorite = isFavorite;
        this.emails = emails;
        this.phones = phones;
        this.addresses = addresses;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getTags() {
        return tags;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public String toHTML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Contact Details</h1>");
        sb.append("<p>ID: ").append(ID).append("</p>");
        sb.append("<p>Name: ").append(name).append("</p>");
        sb.append("<p>Birthday: ").append(birthday).append("</p>");
        sb.append("<p>Creation Date: ").append(creationDate).append("</p>");

        sb.append("<h2>Phones</h2>");
        if (!phones.isEmpty()) {
            for (Phone phone : phones) {
                sb.append(phone.toHTML());
            }
        }

        sb.append("<h2>Emails</h2>");
        if (!emails.isEmpty()) {
            for (Email email : emails) {
                sb.append(email.toHTML());
            }
        }

        sb.append("<h2>Addresses</h2>");
        if (!addresses.isEmpty()) {
            for (Address address : addresses) {
                sb.append(address.toHTML());
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        String[] tagList = {""};

        if (this.tags != null && !this.tags.isEmpty())
            tagList = tags.split(",");

        return "ContactDetails{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", creationDate=" + creationDate +
                ", isFavorite=" + isFavorite +
                ", tags=" + Arrays.toString(tagList) +
                ", phones=" + phones +
                ", addresses=" + addresses +
                ", emails=" + emails +
                '}';
    }
}
