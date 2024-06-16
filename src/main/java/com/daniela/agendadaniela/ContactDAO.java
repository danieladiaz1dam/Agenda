package com.daniela.agendadaniela;

import com.daniela.classes.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    private final DataSource dataSource;
    private Connection connection;

    public ContactDAO() throws NamingException {
        // Coger la conexión del archivo context.xml
        Context initContext = new InitialContext();
        this.dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/agenda");
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed())
            connection.close();
    }

    public boolean connect() throws SQLException {
        boolean res = false;

        if (connection == null || connection.isClosed()) {
            try {
                connection = dataSource.getConnection();
                res = true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return res;
    }

    private List<Contact> getContacts(ResultSet rs) throws SQLException {
        List<Contact> contactList = new ArrayList<>();

        int ID;
        String name;
        String email;
        String phone;
        boolean favorite;

        while (rs.next()) {
            ID = rs.getInt("ID");
            name = rs.getString("name");
            email = rs.getString("Email");
            phone = rs.getString("Phone");
            favorite = rs.getBoolean("favorite");

            contactList.add(new Contact(ID, name, phone, email, favorite));
        }

        return contactList;
    }

    public List<Contact> getAllContacts() throws SQLException {
        List<Contact> contactList = new ArrayList<>();

        if (connect()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from dbo.getContacts()");

            contactList = getContacts(rs);

            rs.close();
            stmt.close();
            disconnect();
        } else System.out.println("Failed to connect!!");

        return contactList;
    }

    private ContactDetails getContact(ResultSet rs, int ID) throws SQLException {
        String name;
        String birthday;
        String creationDate;
        boolean isFavorite;

        ContactDetails contactDetails;

        if (rs.next()) {
            name = rs.getString("name");
            birthday = rs.getString("birthday");
            creationDate = rs.getString("creationDate");
            isFavorite = rs.getBoolean("favorite");
            contactDetails = new ContactDetails(ID, name, birthday, creationDate, isFavorite);
        } else contactDetails = null;

        return contactDetails;
    }

    public List<Group> getAllGroups() throws SQLException {
        List<Group> groupList = new ArrayList<>();

        int ID;
        String name;
        String description;
        int members;

        if (connect()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("EXEC dbo.getGroups");

            while (rs.next()) {
                ID = rs.getInt("ID");
                name = rs.getString("name");
                description = rs.getString("description");
                members = rs.getInt("members");
                groupList.add(new Group(ID, name, description, members));
            }

            rs.close();
            stmt.close();
            disconnect();
        } else System.out.println("Failed to connect!!");

        return groupList;
    }

    public List<Contact> getGroupMembers(int ID) throws SQLException {
        List<Contact> contactList = new ArrayList<>();

        if (connect()) {
            PreparedStatement stmt = connection.prepareStatement("EXEC dbo.getGroupMembers ?");
            stmt.setInt(1, ID);
            ResultSet rs = stmt.executeQuery();

            contactList = getContacts(rs);

            rs.close();
            stmt.close();
            disconnect();
        } else System.out.println("Failed to connect!!");

        return contactList;
    }

    public Group getGroup(int ID) throws SQLException {
        Group group = new Group();

        int groupID;
        String name;
        String description;

        if (connect()) {
            PreparedStatement stmt = connection.prepareStatement("EXEC dbo.getGroup ?");
            stmt.setInt(1, ID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                groupID = rs.getInt("ID");
                name = rs.getString("name");
                description = rs.getString("description");

                group = new Group(groupID, name, description);
            }

            rs.close();
            stmt.close();
            disconnect();
        } else System.out.println("Failed to connect!!");

        return group;
    }

    private void getPhones(ResultSet rs, ContactDetails contactDetails) throws SQLException {
        int id;
        String countryCode;
        String phone;
        String category;

        while (rs.next()) {
            id = rs.getInt("phoneID");
            countryCode = rs.getString("countryCode");
            phone = rs.getString("phone");
            category = rs.getString("category");

            contactDetails.getPhones().add(new Phone(id, countryCode, phone, category));
        }
    }

    private void getEmails(ResultSet rs, ContactDetails contactDetails) throws SQLException {
        int id;
        String email;
        String category;

        while (rs.next()) {
            id = rs.getInt("emailID");
            email = rs.getString("email");
            category = rs.getString("category");

            contactDetails.getEmails().add(new Email(id, email, category));
        }
    }

    private void getAddresses(ResultSet rs, ContactDetails contactDetails) throws SQLException {
        int id;
        String street;
        String houseNumber;
        String city;
        String zip;
        String category;

        while (rs.next()) {
            id = rs.getInt("addressID");
            street = rs.getString("street");
            houseNumber = rs.getString("houseNumber");
            city = rs.getString("city");
            zip = rs.getString("zip");
            category = rs.getString("category");

            contactDetails.getAddresses().add(new Address(id, street, houseNumber, city, zip, category));
        }
    }

    public ContactDetails getContact(int ID) throws SQLException {
        ContactDetails contactDetails = new ContactDetails();

        if (connect()) {
            PreparedStatement stmt = connection.prepareStatement("EXEC dbo.getContactDetails ?");
            stmt.setInt(1, ID);
            boolean hasResults = stmt.execute();
            int resultSetCounter = 1;

            while (contactDetails != null && hasResults) {
                try (ResultSet rs = stmt.getResultSet()) {
                    switch (resultSetCounter) {
                        // Primer conjunto, Detalles del contacto
                        case 1:
                            contactDetails = getContact(rs, ID);
                            break;
                        // Segundo conjunto, Teléfonos del Contacto
                        case 2: {
                            getPhones(rs, contactDetails);
                            break;
                        }
                        // Tercer conjunto, Emails
                        case 3: {
                            getEmails(rs, contactDetails);
                            break;
                        }
                        // Último conjunto, Direcciones
                        case 4: {
                            getAddresses(rs, contactDetails);
                            break;
                        }
                    }
                }
                hasResults = stmt.getMoreResults();
                resultSetCounter++;
            }

            disconnect();
        } else System.out.println("Failed to connect!!");

        return contactDetails;
    }

    public List<Tag> getAllTags() throws SQLException {
        List<Tag> tagList = new ArrayList<>();

        int ID;
        String name;

        if (connect()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("exec dbo.getTags");

            while (rs.next()) {
                ID = rs.getInt("ID");
                name = rs.getString("name");

                tagList.add(new Tag(ID, name));
            }

            rs.close();
            stmt.close();
            disconnect();
        } else System.out.println("Failed to connect!!");

        return tagList;
    }

    public boolean deleteContact(int ID) throws SQLException {
        boolean res = false;

        if (connect()) {
            PreparedStatement stmt = connection.prepareStatement("exec dbo.removeContact ?;");
            stmt.setInt(1, ID);

            ResultSet rs = stmt.executeQuery();
            String dbResponse;

            if (rs.next()) {
                dbResponse = rs.getString(1);

                if (dbResponse.equals("ok")) res = true;
            }

            rs.close();
            stmt.close();
            disconnect();
        } else System.out.println("Failed to connect!!");

        return res;
    }

    private void insertEmails(List<Email> emails, int id) throws SQLException {
        String query;
        PreparedStatement stmt;
        query = "INSERT INTO dbo.Email (idContact, email, category) VALUES (?, ?, ?)";
        stmt = connection.prepareStatement(query);

        for (Email email : emails) {
            stmt.setInt(1, id);
            stmt.setString(2, email.getEmail());
            stmt.setString(3, email.getCategory());
            // Añadir los valores al lote, así solo se ejecutará una sola query
            stmt.addBatch();
        }
        stmt.executeBatch();
        stmt.close();
    }

    private void insertPhones(List<Phone> phones, int id) throws SQLException {
        PreparedStatement stmt;
        String query;
        query = "INSERT INTO dbo.Phone (idContact, countryCode, phone, category) VALUES (?, ?, ?, ?)";
        stmt = connection.prepareStatement(query);

        for (Phone phone : phones) {
            stmt.setInt(1, id);
            stmt.setString(2, phone.getCountryCode());
            stmt.setString(3, phone.getNumber());
            stmt.setString(4, phone.getCategory());
            // Añadir los valores al lote, así solo se ejecutará una sola query
            stmt.addBatch();
        }
        stmt.executeBatch();
        stmt.close();
    }

    private void insertAddresses(List<Address> addresses, int id) throws SQLException {
        String query;
        PreparedStatement stmt;
        query = "INSERT INTO dbo.Address (idContact, street, houseNumber, city, zip, category) VALUES (?, ?, ?, ?, ?, ?)";
        stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        for (Address address : addresses) {
            stmt.setInt(1, id);
            stmt.setString(2, address.getStreet());
            stmt.setString(3, address.getHouseNumber());
            stmt.setString(4, address.getCity());
            stmt.setString(5, address.getZip());
            stmt.setString(6, address.getCategory());
            // Añadir los valores al lote, así solo se ejecutará una sola query
            stmt.addBatch();
        }
        stmt.executeBatch();
        stmt.close();
    }

    public boolean insertContact(ContactDetails contactDetails) throws SQLException {
        boolean res = false;

        if (connect()) {
            // No hacer el commit hasta que yo lo diga
            connection.setAutoCommit(false);
            // Insertar contacto
            String query = "INSERT INTO dbo.Contact (name, birthday) VALUES (?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs;
            int id;

            stmt.setString(1, contactDetails.getName());
            stmt.setString(2, contactDetails.getBirthday());

            // Si se ha insertado / Si hay alguna fila afectada
            if (stmt.executeUpdate() > 0) {
                rs = stmt.getGeneratedKeys();

                // Recoger el ID del nuevo contacto insertado
                if (rs.next()) {
                    id = rs.getInt(1);
                    System.out.printf("Contact %s was added to the database with ID: %d\n", contactDetails.getName(), id);
                    stmt.close();

                    // Insert Emails
                    insertEmails(contactDetails.getEmails(), id);

                    // Insert Phones
                    insertPhones(contactDetails.getPhones(), id);

                    // Insert Addresses
                    insertAddresses(contactDetails.getAddresses(), id);

                    // Una vez esté correctamente insertado, hacer el commit
                    connection.commit();
                    res = true;
                } else {
                    // Si no se pudo obtener el ID del contacto, hacer rollback
                    connection.rollback();
                }

                rs.close();
                stmt.close();
            }


            disconnect();
        } else System.out.println("Failed to connect!!");

        connection.setAutoCommit(true);
        return res;
    }

    public boolean updateContact(ContactDetails contactDetails) throws SQLException {
        boolean res = false;

        if (connect()) {
            // todo hacer el update
            System.out.println("todo");
        } else System.out.println("Failed to connect!!");

        return res;
    }
}
