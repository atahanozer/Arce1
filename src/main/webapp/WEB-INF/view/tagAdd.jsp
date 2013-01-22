<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
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
               <li><a href=<c:url value="user/users/show?userName=admin"/>><sec:authentication property="principal.username"/></a></li>
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
<li class="active"><a href=<c:url value="/home"/>>Ana Sayfa</a></li> 
              <li><a href="#">Yeniler</a></li>
              <li><a href="#">Haritada Göster</a></li>
              <li><a href="#">Kategoriler</a></li>
              <li><a href="#">Bildir</a></li>
            </ul>
          </div><!--/.well -->
        </div><!--/span-->
        <div id="main" class="span9">
    <sec:authorize ifAnyGranted="ROLE_ADMIN">


<c:url var="saveUrl" value="/tag/tags/add" />
<form:form modelAttribute="tagAttribute" method="POST" action="${saveUrl}">
	<table>
		<tr>
			<td><form:label path="title">Baslik:</form:label></td>
			<td><form:input path="title"/></td>
		</tr>

		<tr>
			<td><form:label path="description">Aciklama:</form:label></td>
			<td><form:input path="description"/></td>
		</tr>
		
		<tr>
			<td><form:label path="level">Seviye</form:label></td>
			<td><form:select path="level" items="${levelList}"/></td>
		</tr>
		
		<tr>
			<td><form:label path="parentTagId">Ust Etiket</form:label></td>
			<td><form:select path="parentTagId" items="${parentTagList}" multiple="true" /></td>
		</tr>
	
	</table>
	
	<input type="submit" value="Kaydet" />
</form:form>
    </sec:authorize>
    

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
