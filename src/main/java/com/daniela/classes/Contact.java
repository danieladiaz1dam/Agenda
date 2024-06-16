package com.daniela.classes;

public class Contact {
    private final String BUTTONS = "<td>\n" +
            "                            <button type=\"button\" onclick=\"edit(%d)\"><span class=\"material-symbols-rounded\"> edit </span></button>\n" +
            "                            <button type=\"button\" onclick=\"remove(%d)\"><span class=\"material-symbols-rounded\"> delete </span></button>\n" +
            "                        </td>";
    private final int ID;
    private String name = "";
    private String phone = "";
    private String email = "";
    private boolean favorite = false;

    public Contact(int ID, String name, String phone, String email, boolean favorite) {
        this.ID = ID;
        if (name != null)
            this.name = name;
        if (phone != null)
            this.phone = phone;
        if (email != null)
            this.email = email;
        this.favorite = favorite;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String toTableRowAsContact() {
        String phoneNumber = "";

        if (this.phone.length() > 3)
            phoneNumber = Phone.formatPhoneNumber(this.phone.substring(0, 3), this.phone.substring(3));

        return String.format(
                """
                        <tr>
                            <td>%d</td>
                            <td><a href="?id=%d">%s</a></td>
                            <td>%s</td>
                            <td>%s</td>
                            <td>
                                <button type="button" onclick="edit(%d)"><span class="material-symbols-rounded"> edit </span></button>
                                <button type="button" onclick="remove(%d)"><span class="material-symbols-rounded"> delete </span></button>
                           </td>
                        </tr>
                        """,
                this.ID, this.ID, this.name, this.email,
                phoneNumber,
                this.ID, this.ID
        );
    }

    public String toTableRowAsMember() {
        String phoneNumber = "";

        if (this.phone.length() > 3)
            phoneNumber = Phone.formatPhoneNumber(this.phone.substring(0, 3), this.phone.substring(3));

        return String.format(
                """
                    <tr>
                        <td>%d</td>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%s</td>
                    </tr>
                    """, this.ID, this.name, this.email, phoneNumber
        );
    }

    @Override
    public String toString() {
        return "Contact{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
