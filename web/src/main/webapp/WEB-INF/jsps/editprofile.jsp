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
	<style>
		#gmap_canvas {
		margin-left: 0px;
		margin-right: auto;
		padding: 0;
		width: 600px;
		height: 400px;
		}
		.controls {
		margin-top: 16px;
		border: 1px solid transparent;
		border-radius: 2px 0 0 2px;
		box-sizing: border-box;
		-moz-box-sizing: border-box;
		height: 32px;
		outline: none;
		box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
		}

		#pac-input {
		background-color: #fff;
		padding: 0 11px 0 13px;
		width: 400px;
		font-family: Roboto;
		font-size: 15px;
		font-weight: 300;
		text-overflow: ellipsis;
		}

		#pac-input:focus {
		border-color: #4d90fe;
		margin-left: -1px;
		padding-left: 14px;  /* Regular padding-left + 1. */
		width: 401px;
		}

		.pac-container {
		font-family: Roboto;
		}
	</style>
</head>
<body>
	<div class="outerwrapper">
		<jsp:include page="header.jsp" />

		<div class="right-pane fixed">
			<h2>My Profile</h2>
			<div class="profile">
				<div class="innerblock">
					<img src="${user.person.profilePhoto}?type=square&width=300&height=300" alt="profile-pic" class="profile-pic">
					<p class="read"> <strong>${user.person.name}</strong>
					<br>
					<span>
						<c:if test="${!empty age}">
						${age} Yrs,
						</c:if>
						${user.person.gender}</span>
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
				<b style="color: #FF9933">Latitude : <b/><input type="text" name="node_lat" id="node_lat" value="" readonly>
						<b style="color: #FF9933">Longitude : </b><input type="text" name="node_long" id="node_long" value="" readonly>
						<p name="rev_geo" id="rev_geo"></p>
						<br><button type="button" id="pick" class="btn btn-primary blue btn_round">Pick location</button>
						<button type="button" id="submit" class="btn btn-primary blue btn_round">Save Person</button>
					</div>
				</div>
				<!-- /.right-pane -->
				<div class="locate-user">
					<script type="text/javascript" src="http://maps.google.com/maps/api/js?v=3.exp&libraries=places"></script>
					<div style="overflow:hidden;height:100%;width:100%;">
						<input id="pac-input" class="controls" type="text" placeholder="Search Box">
						<div id="gmap_canvas" style="height:100%;width:100%;"></div>
						<style>#gmap_canvas img{max-width:none!important;background:none!important}</style>
						<a class="google-map-code" href="http://www.mapsembed.com/conrad-gutschein/" id="get-map-data">http://www.mapsembed.com/conrad-gutschein/</a>
					</div>
					<script>
						//Google Maps variables
						var map;
						var layer;
						var kmlPath;
						var myMarker;
						var set = true;
						var urlSuffix;
						var c;
						var geocoder;
						var mylocation = {
							'latitude': 28.61,
							'longitude': 77.23
						};

						$ = $.noConflict();
						$(document).ready(function(){
							var myLatlng = new google.maps.LatLng( mylocation.latitude, mylocation.longitude );
							var mapOptions = {
								//	zoom: 5,
								//center: myLatlng,
								mapTypeId: google.maps.MapTypeId.ROADMAP
							}
							map = new google.maps.Map(document.getElementById('gmap_canvas'), mapOptions);
							var defaultBounds = new google.maps.LatLngBounds(
							new google.maps.LatLng(37.19705959279532, 64.02147375000004),
							new google.maps.LatLng(5.7668215619781575, 99.66112218750004)
							);
							map.fitBounds(defaultBounds);

							if(navigator.geolocation) {
								navigator.geolocation.getCurrentPosition(function(position) {
									var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
									map.setCenter(pos);
									map.setZoom(14);
									}, function() {
									//User didnt give permission to use location
								});
								} else {
								// Browser doesn't support Geolocation
							}

							myMarker = new google.maps.Marker({
								position: myLatlng,
								draggable: true
							});
							myMarker.setMap(map);
							urlSuffix = (new Date).getTime().toString(); //Will be used for KML layer

							google.maps.event.addListener(myMarker, 'dragend', function(evt){
								$('#node_lat').val(evt.latLng.lat());
								$('#node_long').val(evt.latLng.lng());
							});
							google.maps.event.addListener(map, "center_changed", function() {
								c = map.getCenter();
								myMarker.setPosition(c);
								$('#node_lat').val(c.lat());
								$('#node_long').val(c.lng());
							});
							google.maps.event.addListener(map, "zoom_changed", function() {
								c = map.getCenter();
								myMarker.setPosition(c);
								$('#node_lat').val(c.lat());
								$('#node_long').val(c.lng());
							});
							//map new
							var markers = [];
							// Create the search box and link it to the UI element.
							var input = /** @type {HTMLInputElement} */(
							document.getElementById('pac-input'));
							map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

							var searchBox = new google.maps.places.SearchBox(
							/** @type {HTMLInputElement} */(input));

							// Listen for the event fired when the user selects an item from the
							// pick list. Retrieve the matching places for that item.
							google.maps.event.addListener(searchBox, 'places_changed', function() {
								var places = searchBox.getPlaces();

								if (places.length == 0) {
									return;
								}
								for (var i = 0, marker; marker = markers[i]; i++) {
									marker.setMap(null);
								}

								// For each place, get the icon, place name, and location.
								markers = [];
								var bounds = new google.maps.LatLngBounds();
								for (var i = 0, place; place = places[i]; i++) {
									var image = {
										url: place.icon,
										size: new google.maps.Size(71, 71),
										origin: new google.maps.Point(0, 0),
										anchor: new google.maps.Point(17, 34),
										scaledSize: new google.maps.Size(25, 25)
									};

									// Create a marker for each place.
									var marker = new google.maps.Marker({
										map: map,
										icon: image,
										title: place.name,
										position: place.geometry.location
									});

									markers.push(marker);

									bounds.extend(place.geometry.location);
								}

								map.fitBounds(bounds);
								map.setZoom(17);
							});

							// Bias the SearchBox results towards places that are within the bounds of the
							// current map's viewport.
							google.maps.event.addListener(map, 'bounds_changed', function() {
								var bounds = map.getBounds();
								searchBox.setBounds(bounds);
							});

							//$('#new-person').on('shown', function () {
								//	    google.maps.event.trigger(map, "resize");
								//});
							geocoder = new google.maps.Geocoder();

							$("#pick").click(function() {		
								//Call the get api with lat/long from the box. Use it to populate rest of the fields in person variable and then call post api to save person
								//Show reverse geocoded value
								var latlng = new google.maps.LatLng($('#node_lat').val(), $('#node_long').val());
								geocoder.geocode({'latLng': latlng}, function(results, status) {
									if (status == google.maps.GeocoderStatus.OK) {
										if (results[1]) {
											$('#rev_geo').html(results[1].formatted_address);
											} else {
											alert('No results found');
										}
										} else {
										alert('Geocoder failed due to: ' + status);
									}
								});
								//end
							});

							$("#submit").click(function() {		
								//$.ajax({
									//	type: "POST",
									//	url:"/ajax/person/save",
									//	data: JSON.stringify(window.person),
									//	contentType: "application/json; charset=utf-8",
									//	dataType: "JSON",
									//	success: function(data){
										//		if(data.message){
											//			alert("Error: Check POST response");
											//		}
										//		else{
											//			alert("Person Added"+data.name);
											//			$( "#searchButton" ).trigger( "click" );
											//		}
										//	}
									//});
							});
						});

						//map end

					</script>
				</div>
			</div>
			<jsp:include page="footer.jsp" />
		</body>
	</html>
