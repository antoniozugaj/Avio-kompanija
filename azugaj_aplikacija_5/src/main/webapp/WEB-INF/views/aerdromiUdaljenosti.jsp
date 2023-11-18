<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="${autor}">

<meta name="description" content="${opis}">
<title>Aerodromi</title>
</head>
<body>
	<h1>Putevi između aerodroma</h1>
	<hr>
	<table>
		<tr>
			<th>Drzava</th>
			<th>Kilometraža</th>
		</tr>
		<c:forEach var="aer" items="${requestScope.udaljenosti}">
			<tr>
				<td>${aer.drzava}</td>
				<td>${aer.km} km</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	UKUPNO = ${ukupna} KM
	<hr>
	<a href="${pageContext.servletContext.contextPath}">Povratak na početnu stranicu</a><br>
</body>
</html>