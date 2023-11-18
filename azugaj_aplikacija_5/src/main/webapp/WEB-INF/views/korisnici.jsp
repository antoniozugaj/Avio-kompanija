<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="${autor}">

<meta name="description" content="${opis}">
<title>Insert title here</title>
</head>
<body>
<h2>Korisnici</h2>
<form method = "POST" action = "${pageContext.servletContext.contextPath}/mvc/korisnici/korisnici" >

    <label for="ime">Ime:</label>
    <input type="text" id="ime" name="ime"><br><br>
    
    <label for="prezime">Prezime:</label>
    <input type="text" id="prezime" name="prezime"><br><br>
    
    <input type="submit" value="Pretrazi">
 </form>
 <hr>
 <table>
		<tr>
			<th>IME</th>
			<th>PREZIME</th>
			<th>EMAIL</th>
			<th>KORISNIČKO IME</th>
		</tr>
		<c:forEach var="kor" items="${requestScope.podaci}">
			<tr>
				<td>${kor.ime}</td>
				<td>${kor.prezime}</td>
				<td>${kor.email}</td>
				<td>${kor.korIme}</td>
			</tr>
		</c:forEach>
	</table>
 
 <hr>
 <a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetna">Povratak na izbornik</a><br>

</body>
</html>