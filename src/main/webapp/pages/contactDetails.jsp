<%@ page import="com.daniela.classes.ContactDetails" %>
<%@ page import="com.daniela.classes.Email" %>
<%@ page import="com.daniela.classes.Phone" %>
<%@ page import="com.daniela.classes.Address" %>
<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet"
              href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
        <link rel="stylesheet" href="static/css/theme.css">
        <link rel="stylesheet" href="static/css/contactDetails.css">
        <script src="static/js/newContact.js"></script>
        <title>Detalles de Contacto</title>
    </head>
    <body>
        <%@include file="navbar.jsp" %>

        <%
            ContactDetails contact = (ContactDetails) request.getAttribute("contactDetails");

            String favoriteClass = contact.isFavorite() ? "fill favorite" : "";

            // TODO ADD EDIT LINK
            String birthday = contact.getBirthday();
            if (birthday == null)
                birthday = "<span>Puedes añadir su cumpleaños <a href=\"\">aquí</a></span>";
        %>

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
                        <a class="button" id="edit-btn"><span
                                style="position: relative; top: 4px; left: 2px">Edit</span></a>
                        <a class="button"><span class="material-symbols-rounded"> delete </span></a>
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
                            if (!contact.getTags().isEmpty())
                                for (String tag : contact.getTags().split(","))
                                    out.println(String.format("<span class=\"tag\">%s <span class=\"material-symbols-rounded\"> close </span> </span>", tag));
                        %>
                    </div>

                    <div id="details">
                        <h4>
                            <span class="material-symbols-rounded"> mail </span>
                            <span>Emails</span>
                        </h4>
                        <ul>
                            <%
                                for (Email email : contact.getEmails())
                                    out.println("<li>" + email + "</li>");
                            %>
                        </ul>

                        <br>
                        <h4>
                            <span class="material-symbols-rounded"> smartphone </span>
                            <span>Teléfonos</span>
                        </h4>
                        <ul>
                            <%
                                for (Phone phone : contact.getPhones())
                                    out.println("<li>" + phone + "</li>");
                            %>
                        </ul>

                        <br>
                        <h4>
                            <span class="material-symbols-rounded"> location_on </span>
                            <span>Direcciones</span>
                        </h4>
                        <ul>
                            <%
                                for (Address address : contact.getAddresses())
                                    out.println("<li>" + address + "</li>");
                            %>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
