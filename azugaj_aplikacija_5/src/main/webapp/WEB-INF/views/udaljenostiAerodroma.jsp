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
	<h1>Udaljenosti</h1>
	<hr>
	<table>
		<tr>
			<th>Država</th>
			<th> Kilometraža</th>
		</tr>
		<c:forEach var="aer" items="${requestScope.udaljenosti}">
			<tr>
				<td>${aer.drzava}</td>
				<td>${aer.km}</td>
			</tr>
		</c:forEach>
	</table><br>
	<hr>
	UKUPNO = ${ukupno}
	<hr>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiUdaljenostiFilter">Povratak na početnu stranicu</a><br>
</body>
</html>