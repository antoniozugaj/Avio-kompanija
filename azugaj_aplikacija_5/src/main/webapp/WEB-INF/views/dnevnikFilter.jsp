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
	<h2>Dnevnik</h2>
<form method = "POST" action = "${pageContext.servletContext.contextPath}/mvc/dnevnik/ispis" >


    <label for="vrsta">Vrsta:</label>
    <input type="text" id="vrsta" name="vrsta" placeholder="AP2 / AP4 / AP5"><br><br>
    
    <label for="broj">Broj zapisa po stranici:</label>
    <input type="text" id="broj" name="broj"><br><br>
    
    <label for="odBroja">Zapisi od broja:</label>
    <input type="text" id="odBroja" name="odBroja"><br><br>
    
    <input type="submit" value="Pretrazi">
 </form>
 <hr>
 
 <hr>
 <a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/">Povratak na izbornik</a><br>
</body>
</html>