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

 <!--   <script src="${pageContext.request.contextPath}/resources/js/jquery-3.3.1.slim.min.js"> </script> -->
    <script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"> </script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"> </script>
    <script src="${pageContext.request.contextPath}/resources/js/popper.min.js"> </script>
    <script src="${pageContext.request.contextPath}/resources/js/raphael.min.js"> </script>
</head>
<script type="text/javascript">

    $(document).ready(function () {
        initialize();
    });

    function initialize() {
        captcha = Raphael(document.getElementById('captcha-container'), 320, 320);
        dim = (320.0 / 6.0);
        pos = 0;

        vCerchi = new Array(64);
        for (var i = 0; i < 64; ++i)
            vCerchi[i] = new Array(2);	//x e y
        posCerchi = 0;
        vCodici = new Array(64);
        vCodici[0] = "";
        message_color = "gray";
        status = 0;
    };


</script>
<body>
<div class="container">
    <div id="captcha-container" style="background-image: url(resources/img/captcha/mtfa_000000.gif)">

    </div>
</div>
</body>
</html>
