<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
               <li><a href=<c:url value="violation/violations/show?userName=admin"/>><sec:authentication property="principal.username"/></a></li>
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
            <c:url var="addUrl" value="/violation/violations/add" />
            <c:url var="newsUrl" value="/violation/violations/news" />
            <c:url var="showOnMapUrl" value="/violation/violations/showOnMap" />
              <li><a href=<c:url value="/home"/>>Ana Sayfa</a></li> 
              <li class="active"><a href="${newsUrl}">Yeniler</a></li>
              <li><a href="${showOnMapUrl}"/>Haritada Göster</a></li>
              <li><a href="#">Kategoriler</a></li>
              <li><a href="${addUrl}" />Bildir</a></li>
            </ul>
          </div><!--/.well -->
        </div><!--/span-->
        <div id="main" class="span9">
        <h1>Engeller</h1>
<c:url var="addUrl" value="/violation/violations/add" />
<table style="border: 1px solid; width: 500px; text-align:center">
	<thead style="background:#fcf">
		<tr>
			<th>Baslik</th>
			<th>Detay</th>
			<th>Durum</th>
			<th colspan="3"></th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${violations}" var="violation">
			<c:url var="editUrl" value="/violation/violations/edit?id=${violation.id}" />
			<c:url var="deleteUrl" value="/violation/violations/delete?id=${violation.id}" />
			<c:url var="blockUrl" value="/violation/violations/show?id=${violation.id}" />
		<tr>
			<td><c:out value="${violation.title}" /></td>
			<td><c:out value="${violation.description}" /></td>
			<td><c:out value="${violation.status}" /></td>
		<sec:authorize ifAnyGranted="ROLE_ADMIN">
			<td><a href="${editUrl}">Gunelle</a></td>
			<td><a href="${deleteUrl}">Sil</a></td>
		</sec:authorize>
			<td><a href="${blockUrl}">Goster</a></td>
		</tr>
	</c:forEach>
	</tbody>
</table>
<a href="${addUrl}">Ekle</a>
<c:if test="${empty violations}">
	There are currently no violations in the list. <a href="${addUrl}">Add</a> a violation.
</c:if>

    

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
