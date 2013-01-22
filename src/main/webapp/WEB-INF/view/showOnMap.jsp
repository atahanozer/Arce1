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
	width:800px;
	height:600px;
}
</style>
<script type="text/javascript" 
   src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
   
<script src="/spring-hibernate-mysql/js/jquery.js"></script>
 
<script type="text/javascript">
var map;
var markerLatlngs = new Array();
var markerTitles = new Array();
var markerIds = new Array();
var markerUrls = new Array();

var message = ["This","is","the","secret","message"];
var infowindow = new google.maps.InfoWindow(
    { content: message[0],
      size: new google.maps.Size(500,500)
    });


  function loadMap() {
    var latlng = new google.maps.LatLng(41.0856030, 29.0444120);
    var myOptions = {
      zoom: 14,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map_container"),myOptions);
/*    var marker = new google.maps.Marker({
      position: latlng, 
      map: map, 
      title:"my hometown, Malim Nawar!"
    }); */
    
    var i = 0;
    for (var i = 0; i < markerLatlngs.length; i++) {
    	 var tempMarker = new google.maps.Marker({
   	      position: markerLatlngs[i], 
   	      map: map, 
   	      title: markerIds[i]
   	    });
    	 attachMessage(tempMarker, i);
    	 
    	 /*   google.maps.event.addListener(tempMarker, 'click', function(event) {
    	    	alert(marker.title);
    	    	doAjaxPost(marker.title);
    	      });
    	 */
    }
    
  }
  
  function attachMessage(marker, number) {
	  //alert(markerUrls[number]);
	  google.maps.event.addListener(marker, 'click', function() {
		  infowindow.close();
		 
		  infowindow.setContent('Baslik: '+markerTitles[number]+
				  '<br><img width="250" src="'+markerUrls[number]+'" />'+
				  '<br><a href="/spring-hibernate-mysql/violation/violations/show?id='+marker.title+'">Detay</a>');
	    infowindow.open(map,marker);
	    infowindow.zIndex(10);
	  });
	}

  
  function addMarker(title,longtitude,lattitude,violId,url) {
	
	    var latlng = new google.maps.LatLng(lattitude, longtitude);
	    
	 	markerLatlngs[markerLatlngs.length] = latlng;
	 	markerTitles[markerTitles.length] = title;
	 	markerIds[markerIds.length] = violId;
	 	markerUrls[markerUrls.length] = url;
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
            <c:url var="addUrl" value="/violation/violations/add" />
				<li><a href=<c:url value="/home"/>>Ana Sayfa</a></li> 
              <li><a href="${newsUrl}">Yeniler</a></li>
              <li class="active"><a href="#">Haritada Göster</a></li>
              <li><a href="#">Kategoriler</a></li>
              <li><a href="${addUrl}">Bildir</a></li>
            </ul>
          </div><!--/.well -->
        </div><!--/span-->
        <div id="main" class="span9">
<c:url var="saveUrl" value="/violation/violations/getForMap" />
<form:form modelAttribute="violationAttribute" method="POST" action="${saveUrl}" >
	<table>
	<tbody>
	<c:forEach items="${violations}" var="violation">
	<script type="text/javascript">
		addMarker('${violation.title}','${violation.longtitude}','${violation.lattitude}','${violation.id}','${violation.url}');
  	</script>
	</c:forEach>
	
		<tr>
			<td><form:label path="handicapType">Engel Tipi</form:label></td>
			<td><form:select path="handicapType" items="${handicapTypeList}" multiple="true" /></td>
		
			<td><form:label path="tagId">Engel Turu</form:label></td>
			<td id="aftertr"><form:select path="tagId" id ="tagId" items="${tagList}" /></td>
			
			<td><input type="submit" value="Filtre Uygula" /></td>
		</tr>
	<tr>
				<td><form:hidden path="lastAddedViolationId" id="lastAddedViolationId"/></td>
		</tr>
	</tbody>
	</table>
	<div id="map_container"></div>
	
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
