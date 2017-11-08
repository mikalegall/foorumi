<%@ page import="java.util.ArrayList" %>
<%@ page import="luokat.Viesti" %>
<%

%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">

    <title>AcademyFoorumi</title>
    <link rel="stylesheet" href="style.css">

</head>

<body>

<h1>AcademyFoorumi</h1>

<a href="/">foorumi</a> /
<a href="/Kategoria?id=<% out.print(request.getAttribute("keskusteluKategoriaId")); %>">
    <% out.print(request.getAttribute("keskusteluKategoriaNimi")); %></a>

<h2><% out.print(request.getAttribute("keskusteluOtsikko")); %></h2>

<div id="viestit">
    <%
        ArrayList<Viesti> viestit = (ArrayList<Viesti>) request.getAttribute("viestit");
        for (Viesti viesti : viestit) {
            out.println("<div class='viesti'>");
            out.println(viesti.getKirjoittaja() + ": " + viesti.getTeksti());
            out.println("</div>");
        }
    %>

</div>
<div class="kirjoita">
    <form method="post" action="/Kirjoita">
        <textarea name="viestiTeksti" rows="20" cols="50">Kirjoita kommentti tähän</textarea><br>
        <input type="hidden" name="keskusteluId" value="<% out.print(request.getAttribute("keskusteluId")); %>"/>
        <input type="submit" value="Lähetä">
    </form>
</div>
</body>
</html>