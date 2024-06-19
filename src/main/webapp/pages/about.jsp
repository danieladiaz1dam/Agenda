<%@ page pageEncoding="UTF-8" %>

<%
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
        <link rel="stylesheet" href="<%= baseUrl %>/static/css/about.css">
        <script src="<%= baseUrl %>/static/js/editContact.js"></script>
        <title>About</title>
    </head>
    <body>
        <%@ include file="navbar.jsp" %>

        <div id="body">
            <div id="wrapper">
                <header>
                    <h1>Agenda App 📒</h1>
                </header>

                <section>
                    <h2>Bienvenidos a Agenda App</h2>
                    <p>Agenda App es un servicio self-hosted para la gestión de contactos solo en local.</p>
                </section>

                <section>
                    <h2>Herramientas</h2>
                    <div class="miembro-equipo">
                        <img src="<%= baseUrl %>/static/imgs/tomcat.png" alt="Foto de Tomcat">
                        <div class="textblock">
                            <h3>🐯 Tomcat - Servidor web</h3>
                            <p>Proporciona un entorno para ejecutar mi proyecto Web Dinámico con Java basado en Servlets y JSP.</p>
                            <p>Tomcat es un wrapper de Jakarta Servlet, Jakarta Expression Language y WebSockets, proporcionando un servidor HTTP en el que se puede ejecutar código java.</p>
                        </div>
                    </div>
                    <div class="miembro-equipo">
                        <img src="<%= baseUrl %>/static/imgs/jdbc.png" alt="Foto de jdbc">
                        <div class="textblock">
                            <h3>📞 JDBC - Java Database Connectivity</h3>
                            <p>JDBC es una api para java, la cual define como el cliente puede acceder a la base de datos.</p>
                            <p>Tomcat nos "facilita" esto, usando las connection pools. Esto no es nada más que definir las conexiones a la base de datos en un archivo para usarlas cuando sea necesario, aunque también se pueden omitir.</p>
                        </div>
                    </div>
                    <div class="miembro-equipo">
                        <img src="<%= baseUrl %>/static/imgs/database.png" alt="Foto de database">
                        <div class="textblock">
                            <h3>💾 Microsoft SQL Server - Base de Datos</h3>
                            <p>Sql server es un sistema de gestión de bases de datos relacionales propietario desarrollado por Microsoft.</p>
                            <p>Permite la ejecución de scripts y queries para guardar y recibir datos de manera eficiente. Mi diagrama <a href="<%= baseUrl %>/static/files/database.html">aquí</a></p>
                        </div>
                    </div>
                    <div class="miembro-equipo">
                        <img src="<%= baseUrl %>/static/imgs/docker.png" alt="Foto de docker">
                        <div class="textblock">
                            <h3>🐋 Docker - Contenedor de servicios</h3>
                            <p>Usa Docker Engine, para virtualizar y hostear servicios guardados en contenedores en un sistema operativo host.</p>
                            <p>En mi app, uso docker compose, que se usa para definir y arrancar aplicaciones multi-container como esta, ya que consta de Tomcat y la base de datos.</p>
                        </div>
                    </div>
                </section>

                <section id="equipo">
                    <h2>Nuestro Equipo</h2>
                    <div class="miembro-equipo">
                        <img src="<%= baseUrl %>/static/imgs/me.png" alt="Foto de Daniela">
                        <div class="textblock">
                            <h3>🌷 Daniela</h3>
                            <p>Desarrolladora</p>
                            <p>Diseñadora</p>
                        </div>
                    </div>
                    <div class="miembro-equipo">
                        <img src="<%= baseUrl %>/static/imgs/jaime.png" alt="Foto de Jaime">
                        <div class="textblock">
                            <h3>💍 Jaime</h3>
                            <p>Cocina bien 🍑</p>
                            <p>Buen gusto musical</p>
                        </div>
                    </div>
                    <div class="miembro-equipo">
                        <img src="<%= baseUrl %>/static/imgs/alonso.png" alt="Foto de Alonso">
                        <div class="textblock">
                            <h3>✨ Alonso</h3>
                            <p>Opiniones</p>
                            <p>Tests</p>
                        </div>
                    </div>
                    <div class="miembro-equipo">
                        <img src="<%= baseUrl %>/static/imgs/demon.png" alt="Foto de mi demonio">
                        <div class="textblock">
                            <h3>🔥 Demonio de mi cuarto</h3>
                            <p>Debugging</p>
                            <p>Hace que la aplicación funcione o no sin tocar nada</p>
                        </div>
                    </div>
                </section>

                <footer>
                    <p>&copy; 2024 AgendaApp. Todos los derechos reservados.</p>
                </footer>
            </div>
        </div>
    </body>
</html>
