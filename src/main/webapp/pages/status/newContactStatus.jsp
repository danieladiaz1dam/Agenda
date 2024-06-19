<%--suppress ALL --%>
<%--suppress ALL --%>
<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link rel="stylesheet"
              href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"/>
        <link href="https://fonts.googleapis.com/css2?family=M+PLUS+Rounded+1c:wght@400;500;700;800;900&display=swap"
              rel="stylesheet">
        <title>Contacto añadido Exitosamente</title>
    </head>

    <style>
        * {
            font-family: "M PLUS Rounded 1c", sans-serif;
        }
    </style>

    <body>
        <h1>Resultado de la Adición de Contacto</h1>

        <%
            Boolean status = (Boolean) request.getAttribute("status");
            String name = (String) request.getAttribute("name");
            String error = (String) request.getAttribute("error");

            if (status != null && status)
                out.println(String.format("<p>El contacto <strong>%s</strong> ha sido añadido a la base de datos correctamente.</p>", name));
                else {
                    out.println(String.format("<p>Hubo un error al añadir el contacto <strong>%s</strong> a la base de datos.</p>", name));
                    out.println(String.format("<p>Error: %s</p>", error));
                }
        %>

        <a href="newContact">Añadir otro contacto</a>
        <a href="contacts">Ver todos los contactos</a>
    </body>
</html>
