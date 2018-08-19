<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Registrazione effettuata</title>
</head>
<body>
    <h2>registrazione effettuata</h2>
    <div id="welcomeTextDiv">
        <span id="btnText" style="padding-left: 24px;">fatto</span>
        <div id="redirectBtnTable" style="padding: 23px 0px 0px 35px;">
        
                      
           <h1>${msg}</h1>
                
             <form:form id="registrationForm" action="authentication" method="GET"> 
             <br>
             <br>
             <b>Registrazione effettuata</b>
             <br>  
              <table>
                    <tbody>
                        <tr>
                            <td>
                                <input id="redirectBtn" type="submit" value="Torna autenticazione" />
                            </td>
                        </tr>
                    </tbody>
                </table>
              
             </form:form>
                                                                                               
        </div>
    </div>
</body>
</html>