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
<h2>Prijava</h2>
<form method = "POST" action = "${pageContext.servletContext.contextPath}/mvc/korisnici/login" >
	<label for="korime">Korisničko ime:</label>
    <input type="text" id="korime" name="korime" required><br><br>
    
    <label for="lozinka">Lozinka:</label>
    <input type="password" id="lozinka" name="lozinka" required><br><br>

    <input type="submit" value="Prijavi se">
 </form>
 <hr>
 <a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetna">Povratak na izbornik</a><br>

</body>
</html>