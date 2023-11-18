<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="${autor}">

<meta name="description" content="${opis}">
<title>Aerodrom</title>
</head>
<body>
<h1>Aerodrom</h1>
<hr>
Icao: ${aerodrom.icao}<br>
Naziv: ${aerodrom.naziv}<br>
Država: ${aerodrom.drzava}<br>
Lon: ${aerodrom.lokacija.longitude}<br>
Lat: ${aerodrom.lokacija.latitude}
<hr>
Vlaga: ${meteo.getHumidityValue()} ${meteo.getHumidityUnit()}<br>
Temperatura: ${meteo.getTemperatureValue()} ${meteo.getTemperatureUnit()}<br>
Pritisak: ${meteo.getPressureValue()} ${meteo.getPressureUnit()}<br>
<hr>
<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiFilter">Povratak na sve aerodrome</a><br>
</body>
</html>