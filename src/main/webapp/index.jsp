<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="luokat.Kategoria" %>
<%
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Academy forum</title>
    <link rel="stylesheet" type="text/css" href="forumstyle.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="stuff">
    <blockquote>
        <p style="text-align: center">Täällä voit keskustella Academyyn liittyvistä aiheista</p>
    </blockquote>
    <p >Valitse aihe luettavaksi, tai kirjaudu ja lisää kommentteja.</p>
    <p id="intro"> Voit myös luoda käyttäjätunnukset jos sinulla ei vielä ole sellaisia.</p>
    <div class="topics">
        <div class="leftDIV" onclick="location.href='/kategoria?id=1';" style="cursor: pointer; text-align: center;"><p class="categories">KOODAUS</p></div>
        <div class="middleDIV" onclick="location.href='/kategoria?id=2';" style="text-align: center"><p class="categories">SOFT SKILLS</p></div>
        <div class="rightDIV" onclick="location.href='/kategoria?id=3';" style="text-align: center"><p class="categories">RANDOM</p></div>
    </div>
</div>
<div class="bottom">
    <p>Varoitus: sivulla tietoturva-aukkoja</p>
</div>
</body>
</html>