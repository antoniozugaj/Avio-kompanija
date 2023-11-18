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
	<h2>Letovi</h2>
	<table>
		<tr>
			<th>arrivalAirportCandidatesCount</th>
			<th>callsign</th>
			<th>departureAirportCandidatesCount</th>
			<th>estArrivalAirport</th>
			<th>estArrivalAirportHorizDistance</th>
			<th>estArrivalAirportVertDistance</th>
			<th>estDepartureAirport</th>
			<th>estDepartureAirportHorizDistance</th>
			<th>estDepartureAirportVertDistance</th>
			<th>firstSeen</th>
			<th>icao24</th>
			<th>lastSeen</th>
		</tr>
		<c:forEach var="x" items="${requestScope.podaci}">
			<tr>
				<td>${x.arrivalAirportCandidatesCount}</td>
				<td>${x.callsign}</td>
				<td>${x.departureAirportCandidatesCount}</td>
				<td>${x.estArrivalAirport}</td>
				<td>${x.estArrivalAirportHorizDistance}</td>
				<td>${x.estArrivalAirportVertDistance}</td>
				<td>${x.estDepartureAirport}</td>
				<td>${x.estDepartureAirportHorizDistance}</td>
				<td>${x.estDepartureAirportVertDistance}</td>
				<td>${x.firstSeen}</td>
				<td>${x.icao24}</td>
				<td>${x.lastSeen}</td>
			</tr>
		</c:forEach>
	</table>
 <hr>
 <a href="${pageContext.servletContext.contextPath}/mvc/letovi/pocetna">Povratak na izbornik</a><br>
</body>
</html>