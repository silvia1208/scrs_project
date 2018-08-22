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
    <form:form id="registrationForm" action="creaPasswordCifrata" method="GET" class="form-signin">
        <h2 class="form-signin-heading">Registrazione Captcha</h2>
        <label class="sr-only">Inserimento Captcha</label>
        <label for="captchaPin" class="sr-only">Pin</label>
        <input class="form-control" id="captchaPin" name="captchaPin" type="text" value="" required placeholder="Pin" />
        <button class="btn btn-lg btn-primary btn-block" type="submit">Effettua registrazione</button>
    </form:form>
</div>

</body>
</html>