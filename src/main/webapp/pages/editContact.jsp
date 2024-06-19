<%@ page import="java.time.Year" %>
<%@ page import="java.util.List" %>
<%@ page import="com.daniela.classes.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<%
    // Opciones para el input de tipo select para el cumpleaños
    final int YEAR = Year.now().getValue();
    final String[] MESES = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    String baseUrl = request.getContextPath();

    // Detalles del contacto
    ContactDetails contact = (ContactDetails) request.getAttribute("contactDetails");

    // Lista de tags que el usuario puede usar
    @SuppressWarnings({"unchecked", "rawtypes"})
    List<Tag> availableTags = (List) request.getAttribute("availableTags");

    // Tags actuales del contacto
    String[] tags = {};
    if (!contact.getTags().isBlank())
        tags = contact.getTags().split(",");

    // Dividir el String de la fecha en tres números con substrings y parseInt
    String birthday = contact.getBirthday();
    Integer day = -1, month = -1, year = -1;

    if (birthday != null && !birthday.isBlank()) {
        day = Util.parseInt(birthday.substring(birthday.lastIndexOf("-") + 1));
        month = Util.parseInt(birthday.substring(birthday.indexOf("-") + 1, birthday.lastIndexOf("-")));
        year = Util.parseInt(birthday.substring(0, birthday.indexOf("-")));
    }

%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet"
              href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
        <link rel="stylesheet" href="<%= baseUrl %>/static/css/theme.css">
        <link rel="stylesheet" href="<%= baseUrl %>/static/css/newContact.css">
        <link rel="stylesheet" href="<%= baseUrl %>/static/css/editContact.css">
        <script src="<%= baseUrl %>/static/js/editContact.js"></script>
        <title>Editar a <%= contact.getName() %>
        </title>
    </head>
    <body>
        <%@ include file="navbar.jsp" %>

        <div id="body">
            <div id="wrapper">
                <form id="form" action="updateContact" method="post" autocomplete="off">
                    <div id="title">
                        <h1>Datos de <%= contact.getName() %>
                        </h1>
                        <label id="favorite-label">
                            <input type="checkbox" name="favorite"
                                   id="favorite" <% if (contact.isFavorite()) { %> checked <% } %>>
                            <span class="material-symbols-rounded <% if (contact.isFavorite()) { %> checked <% } %>" style="position: relative; top: 12px;">favorite</span>
                        </label>
                    </div>

                    <!-- BIRTHDAY -->
                    <div>
                        <label>
                            <span class="material-symbols-rounded" style="position: relative;top: 4px;"> cake </span>
                            <span> Cumpleaños: </span>
                        </label>

                        <select id="day" name="day">
                            <option value="" disabled selected hidden>Dia</option>
                            <%
                                for (int i = 1; i <= 31; i++)
                                    out.write(String.format("<option value='%d' %s>%d</option>", i, i == day ? "selected" : "", i));
                            %>
                        </select>


                        <select id="month" name="month">
                            <option value="" disabled selected hidden>Mes</option>
                            <%
                                for (int i = 0; i < MESES.length; i++) {
                                    out.write(String.format("<option value='%d' %s>%s</option>", i + 1, i == month ? "selected" : "", MESES[i]));
                                }
                            %>
                        </select>

                        <select id="year" name="year">
                            <option value="" disabled selected hidden>Año</option>
                            <%
                                for (int i = YEAR; i >= YEAR - 120; i--)
                                    out.write(String.format("<option value='%d' %s>%d</option>", i, i == year ? "selected" : "", i));
                            %>
                        </select>
                    </div>

                    <div id="fields">
                        <input type="hidden" name="contactID" value="<%= contact.getID() %>">
                        <input type="hidden" id="deletedEmails" name="deletedEmails" placeholder="deletedEmails">
                        <input type="hidden" id="deletedPhones" name="deletedPhones" placeholder="deletedPhones">
                        <input type="hidden" id="deletedAddresses" name="deletedAddresses" placeholder="deletedAddresses">

                        <p class="field">
                            <input type="text" name="name" required placeholder="Nombre" value="<%= contact.getName() %>">
                        </p>

                        <!-- EMAILS -->
                        <div id="emailList">
                            <%
                                for (Email email : contact.getEmails()) {
                                    out.println(email.toFormField());
                                }
                            %>
                        </div>
                        <button class="add" type="button" onclick="addEmail()">
                            <span class="material-symbols-rounded" style="position:relative; top:4px;"> add </span>
                            <span style="position:relative; top:-4px;">Agregar email</span>
                        </button>

                        <!-- PHONES -->
                        <div id="phoneList">
                            <%
                                for (Phone phone : contact.getPhones())
                                    out.println(phone.toFormField());
                            %>
                        </div>
                        <button class="add" type="button" onclick="addPhone()">
                            <span class="material-symbols-rounded" style="position:relative; top:4px;"> add </span>
                            <span style="position:relative; top:-4px;">Agregar teléfono</span>
                        </button>


                        <!-- ADDRESSES -->
                        <div id="AddressList">
                            <%
                                for (Address address : contact.getAddresses())
                                    out.println(address.toFormField());
                            %>
                        </div>
                        <button class="add" type="button" onclick="addAddress()">
                            <span class="material-symbols-rounded" style="position:relative; top:4px;"> add </span>
                            <span style="position:relative; top:-4px;">Agregar dirección</span>
                        </button>
                    </div>

                    <button type="submit">Guardar</button>
                </form>
            </div>
        </div>
    </body>
</html>
