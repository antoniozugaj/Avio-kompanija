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
	<h2>Udaljenosti Aerodroma</h2>
	<table>
		<tr>
			<th>ICAO</th>
			<th>Država</th>
			<th> Kilometraža</th>
		</tr>
		<c:forEach var="ud" items="${requestScope.udaljenost}">
			<tr>
				<td>${ud.icao}</td>
				<td>${ud.udaljenost.drzava}</td>
				<td>${ud.udaljenost.km}</td>
			</tr>
		</c:forEach>
	</table>

 <hr>

 <a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiUdaljenostiIcaoFilter">Povratak na unos aerodroma</a><br>
</body>
</html>