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
<%@ include file="header.jsp" %>
<a href="/">Etusivu</a> / Hakutulokset
<blockquote>
    <p style="text-align: center">Haku: <% out.print(request.getAttribute("haku")); %></p>
</blockquote>
<div id="hakutulokset">
    <%
        ArrayList<Viesti> hakutulokset = (ArrayList<Viesti>) request.getAttribute("hakutulokset");
        for (Viesti osuma : hakutulokset) {
            out.println("<div class='hakutulos'>");
            String osoite = "/keskustelu?id=" + osuma.getKeskusteluId() + "#" + osuma.getId();
            out.println("<a href='" + osoite + "'>" + osuma.getTeksti() + "</a>");
            out.println("<br><br><hr>");
            out.println("</div>");
        }
    %>
</div>
</body>
</html>