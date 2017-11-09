<%@ page import="java.io.PrintWriter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<sql:query var="rs" dataSource="jdbc/foorumi">
    SELECT id, kayttajatunnus, rooli FROM kayttaja
</sql:query>
<html>
<head>
    <title>Käyttäjät</title>
    <link rel="stylesheet" href="forumstyle.css">
</head>
<body>
<%@ include file="header.jsp" %>
<a href="/">Etusivu</a>
<%

    HttpSession istunto = request.getSession(false);
    if (istunto == null || istunto.getAttribute("nimi") == null) {
        response.sendRedirect("/");

    } else {
        String rooli = (String) istunto.getAttribute("rooli");
        if (rooli.equals("moderaattori")) {
            out.println("<p>Olet kirjautunut ylläpitäjänä</p>");
        } else {
            response.sendRedirect("/");
        }


    }
%>
<h1>Käyttäjät</h1>
<div id="viestit">
    <table style="width:100%">
        <tr>
            <th align="left">Kayttäjätunnus</th>
            <th align="left">Rooli</th>
        </tr>
        <c:forEach var="row" items="${rs.rows}">
            <tr>
                <td><c:out value="${row.kayttajatunnus}"></c:out></td>
                <td><c:out value="${row.rooli}"></c:out></td>
                <td>
                    <form action="/poista?kayttajaID=${row.id}" method="post">
                        <input type="submit" value="Poista">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
