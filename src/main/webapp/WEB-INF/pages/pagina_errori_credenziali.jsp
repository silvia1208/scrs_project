<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    
</head>
<body>
<h1>${msg}</h1>
    
    <div id="welcomeTextDiv">
        <span id="btnText" style="padding-left: 24px;"></span>
        <div id="redirectBtnTable" style="padding: 23px 0px 0px 35px;">
        
                      
         
                
             <form:form id="registrationForm" action="authentication" method="GET"> 
             <br>
             <br>
             
             <br>  
              <table>
                    <tbody>
                        <tr>
                            <td>
                                <input id="redirectBtn" type="submit" value="Ritorna Autenticazione" />
                            </td>
                        </tr>
                    </tbody>
                </table>
              
             </form:form>
                                                                                               
        </div>
    </div>
</body>
</html>