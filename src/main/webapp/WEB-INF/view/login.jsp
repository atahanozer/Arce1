<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>ARCEONE</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <link href="<c:url value="/css/bootstrap.css"/>" rel="stylesheet" media="screen" type="text/css" /> 
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" media="screen" type="text/css" /> 
 	<link href="<c:url value="/css/animate.min.css"/>" rel="stylesheet" media="screen" type="text/css" /> 
 <link href="<c:url value="/css/login_style.css"/>" rel="stylesheet" media="screen" type="text/css" /> 
 	

	<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    
  </head>
  <body>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
        <a class="brand" href="#"><img src="http://swe-574-2012-fall-group3.googlecode.com/files/ARCE1.png" /></a>
                   <div class="nav-collapse collapse">
            <ul class="nav pull-right">
               <sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_USER">
              <li><a href="user/use"><sec:authentication property="principal.username"/></a></li>
              </sec:authorize>
              <sec:authorize ifNotGranted="ROLE_ADMIN,ROLE_USER">
              <li><a href="login">Giris</a></li>
              </sec:authorize>
              <sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_USER">
              <li><a href=<c:url value="/j_spring_security_logout"/>>Cikis</a></li>
              </sec:authorize>
              <sec:authorize ifNotGranted="ROLE_ADMIN,ROLE_USER">
              <li><a href="showRegistration">Kayit Ol!</a></li>
              </sec:authorize>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <div class="container-fluid">
      <div class="row-fluid">
        <div class="span3">
          <div class="well sidebar-nav">
            <ul class="nav nav-list">
             <li class="active"><a href=<c:url value="home"/>>Ana Sayfa</a><br/></li> 
              <li><a href="#">Yeniler</a></li>
              <li><a href="#">Haritada Göster</a></li>
              <li><a href="#">Kategoriler</a></li>
              <li><a href="#">Bildir</a></li>
            </ul>
          </div><!--/.well -->
        </div><!--/span-->
        <div id="main" class="span9">
   <div class="container">
			<c:if test="${not empty param.error}">
    <font color="red">
        Login error. <br />
        Reason : ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
    </font>
</c:if>
<form method="POST" class="form-signin animated bounceInDown" action="<c:url value="/j_spring_security_check" />">
       <img src="http://swe-574-2012-fall-group3.googlecode.com/files/ARCE1.png" style="width:30%;float: right;"/>
        
        
    <table>
        <tr>

            <td><input type="text" class="input-block-level" placeholder="Kulanici Adi" name="j_username" /></td>
        </tr>
        <tr>

            <td><input type="password" class="input-block-level" placeholder="Sifre" name="j_password" /></td>
        </tr>

        <tr>
            <td colspan="2" align="right">
                <input type="submit" class="btn btn-large btn-primary btn-block"  value="Login" />
            </td>
        </tr>
    </table>
</form>


    </div>
        </div>
      </div><!--/row-->

      <hr>

      <footer>
        <p>&copy; ARCEONE 2012</p>
      </footer>

    </div><!--/.fluid-container-->
       <script src="js/bootstrap.js"></script>
	<script src="js/sammy.js"></script>
	<script src="js/sammy.template.js"></script>
	<script src="js/script.js"></script>
  </body>
</html>
