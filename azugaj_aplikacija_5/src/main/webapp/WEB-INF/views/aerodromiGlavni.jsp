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
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiFilter">Pregled svih Aerodroma</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiSpremanje">Pregled Aerodroma za preuzimanje</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiUdaljenostFilter">Udaljenosti između aerodroma</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiIzracunFilter">Izračun udaljenosti između aerodrama</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiUdaljenostiIcaoFilter">Izračun svih udaljenosti između aerodrama</a><br>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiUdaljenostiKmFilter">Izračun svih udaljenosti između aerodrama prema kilometraži</a><br>
	<hr>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/">Povratak na glavni izbornik</a><br>
	
</body>
</html>