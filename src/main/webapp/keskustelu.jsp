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

<h2><% out.print(request.getAttribute("keskustelunimi")); %></h2>

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
</body>
</html>