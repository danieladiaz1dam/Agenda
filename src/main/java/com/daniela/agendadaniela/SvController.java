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
    private ContactDAO contactDAO;
    private RequestDispatcher dispatcher;

    @Override
    public void init() throws ServletException {
        try {
            contactDAO = new ContactDAO();
        } catch (NamingException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        switch (action) {
            case "/addContact":
                addContact(req, resp);
                break;
            case "/updateContact":
                out.println("Update Contact Details");
                break;
            case "/deleteContact":
                out.println("Bye hater");
                break;
            case "/addGroup":
                out.println("Add Group");
                break;
            case "/updateGroup":
                out.println("Update Group");
                break;
            case "/deleteGroup":
                out.println("Bye haters");
                break;
            default:
                out.printf("<h1>%s</h1>", action);
                Enumeration<String> params = req.getParameterNames();
                while (params.hasMoreElements()) {
                    String paramName = params.nextElement();
                    out.println(paramName + " = " + req.getParameter(paramName) + "<br>");
                }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();
        boolean args = !req.getParameterMap().isEmpty();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {
            switch (action) {
                case "/contacts":
                    listContacts(req, resp, args);
                    break;
                case "/groups":
                    listGroups(req, resp, args);
                    break;
                case "/newContact":
                    newContact(req, resp);
                    break;
                case "/home":
                    goHome(req, resp);
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
            contactDAO.insertContact(newContact);
            req.setAttribute("status", true);
            req.setAttribute("name", newContact.getName());
        } catch (SQLException e) {
            req.setAttribute("status", false);
            req.setAttribute("name", newContact.getName());
            req.setAttribute("error", e.getMessage());
        }

        dispatcher = req.getRequestDispatcher("pages/status/newContactStatus.jsp");
        dispatcher.forward(req, resp);
    }

    private void newContact(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        List<Tag> tagList = contactDAO.getAllTags();
        req.setAttribute("tagList", tagList);
        dispatcher = req.getRequestDispatcher("pages/newContact.jsp");
        dispatcher.forward(req, resp);
    }

    private void listGroups(HttpServletRequest req, HttpServletResponse resp, boolean details) throws ServletException, IOException, SQLException {
        if (details && req.getParameter("id") != null) {
            List<Contact> memberList = contactDAO.getGroupMembers(Integer.parseInt(req.getParameter("id")));
            Group group = contactDAO.getGroup(Integer.parseInt(req.getParameter("id")));
            req.setAttribute("memberList", memberList);
            req.setAttribute("group", group);
            dispatcher = req.getRequestDispatcher("pages/groupDetails.jsp");
            dispatcher.forward(req, resp);
        } else {
            List<Group> groupList = contactDAO.getAllGroups();
            req.setAttribute("groupList", groupList);
            dispatcher = req.getRequestDispatcher("pages/listGroups.jsp");
            dispatcher.forward(req, resp);
        }
    }

    private void listContacts(HttpServletRequest req, HttpServletResponse resp, boolean details) throws SQLException, IOException, ServletException {
        if (details && req.getParameter("id") != null) {
            System.out.printf("Get contact %s%n", req.getParameter("id"));
            ContactDetails contactDetails = contactDAO.getContact(Integer.parseInt(req.getParameter("id")));
            req.setAttribute("contactDetails", contactDetails);
            dispatcher = req.getRequestDispatcher("pages/contactDetails.jsp");
            dispatcher.forward(req, resp);
        } else {
            System.out.println("Get all contacts");
            List<Contact> contactList = contactDAO.getAllContacts();
            System.out.println(contactList);
            req.setAttribute("contactList", contactList);
            dispatcher = req.getRequestDispatcher("pages/listContacts.jsp");
            dispatcher.forward(req, resp);
        }
    }

    private void goHome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("home.jsp");
        dispatcher.forward(req, resp);
    }
}
