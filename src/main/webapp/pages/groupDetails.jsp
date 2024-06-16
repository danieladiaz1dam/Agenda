<%@ page import="java.util.List" %>
<%@ page import="com.daniela.classes.Contact" %>
<%@ page import="com.daniela.classes.Group" %>
<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet"
              href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
        <link rel="stylesheet" href="static/css/theme.css">
        <link rel="stylesheet" href="static/css/groupDetails.css">
        <script src="static/js/listContacts.js"></script>
        <title>Group details</title>
    </head>
    <body>
        <%@include file="navbar.jsp" %>

        <%
            List<Contact> members = (List) request.getAttribute("memberList");
            Group group = (Group) request.getAttribute("group");
        %>

        <div id="body">
            <div id="wrapper">

                <div id="title">
                    <a class="button" href="<%= path %>/groups"><span
                            class="material-symbols-rounded"> arrow_back </span>
                    </a>
                    <h1>
                        <%= group.getName() %>
                    </h1>
                    <a class="button" href=""><span class="material-symbols-rounded"> person_add </span></a>
                    <a class="button" href=""><span class="material-symbols-rounded"> delete </span></a>
                </div>

                <h3 id="description">
                    <%= group.getDescription() %>
                </h3>

                <%

                    if (members == null || members.isEmpty()) {
                        out.println("<h1>Este grupo no tiene ningún miembro<h1>");
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
                            for (Contact member : members) {
                                out.write(member.toTableRowAsMember());
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
