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
	<h2>Udaljenosti Aerodroma</h2>
<form method = "POST" action = "${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiUdaljenostiKm">

    <label for="icao">Icao:</label>
    <input type="text" id="icao" name="icao" required><br><br>
    
    <label for="km">Km:</label>
    <input type="text" id="km" name="km" required><br><br>
    
    <label for="drzava">Država:</label>
    <input type="text" id="drzava" name="drzava" required><br><br>
    
    
    <input type="submit" value="Izracunaj">
 </form>
 <hr>

 <a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">Povratak na izbornik</a><br>
</body>
</html>