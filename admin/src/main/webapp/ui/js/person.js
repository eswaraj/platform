window.person=new Object();
window.personAddress=new Object();
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

$(function(){
	$("#menu_new").load("../ui/menu.html"); 
});

function setNodeId(event) {
	var target = event.target || event.srcElement;
	if($('#'+target.id).attr('pid') !== 'null')
		$("#new_person_id").val($('#'+target.id).attr('pid'));
	if($('#'+target.id).attr('pname') !== 'null')
		$("#new_person_name").val($('#'+target.id).attr('pname'));
	if($('#'+target.id).attr('biodata') !== 'null')
		$("#new_person_biodata").val($('#'+target.id).attr('biodata'));
	if($('#'+target.id).attr('dob') !== 'null')
		$("#new_person_dob").val($('#'+target.id).attr('dob'));
	if($('#'+target.id).attr('photo') !== 'null')
		$("#new_person_photo").val($('#'+target.id).attr('photo'));
	if($('#'+target.id).attr('gender') !== 'null')
		$("#new_person_gender").val($('#'+target.id).attr('gender'));
	if($('#'+target.id).attr('email') !== 'null')
		$("#new_person_email").val($('#'+target.id).attr('email'));
	if($('#'+target.id).attr('ll1') !== 'null')
		$("#new_person_ll1").val($('#'+target.id).attr('ll1'));
	if($('#'+target.id).attr('ll2') !== 'null')
		$("#new_person_ll2").val($('#'+target.id).attr('ll2'));
	if($('#'+target.id).attr('mobile1') !== 'null')
		$("#new_person_mobile1").val($('#'+target.id).attr('mobile1'));
	if($('#'+target.id).attr('mobile2') !== 'null')
		$("#new_person_mobile2").val($('#'+target.id).attr('mobile2'));
	if($('#'+target.id).attr('addressId') !== 'null')
		$("#new_person_addressId").val($('#'+target.id).attr('addressId'));
	if($('#'+target.id).attr('line1') !== 'null')
		$("#new_person_line1").val($('#'+target.id).attr('line1'));
	if($('#'+target.id).attr('line2') !== 'null')
		$("#new_person_line2").val($('#'+target.id).attr('line2'));
	if($('#'+target.id).attr('line3') !== 'null')
		$("#new_person_line3").val($('#'+target.id).attr('line3'));
	if($('#'+target.id).attr('postal') !== 'null')
		$("#new_person_postal").val($('#'+target.id).attr('postal'));
	if($('#'+target.id).attr('villageId') !== 'null')
		$("#village-list").val($('#'+target.id).attr('villageId'));
	if($('#'+target.id).attr('wardId') !== 'null')
		$("#ward-list").val($('#'+target.id).attr('wardId'));
	if($('#'+target.id).attr('cityId') !== 'null')
		$("#city-list").val($('#'+target.id).attr('cityId'));
	if($('#'+target.id).attr('districtId') !== 'null')
		$("#district-list").val($('#'+target.id).attr('districtId'));
	if($('#'+target.id).attr('stateId') !== 'null')
		$("#state-list").val($('#'+target.id).attr('stateId'));
	if($('#'+target.id).attr('countryId') !== 'null')
		$("#country-list").val($('#'+target.id).attr('countryId'));
}

