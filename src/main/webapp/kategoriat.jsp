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

<h2> <% out.println(request.getAttribute("kategoria")); %> </h2>


<div id="keskustelut">
    <%
        ArrayList<String> keskustelut = (ArrayList<String>) request.getAttribute("keskusteluotsikot");
        for (String keskustelu : keskustelut) {
            out.println("<a href='index.html?kategoria=" + keskustelu + "'>" + keskustelu + "</a><br>");
        }
    %>

</div>
</body>
</html>