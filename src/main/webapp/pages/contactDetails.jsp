<%@ page import="com.daniela.classes.ContactDetails" %>
<%@ page import="com.daniela.classes.Email" %>
<%@ page import="com.daniela.classes.Phone" %>
<%@ page import="com.daniela.classes.Address" %>
<%@ page pageEncoding="UTF-8" %>

<%
    ContactDetails contact = (ContactDetails) request.getAttribute("contactDetails");

    String favoriteClass = contact.isFavorite() ? "fill favorite" : "";

    String birthday = contact.getBirthday();
    if (birthday == null)
        birthday = String.format("<a href=\"editContact?id=%d\">agregar cumpleaños</a>", contact.getID());

    String baseUrl = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet"
              href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
        <link rel="stylesheet" href="<%= baseUrl %>/static/css/theme.css">
        <link rel="stylesheet" href="<%= baseUrl %>/static/css/contactDetails.css">
        <script src="<%= baseUrl %>/static/js/agendaDAO.js"></script>
        <title>Detalles de <%= contact.getName() %></title>
    </head>
    <body>
        <%@include file="navbar.jsp" %>

        <div id="body">
            <div id="wrapper">
                <div style="max-width: var(--max-width)">
                    <div id="title">
                        <a class="button" href="<%= path %>/contacts"><span
                                class="material-symbols-rounded"> arrow_back </span>
                        </a>
                        <h1>
                            <%= contact.getName() %>
                        </h1>
                        <a><span style="font-size: 2rem; top: 8px"
                                 class="material-symbols-rounded <%= favoriteClass %>"> favorite </span></a>
                        <a class="button" id="edit-btn" href="editContact?id=<%= contact.getID() %>"><span
                                style="position: relative; top: 4px; left: 2px">Edit</span></a>
                        <a class="button" onclick="removeContact(<%= contact.getID() %>)"><span class="material-symbols-rounded"> delete </span></a>
                    </div>

                    <h3>Detalles del Contacto</h3>
                    <p>
                        <span class="material-symbols-rounded"> cake </span>
                        <span> Cumpleaños: <%= birthday %> </span>
                    </p>
                    <p>
                        <span class="material-symbols-rounded"> calendar_month </span>
                        <span>Fecha de Creación: <%= contact.getCreationDate() %></span>
                    </p>

                    <div id="tags">
                        <%
                            String tags = contact.getTags();
                            if (tags != null && !tags.isEmpty())
                                for (String tag : tags.split(","))
                                    out.println(String.format("<span class=\"tag\">%s </span>", tag));
                        %>
                    </div>

                    <div id="details">
                        <h4>
                            <span class="material-symbols-rounded"> mail </span>
                            <span>Emails</span>
                        </h4>
                        <ul>
                            <%
                                if (!contact.getEmails().isEmpty())
                                    for (Email email : contact.getEmails())
                                        out.println("<li>" + email + "</li>");
                                else
                                    out.println(String.format("<li><a href=\"editContact?id=%d\">Agregar mail</a></li>", contact.getID()));
                            %>
                        </ul>

                        <br>
                        <h4>
                            <span class="material-symbols-rounded"> smartphone </span>
                            <span>Teléfonos</span>
                        </h4>
                        <ul>
                            <%
                                if (!contact.getPhones().isEmpty())
                                    for (Phone phone : contact.getPhones())
                                        out.println("<li>" + phone + "</li>");
                                else
                                    out.println(String.format("<li><a href=\"editContact?id=%d\">Agregar teléfono</a></li>", contact.getID()));
                            %>
                        </ul>

                        <br>
                        <h4>
                            <span class="material-symbols-rounded"> location_on </span>
                            <span>Direcciones</span>
                        </h4>
                        <ul>
                            <%
                                if (!contact.getAddresses().isEmpty())
                                    for (Address address : contact.getAddresses())
                                        out.println("<li>" + address + "</li>");
                                else
                                    out.println(String.format("<li><a href=\"editContact?id=%d\">Agregar dirección</a></li>", contact.getID()));
                            %>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
