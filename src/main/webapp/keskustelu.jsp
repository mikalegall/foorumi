<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="luokat.Viesti" %>
<%
%>
<!doctype html>
<html lang="fi">
<head>
    <meta charset="utf-8">
    <title>AcademyFoorumi</title>
    <link rel="stylesheet" href="forumstyle.css">
</head>
<body>
<nav class="navbar">
    <ul>
        <li><a href="badges.html">KIRJAUTUMINEN</a></li>
        <li><input type="text" name="search" placeholder="Etsi.."></li>
    </ul>
    <br>
</nav>
<div class="banner">
    <h1 class="maintitle" onclick="location.href='/'">ACADEMY FORUM</h1>
</div>
<a href="/">Etusivu</a> /
<a href="/kategoria?id=<% out.print(request.getAttribute("keskusteluKategoriaId")); %>">
    <% out.print(request.getAttribute("keskusteluKategoriaNimi")); %></a> /
    <% out.print(request.getAttribute("keskusteluOtsikko")); %>
<blockquote>
    <p style="text-align: center"><% out.print(request.getAttribute("keskusteluOtsikko")); %></p>
</blockquote>
<div id="viestit">
    <%
        ArrayList<Viesti> viestit = (ArrayList<Viesti>) request.getAttribute("viestit");
        for (Viesti viesti : viestit) {
            out.println("<div class='viesti'>");
            out.println("<strong>" + viesti.getKirjoittaja() + "</strong><br>");
            out.println("<br>" + viesti.getTeksti() + "<br><br><hr>");
            out.println("</div>");
        }
    %>
</div>
<%
    HttpSession istunto = request.getSession(false);
    //String kayttajatunnus = (String) istunto.getAttribute("nimi");
    if (istunto == null || istunto.getAttribute("nimi") == null) {
        out.println("<p>Kirjaudu sisään kirjoittaaksesi viestin</p>");
    } else {
        String id = (String) request.getAttribute("keskusteluId");
        out.println("<div class='kirjoita'>");
        out.println("<form method = 'post' action = '/kirjoita'>");
        out.println("<textarea name = 'viestiTeksti' rows = '20' cols = '50' > Kirjoita kommentti tähän</textarea ><br >");
        out.println("<input type = 'hidden' name = 'keskusteluId' value = '" + id + "'/>");
        out.println("<input type='submit' value='Lähetä'>");
        out.println("</form >");
        out.println("</div >");
    }
%>
</body>
</html>