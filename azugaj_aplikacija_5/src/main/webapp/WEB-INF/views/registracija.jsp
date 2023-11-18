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
<h2>Registracija</h2>
<form method = "POST" action = "${pageContext.servletContext.contextPath}/mvc/korisnici/registriraj" >
	<label for="korime">Korisničko ime:</label>
    <input type="text" id="korime" name="korime" required><br><br>
    
    <label for="lozinka">Lozinka:</label>
    <input type="password" id="lozinka" name="lozinka" required><br><br>
    
    <label for="ime">Ime:</label>
    <input type="text" id="ime" name="ime" required><br><br>
    
    <label for="prezime">Prezime:</label>
    <input type="text" id="prezime" name="prezime" required><br><br>
    
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required><br><br>
    
    <input type="submit" value="Registriraj se">
 </form>
 <hr>
 <a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pocetna">Povratak</a><br>

</body>
</html>