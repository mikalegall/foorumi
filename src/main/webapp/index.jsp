<%@ page import="java.util.ArrayList" %>
<%@ page import="luokat.Kategoria" %>
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

<div id="kategoriat">
    <%
        ArrayList<Kategoria> kategoriat = (ArrayList<Kategoria>) request.getAttribute("kategoriat");
        for (Kategoria k : kategoriat) {
            out.println("<a href='/kategoria?id=" + k.getId() + "'>" + k.getNimi() + "</a><br>");
        }
    %>

</div>
</body>
</html>