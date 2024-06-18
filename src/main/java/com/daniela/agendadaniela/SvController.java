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
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;

public class SvController extends HttpServlet {
    /**
     * Objeto DAO encargado de las operaciones con la base de datos
     */
    private AgendaDAO agendaDAO;

    /**
     * Dispatcher, utilizado para hacer redirections y pasar datos a la nueva página
     */
    private RequestDispatcher dispatcher;

    /**
     * Se usa para escribir en la response usando un println(), pero no tiene format de forma nativa :/
     */
    private PrintWriter out;

    /**
     * Inicializa el servlet, inicializando consigo el objeto encargado de la manipulación de la base de datos
     *
     * @throws ServletException Es lanzada si se no se encuentra la configuración de la base de datos
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
                out.println("Update Contact Details");
                break;
            case "/addGroup":
                addGroup(req, resp);
                break;
            case "/updateGroup":
                out.println("Update Group");
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
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();
        boolean args = !req.getParameterMap().isEmpty();

        resp.setContentType("text/html");
        out = resp.getWriter();

        try {
            switch (action) {
                case "/contacts":
                    listContacts(req, resp, args);
                    break;
                case "/editContact":
                    out.println("<h1>Edit Contact Details</h1>");
                    out.println(String.format("contactID: %s", req.getParameter("id")));
                    break;
                case "/groups":
                    listGroups(req, resp, args);
                    break;
                case "/newContact":
                    newContact(req, resp);
                    break;
                case "/jaimito":
                    out.println("te como el pito");
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
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

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

    private void addContact(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // --- Obtener todos los datos del formulario ---
        boolean isFavorite = req.getParameter("favorite") != null;
        System.out.println("isFavorite = " + isFavorite);

        String name = req.getParameter("name");
        System.out.println("name = " + name);

        String tagList = req.getParameter("tagList");
        System.out.println("tagList = " + tagList);

        Integer day = Util.parseInt(req.getParameter("day"));
        Integer month = Util.parseInt(req.getParameter("month"));
        Integer year = Util.parseInt(req.getParameter("year"));
        String date;

        if (day == null || month == null || year == null)
            date = null;
        else
            date = String.format("%4d-%02d-%02d", year, month, day);
        System.out.println("date = " + date);

        String[] emails = req.getParameterValues("emails");
        String[] emailCategories = req.getParameterValues("emailCategories");
        ArrayList<Email> emailList = Util.emailsFromArrays(emails, emailCategories);
        System.out.println("emailList = " + emailList);

        String[] countryCodes = req.getParameterValues("countryCodes");
        String[] phones = req.getParameterValues("phones");
        String[] phonesCategories = req.getParameterValues("phoneCategories");
        ArrayList<Phone> phoneList = Util.phonesFromArrays(countryCodes, phones, phonesCategories);
        System.out.println("phoneList = " + phoneList);

        String[] streets = req.getParameterValues("streets");
        String[] houseNumbers = req.getParameterValues("houseNumbers");
        String[] cities = req.getParameterValues("cities");
        String[] zipCodes = req.getParameterValues("zipCodes");
        String[] addressCategories = req.getParameterValues("addressCategories");
        ArrayList<Address> addressList = Util.addressesFromArrays(streets, houseNumbers, cities, zipCodes, addressCategories);
        System.out.println("addressList = " + addressList);
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
            req.setAttribute("status", false);
            req.setAttribute("name", newContact.getName());
            req.setAttribute("error", e.getMessage());
        }

        dispatcher = req.getRequestDispatcher("pages/status/newContactStatus.jsp");
        dispatcher.forward(req, resp);
    }

    private void newContact(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        List<Tag> tagList = agendaDAO.getAllTags();
        req.setAttribute("tagList", tagList);
        dispatcher = req.getRequestDispatcher("pages/newContact.jsp");
        dispatcher.forward(req, resp);
    }

    private void listGroups(HttpServletRequest req, HttpServletResponse resp, boolean details) throws ServletException, IOException, SQLException {
        if (details && req.getParameter("id") != null) {
            getGroupDetails(req, resp);
        } else {
            List<Group> groupList = agendaDAO.getAllGroups();
            List<Contact> contactList = agendaDAO.getAllContacts();

            req.setAttribute("groupList", groupList);
            req.setAttribute("contactList", contactList);

            dispatcher = req.getRequestDispatcher("pages/listGroups.jsp");
            dispatcher.forward(req, resp);
        }
    }

    private void getGroupDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        if (req.getParameter("id") != null) {
            Group group = agendaDAO.getGroup(Integer.parseInt(req.getParameter("id")));
            if (group != null) {
                List<Contact> memberList = agendaDAO.getGroupMembers(Integer.parseInt(req.getParameter("id")));
                List<Contact> availableMembersList = agendaDAO.getGroupAvailableMembers(Integer.parseInt(req.getParameter("id")));

                req.setAttribute("memberList", memberList);
                req.setAttribute("availableMembersList", availableMembersList);
                req.setAttribute("group", group);

                dispatcher = req.getRequestDispatcher("pages/groupDetails.jsp");
                dispatcher.forward(req, resp);
            } else
                resp.sendRedirect(req.getContextPath() + "/groups");
        } else
            resp.sendRedirect(req.getContextPath() + "/groups");
    }

    private void getContactsDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        if (req.getParameter("id") != null) {
            ContactDetails contactDetails = agendaDAO.getContact(Integer.parseInt(req.getParameter("id")));
            if (contactDetails != null) {
                req.setAttribute("contactDetails", contactDetails);
                dispatcher = req.getRequestDispatcher("pages/contactDetails.jsp");
                dispatcher.forward(req, resp);
            } else
                resp.sendRedirect(req.getContextPath() + "/contacts");
        } else
            resp.sendRedirect(req.getContextPath() + "/contacts");
    }

    private void listContacts(HttpServletRequest req, HttpServletResponse resp, boolean details) throws SQLException, IOException, ServletException {
        if (details)
            getContactsDetails(req, resp);
        else {
            List<Contact> contactList = agendaDAO.getAllContacts();
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
     * @throws IOException      Es lanzada por alguna función
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

    private void removeGroupMember(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer groupID = Util.parseInt(req.getParameter("groupID"));
        Integer memberID = Util.parseInt(req.getParameter("memberID"));

        System.out.println("groupID = " + groupID);
        System.out.println("memberID = " + memberID);

        if (groupID == null || memberID == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("invalid ids");
            out.write("Invalid IDs");
        } else {
            try {
                agendaDAO.removeGroupMember(groupID, memberID);
            } catch (SQLException e) {
                System.out.println("error eliminar miembro");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Error al eliminar al miembro: " + e.getMessage());
            }
        }
    }
}
