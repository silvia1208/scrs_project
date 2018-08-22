<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>SCRS Progetto</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css"  rel="stylesheet" type="text/css" media="all">
    <link href="${pageContext.request.contextPath}/resources/css/signin.css" rel="stylesheet" type="text/css" media="all">
    <link href="${pageContext.request.contextPath}/resources/css/custom.css" rel="stylesheet" type="text/css" media="all">

    <script src="${pageContext.request.contextPath}/resources/js/jquery-3.3.1.slim.min.js"> </script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"> </script>
    <script src="${pageContext.request.contextPath}/resources/js/popper.min.js"> </script>

</head>
<body>

<div class="container">
    <form:form id="registrationForm" action="esegui_registrazione" method="GET" class="form-signin">
        <h2 class="form-signin-heading">Registrazione </h2>
        <label for="domain" class="sr-only">Dominio</label>
        <input  class="form-control" id="domain" name="dominio" type="text" value="" placeholder="Dominio" required autofocus />
        <label for="user" class="sr-only">Username</label>
        <input class="form-control" id="user" name="username" type="text" value="" required placeholder="Username" />
        <label for="password" class="sr-only">Password</label>
        <input class="form-control" id="password" name="password" type="password" value="" required placeholder="Password" />
        <button class="btn btn-lg btn-primary btn-block" type="submit">
            Inserisci Pin
        </button>
    </form:form>
</div>

</body>
</html>