<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">
<head>
<jsp:include page="include.jsp" />
<link rel="stylesheet" href="${staticHost}/css/dashboard.css">
<script type="text/javascript" src="${staticHost}/js/slide_notification.js"></script>
<!-- Script -->
<link rel="stylesheet" href="${staticHost}/css/editprofile.css">
</head>
<body>
	<div class="">
		<jsp:include page="header.jsp" />

		<div class="right-pane fixed" id="profile_show">
			<h3>My Profile</h3>
			<div class="profile">
				<div class="innerblock">
					<img
						src="${user.person.profilePhoto}?type=square&width=300&height=300"
						alt="profile-pic" class="profile-pic">
					<p class="read center-align">
						<strong>${user.person.name}</strong> <br> <span> <c:if
								test="${!empty age}">
						${age} Yrs,
						</c:if> ${user.person.gender}
						</span>
					</p>
				</div>
				<div class="innerblock">
					<p>${user.id}</p>
					<p>${user.externalId}</p>
					<p class="political_details">Political Details :</p>
					<p class="read">
						Ward : <strong id="ward_details">${user.person.personAddress.ward.name}</strong>
					</p>
					<p class="read">
						AC : <strong>${user.person.personAddress.ac.name}</strong>
					</p>
					<p class="read">
						PC : <strong>${user.person.personAddress.pc.name}</strong>
					</p>
					<p class="read">
						Voter ID Number : <strong>${user.person.voterId}</strong>
					</p>
					<button type="button" id="edit_btn"
						class="btn btn-primary blue btn_round">Edit Profile</button>
				</div>
			</div>
			</div>
			<div class="right-pane fixed" id="profile_edit">
				<h3>Edit Profile</h3>
				<div class="profile">
					<form:form id="profile_form" commandName="profile" method="post"
						action="/editprofile.html">
						<div class="innerblock">
							Name:
							<form:input class="form-control" path="name" placeholder="Name" />
							Voter ID:
							<form:input class="form-control" path="voterId"
								placeholder="Voter Card No" />
							<hr>
							Latitude :
							<form:input class="form-control" path="lattitude" name="node_lat"
								id="node_lat" placeholder="Latitude" readonly="true"
								disabled="disabled" />

							Longitude :
							<form:input class="form-control" path="longitude"
								name="node_long" id="node_long" placeholder="Lattitude"
								readonly="true" disabled="disabled" />
							<p name="rev_geo" id="rev_geo"></p>

							<input type="submit"
								class="form-control btn btn-primary blue btn_round"
								value="Save Profile"> <input type="button"
								class="form-control btn btn-primary blue btn_round"
								value="Cancel" id="cancel_btn">


						</div>

					</form:form>
				</div>
			</div>
			<!-- /.right-pane -->
			<div class="locate-user">
				<script type="text/javascript"
					src="http://maps.google.com/maps/api/js?v=3.exp&libraries=places"></script>
				<div style="overflow: hidden; height: 100%; width: 100%;">
					<input id="pac-input" class="controls" type="text" placeholder="Search Box">
					<div id="gmap_canvas" style="height: 100%; width: 100%;"></div>

					<a class="google-map-code"
						href="http://www.mapsembed.com/conrad-gutschein/"
						id="get-map-data">http://www.mapsembed.com/conrad-gutschein/</a>
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
						'latitude' : 28.61,
						'longitude' : 77.23
					};

					$ = $.noConflict(); $(document) .ready( function() {
						$("#profile_edit").hide();

						var myLatlng = new google.maps.LatLng(
						mylocation.latitude,
						mylocation.longitude);
						var mapOptions = {
							//	zoom: 5,
							//center: myLatlng,
							mapTypeId : google.maps.MapTypeId.ROADMAP
						}
						map = new google.maps.Map(document .getElementById('gmap_canvas'), mapOptions);
						//var defaultBounds = new google.maps.LatLngBounds( new google.maps.LatLng( 37.19705959279532, 64.02147375000004), new google.maps.LatLng( 5.7668215619781575, 99.66112218750004));
						//map.fitBounds(defaultBounds);

						<c:if test="${!empty user.person.personAddress.lattitude}">
						myLatlng = new google.maps.LatLng( ${user.person.personAddress.lattitude}, ${user.person.personAddress.longitude} );
						map.setCenter(myLatlng);
						map.setZoom(14);
						</c:if>

						myMarker = new google.maps.Marker({
							position : myLatlng,
							draggable : true
						});
						myMarker.setMap(map);
						urlSuffix = (new Date).getTime().toString(); //Will be used for KML layer

						//map new
						var markers = [];
						// Create the search box and link it to the UI element.
						var input = /** @type {HTMLInputElement} */
						(document.getElementById('pac-input'));
						map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

						var searchBox = new google.maps.places.SearchBox(
						/** @type {HTMLInputElement} */
						(input));

						// Listen for the event fired when the user selects an item from the
						// pick list. Retrieve the matching places for that item.
						google.maps.event.addListener( searchBox, 'places_changed', function() {
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
									url : place.icon,
									size : new google.maps.Size( 71, 71),
									origin : new google.maps.Point( 0, 0),
									anchor : new google.maps.Point( 17, 34),
									scaledSize : new google.maps.Size( 25, 25)
								};

								// Create a marker for each place.
								var marker = new google.maps.Marker(
								{
									map : map,
									icon : image,
									title : place.name,
									position : place.geometry.location
								});

								markers.push(marker);

								bounds.extend(place.geometry.location);
							}

							map.fitBounds(bounds);
							map.setZoom(17);
						});

						// Bias the SearchBox results towards places that are within the bounds of the
						// current map's viewport.
						google.maps.event.addListener(map,
						'bounds_changed', function() {
							var bounds = map.getBounds();
							searchBox.setBounds(bounds);
						});

						geocoder = new google.maps.Geocoder();

						$("#edit_btn") .click( function() {
							google.maps.event.addListener( myMarker, 'dragend', function( evt) {
								$('#node_lat').val(evt.latLng.lat());
								$('#node_long').val(evt.latLng.lng());
								showGeoLocation();
							});
							google.maps.event.addListener( map, "center_changed", function() {
								c = map.getCenter();
								myMarker.setPosition(c);
								$('#node_lat').val(c.lat());
								$('#node_long').val(c.lng());
								showGeoLocation();
							});
							google.maps.event.addListener( map, "zoom_changed", function() {
								c = map.getCenter();
								myMarker.setPosition(c);
								$('#node_lat').val(c.lat());
								$('#node_long').val(c.lng());
								showGeoLocation();
							});

							//Change the right panel to display a form
							$("#profile_show").hide();
							$("#profile_edit").show();

							$("#cancel_btn").click( function() {
								//window.location = "http://dev.eswaraj.com/editprofile.html";
								$("#profile_edit").hide();
								$("#profile_show").show();
								<c:if test="${!empty user.person.personAddress.lattitude}">
								var pos = new google.maps.LatLng( ${user.person.personAddress.lattitude}, ${user.person.personAddress.longitude});
								map.setCenter(pos);
								map.setZoom(14);
								</c:if>
							});

							<c:if test="${empty user.person.personAddress.lattitude}">
							if (navigator.geolocation) {
								navigator.geolocation.getCurrentPosition( function( position) {
									var pos = new google.maps.LatLng( position.coords.latitude, position.coords.longitude);
									map.setCenter(pos);
									map.setZoom(14);
									showGeoLocation();
								},
								function() {
									//User didnt give permission to use location
								});
							} else {
								// Browser doesn't support Geolocation
							}
							</c:if>
							<c:if test="${!empty user.person.personAddress.lattitude}">
							var pos = new google.maps.LatLng( ${user.person.personAddress.lattitude}, ${user.person.personAddress.longitude});
							map.setCenter(pos);
							map.setZoom(14);
							</c:if>
						}); //edit button click
					}); //document ready

					function showGeoLocation() {
						if (navigator.geolocation) {
							//Show reverse geocoded value
							var latlng = new google.maps.LatLng($('#node_lat').val(), $('#node_long').val());
							geocoder.geocode({
								'latLng' : latlng
								}, function(results, status) {
								if (status == google.maps.GeocoderStatus.OK) {
									if (results[1]) {
										$('#rev_geo').html(
										results[1].formatted_address);
										} else {
										alert('No results found');
									}
									} else {
									//alert('Geocoder failed due to: ' + status);
								}
							});
							//end
						}
					}
				</script>
				<div id="top_slide_notification"></div>
			</div>
		</div>
	
				<jsp:include page="footer.jsp" />
</body>
</html>
