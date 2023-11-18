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
	<h2>Aerodromi za preuzimanje</h2>

 <table>
		<tr>
			<th>ICAO</th>
			<th>Naziv</th>
			<th>DRŽAVA</th>
			<th>STATUS</th>
			<th>AKTIVIRAJ</th>
			<th>PAUZIRAJ</th>
		</tr>
		<c:forEach var="aer" items="${requestScope.podaci}">
			<tr>
				<td>${aer.aerodrom.icao}</td>
				<td>${aer.aerodrom.naziv}</td>
				<td>${aer.aerodrom.drzava}</td>
				<td>${aer.status}</td>
				<td><a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiSpremanje/aktiviraj/${aer.aerodrom.icao}">AKTIVIRAJ AERODROM</a></td>
				<td><a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiSpremanje/pauziraj/${aer.aerodrom.icao}">PAUZIRAJ AERODROM</a></td>
			</tr>
		</c:forEach>
	</table>
 <hr>
 <a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">Povratak na izbornik</a><br>
</body>
</html>