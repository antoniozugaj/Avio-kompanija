<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="${autor}">

<meta name="description" content="${opis}">

<title>Naredbe za prvi server</title>

</head>
<body>
	<form action="${pageContext.servletContext.contextPath}/mvc/naredbe/naredba" method="POST">
  		<input type="hidden" name="naredba" value="INIT">
  		<input type="submit" name="submit" value="INIT">
	</form>
	<form action="${pageContext.servletContext.contextPath}/mvc/naredbe/naredba" method="POST">
  		<input type="hidden" name="naredba" value="STATUS">
  		<input type="submit" name="submit" value="STATUS">
	</form>
	<form action="${pageContext.servletContext.contextPath}/mvc/naredbe/naredba" method="POST">
  		<input type="hidden" name="naredba" value="KRAJ">
  		<input type="submit" name="submit" value="KRAJ">
	</form>
	<form action="${pageContext.servletContext.contextPath}/mvc/naredbe/naredba" method="POST">
  		<input type="hidden" name="naredba" value="PAUZA">
  		<input type="submit" name="submit" value="PAUZA">
	</form>
	<form action="${pageContext.servletContext.contextPath}/mvc/naredbe/naredba" method="POST">
  		<input type="hidden" name="naredba" value="INFO DA">
  		<input type="submit" name="submit" value="INFO DA">
	</form>
	<form action="${pageContext.servletContext.contextPath}/mvc/naredbe/naredba" method="POST">
  		<input type="hidden" name="naredba" value="INFO NE">
  		<input type="submit" name="submit" value="INFO NE">
	</form>
	<br>
	<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/">Natrag na glavni izbornik</a><br>
</body>
</html>