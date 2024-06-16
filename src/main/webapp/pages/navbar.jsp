<%@ page pageEncoding="UTF-8" %>

<%
    String path = request.getContextPath();
    String uri = request.getRequestURI();
    String pageName = uri.substring(uri.lastIndexOf("/") + 1);

    boolean isContactPage = pageName.equals("listContacts.jsp") || pageName.equals("contactDetails.jsp");
    boolean isGroupPage = pageName.equals("listGroups.jsp") || pageName.equals("groupDetails.jsp");
%>

<nav class="navbar">
    <ul class="navbar-nav">
        <li class="logo">
            <a href="" class="nav-link">Agenda</a>
        </li>
        <li class="nav-item">
            <a href="<%= path %>/newContact" class="nav-link">
                <span class="material-symbols-rounded"> add_circle </span>
                <span class="link-text">Nuevo Contacto</span>
            </a>
        </li>
        <li class="nav-item <%= isContactPage ? "active" : "" %>">
            <a href="<%= path %>/contacts" class="nav-link">
                <span class="material-symbols-rounded fill"> person </span>
                <span class="link-text">Contactos</span>
            </a>
        </li>
        <li class="nav-item <%= isGroupPage ? "active" : "" %>">
            <a href="<%= path %>/groups" class="nav-link">
                <span class="material-symbols-rounded fill"> groups </span>
                <span class="link-text">Grupos</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="" class="nav-link">
                <span class="material-symbols-rounded"> info </span>
                <span class="link-text">About</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="" class="nav-link">
                <span class="material-symbols-rounded fill"> light_mode </span>
                <span class="link-text">Theme</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="" class="nav-link">
                <span class="material-symbols-rounded fill"> logout </span>
                <span class="link-text">Cerrar Sesión</span>
            </a>
        </li>
    </ul>
</nav>