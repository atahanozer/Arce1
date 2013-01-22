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
  function loadMap() {
	    document.getElementById('longtitude').value = 29.0444120;
	    document.getElementById('lattitude').value = 41.0856030;
    var latlng = new google.maps.LatLng(41.0856030, 29.0444120);
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
    
  
    
    google.maps.event.addListener(map, "click", function(event) {
        var lat = event.latLng.lat();
        var lng = event.latLng.lng();
        marker.setMap(null);
        marker = new google.maps.Marker({
            position: new google.maps.LatLng(lat, lng), 
            map: map, 
            title:"my hometown, Malim Nawar!"
          }); 
        google.maps.event.addListener(marker, 'dblclick', function() {
            map.setZoom(16);
            map.setCenter(marker.getPosition());
          });
        document.getElementById('longtitude').value = lng;
        document.getElementById('lattitude').value = lat;
        // populate yor box/field with lat, lng
       // alert("Lat=" + lat + "; Lng=" + lng);
    });
 
  }
  
  function doAjaxPost(violId) {
		 // alert(violId);
	        // get the form values
	        var name = $('#tagTitle').val();
	        var lastAddedViolId = $('#lastAddedViolationId').val();
	        $.ajax({
	        type: "POST",
	        url: "/spring-hibernate-mysql/violation/violations/addTag",
	        data: "tagTitle=" + name + "&lastAddedViolationId=" + lastAddedViolId,
	        success: function(response){
	        // we have the response
	       // $('#info').html(response);
	        if(response != ""){
	         $('#info').after("</br>"+response);
	        }
	        $('#tagTitle').val('');
	        },
	        error: function(e){
	        alert('Error: ' + e);
	        }
	        });
	        }
  
	var comboCounter =0;
  
  function getCombo(tagId) {
		 // alert(violId);
	        // get the form values
	        var name = $('#tagTitle').val();
	      //  var lastAddedViolId = $('#lastAddedViolationId').val();
	        $.ajax({
	        type: "POST",
	        url: "/spring-hibernate-mysql/violation/violations/getTagCombo",
	        data: "tagId=" + tagId + "&lastAddedViolationId=" + name,
	        success: function(response){
	        // we have the response
	       // $('#info').html(response);
	     //   if(response != ""){
		        if(comboCounter >0){
		        	//alert(comboCounter);
			        $('#aftertr2').remove();
			        }
	         $('#aftertr').after(response);
	     //   }

	        comboCounter++;
	        },
	        error: function(e){
	        alert('Error: ' + e);
	        }
	        });
	        }
</script>
    
  </head>
  <body onload="loadMap()">
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
            <c:url var="newsUrl" value="/violation/violations/news" />
<li class="active"><a href=<c:url value="/home"/>>Ana Sayfa</a></li> 
              <li><a href="${newsUrl}">Yeniler</a></li>
              <li><a href="#">Haritada Göster</a></li>
              <li><a href="#">Kategoriler</a></li>
              <li><a href="#">Bildir</a></li>
            </ul>
          </div><!--/.well -->
        </div><!--/span-->
        <div id="main" class="span9">
<c:url var="saveUrl" value="/violation/violations/add" />
<form:form modelAttribute="violationAttribute" method="POST" action="${saveUrl}" enctype="multipart/form-data" >
	<table>
	     <tr>
			<td><form:label path="title">Baslik</form:label></td>
			<td><form:input path="title"/></td>
		</tr>
	    
		<tr>
			<td><form:label path="description">Tanim:</form:label></td>
			<td><form:textarea path="description" rows="3" cols="30"/></td>
		</tr>
		
		<tr>
			<td><form:label path="handicapType">Engel Tipi</form:label></td>
			<td><form:select path="handicapType" items="${handicapTypeList}" multiple="true" /></td>
		</tr>
		
		 <tr id="aftertr">
			<td><form:label path="tagId">Engel Turu</form:label></td>
			<td><form:select path="tagId" id ="tagId" items="${tagList}" /></td>
		</tr>

		<tr>
			<td><form:label path="tagTitle">Etiketler:</form:label></td>
		</tr>
		
		<tr>	<td><form:input path="tagTitle" id="tagTitle"/></td>
				<td><input type="button" value="Etiket Ekle" onclick="doAjaxPost('${violationAttribute.id}')"></td>
	
		</tr>

		<tr><td colspan="2"><div id="info" style="color: green;"></div></td></tr>
		
	    <tr>
			<td><form:label path="violationLevel">Seviye</form:label></td>
			<td><form:select path="violationLevel" items="${violationLevelList}" /></td>
		</tr>
		
		<tr>
			<td><form:label path="fileData">Resim</form:label></td>
			<td><form:input path="fileData" type="file"/></td>
		</tr>
		
		<tr>
				<td><form:hidden path="longtitude" id="longtitude"/></td>
		</tr>
		
		<tr>
				<td><form:hidden path="lattitude" id="lattitude"/></td>
		</tr>
		<tr>
				<td><form:hidden path="lastAddedViolationId" id="lastAddedViolationId"/></td>
		</tr>
		
	</table>
	<div id="map_container"></div>
	    
	
	<input type="submit" value="Bildir" />
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
	
	<script>
	var changeCounter = 0;
	  $("#tagId").change(function () {
	      var str = "";
	      var i=0;
	     
	      $("select option:selected").each(function () {
	    	  if(i==0 && changeCounter > 0){
	            str += $(this).val() + " ";
	          //  alert($(this).val());
	            getCombo($(this).val());
	            i++;
	          }    
	    	  changeCounter++;
	      });
	     // $("div").text(str);
	    })
	    .trigger('change');
	</script>
	
  </body>
</html>
