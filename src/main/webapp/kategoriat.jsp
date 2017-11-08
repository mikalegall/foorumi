<%@ page import="java.util.ArrayList" %>
<%@ page import="luokat.Kategoria" %>
<%@ page import="luokat.Keskustelu" %>
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

<h2> <% out.println(request.getAttribute("kategoria")); %> </h2>


<div id="keskustelut">
    <%
        ArrayList<Keskustelu> keskustelut = (ArrayList<Keskustelu>) request.getAttribute("keskustelut");
        for (Keskustelu keskustelu : keskustelut) {
            out.println("<a href='index.html?id=" + keskustelu.getId() + "'>" + keskustelu.getOtsikko() + "</a><br>");
        }
    %>

</div>
</body>
</html>