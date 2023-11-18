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
	<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetna">Korisnici</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/naredbe/pocetak">Naredbe</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pocetak">Aerodromi</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/letovi/pocetna">Letovi</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/dnevnik/filter">Dnevnik</a><br>


</body>
</html>