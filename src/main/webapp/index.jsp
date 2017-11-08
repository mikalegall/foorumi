<%@ page import="java.util.ArrayList" %>
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
        out.println(request.getAttribute("foo"));
        ArrayList<String> kategorianimet = (ArrayList<String>) request.getAttribute("kategorianimet");
        for (String kategorianimi : kategorianimet) {
            out.println("<a href='index.html?kategoria=" + kategorianimi + "'>" + kategorianimi + "</a><br>");
        }
    %>

</div>
</body>
</html>