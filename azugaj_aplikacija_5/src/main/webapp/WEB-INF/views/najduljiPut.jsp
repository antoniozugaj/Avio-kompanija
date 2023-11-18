<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="${autor}">

<meta name="description" content="${opis}">
<title>Aerodromi udaljenosti</title>
</head>
<body>
	<h1>Najdulji Put</h1>
	<hr>
	<table>
		<tr>
			<th>ICAO</th>
			<th>Država</th>
			<th> Kilometraža</th>
		</tr>
		<c:forEach var="aer" items="${requestScope.aerodromi}">
			<tr>
				<td>${aer.icao}</td>
				<td>${aer.udaljenost.drzava}</td>
				<td>${aer.udaljenost.km}</td>
			</tr>
		</c:forEach>
	</table>
	<hr>
	<a href="${pageContext.servletContext.contextPath}">Povratak na početnu stranicu</a><br>
</body>
</html>