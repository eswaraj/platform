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
		#gmap_canvas img{
		max-width:none!important;
		background:none!important
		}
	</style>
</head>
<body>
	<div class="outerwrapper">
		<jsp:include page="header.jsp" />

		<div class="right-pane fixed" id="profile_show">
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
					<c:if test="${!empty user.person.ward}">
					<p class="read"> <strong>${user.person.ward}</strong>
					</p>
					, 
					</c:if>
					<c:if test="${!empty user.person.ac}">
					<p class="read"> <strong>${user.person.ac}</strong>
					</p>
					<br>
					</c:if>
					<c:if test="${!empty user.person.pc}">
					<p class="read"> <strong>${user.person.pc}</strong>
					</p>
					</c:if>
					<c:if test="${!empty user.person.voterid}">
					<p class="read"> <strong>Voter ID: ${user.person.voterid}</strong>
					</p>
					</c:if>
					<button type="button" id="edit_btn" class="btn btn-primary blue btn_round">Edit Profile</button>
				</div>
			</div>
		</div>
				<!-- /.right-pane -->
				<div class="locate-user">
					<script type="text/javascript" src="http://maps.google.com/maps/api/js?v=3.exp&libraries=places"></script>
					<div style="overflow:hidden;height:100%;width:100%;">
						<input id="pac-input" class="controls" type="text" placeholder="Search Box">
						<div id="gmap_canvas" style="height:100%;width:100%;"></div>
						
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

							<c:if test="${!empty user.person.pc}">
								myLatlng = new google.maps.LatLng( ${user.person.latitude}, ${user.person.longitude} );
								map.setCenter(myLatlng);
								map.setZoom(14);
							</c:if>

							myMarker = new google.maps.Marker({
								position: myLatlng,
								draggable: true
							});
							myMarker.setMap(map);
							urlSuffix = (new Date).getTime().toString(); //Will be used for KML layer

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

							geocoder = new google.maps.Geocoder();

							$("#edit_btn").click(function() {		
								google.maps.event.addListener(myMarker, 'dragend', function(evt){
									$('#node_lat').val(evt.latLng.lat());
									$('#node_long').val(evt.latLng.lng());
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

								//Change the right panel to display a form
								$("#profile_show").replaceWith(function() {		
									return '
									<div class="right-pane fixed" id="profile_edit">
										<h2>My Profile</h2>
										<div class="profile">
											<div class="innerblock">
												<form id="profile_form" class="" action="">
													<input type="text" class="form-control" placeholder="Name" value="${user.person.name}">
													<input type="text" class="form-control" placeholder="Voter Card No" value="${user.person.voterid}">
													<input type="submit" class="form-control" value="Save Profile">
												</form>
											</div>
											<hr>
											<div class="innerblock">
												<b>Latitude : <b/><input type="text" name="node_lat" id="node_lat" value="" readonly>
												<b>Longitude : </b><input type="text" name="node_long" id="node_long" value="" readonly>
												<p name="rev_geo" id="rev_geo"></p>
											</div>
										</div>
									</div>
									';
								});
							});
						});
					</script>
				</div>
			</div>
			<jsp:include page="footer.jsp" />
		</body>
	</html>
