<%@ page import="com.daniela.classes.Contact" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<% String baseUrl = request.getContextPath(); %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet"
              href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
        <link rel="stylesheet" href="<%= baseUrl %>/static/css/theme.css">
        <link rel="stylesheet" href="<%= baseUrl %>/static/css/listContacts.css">
        <script src="<%= baseUrl %>/static/js/agendaDAO.js"></script>
        <title>Contactos</title>
    </head>
    <body>
        <%@include file="navbar.jsp" %>

        <div id="body">
            <div id="wrapper">
                <h1>Contactos</h1>
                <%
                    @SuppressWarnings({"unchecked", "rawtypes"})
                    List<Contact> contactList = (List) request.getAttribute("contactList");

                    if (contactList == null || contactList.isEmpty()) {
                        out.println("<h1>No tienes contactos!<h1>");
                    } else {
                %>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (Contact contact : contactList) {
                                out.write(contact.toTableRowAsContact());
                            }
                        %>
                    </tbody>
                </table>
                <%
                    }
                %>
            </div>
        </div>
    </body>
</html>
