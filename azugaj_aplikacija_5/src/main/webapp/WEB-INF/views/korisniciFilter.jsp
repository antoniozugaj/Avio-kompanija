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
<h2>Korisnici</h2>
<form method = "POST" action = "${pageContext.servletContext.contextPath}/mvc/korisnici/korisnici" >

    <label for="ime">Ime:</label>
    <input type="text" id="ime" name="ime"><br><br>
    
    <label for="prezime">Prezime:</label>
    <input type="text" id="prezime" name="prezime"><br><br>
    
    <input type="submit" value="Pretrazi">
 </form>
 <hr>
 <hr>
 <a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetna">Povratak na izbornik</a><br>

</body>
</html>