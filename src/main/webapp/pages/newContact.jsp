<%@ page import="java.time.Year" %>
<%@ page import="java.util.List" %>
<%@ page import="com.daniela.classes.Tag" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%!
    final int YEAR = Year.now().getValue();
    final String[] MESES = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet"
              href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
        <link rel="stylesheet" href="static/css/theme.css">
        <link rel="stylesheet" href="static/css/newContact.css">
        <script src="static/js/newContact.js"></script>
        <title>Nuevo Contacto</title>
    </head>
    <body>
        <%@ include file="navbar.jsp" %>

        <div id="body">
            <div id="wrapper">
                <form id="form" action="addContact" method="post" autocomplete="off">
                    <div id="title">
                        <h1>Datos del nuevo contacto</h1>
                        <label id="favorite-label">
                            <input type="checkbox" name="favorite" id="favorite">
                            <span class="material-symbols-rounded" style="position: relative;top: 12px;">favorite</span>
                        </label>
                    </div>

                    <!-- TAGS -->
                    <input type="hidden" name="tagList" id="tagList" value="default">

                    <label for="tagSelect">Añadir etiqueta:</label>
                    <select id="tagSelect" name="tags">
                        <option value="" disabled selected hidden>Tag</option>
                        <%
                            List<Tag> tags = (List) request.getAttribute("tagList");
                            for (Tag tag : tags) {
                        %>
                        <option value="<%= tag.getName() %>"><%= tag.getName() %>
                        </option>
                        <%
                            }
                        %>
                        <option value="add_new_tag">Añadir nueva etiqueta</option>
                    </select>
                    <br>
                    <div id="newTagContainer" style="display: none;">
                        <label for="newTag">Nueva etiqueta:</label>
                        <input type="text" id="newTag" name="newTag">
                        <button onclick="createNewTag()">Añadir</button>
                    </div>
                    <br>
                    <div id="tagListContainer">
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
                                    out.write(String.format("<option value='%d'>%d</option>", i, i));
                            %>
                        </select>


                        <select id="month" name="month">
                            <option value="" disabled selected hidden>Mes</option>
                            <%
                                for (int i = 0; i < MESES.length; i++) {
                                    out.write(String.format("<option value='%d'>%s</option>", i + 1, MESES[i]));
                                }
                            %>
                        </select>

                        <select id="year" name="year">
                            <option value="" disabled selected hidden>Año</option>
                            <%
                                for (int i = YEAR; i >= YEAR - 120; i--)
                                    out.write(String.format("<option value='%d'>%d</option>", i, i));
                            %>
                        </select>
                    </div>

                    <div id="fields">
                        <p class="field">
                            <input type="text" name="name" required placeholder="Nombre">
                        </p>

                        <!-- EMAILS -->
                        <div id="emailList">
                            <p class="field">
                                <input type="email" name="emails" placeholder="Email" required>
                                <span class="gap10"></span>
                                <input type="text" name="emailCategories" placeholder="Category" required>

                                <button class="remove-field" type="button" onclick="removeField(this.parentNode)">
                                    <span class="material-symbols-rounded"> close </span>
                                </button>
                            </p>
                        </div>
                        <button class="add" type="button" onclick="addEmail()">
                            <span class="material-symbols-rounded" style="position:relative; top:4px;"> add </span>
                            <span style="position:relative; top:-4px;">Agregar email</span>
                        </button>

                        <!-- PHONES -->
                        <div id="phoneList">
                            <p class="field">
                                <input class="small-field" type="number" name="countryCodes" placeholder="+34"
                                       pattern="\d*"
                                       min="1" required>
                                <span class="gap10"></span>
                                <input type="number" name="phones" placeholder="Telephone" pattern="d*" min="100000000"
                                       max="999999999" required>
                                <span class="gap10"></span>
                                <input type="text" name="phoneCategories" placeholder="Category" required>

                                <button class="remove-field" type="button" onclick="removeField(this.parentNode)">
                                    <span class="material-symbols-rounded"> close </span>
                                </button>
                            </p>
                        </div>
                        <button class="add" type="button" onclick="addPhone()">
                            <span class="material-symbols-rounded" style="position:relative; top:4px;"> add </span>
                            <span style="position:relative; top:-4px;">Agregar teléfono</span>
                        </button>


                        <!-- ADDRESSES -->
                        <div id="AddressList">
                            <div class="field">
                                <p class="field">
                                    <input type="text" name="streets" placeholder="Calle" required>
                                    <span class="gap10"></span>
                                    <input class="small-field" type="number" name="houseNumbers" placeholder="Num."
                                           pattern="\d*" min="1" required>
                                    <button class="remove-field" type="button"
                                            onclick="removeField(this.parentNode.parentNode)">
                                        <span class="material-symbols-rounded"> close </span>
                                    </button>
                                </p>
                                <p class="field">
                                    <input type="text" name="cities" placeholder="Ciudad" required>
                                    <span class="gap10"></span>
                                    <input class="small-field" type="text" name="zipCodes" placeholder="CP"
                                           pattern="\d*"
                                           min="10000" max="99999" required>
                                </p>
                                <p class="field">
                                    <input type="text" name="addressCategories" placeholder="Category" required>
                                </p>
                            </div>
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
