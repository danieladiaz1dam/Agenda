package com.daniela.agendadaniela;

import com.daniela.classes.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;

public class SvController extends HttpServlet {
    /**
     * Objeto DAO encargado de las operaciones con la base de datos
     *
     * @see AgendaDAO
     */
    private AgendaDAO agendaDAO;

    /**
     * Dispatcher, utilizado para hacer redirections y pasar datos a la nueva página
     *
     * @see RequestDispatcher
     */
    private RequestDispatcher dispatcher;

    /**
     * Se usa para escribir en la response usando un println(), pero no tiene format de forma nativa :/
     *
     * @see PrintWriter
     */
    private PrintWriter out;

    /**
     * Inicializa el servlet, inicializando consigo el objeto encargado de la manipulación de la base de datos
     *
     * @throws ServletException Es lanzada si no se encuentra la configuración de la base de datos
     */
    @Override
    public void init() throws ServletException {
        try {
            agendaDAO = new AgendaDAO();
        } catch (NamingException e) {
            throw new ServletException(e);
        }
    }

    /**
     * Encargada de las peticiones POST
     *
     * @param req  an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws ServletException Es lanzada por alguna función
     * @throws IOException      Es lanzada por alguna función
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();

        resp.setContentType("text/html");
        out = resp.getWriter();

        switch (action) {
            case "/addContact":
                addContact(req, resp);
                break;
            case "/updateContact":
                updateContact(req, resp);
                break;
            case "/addGroup":
                addGroup(req, resp);
                break;
            case "/addGroupMembers":
                addGroupMembers(req, resp);
            default:
                out.printf("<h1>%s</h1>", action);
                Enumeration<String> params = req.getParameterNames();
                while (params.hasMoreElements()) {
                    String paramName = params.nextElement();
                    out.println(paramName + " = " + req.getParameter(paramName) + "<br>");
                }
        }
    }

    /**
     * Encargada de las peticiones GET
     *
     * @param req  an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws ServletException Es lanzada por alguna función
     * @throws IOException      Es lanzada por alguna función
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();
        boolean args = !req.getParameterMap().isEmpty();

        resp.setContentType("text/html");
        out = resp.getWriter();

        switch (action) {
            case "/":
                resp.sendRedirect(req.getContextPath() + "/contacts");
                break;
            case "/contacts":
                listContacts(req, resp, args);
                break;
            case "/editContact":
                editContact(req, resp);
                break;
            case "/groups":
                listGroups(req, resp, args);
                break;
            case "/newContact":
                newContact(req, resp);
                break;
            case "/about":
                dispatcher = req.getRequestDispatcher("pages/about.jsp");
                dispatcher.forward(req, resp);
                break;
            default:
                out.printf("<h1>%s</h1>", action);
                if (args) {
                    Enumeration<String> params = req.getParameterNames();
                    while (params.hasMoreElements()) {
                        String paramName = params.nextElement();
                        out.println(paramName + " = " + req.getParameter(paramName) + "<br>");
                    }
                }
        }
    }

    private void updateContact(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //out.println("<pre>");

        // --- Obtener todos los datos del formulario ---
        int ID = Util.parseInt(req.getParameter("contactID"));

        boolean isFavorite = req.getParameter("favorite") != null;
        // out.println("isFavorite = " + isFavorite);

        String name = req.getParameter("name");
        //out.println("name = " + name);

        String tagList = req.getParameter("tagList");
        //out.println("tagList = " + tagList);

        Integer day = Util.parseInt(req.getParameter("day"));
        Integer month = Util.parseInt(req.getParameter("month"));
        Integer year = Util.parseInt(req.getParameter("year"));
        String date;

        if (day == null || month == null || year == null)
            date = null;
        else
            date = String.format("%4d-%02d-%02d", year, month, day);

        /*out.println("date = " + date);*/

        String deletedEmails = Util.removeLastChar(req.getParameter("deletedEmails"));
        String deletedPhones = Util.removeLastChar(req.getParameter("deletedPhones"));
        String deletedAddresses = Util.removeLastChar(req.getParameter("deletedAddresses"));

     /*   out.println("deletedEmails = " + deletedEmails);
        out.println("deletedPhones = " + deletedPhones);
        out.println("deletedAddress = " + deletedAddresses);*/

        String[] emailIDs = req.getParameterValues("emailIDs");
        String[] emails = req.getParameterValues("emails");
        String[] emailCategories = req.getParameterValues("emailCategories");
        ArrayList<Email> emailList = Util.emailsFromArrays(emailIDs, emails, emailCategories);

