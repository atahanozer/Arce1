<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


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

      <form class="form-signin animated bounceInDown">
        <img src="http://swe-574-2012-fall-group3.googlecode.com/files/ARCE1.png" style="width:30%;float: right;"/>
        
        

			<c:if test="${not empty param.error}">
    <font color="red">
        Login error. <br />
        Reason : ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
    </font>
</c:if>
<form method="POST" action="<c:url value="/j_spring_security_check" />">
    <table>
        <tr>
            <td align="right">Username</td>
            <td><input type="text" class="input-block-level" placeholder="Kulanici Adi" name="j_username" /></td>
        </tr>
        <tr>
            <td align="right">Password</td>
            <td><input type="password" class="input-block-level" placeholder="Sifre" name="j_password" /></td>
        </tr>

        <tr>
            <td colspan="2" align="right">
                <input type="submit" value="Login" />
            </td>
        </tr>
    </table>
</form>

<ul>
<a href="showRegistration">Register!</a>
    
        
        
        <input type="text" class="input-block-level" placeholder="Kullanici adi">
        <input type="password" class="input-block-level" placeholder="Sifre">
        <button class="btn btn-large btn-primary btn-block" type="submit">&nbsp;&nbsp;Giris&nbsp;&nbsp;</button>
      </form>

    </div>
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="js/bootstrap.js"></script>
  </body>
</html>

