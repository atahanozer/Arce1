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
            <c:url var="addUrl" value="/violation/violations/add" />
            <c:url var="newsUrl" value="/violation/violations/news" />
             <c:url var="showOnMapUrl" value="/violation/violations/showOnMap" />
              <li class="active"><a href="home">Ana Sayfa</a></li> 
              <li><a href="${newsUrl}">Yeniler</a></li>
              <li><a href="${showOnMapUrl}">Haritada Göster</a></li>
              <li><a href="#">Kategoriler</a></li>
              <li><a href="${addUrl}">Bildir</a></li>
            </ul>
          </div><!--/.well -->
        </div><!--/span-->
        <div id="main" class="span9">
    <sec:authorize ifAnyGranted="ROLE_ADMIN">
        <h1>Yonetici Paneli</h1>
    <a href=<c:url value="/user/users"/>>Kullanicilar</a><br/>
    <a href=<c:url value="/violation/violations"/>>Tum Engeller Bildirimleri</a><br/>
    <a href=<c:url value="/handicapType/handicapTypes"/>>Engel Turleri</a><br/>
    <a href=<c:url value="/tag/tags"/>>Etiket Tipleri</a><br/>
    
    <table style="border: 1px solid; width: 500px; text-align:center">
			<thead style="background:#fcf">
			<tr style="background:#08C">
					<th colspan="4">Cozumledigim Engeller</th>
				</tr>
				<tr>
					<th>Baslik</th>
					<th>Detay</th>
					<th>Durum</th>
					<th colspan="1"></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${resolvedViolations}" var="violation">
					<c:url var="showUrl" value="/violation/violations/show?id=${violation.id}" />
				<tr>
					<td><c:out value="${violation.title}" /></td>
					<td><c:out value="${violation.description}" /></td>
					<td><c:out value="${violation.status}" /></td>
					<td><a href="${showUrl}">Goster</a></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<br> <br>
    
    </sec:authorize>
    
    <sec:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
		    <table style="border: 1px solid; width: 500px; text-align:center">
			<thead style="background:#fcf">
			<tr style="background:#08C">
					<th colspan="4">Bildirdigim Engeller</th>
				</tr>
				<tr>
					<th>Baslik</th>
					<th>Detay</th>
					<th>Durum</th>
					<th colspan="1"></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${reportedViolations}" var="violation">
					<c:url var="showUrl" value="/violation/violations/show?id=${violation.id}" />
				<tr>
					<td><c:out value="${violation.title}" /></td>
					<td><c:out value="${violation.description}" /></td>
					<td><c:out value="${violation.status}" /></td>
					<td><a href="${showUrl}">Goster</a></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<br> <br>
		 <table style="border: 1px solid; width: 500px; text-align:center">
			<thead style="background:#fcf">
			<tr style="background:#08C">
					<th colspan="4">Takip Ettigim Engeller</th>
				</tr>
				<tr>
					<th>Baslik</th>
					<th>Detay</th>
					<th>Durum</th>
					<th colspan="1"></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${followedViolations}" var="violation">
					<c:url var="showUrl" value="/violation/violations/show?id=${violation.id}" />
				<tr>
					<td><c:out value="${violation.title}" /></td>
					<td><c:out value="${violation.description}" /></td>
					<td><c:out value="${violation.status}" /></td>
					<td><a href="${showUrl}">Goster</a></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
				<br> <br>
		 <table style="border: 1px solid; width: 500px; text-align:center">
			<thead style="background:#fcf">
			<tr style="background:#08C">
					<th colspan="4">Benim Ile Ilgili Engeller</th>
				</tr>
				<tr>
					<th>Baslik</th>
					<th>Detay</th>
					<th>Durum</th>
					<th colspan="1"></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${relatedViolations}" var="violation">
					<c:url var="showUrl" value="/violation/violations/show?id=${violation.id}" />
				<tr>
					<td><c:out value="${violation.title}" /></td>
					<td><c:out value="${violation.description}" /></td>
					<td><c:out value="${violation.status}" /></td>
					<td><a href="${showUrl}">Goster</a></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
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