        String[] phoneIDs = req.getParameterValues("phoneIDs");
        String[] countryCodes = req.getParameterValues("countryCodes");
        String[] phones = req.getParameterValues("phones");
        String[] phonesCategories = req.getParameterValues("phoneCategories");
        ArrayList<Phone> phoneList = Util.phonesFromArrays(phoneIDs, countryCodes, phones, phonesCategories);

        String[] addressIDs = req.getParameterValues("addressIDs");
        String[] streets = req.getParameterValues("streets");
        String[] houseNumbers = req.getParameterValues("houseNumbers");
        String[] cities = req.getParameterValues("cities");
        String[] zipCodes = req.getParameterValues("zipCodes");
        String[] addressCategories = req.getParameterValues("addressCategories");
        ArrayList<Address> addressList = Util.addressesFromArrays(addressIDs, streets, houseNumbers, cities, zipCodes, addressCategories);

        /*out.println("New emailList = " + emailList);
        out.println("New phoneList = " + phoneList);
        out.println("New addressList = " + addressList);

        out.println("</pre>");*/

        // Crear objeto con esos datos
        ContactDetails newContact = new ContactDetails(ID, name, date, isFavorite, tagList, emailList, phoneList, addressList);

        try {
            agendaDAO.updateContact(newContact, deletedEmails, deletedPhones, deletedAddresses);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        resp.sendRedirect(req.getContextPath() + "/contacts?id=" + newContact.getID());
    }

