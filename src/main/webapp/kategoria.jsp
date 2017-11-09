<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="luokat.Kategoria" %>
<%@ page import="luokat.Keskustelu" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Academy forum</title>
    <link rel="stylesheet" type="text/css" href="forumstyle.css">
</head>
<body>
<nav>
    <h3 style="text-align: right">Kirjaudu</h3>
    <br>
</nav>
<div class="banner">
    <h1 class="maintitle">ACADEMY FORUM</h1>
</div>
<blockquote>
    <p style="text-align: center"><% out.println(request.getAttribute("kategoria")); %></p>
</blockquote>
<div id="keskustelut">
    <%
        ArrayList<Keskustelu> keskustelut = (ArrayList<Keskustelu>) request.getAttribute("keskustelut");
        for (Keskustelu keskustelu : keskustelut) {
            out.println("<a href='keskustelu?id=" + keskustelu.getId() + "'>" + keskustelu.getOtsikko() + "</a><br>");
        }
    %>
</div>

<%
    HttpSession istunto = request.getSession(false);
    //String kayttajatunnus = (String) istunto.getAttribute("nimi");
    if (istunto == null || istunto.getAttribute("nimi") == null) {
        out.println("<p>Kirjaudu sisään aloittaaksesi uuden viestiketjun</p>");
    } else {
        out.println("<div class='kirjoita'>");
        out.println("<form method = 'post' action = '/kirjoita'>");
        out.println("<input type='text' name='otsikko' />");
        out.println("<textarea name = 'viestiTeksti' rows = '20' cols = '50' > Kirjoita kommentti tähän</textarea ><br >");
        out.println("<input type = 'hidden' name = 'kategoriaId' value = '" + request.getAttribute("kategoriaid") + "'/>");
        out.println("<input type='submit' value='Lähetä'>");
        out.println("</form >");
        out.println("</div >");
    }


%>

</body>
</html>
</body>
</html>