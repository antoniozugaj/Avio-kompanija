<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="${autor}">

<meta name="description" content="${opis}">

<title>Početna</title>

</head>
<body>
	<h2>Aerodromi</h2>
<form method = "POST" action = "${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiUdaljenosti">

    <label for="icaoOd">Icao od:</label>
    <input type="text" id="icaoOd" name="icaoOd"><br><br>
    
    <label for="icaoDo">Icao do:</label>
    <input type="text" id="icaoDo" name="icaoDo"><br><br>
    
    
    <input type="submit" value="Izracunaj">
 </form>
 <hr>

 <a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">Povratak na izbornik</a><br>
</body>
</html>