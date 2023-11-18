<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="${autor}">

<meta name="description" content="${opis}">

<title>Početna</title>

</head>
<body>
	<h2>Letovu na dan</h2>
<form method = "POST" action = "${pageContext.servletContext.contextPath}/mvc/letovi/letoviDan">

    <label for="icao">Icao:</label>
    <input type="text" id="icao" name="icao" required><br><br>
    
    <label for="datum">Datum od:</label>
    <input type="text" id="datum" name="datum" placeholder="dd.mm.gggg" required><br><br>
    
    
    <label for="broj">Broj prikaza po stranici:</label>
    <input type="text" id="broj" name="broj"><br><br>
    
    <label for="odBroja">Broj početnog zapisa:</label>
    <input type="text" id="odBroja" name="odBroja"><br><br>
    
    <input type="submit" value="Prikaži">
 </form>
 <hr>

 <hr>
 <a href="${pageContext.servletContext.contextPath}/mvc/letovi/pocetna">Povratak na izbornik</a><br>
</body>
</html>