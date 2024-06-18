<%@ page import="java.util.List" %>
<%@ page import="com.daniela.classes.Contact" %>
<%@ page import="com.daniela.classes.Group" %>
<%@ page pageEncoding="UTF-8" %>

<%
    @SuppressWarnings({"unchecked", "rawtypes"})
    List<Contact> members = (List) request.getAttribute("memberList");

    @SuppressWarnings({"unchecked", "rawtypes"})
    List<Contact> availableMembers = (List) request.getAttribute("availableMembersList");

    Group group = (Group) request.getAttribute("group");
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
        <link rel="stylesheet" href="<%= baseUrl %>/static/css/groupDetails.css">
        <script src="<%= baseUrl %>/static/js/dialog.js"></script>
        <script src="<%= baseUrl %>/static/js/agendaDAO.js"></script>
        <title>Grupo <%= group.getName() %>
        </title>
    </head>
    <body>
        <%@include file="navbar.jsp" %>

        <div id="body">
            <div id="wrapper">
                <div style="max-width: var(--max-width);">
                    <div id="title">
                        <a class="button" href="<%= path %>/groups"><span
                                class="material-symbols-rounded"> arrow_back </span>
                        </a>
                        <h1>
                            <%= group.getName() %>
                        </h1>
                        <a class="button" onclick="openDialog()"><span class="material-symbols-rounded"> person_add </span></a>
                        <a class="button" onclick="removeGroup(<%= group.getId() %>)"><span
                                class="material-symbols-rounded"> delete </span></a>
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
                                    out.write(member.toTableRowAsMember(group.getId()));
                                }
                            %>
                        </tbody>
                    </table>
                    <%
                        }
                    %>
                    <dialog id="dialog">
                        <h3>Add New Members:</h3>
                        <form action="addGroupMembers" method="post">
                            <input type="hidden" name="groupId" value="<%= group.getId() %>">
                            <div class="form-group">
                                <select id="members" name="members" multiple style="min-width: 50%;">
                                    <%
                                        for (Contact contact : availableMembers)
                                            out.println(String.format("<option value='%d'>%s</option>", contact.getID(), contact.getName()));
                                    %>
                                </select>
                            </div>
                            <div class="form-group">
                                <button type="submit">Agregar</button>
                                <button type="button" class="cancel-btn" onclick="closeDialog()">Cancelar</button>
                            </div>
                        </form>
                    </dialog>

                </div>
            </div>
        </div>
    </body>
</html>