$ = $.noConflict();
$(document).ready(function(){
	var myLatlng = new google.maps.LatLng( mylocation.latitude, mylocation.longitude );
	var mapOptions = {
		//	zoom: 5,
		//center: myLatlng,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	}
	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
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

	//map end
	$("#person_search").autocomplete({		
		source: "/ajax/person/search/name/",
		minLength: 3,
		response: function( event, ui ) {
			var d;
			data = ui.content;
			$('#users tbody').html("");
			for(var i=0; i<data.length; i++) {
				if(data[i].personAddress !=null) {
					d = "<td>" + "<a id='update" + i + "' class='btn blue' href='#new-person' data-toggle='modal' onClick='setNodeId(event);'" +"pid='"+data[i].id+"' pname='"+data[i].name+"' biodata='"+data[i].biodata+"' dob='"+data[i].dob+"' gender='"+data[i].gender+"' photo='"+data[i].profilePhoto+"' email='"+data[i].email+"' ll1='"+data[i].landlineNumber1+"' ll2='"+data[i].landlineNumber2+"' mobile1='"+data[i].mobileNumber1+"' mobile2='"+data[i].mobileNumber2+"' addressId='"+data[i].personAddress.id+"' line1='"+data[i].personAddress.line1+"' line2='"+data[i].personAddress.line2+"' line3='"+data[i].personAddress.line3+"' value='"+data[i].id+"' postal='"+data[i].personAddress.postalCode+"' villageId='"+data[i].personAddress.villageId+"' wardId='"+data[i].personAddress.wardId+"' cityId='"+data[i].personAddress.cityId+"' districtId='"+data[i].personAddress.districtId+"' stateId='"+data[i].personAddress.stateId+"' countryId='"+data[i].personAddress.countryId+"'"  + '>Edit</a>' + "</td>" ;
				}
				else {
					d = "<td>" + "<a id='update" + i + "' class='btn blue' href='#new-person' data-toggle='modal' onClick='setNodeId(event);'" +"pid='"+data[i].id+"' pname='"+data[i].name+"' biodata='"+data[i].biodata+"' dob='"+data[i].dob+"' gender='"+data[i].gender+"' photo='"+data[i].profilePhoto+"' email='"+data[i].email+"' ll1='"+data[i].landlineNumber1+"' ll2='"+data[i].landlineNumber2+"' mobile1='"+data[i].mobileNumber1+"' mobile2='"+data[i].mobileNumber2+"' addressId='' line1='' line2='' line3='' value='' postal='' villageId='' wardId='' cityId='' districtId='' stateId='' countryId=''"  + '>Edit</a>' + "</td>" ;
				}
				$('#users tbody').append("<tr>" +
					"<td>" + data[i].name + "</td>" +
					"<td>" + data[i].email + "</td>" +
					"<td>" + data[i].line1 + "</td>" +
					d +
					"</tr>" 
					);
			}
		}
	});
	$("#searchButton").click(function() {		
		var d;
		var s = $('#person_search').val();
		$.ajax({
			type: "GET",
			url:"/ajax/person/search/name/"+s,
			data: "",
			contentType: "application/json; charset=utf-8",
			dataType: "JSON",
			success: function(data){
				$('#users tbody').html("");
				for(var i=0; i<data.length; i++) {
					if(data[i].personAddress !=null) {
						d = "<td>" + "<a id='update" + i + "' class='btn blue' href='#new-person' data-toggle='modal' onClick='setNodeId(event);'" +"pid='"+data[i].id+"' pname='"+data[i].name+"' biodata='"+data[i].biodata+"' dob='"+data[i].dob+"' gender='"+data[i].gender+"' photo='"+data[i].profilePhoto+"' email='"+data[i].email+"' ll1='"+data[i].landlineNumber1+"' ll2='"+data[i].landlineNumber2+"' mobile1='"+data[i].mobileNumber1+"' mobile2='"+data[i].mobileNumber2+"' addressId='"+data[i].personAddress.id+"' line1='"+data[i].personAddress.line1+"' line2='"+data[i].personAddress.line2+"' line3='"+data[i].personAddress.line3+"' value='"+data[i].id+"' postal='"+data[i].personAddress.postalCode+"' villageId='"+data[i].personAddress.villageId+"' wardId='"+data[i].personAddress.wardId+"' cityId='"+data[i].personAddress.cityId+"' districtId='"+data[i].personAddress.districtId+"' stateId='"+data[i].personAddress.stateId+"' countryId='"+data[i].personAddress.countryId+"'"  + '>Edit</a>' + "</td>" ;
					}
					else {
						d = "<td>" + "<a id='update" + i + "' class='btn blue' href='#new-person' data-toggle='modal' onClick='setNodeId(event);'" +"pid='"+data[i].id+"' pname='"+data[i].name+"' biodata='"+data[i].biodata+"' dob='"+data[i].dob+"' gender='"+data[i].gender+"' photo='"+data[i].profilePhoto+"' email='"+data[i].email+"' ll1='"+data[i].landlineNumber1+"' ll2='"+data[i].landlineNumber2+"' mobile1='"+data[i].mobileNumber1+"' mobile2='"+data[i].mobileNumber2+"' addressId='' line1='' line2='' line3='' value='' postal='' villageId='' wardId='' cityId='' districtId='' stateId='' countryId=''"  + '>Edit</a>' + "</td>" ;
					}
		$('#users tbody').append("<tr>" +
			"<td>" + data[i].name + "</td>" +
			"<td>" + data[i].email + "</td>" +
			"<td>" + data[i].line1 + "</td>" +
			d +
			"</tr>" 
			);
				}
			}
		});
	});

	$("#form-save").click(function() {		
		window.person.id = $('#new_person_id').val();
		window.person.name = $('#new_person_name').val();
		window.person.biodata = $('#new_person_biodata').val();
		window.person.dob = $('#new_person_dob').val();
		window.person.profilePhoto = $('#new_person_photo').val();
		window.person.gender = $('#new_person_gender').val();
		if($('#new_person_email').val() !== "")
		window.person.email = $('#new_person_email').val();
	window.person.landlineNumber1 = $('#new_person_ll1').val();
	window.person.landlineNumber2 = $('#new_person_ll2').val();
	window.person.mobileNumber1 = $('#new_person_mobile1').val();
	window.person.mobileNumber2 = $('#new_person_mobile2').val();
	window.personAddress.id = $('#new_person_addressId').val();
	window.personAddress.line1 = $('#new_person_line1').val();
	window.personAddress.line2 = $('#new_person_line2').val();
	window.personAddress.line3 = $('#new_person_line3').val();
	window.personAddress.postalCode = $('#new_person_postal').val();
	window.personAddress.villageId = $('#village-list').val();
	window.personAddress.wardId = $('#ward-list').val();
	window.personAddress.cityId = $('#city-list').val();
	window.personAddress.districtId = $('#district-list').val();
	window.personAddress.stateId = $('#state-list').val();
	window.personAddress.countryId = $('#country-list').val();
	window.person.personAddress = window.personAddress;
	alert("Final Step:Pick your location on map and then save the person");
	});

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
		$.ajax({
			type: "POST",
			url:"/ajax/person/save",
			data: JSON.stringify(window.person),
			contentType: "application/json; charset=utf-8",
			dataType: "JSON",
			success: function(data){
				if(data.message){
					alert("Error: Check POST response");
				}
				else{
					alert("Person Added"+data.name);
					$( "#searchButton" ).trigger( "click" );
				}
			}
		});
	});
});