    /**
     * Recoge los datos necesarios y redirecciona a la página para editar un contacto
     *
     * @param req  Request Object
     * @param resp Response Object
     * @throws ServletException Lanzada por el servlet cuando algo falla
     * @throws IOException      Lanzada si no se encuentra el archivo jsp
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    private void editContact(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") != null) {
            ContactDetails contactDetails;
            List<Tag> availableTags;

            try {
                contactDetails = agendaDAO.getContact(Integer.parseInt(req.getParameter("id")));
                availableTags = agendaDAO.getAvailableTags(contactDetails.getID());
            } catch (SQLException e) {
                throw new ServletException(e);
            }

            if (availableTags != null) {
                req.setAttribute("contactDetails", contactDetails);
                req.setAttribute("availableTags", availableTags);
                dispatcher = req.getRequestDispatcher("pages/editContact.jsp");
                dispatcher.forward(req, resp);
            } else
                resp.sendRedirect(req.getContextPath() + "/contacts");
        } else
            resp.sendRedirect(req.getContextPath() + "/contacts");
    }

    /**
     * Recoge los datos necesarios para añadir contacto(s) a un grupo en la base de datos
     * Luego recarga la página
     *
     * @param req  Request Object
     * @param resp Response Object
     * @throws ServletException Lanzada por el servlet cuando algo falla
     * @throws IOException      Lanzada si no se encuentra el archivo jsp
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    private void addGroupMembers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int groupID = Util.parseInt(req.getParameter("groupId"));
        String[] membersArray = req.getParameterValues("members");
        String members = membersArray == null ? "" : String.join(",", membersArray);

        try {
            agendaDAO.insertGroupMembers(groupID, members);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        resp.sendRedirect(req.getContextPath() + "/groups?id=" + groupID);
    }

    /**
     * Recoge los datos necesarios para crear un grupo nuevo en la base de datos
     * Luego recarga la página
     *
     * @param req  Request Object
     * @param resp Response Object
     * @throws ServletException Lanzada por el servlet cuando algo falla
     * @throws IOException      Lanzada si no se encuentra el archivo jsp
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    private void addGroup(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String[] membersArray = req.getParameterValues("members");
        String members = membersArray == null ? "" : String.join(",", membersArray);
        Group group = new Group(name, description);

        try {
            agendaDAO.insertGroup(group, members);
            resp.sendRedirect(req.getContextPath() + "/groups");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    /**
     * Recoge los datos necesarios para crear un contacto en la base de datos.
     * También añade sus emails, teléfonos y direcciones
     *
     * @param req  Request Object
     * @param resp Response Object
     * @throws ServletException Lanzada por el servlet cuando algo falla
     * @throws IOException      Lanzada si no se encuentra el archivo jsp
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    private void addContact(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // --- Obtener todos los datos del formulario ---
        boolean isFavorite = req.getParameter("favorite") != null;

        String name = req.getParameter("name");

        String tagList = req.getParameter("tagList");

        Integer day = Util.parseInt(req.getParameter("day"));
        Integer month = Util.parseInt(req.getParameter("month"));
        Integer year = Util.parseInt(req.getParameter("year"));
        String date;

        if (day == null || month == null || year == null)
            date = null;
        else
            date = String.format("%4d-%02d-%02d", year, month, day);

        String[] emails = req.getParameterValues("emails");
        String[] emailCategories = req.getParameterValues("emailCategories");
        ArrayList<Email> emailList = Util.emailsFromArrays(emails, emailCategories);

        String[] countryCodes = req.getParameterValues("countryCodes");
        String[] phones = req.getParameterValues("phones");
        String[] phonesCategories = req.getParameterValues("phoneCategories");
        ArrayList<Phone> phoneList = Util.phonesFromArrays(countryCodes, phones, phonesCategories);

        String[] streets = req.getParameterValues("streets");
        String[] houseNumbers = req.getParameterValues("houseNumbers");
        String[] cities = req.getParameterValues("cities");
        String[] zipCodes = req.getParameterValues("zipCodes");
        String[] addressCategories = req.getParameterValues("addressCategories");
        ArrayList<Address> addressList = Util.addressesFromArrays(streets, houseNumbers, zipCodes, cities, addressCategories);
        // --- Obtener todos los datos del formulario ---

        // Crear objeto con esos datos
        ContactDetails newContact = new ContactDetails(name, date, isFavorite, tagList, emailList, phoneList, addressList);

        // Agregar contacto
        try {
            if (agendaDAO.insertContact(newContact)) {
                req.setAttribute("status", true);
                req.setAttribute("name", newContact.getName());
            } else {
                req.setAttribute("status", false);
                req.setAttribute("name", newContact.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("status", false);
            req.setAttribute("name", newContact.getName());
            req.setAttribute("error", e.getMessage());
        }

        dispatcher = req.getRequestDispatcher("pages/status/newContactStatus.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * Recoge los datos necesarios de la base de datos para mostrar la página para añadir un nuevo contacto
     *
     * @param req  Request Object
     * @param resp Response Object
     * @throws ServletException Lanzada por el servlet cuando algo falla
     * @throws IOException      Lanzada si no se encuentra el archivo jsp
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    private void newContact(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Tag> tagList = agendaDAO.getAllTags();
            req.setAttribute("tagList", tagList);
            dispatcher = req.getRequestDispatcher("pages/newContact.jsp");
            dispatcher.forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    /**
     * Recoge los datos necesarios de la base de datos y redirecciona a la página para ver los detalles y miembros de un grupo
     *
     * @param req  Request Object
     * @param resp Response Object
     * @throws ServletException Lanzada por el servlet cuando algo falla
     * @throws IOException      Lanzada si no se encuentra el archivo jsp
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    private void getGroupDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") != null) {
            Group group;

            try {
                group = agendaDAO.getGroup(Integer.parseInt(req.getParameter("id")));
            } catch (SQLException e) {
                throw new ServletException(e);
            }

            if (group != null) {
                try {
                    List<Contact> memberList = agendaDAO.getGroupMembers(Integer.parseInt(req.getParameter("id")));
                    List<Contact> availableMembersList = agendaDAO.getGroupAvailableMembers(Integer.parseInt(req.getParameter("id")));

                    req.setAttribute("memberList", memberList);
                    req.setAttribute("availableMembersList", availableMembersList);
                    req.setAttribute("group", group);

                    dispatcher = req.getRequestDispatcher("pages/groupDetails.jsp");
                    dispatcher.forward(req, resp);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            } else
                resp.sendRedirect(req.getContextPath() + "/groups");
        } else
            resp.sendRedirect(req.getContextPath() + "/groups");
    }

    /**
     * Si se piden detalles, llama a getGroupDetails. De lo contrario, Recoge los datos necesarios de la base de datos
     * para la página de los grupos
     *
     * @param req  Request Object
     * @param resp Response Object
     * @throws ServletException Lanzada por el servlet cuando algo falla
     * @throws IOException      Lanzada si no se encuentra el archivo jsp
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    private void listGroups(HttpServletRequest req, HttpServletResponse resp, boolean details) throws ServletException, IOException {
        if (details && req.getParameter("id") != null) {
            getGroupDetails(req, resp);
        } else {
            try {
                List<Group> groupList = agendaDAO.getAllGroups();
                List<Contact> contactList = agendaDAO.getAllContacts();

                req.setAttribute("groupList", groupList);
                req.setAttribute("contactList", contactList);

                dispatcher = req.getRequestDispatcher("pages/listGroups.jsp");
                dispatcher.forward(req, resp);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
    }

    /**
     * Recoge los datos necesarios de la base de datos y redirecciona a la página de los datos de un contacto.
     * Si no se encontró el contacto, redirecciona a la página de listar contactos
     *
     * @param req  Request Object
     * @param resp Response Object
     * @throws ServletException Lanzada por el servlet cuando algo falla
     * @throws IOException      Lanzada si no se encuentra el archivo jsp
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    private void getContactsDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") != null) {
            ContactDetails contactDetails;
            try {
                contactDetails = agendaDAO.getContact(Integer.parseInt(req.getParameter("id")));
            } catch (SQLException e) {
                throw new ServletException(e);
            }
            if (contactDetails != null) {
                req.setAttribute("contactDetails", contactDetails);
                dispatcher = req.getRequestDispatcher("pages/contactDetails.jsp");
                dispatcher.forward(req, resp);
            } else
                resp.sendRedirect(req.getContextPath() + "/contacts");
        } else
            resp.sendRedirect(req.getContextPath() + "/contacts");
    }

    /**
     * Si se piden detalles, llama a getContactsDetails. De lo contrario, Recoge los datos necesarios de la base de datos
     * para la página de los contactos
     *
     * @param req  Request Object
     * @param resp Response Object
     * @throws ServletException Lanzada por el servlet cuando algo falla
     * @throws IOException      Lanzada si no se encuentra el archivo jsp
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    private void listContacts(HttpServletRequest req, HttpServletResponse resp, boolean details) throws ServletException, IOException {
        if (details)
            getContactsDetails(req, resp);
        else {
            List<Contact> contactList;
            try {
                contactList = agendaDAO.getAllContacts();
            } catch (SQLException e) {
                throw new ServletException(e);
            }
            req.setAttribute("contactList", contactList);

            dispatcher = req.getRequestDispatcher("pages/listContacts.jsp");
            dispatcher.forward(req, resp);
        }
    }

    /**
     * Encargada de las peticiones DELETE
     *
     * @param req  the {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws IOException Es lanzada por alguna función
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getServletPath();

        // Si nos pasan un ID válido (un número)
        switch (action) {
            case "/deleteContact":
                deleteContact(req, resp);
                break;
            case "/deleteGroup":
                deleteGroup(req, resp);
                break;
            case "/removeGroupMember":
                removeGroupMember(req, resp);
                break;
            default:
                System.out.println(action);
                out.println("u ok??");
        }
    }

    /**
     * Maneja una petición delete, recogiendo sus datos, comunicándose con la base de datos y devolviendo una respuesta
     * de si se pudo eliminar el contacto, si hubo algún error o si no se encontró
     *
     * @param req  Request Object
     * @param resp Response Object
     * @throws IOException Lanzada si no se encuentra el archivo jsp
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    private void deleteContact(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        out = resp.getWriter();
        int id = Util.parseInt(req.getParameter("id"));
        try {
            if (agendaDAO.deleteContact(id)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write("Contacto eliminado exitosamente");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Contacto no encontrado");
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error al eliminar el contacto: " + e.getMessage());
        }
    }

    /**
     * Maneja una petición delete, recogiendo sus datos, comunicándose con la base de datos y devolviendo una respuesta
     * de si se pudo eliminar el grupo, si hubo algún error o si no se encontró
     *
     * @param req  Request Object
     * @param resp Response Object
     * @throws IOException Lanzada si no se encuentra el archivo jsp
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    private void deleteGroup(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        out = resp.getWriter();
        int id = Util.parseInt(req.getParameter("id"));
        try {
            if (agendaDAO.deleteGroup(id)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write("Grupo eliminado exitosamente");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Contact not found");
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error deleting contact: " + e.getMessage());
        }
    }

    /**
     * Maneja una petición delete, recogiendo sus datos, comunicándose con la base de datos y devolviendo una respuesta
     * de si se pudo eliminar el contacto del grupo, si hubo algún error o si no se encontró
     *
     * @param req  Request Object
     * @param resp Response Object
     * @throws IOException Lanzada si no se encuentra el archivo jsp
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    private void removeGroupMember(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer groupID = Util.parseInt(req.getParameter("groupID"));
        Integer memberID = Util.parseInt(req.getParameter("memberID"));

        if (groupID == null || memberID == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Invalid IDs");
        } else {
            try {
                agendaDAO.removeGroupMember(groupID, memberID);
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Error al eliminar al miembro: " + e.getMessage());
            }
        }
    }
}
