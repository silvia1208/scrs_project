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
    var captcha;

    $(document).ready(function () {
        initialize();
    });

    function initialize() {
        captcha = Raphael(document.getElementById('captcha-container'),320,320);
        dim = (100.0 / 6.0);
        pos = 0;

    };

    function select_it(event,quale) {
        var pos_x = 0;
        var pos_y = 0;
        var container_captcha = document.getElementById('captcha-container');
        pos_x = (event.offsetX ? (event.offsetX) : event.layerX) - container_captcha.offsetLeft;
        pos_y = (event.offsetY ? (event.offsetY) : event.layerY) - container_captcha.offsetTop;
        alert('X: '+pos_x+'Y: '+pos_y)

        var x = Math.floor((pos_x+dim/4) / dim);
        var y = Math.floor((pos_y) / dim);
        alert('X: '+x+'Y: '+y)

        var cerchio = captcha.circle(0, 0, 0).attr({stroke: "green", "stroke-width": 0});
        cerchio.animate({
            "20%": {cx: pos_x, cy: pos_y, r: 0, "stroke-width": 0},
            "100%": {cx: x*dim+dim/2, cy: y*dim+dim/2, r: dim / 2.5, "stroke-width": 4, easing: ">"}
        }, 100);
    }

</script>
<body>
<div class="container">
    <img>
    <div id="captcha-container" onclick="select_it(event,0)" style="background-image: url(resources/img/captcha/mtfa_000000.gif)">

    </div>
</div>
</body>
</html>
