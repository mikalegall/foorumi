<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<nav class="navbar">
    <ul>
        <%
            HttpSession istunto = request.getSession(false);
            if (istunto == null || istunto.getAttribute("nimi") == null) {
                out.println("<li><a href='/kirjaudu?url=" + request.getRequestURI() + "'>KIRJAUDU</a></li>");
            } else if (istunto.getAttribute("rooli").equals("tavis")) {
                out.println("<li><a href='/logout'>KIRJAUDU ULOS</a></li>");
                out.println("<li><a href='/VaihdaSalasana'>VAIHDA SALASANA</a></li>");
            } else if (istunto.getAttribute("rooli").equals("moderaattori")) {
                out.println("<li><a href='/logout'>KIRJAUDU ULOS</a></li>");
                out.println("<li><a href='/VaihdaSalasana'>VAIHDA SALASANA</a></li>");
                out.println("<li><a href='/kayttajat.jsp'>KÄYTTÄJÄT</a></li>");
            }
        %>
        <li><form method="post" action="/haku"><input type="text" name="haku" placeholder="Etsi.."></form></li>
    </ul>
    <br>
</nav>
<div class="banner">
    <h1 class="maintitle" onclick="location.href='/'">ACADEMY FORUM</h1>
</div>