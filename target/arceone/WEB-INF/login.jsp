<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>ARCEONE</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <link href="WEB-INF/view/css/bootstrap.css" rel="stylesheet" media="screen">
    <link href="WEB-INF/view/css/style.css" rel="stylesheet" media="screen">
    <link href="WEB-INF/view/css/animate.min.css" rel="stylesheet" media="screen">
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
              <li><a href="#">Kayit ol</a></li>
              <li><a href="#contact">Giris</a></li>
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
              <li class="active"><a href="#">Yeniler</a></li>
              <li><a href="#">Haritada Göster</a></li>
              <li><a href="#">Kategoriler</a></li>
              <li><a href="#">Bildir</a></li>
            </ul>
          </div><!--/.well -->
        </div><!--/span-->
        <div id="main" class="span9">
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
            <td><input type="text" name="j_username" /></td>
        </tr>
        <tr>
            <td align="right">Password</td>
            <td><input type="password" name="j_password" /></td>
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
        </div>
      </div><!--/row-->

      <hr>

      <footer>
        <p>&copy; ARCEONE 2012</p>
      </footer>

    </div><!--/.fluid-container-->
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="WEB-INF/view/js/bootstrap.js"></script>
	<script src="WEB-INF/view/js/sammy.js"></script>
	<script src="WEB-INF/view/js/sammy.template.js"></script>
	<script src="WEB-INF/view/js/script.js"></script>
  </body>
</html>

