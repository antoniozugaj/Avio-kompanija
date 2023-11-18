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
	<a href="${pageContext.servletContext.contextPath}/mvc/letovi/letoviIntervalFilter">Pretraga letova po intervalu</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/letovi/letoviDanFilter">Pretraga letova po danu</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/letovi/letoviDanOSFilter">Pretraga letova po danu - OS</a><br>
	<hr>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/">Povratak na glavni izbornik</a><br>
	
</body>
</html>