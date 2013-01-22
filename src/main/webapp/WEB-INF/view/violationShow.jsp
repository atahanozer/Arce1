<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <link href="<c:url value="/css/style.css"/>" rel="stylesheet" media="screen" type="text/css" /> 
 	<link href="<c:url value="/css/animate.min.css"/>" rel="stylesheet" media="screen" type="text/css" /> 
 
	
	<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    
      <style type="text/css">
div#map_container{
	width:450px;
	height:350px;
}
</style>
<script type="text/javascript" 
   src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
 
<script src="/spring-hibernate-mysql/js/jquery.js"></script>
 
<script type="text/javascript">
  function loadMap(longg, latt) {

    var latlng = new google.maps.LatLng(latt, longg);
    var myOptions = {
      zoom: 16,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map_container"),myOptions);

    var marker = new google.maps.Marker({
      position: latlng, 
      map: map, 
      title:"my hometown, Malim Nawar!"
    }); 
 
  }
  
  function setAbuseReport() {
      var name = $('#commentDescription').val();
      var education = $('#commentDescription').val();
      $('#status').val('ABUSE');
      alert($('#status').val());
	        }
  
  //Ajax
  function doAjaxPost(violId) {
	 // alert(violId);
        // get the form values
        var name = $('#commentDescription').val();
        var education = $('#commentDescription').val();
        $.ajax({
        type: "POST",
        url: "/spring-hibernate-mysql/violation/violations/addComment",
        data: "commentDescription=" + name + "&id=" + violId,
        success: function(response){
        // we have the response
       // $('#info').html(response);
         $('#info').after("</br>"+response);
        $('#commentDescription').val('');
        },
        error: function(e){
        alert('Error: ' + e);
        }
        });
        }
  
  function doFollow(violId) {
		 // alert(violId);
	        // get the form values
	        var name = $('#commentDescription').val();
	        $.ajax({
	        type: "POST",
	        url: "/spring-hibernate-mysql/violation/violations/follow",
	        data: "commentDescription=" + name + "&id=" + violId,
	        success: function(response){
	        // we have the response
	       // $('#info').html(response);
	         $('#followButton').html("Takipteyim");
	         $('#followButton').style("color:green;");
	        $('#commentDescription').val('');
	        },
	        error: function(e){
	        alert('Error: ' + e);
	        }
	        });
	}
  function doUnFollow(violId) {
		 // alert(violId);
	        // get the form values
	        var name = $('#commentDescription').val();
	        $.ajax({
	        type: "POST",
	        url: "/spring-hibernate-mysql/violation/violations/unFollow",
	        data: "commentDescription=" + name + "&id=" + violId,
	        success: function(response){
	        // we have the response
	       // $('#info').html(response);
	         $('#followButton').html("Takipteyim");
	         $('#followButton').style("color:green;");
	        $('#commentDescription').val('');
	        },
	        error: function(e){
	        alert('Error: ' + e);
	        }
	        });
	 }
</script>
    
  </head>
  <body onload="loadMap('${violationAttribute.longtitude}','${violationAttribute.lattitude}')">
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
        <a class="brand" href="#"><img src="http://swe-574-2012-fall-group3.googlecode.com/files/ARCE1.png" /></a>
                   <div class="nav-collapse collapse">
            <ul class="nav pull-right">
               <sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_USER">
              <li><a href="/user/users/edit?userName=<sec:authentication property="principal.username"/>"><sec:authentication property="principal.username"/></a></li>
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
              <li><a href="${newsUrl}">Yeniler</a></li>
              <li class="active"><a href="${showOnMapUrl}">Haritada Göster</a></li>
              <li><a href="#">Kategoriler</a></li>
              <li><a href="${addUrl}" />Bildir</a></li>
            </ul>
          </div><!--/.well -->
        </div><!--/span-->
        <div id="main" class="span9">
<c:url var="saveUrl" value="/violation/violations/resolve?id=${violationAttribute.id}" />
<form:form modelAttribute="violationAttribute" method="POST" action="${saveUrl}" enctype="multipart/form-data">
	<table>
	
		<tr>
			<td><form:label path="title">Engel:</form:label></td>
			<td>
			<sec:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
			<c:choose>
			  <c:when test="${violationAttribute.isFollowing == 1}"><span id="unFollowButton"><input type="button" value="Takip Etmeyi Birak" onclick="doUnFollow('${violationAttribute.id}')"></span> </c:when>

			  <c:otherwise><span id="followButton"><input type="button" value="Takip Et" onclick="doFollow('${violationAttribute.id}')"></span>
				</c:otherwise>
			</c:choose>
			</sec:authorize>
			</td>
			</tr>
		<tr>	<td><form:input path="title" disabled="true"/></td>
		</tr>
		<tr>
			<td><form:label path="description">Detay:</form:label></td>
			</tr>
		<tr>	<td><form:input path="description" disabled="true"/></td>
		</tr>
		
		<tr>
			<td><form:label path="handicapType">Engel Tipi</form:label>
			<form:select path="handicapType" items="${handicapTypeList}" multiple="true" disabled="true"/>
			</td>
		
		
			<td><form:label path="handicapType">Etiketler</form:label>

		   <c:forEach items="${tags}" var="tag">
				
					<span style="border-width: 1px; border-style: solid; border-color: blue; margin-left:2px; padding:2px;"><b><c:out value="${tag.title} " /></b></span>
				
			</c:forEach>
			</td>
		</tr>
		
		 <c:forEach items="${proofs}" var="proof">
				<tr>
				<td> 
				<span> <b><c:out value="${proof.reportType} " /></b></span>
				<span style="border-width: 1px; border-style: solid; border-color: blue; margin-left:2px; padding:2px;"><b><c:out value="${proof.userName} " /></b></span>
				 <span><b><c:out value="${proof.date} " /> tarihinde ekledi:</b></span>
				 </td>
				<td> <a href="${proof.url}"><img src="${proof.url}" width="450" height="350"/></a></td>
			</tr>
			</c:forEach>
		</br>
		<tr>
		       	<td>	<div id="map_container"></div></td>
		</tr>
		
		<tr>
				<td><form:hidden path="status" id="status"/></td>
		</tr>
		
		<tr>
			<td><form:label path="commentDescription">Yorumlar:</form:label></td>
			</tr>
		
		<tr>	<td><form:input path="commentDescription" id="commentDescription"/></td>
		</tr>
		<sec:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
			<tr><td colspan="2"><input type="button" value="Yorum Ekle" onclick="doAjaxPost('${violationAttribute.id}')"></td></tr>
			
			<tr><td colspan="2"><div id="info" style="color: green;"></div></td></tr>
		</sec:authorize>
		
	 <c:forEach items="${comments}" var="comment">
		<tr>
			<td><b><c:out value="${comment.userName} (${comment.date}) dedi ki: " /></b><br>
			<c:out value="${comment.description}" /></td>
		</tr>
	</c:forEach>
		
	</table>
			 </br>
		
		 <c:choose>
		 
			  <c:when test="${violationAttribute.status == 'ACIK'}">
			   <sec:authorize ifAnyGranted="ROLE_ADMIN">

		

		 <table> 
		
		<tr>
			<td><form:label path="vrDescription">Baslik</form:label></td>
			<td><form:input path="vrDescription"/></td>
		</tr>    
		    
	   <tr>
			<td><form:label path="fileData">Resim</form:label></td>
			<td><form:input path="fileData" type="file"/></td>
		</tr>
		
		<tr><td><input type="submit" value="Kapat" /></td></tr>

		</table>
		</sec:authorize>
		</c:when>
	</c:choose> 
	    
			  <sec:authorize ifAnyGranted="ROLE_USER">
		<table> 
		
		<tr>
			<td><form:label path="vrDescription">Baslik</form:label></td>
			<td><form:input path="vrDescription"/></td>
		</tr>    
		    
	   <tr>
			<td><form:label path="fileData">Resim</form:label></td>
			<td><form:input path="fileData" type="file"/></td>
		</tr>
		
		<tr><td><input type="submit" value="Tekrar Rapor Et" /></td></tr>
		<tr><td><input type="submit" value="Kotuye Kullanim Bildir" onClick="setAbuseReport()" /></td></tr>
		</table>
		</sec:authorize>
		
			  
	
		
	

</form:form>
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