<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="${autor}">

<meta name="description" content="${opis}">
<title>Insert title here</title>
</head>
<body>
<h2>Izbornik korisnici</h2>

 	<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/prijava">Prijava</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/registracija">Registracija</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/korisniciFilter">Korisnici</a><br>
	<hr>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/">Prijašnji izbornik</a><br>
	
</body>
</html>