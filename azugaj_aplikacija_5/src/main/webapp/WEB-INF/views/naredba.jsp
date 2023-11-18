<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="${autor}">

<meta name="description" content="${opis}">

<title>Naredbe za prvi server</title>

</head>
<body>
	<h2>${podaci.opis()}</h2>
	<br>
	<hr>
	<a href="${pageContext.servletContext.contextPath}/mvc/naredbe/pocetak">Natrag na izbornik komandi</a><br>
</body>
</html>