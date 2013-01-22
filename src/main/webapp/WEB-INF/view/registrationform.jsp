<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
<head>
    <title>ARCEONE</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <link href="<c:url value="/css/bootstrap.css"/>" rel="stylesheet" media="screen" type="text/css" /> 
    <link href="<c:url value="/css/login_style.css"/>" rel="stylesheet" media="screen" type="text/css" /> 
 	<link href="<c:url value="/css/animate.min.css"/>" rel="stylesheet" media="screen" type="text/css" /> 
    
  </head>
  <body>
    <div class="container">
<c:url var="saveUrl" value="processRegistration" />
<form:form modelAttribute="registration" class="form-signin animated bounceInDown" method="POST" action="${saveUrl}">

       <img src="http://swe-574-2012-fall-group3.googlecode.com/files/ARCE1.png" style="width:30%;float: right;"/>
        
        
    <table>
        <tr>

            <td><input type="text" class="input-block-level" placeholder="Kulanici Adi" name="userName" /></td>
        </tr>
        <tr>

            <td><input type="password" class="input-block-level" placeholder="Sifre" name="password" /></td>
        </tr>
        
        <tr>

            <td><input type="password" class="input-block-level" placeholder="Sifre Tekrar" name="confirmPassword" /></td>
        </tr>
        
        <tr>

            <td><input type="text" class="input-block-level" placeholder="E-mail" name="email" /></td>
        </tr>
		
		
		<tr>
			<td><form:select path="handicapTypes" items="${handicapTypeList}" multiple="true" /></td>
		</tr>
		
        <tr>
            <td colspan="2" align="right">
                <input type="submit" class="btn btn-large btn-primary btn-block"  value="Kayit Ol!" />
            </td>
        </tr>
    </table>
</form:form>


    </div>
        <script src="js/bootstrap.js"></script>
  </body>
</html>

