<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="${autor}">

<meta name="description" content="${opis}">

<title>Početna</title>

</head>
<body>
	<h2>Aerodromi</h2>
<form method = "POST" action = "${pageContext.servletContext.contextPath}/mvc/aerodromi/svi" >

    <label for="naziv">Naziv Aerodroma:</label>
    <input type="text" id="naziv" name="naziv"><br><br>
    
    <label for="drzava">Drzava:</label>
    <input type="text" id="drzava" name="drzava"><br><br>
    
    <label for="broj">Broj zapisa po stranici:</label>
    <input type="text" id="broj" name="broj"><br><br>
    
    <label for="odBroja">Zapisi od broja:</label>
    <input type="text" id="odBroja" name="odBroja"><br><br>
    
    <input type="submit" value="Pretrazi">
 </form>
 <hr>
 <table>
		<tr>
			<th>ICAO</th>
			<th>Naziv</th>
			<th>Država</th>
		</tr>
		<c:forEach var="aer" items="${requestScope.aerodromi}">
			<tr>
				<td><a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/${aer.icao}">${aer.icao}</a></td>
				<td>${aer.naziv}</td>
				<td>${aer.drzava}</td>
			</tr>
		</c:forEach>
	</table>
 <hr>
 <a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">Povratak na izbornik</a><br>
</body>
</html>