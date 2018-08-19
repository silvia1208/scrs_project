<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Sping MVC Redirection Application</title>
</head>
<body>
    <h2>Spring Page Redirection Example</h2>
    <div id="welcomeTextDiv">
        <span id="btnText" style="padding-left: 24px;">!! Click Below Button To Redirect The Result To The New Page !!</span>
        <div id="redirectBtnTable" style="padding: 23px 0px 0px 35px;">
        
        
        
        
            <form:form id="redirectionForm" action="redirect_page" method="GET">
            <input name="userName" type="text" value="qui il tuo testo" size="40" maxlength="200" />
             <input name="password" type="password" value="qui il tuo testo" size="40" maxlength="200" />
            
                <table>
                    <tbody>
                        <tr>
                            <td>
                                <input id="redirectBtn" type="submit" value="Redirect Page" />
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form:form>
        </div>
    </div>
</body>
</html>
