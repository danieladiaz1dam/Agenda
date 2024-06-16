<%@ page import="java.util.List" %>
<%@ page import="com.daniela.classes.Group" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet"
              href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
        <link rel="stylesheet" href="static/css/theme.css">
        <link rel="stylesheet" href="static/css/listGroups.css">
        <script src="static/js/listContacts.js"></script>
        <title>Grupos</title>
    </head>
    <body>
        <%@include file="navbar.jsp" %>

        <div id="body">
            <div id="wrapper">
                <h1>Grupos</h1>
                <%
                    List<Group> groups = (List) request.getAttribute("groupList");
                    if (groups == null || groups.isEmpty()) {
                        out.println("<h1>No tienes grupos!<h1>");
                        out.println("<h3>Puedes añadir uno <a href='#'>aquí</a>.</h3>");
                    } else {
                %>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Descripción</th>
                            <th>Miembros</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (Group group : groups) {
                                out.write(group.toHTML());
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
