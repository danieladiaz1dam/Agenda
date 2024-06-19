package com.daniela.agendadaniela;


import com.daniela.classes.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se comunica directamente con la base de datos
 */
public class AgendaDAO {
    /**
     * Objeto de tomcat que contiene los datos necesarios para una conexión con la base de datos
     */
    private final DataSource DATA_SOURCE;

    /**
     * Conexión obtenida después de procesar el dataSource
     */
    private Connection connection;

    /**
     * Constructor
     * Inicializa el dataSource
     *
     * @throws NamingException Es lanzada si no encuentra la "configuración" deseada
     */
    public AgendaDAO() throws NamingException {
        // Coger la conexión del archivo context.xml
        Context initContext = new InitialContext();
        this.DATA_SOURCE = (DataSource) initContext.lookup("java:comp/env/jdbc/agenda");
    }

    /**
     * Cierra la conexión con la base de datos
     *
     * @throws SQLException Es lanzada si se encuentra algún error al cerrar la base de datos
     */
    private void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed())
            connection.close();
    }

    /**
     * Recoge la conexión del dataSource para que se pueda usar y ejecutar queries
     *
     * @return true si se pudo conectar, false en caso contrario
     * @throws SQLException Es lanzada si ocurre algún error al acceder a la base de datos o al intentar conectarse
     */
    private boolean connect() throws SQLException {
        boolean res = false;

        if (connection == null || connection.isClosed()) {
            try {
                connection = DATA_SOURCE.getConnection();
                res = true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (connection != null && !connection.isClosed())
            res = true;

        return res;
    }

    /**
     * Devuelve una lista de contactos a partir de un ResultSet obtenido de una consulta anterior
     *
     * @param rs ResultSet de una consulta anteriór ya enviada y ejecutada
     * @return Lista de contactos, puede estar vacía
     * @throws SQLException Es lanzada si el ResultSet está cerrado o hay algún problema en la comunicación con
     *                      la base de datos
     */
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

    /**
     * Devuelve una lista con todos los contactos de la base de datos
     *
     * @return Lista de contactos, puede estar vacía
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
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

    /**
     * Devuelve una lista con todos los contactos
     *
     * @return Lista de contactos
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
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

    /**
     * Devuelve los miembros de un grupo
     *
     * @param ID Id del grupo
     * @return Lista de contactos
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
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

    /**
     * Devuelve los miembros que NO están en un grupo
     *
     * @param ID Id del grupo
     * @return Lista de contactos
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    public List<Contact> getGroupAvailableMembers(int ID) throws SQLException {
        List<Contact> contactList = new ArrayList<>();

        if (connect()) {
            PreparedStatement stmt = connection.prepareStatement("EXEC dbo.getGroupAvailableMembers ?");
            stmt.setInt(1, ID);
            ResultSet rs = stmt.executeQuery();

            contactList = getContacts(rs);

            rs.close();
            stmt.close();
            disconnect();
        } else System.out.println("Failed to connect!!");

        return contactList;
    }

    /**
     * Devuelve un grupo a partir de su ID
     *
     * @param ID Id del grupo
     * @return Objeto de tipo Grupo con sus detalles
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    public Group getGroup(int ID) throws SQLException {
        Group group = null;

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

    /**
     * Agrega un nuevo grupo con miembros
     *
     * @param group      Grupo para añadir
     * @param memberList Lista de ID de miembros separadas por comas
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    public void insertGroup(Group group, String memberList) throws SQLException {
        if (connect()) {
            String query = "exec dbo.addGroup ?, ?, ?;";
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, group.getName());
            stmt.setString(2, group.getDescription());
            stmt.setString(3, memberList);

            stmt.execute();

            stmt.close();
            disconnect();
        } else System.out.println("Failed to connect!!");
    }

    /**
     * Añade miembros nuevos a un grupo
     *
     * @param groupID    Id del grupo
     * @param memberList Lista de IDs de miembros, separadas por coma
     */
    public void insertGroupMembers(int groupID, String memberList) throws SQLException {
        if (connect()) {
            String query = "exec dbo.addGroupMembers  ?, ?;";
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, groupID);
            stmt.setString(2, memberList);

            stmt.execute();
        } else System.out.println("Failed to connect!!");
    }

    /**
     * Devuelve los detalles básicos de un contacto en específico.
     * Se usa en conjunto con otras funciones para obtener todos los detalles
     *
     * @param rs ResultSet de una consulta anteriór ya enviada y ejecutada
     * @param ID Id del contacto
     * @return Devuelve un objeto ContactDetails sin Emails, Phones o Addresses
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    private ContactDetails getContactBasicDetails(ResultSet rs, int ID) throws SQLException {
        String name;
        String birthday;
        String creationDate;
        boolean isFavorite;
        String tags;

        ContactDetails contactDetails;

        if (rs.next()) {
            name = rs.getString("name");
            birthday = rs.getString("birthday");
            creationDate = rs.getString("creationDate");
            isFavorite = rs.getBoolean("favorite");
            tags = rs.getString("tags");
            contactDetails = new ContactDetails(ID, name, birthday, creationDate, isFavorite, tags);
        } else contactDetails = null;

        return contactDetails;
    }

    /**
     * Añade la lista de emails a un contacto
     *
     * @param rs             ResultSet de una consulta anteriór ya enviada y ejecutada
     * @param contactDetails Objeto al que añadir la lista de emails
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
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

    /**
     * Añade la lista de teléfonos a un contacto
     *
     * @param rs             ResultSet de una consulta anteriór ya enviada y ejecutada
     * @param contactDetails Objeto al que añadir la lista de teléfonos
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
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

    /**
     * Añade la lista de direcciones a un contacto
     *
     * @param rs             ResultSet de una consulta anteriór ya enviada y ejecutada
     * @param contactDetails Objeto al que añadir la lista de direcciones
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
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
            zip = rs.getString("zip");
            city = rs.getString("city");
            category = rs.getString("category");

            contactDetails.getAddresses().add(new Address(id, street, houseNumber, zip, city, category));
        }
    }

    /**
     * Une todas las funciones anteriores para devolver todos los detalles completos de un contacto
     *
     * @param ID Id del contacto
     * @return Devuelve todos los detalles de un contacto
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
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
                            contactDetails = getContactBasicDetails(rs, ID);
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

    /**
     * Devuelve una lista de Tags disponibles en la base de datos
     *
     * @return Lista de Tags
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
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

    /**
     * Devuelve una lista de Tags disponibles para un contacto en concreto
     *
     * @param contactID Id del contacto
     * @return Devuelve una lista de Tags
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    public List<Tag> getAvailableTags(int contactID) throws SQLException {
        List<Tag> tagList = new ArrayList<>();

        int ID;
        String name;

        if (connect()) {
            PreparedStatement stmt = connection.prepareStatement("exec dbo.getAvailableTags ?");
            stmt.setInt(1, contactID);

            ResultSet rs = stmt.executeQuery();

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

    /**
     * Elimina un contacto o un grupo a partir de un Statement predefinido
     *
     * @param stmt Statement predefinido
     * @return true si se pudo eliminar el objeto, false en caso contrario
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    private boolean delete(PreparedStatement stmt) throws SQLException {
        boolean res = false;
        ResultSet rs = stmt.executeQuery();
        String dbResponse;

        if (rs.next()) {
            dbResponse = rs.getString(1);
            if (dbResponse != null && dbResponse.equals("ok"))
                res = true;
        }

        rs.close();
        return res;
    }

    /**
     * Elimina un contacto
     *
     * @param ID Id del contacto
     * @return true si se pudo eliminar, false en caso contrario
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    public boolean deleteContact(int ID) throws SQLException {
        boolean res = false;

        if (connect()) {
            PreparedStatement stmt = connection.prepareStatement("exec dbo.removeContact ?;");
            stmt.setInt(1, ID);

            res = delete(stmt);

            stmt.close();
            disconnect();
        } else System.out.println("Failed to connect!!");

        return res;
    }

    /**
     * Elimina un grupo
     *
     * @param ID Id del grupo
     * @return true si se pudo eliminar, false en caso contrario
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    public boolean deleteGroup(int ID) throws SQLException {
        boolean res = false;

        if (connect()) {
            PreparedStatement stmt = connection.prepareStatement("exec dbo.removeGroup ?;");
            stmt.setInt(1, ID);

            res = delete(stmt);

            stmt.close();
            disconnect();
        } else System.out.println("Failed to connect!!");

        return res;
    }

    /**
     * Inserta Emails a un contacto específico
     *
     * @param emails Lista de emails
     * @param id     Id del contacto
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    private void insertEmails(List<Email> emails, int id) throws SQLException {
        String query;
        PreparedStatement stmt;
        query = "insert into dbo.Email (idContact, email, category) VALUES (?, ?, ?)";
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

    /**
     * Inserta Phones a un contacto específico
     *
     * @param phones Lista de teléfonos
     * @param id     Id del contacto
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    private void insertPhones(List<Phone> phones, int id) throws SQLException {
        PreparedStatement stmt;
        String query;
        query = "insert into dbo.Phone (idContact, countryCode, phone, category) values (?, ?, ?, ?)";
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

    /**
     * Inserta Addresses a un contacto específico
     *
     * @param addresses Lista de direcciones
     * @param id        Id del contacto
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    private void insertAddresses(List<Address> addresses, int id) throws SQLException {
        String query;
        PreparedStatement stmt;
        query = "insert into dbo.Address (idContact, street, houseNumber, city, zip, category) values (?, ?, ?, ?, ?, ?)";
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

    /**
     * Inserta un contacto en la base de datos, con todos sus detalles
     *
     * @param contactDetails Detalles del contacto a insertar
     * @return true si se pudo insertar, false en caso contrario
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    public boolean insertContact(ContactDetails contactDetails) throws SQLException {
        boolean res = false;

        if (connect()) {
            // No hacer el commit hasta que yo lo diga
            connection.setAutoCommit(false);
            // Insertar contacto
            String query = "insert into dbo.Contact (name, birthday) values (?, ?)";
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

                    // Insertar Tags
                    query = "exec insertContactTags ?, ?;";
                    stmt = connection.prepareStatement(query);
                    stmt.setInt(1, id );
                    stmt.setString(2, contactDetails.getTags());

                    stmt.executeUpdate();

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

            connection.setAutoCommit(true);
            disconnect();
        } else System.out.println("Failed to connect!!");

        return res;
    }

    /**
     * Actualizar un contacto existente en la base de datos
     *
     * @param contactDetails Detalles nuevos para el contacto
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    public void updateContact(ContactDetails contactDetails, String deletedEmails, String deletedPhones, String deletedAddress) throws SQLException {

        if (connect()) {
            connection.setAutoCommit(false);

            // Update contact details
            String query = "exec updateContact ?, ?, ?, ?, ?, ?, ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, contactDetails.getID());
            stmt.setString(2, contactDetails.getName());
            stmt.setString(3, contactDetails.getBirthday());
            stmt.setBoolean(4, contactDetails.isFavorite());
            stmt.setString(5, deletedEmails);
            stmt.setString(6, deletedPhones);
            stmt.setString(7, deletedAddress);

            stmt.executeUpdate();

            // Update emails
            query = "exec updateEmail ?, ?, ?, ?";
            stmt = connection.prepareStatement(query);

            for (Email email : contactDetails.getEmails()) {
                stmt.setInt(1, contactDetails.getID());
                stmt.setInt(2, email.getID());
                stmt.setString(3, email.getEmail());
                stmt.setString(4, email.getCategory());
                stmt.addBatch();
            }
            stmt.executeBatch();

            // Update phones
            query = "exec updatePhone ?, ?, ?, ?, ?";
            stmt = connection.prepareStatement(query);

            for (Phone phone : contactDetails.getPhones()) {
                stmt.setInt(1, contactDetails.getID());
                stmt.setInt(2, phone.getID());
                stmt.setString(3, phone.getCountryCode());
                stmt.setString(4, phone.getNumber());
                stmt.setString(5, phone.getCategory());
                stmt.addBatch();
            }
            stmt.executeBatch();

            // Update addresses
            query = "exec updateAddress ?, ?, ?, ?, ?, ?, ?";
            stmt = connection.prepareStatement(query);

            for (Address address : contactDetails.getAddresses()) {
                stmt.setInt(1, contactDetails.getID());
                stmt.setInt(2, address.getID());
                stmt.setString(3, address.getStreet());
                stmt.setString(4, address.getHouseNumber());
                stmt.setString(5, address.getCity());
                stmt.setString(6, address.getZip());
                stmt.setString(7, address.getCategory());
                stmt.addBatch();
            }
            stmt.executeBatch();

            connection.commit();
            connection.setAutoCommit(true);
        } else System.out.println("Failed to connect!!");
        disconnect();
    }

    /**
     * Elimina a un contacto de un grupo
     *
     * @param groupID  Id del grupo
     * @param memberID Id del contacto
     * @throws SQLException Es lanzada si hay algún problema en la comunicación con la base de datos
     */
    public void removeGroupMember(int groupID, int memberID) throws SQLException {
        if (connect()) {
            String query = "exec dbo.removeGroupMember ?, ?;";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, groupID);
            stmt.setInt(2, memberID);
            stmt.execute();
            stmt.close();
            disconnect();
        }
    }
}