<%@ page import="java.util.List" %>
<%@ page import="com.daniela.classes.Group" %>
<%@ page import="com.daniela.classes.Contact" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<%
    String baseUrl = request.getContextPath();
    @SuppressWarnings({"unchecked", "rawtypes"})
    List<Group> groups = (List) request.getAttribute("groupList");

    @SuppressWarnings({"unchecked", "rawtypes"})
    List<Contact> contacts = (List) request.getAttribute("contactList");
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet"
              href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
        <link rel="stylesheet" href="<%= baseUrl %>/static/css/theme.css">
        <link rel="stylesheet" href="<%= baseUrl %>/static/css/listGroups.css">
        <script src="<%= baseUrl %>/static/js/dialog.js"></script>
        <title>Grupos</title>
    </head>
    <body>
        <%@include file="navbar.jsp" %>

        <div id="body">
            <div id="wrapper">
                <div id="title">
                    <h1>Grupos</h1>

                    <a id="addGroupDialogBtn" class="button" onclick="openDialog()">
                        <span class="material-symbols-rounded"
                              style="left: unset !important; scale: 1.25;"> add_circle </span>
                    </a>
                </div>
                <%
                    if (groups == null || groups.isEmpty()) {
                        out.println("<h2>No tienes grupos!<h2>");
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

                <dialog id="dialog">
                    <h2>Create New Group</h2>
                    <form action="addGroup" method="post">
                        <div class="form-group">
                            <label for="name">Nombre: </label>
                            <input type="text" id="name" name="name" required>
                        </div>
                        <div class="form-group">
                            <label for="description">Descripción: </label>
                            <input type="text" id="description" name="description" required>
                        </div>
                        <div class="form-group">
                            <label for="members">Miembros: </label>
                            <select id="members" name="members" multiple style="min-width: 50%;">
                                <%
                                    for (Contact contact : contacts)
                                        out.println(String.format("<option value='%d'>%s</option>", contact.getID(), contact.getName()));
                                %>
                            </select>
                        </div>
                        <div class="form-group">
                            <button id="createGroupBtn" type="submit">Crear grupo</button>
                            <button type="button" class="cancel-btn" onclick="closeDialog()">Cancelar</button>
                        </div>
                    </form>
                </dialog>
            </div>
        </div>
    </body>
</html>
