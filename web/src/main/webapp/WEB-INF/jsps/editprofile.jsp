<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="include.jsp" />
</head>
<body>
    <div class="outerwrapper">
        <jsp:include page="header.jsp" />

<div class="right-pane fixed">
    <h2>My Profile</h2>
    <div class="profile">
        <div class="innerblock">
            <img src="${user.person.profilePhoto}" alt="profile-pic" class="profile-pic">
            <p class="read"> <strong>${user.person.name}</strong>
                <br>
                <span>25 Yrs, ${user.person.gender}</span>
            </p>
        </div>
        <div class="innerblock">
            <p class="read"> <strong>CBD-Belapur Constituency</strong>
                <br>
                <span>Thane District, Maharashtra</span>
                <a href="10-profile-edit.php" class="edit-fields" title="Edit"> <i class="glyphicon glyphicon-pencil"></i>
                </a>
            </p>
        </div>
        <div class="innerblock">
            <p class="edit">
                <i class="glyphicon glyphicon-plus-sign"></i>
                Add your Voter ID details
            </p>
            <input type="text" class="form-control" placeholder="Voter Card No"></div>
    </div>
</div>
<!-- /.right-pane -->
<div class="locate-user">
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
    <div style="overflow:hidden;height:100%;width:100%;">
        <div id="gmap_canvas" style="height:100%;width:100%;"></div>
        <style>#gmap_canvas img{max-width:none!important;background:none!important}</style>
        <a class="google-map-code" href="http://www.mapsembed.com/conrad-gutschein/" id="get-map-data">http://www.mapsembed.com/conrad-gutschein/</a>
    </div>
    <script type="text/javascript"> function init_map(){var myOptions = {zoom:14,center:new google.maps.LatLng(19.0300657,73.03511379999998),mapTypeId: google.maps.MapTypeId.ROADMAP};map = new google.maps.Map(document.getElementById("gmap_canvas"), myOptions);marker = new google.maps.Marker({map: map,position: new google.maps.LatLng(19.0300657, 73.03511379999998)});infowindow = new google.maps.InfoWindow({content:"<b>Somnath Nabajja</b><br/>CBD Belapur<br/>400706 Navi Mumbai" });google.maps.event.addListener(marker, "click", function(){infowindow.open(map,marker);});infowindow.open(map,marker);}google.maps.event.addDomListener(window, 'load', init_map);</script>
</div>
</div>
<jsp:include page="footer.jsp" />
</body>
</html>
