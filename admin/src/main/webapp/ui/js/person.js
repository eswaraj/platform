var person;
//Google Maps variables
var map;
var layer;
var kmlPath;
var myMarker;
var set = true;
var urlSuffix;
var c;
var mylocation = {
	'latitude': 28.61,
	'longitude': 77.23
};

$(function(){
	$("#menu").load("../ui/sidebar_menu.html"); 
});

function setNodeId(event) {
	var target = event.target || event.srcElement;
	$("#new_person_id").val($('#'+target.id).attr('pid'));
	$("#new_person_name").val($('#'+target.id).attr('pname'));
	$("#new_person_biodata").val($('#'+target.id).attr('biodata'));
	$("#new_person_dob").val($('#'+target.id).attr('dob'));
	$("#new_person_photo").val($('#'+target.id).attr('photo'));
	$("#new_person_gender").val($('#'+target.id).attr('gender'));
	$("#new_person_email").val($('#'+target.id).attr('email'));
	$("#new_person_ll1").val($('#'+target.id).attr('ll1'));
	$("#new_person_ll2").val($('#'+target.id).attr('ll2'));
	$("#new_person_mobile1").val($('#'+target.id).attr('mobile1'));
	$("#new_person_mobile2").val($('#'+target.id).attr('mobile2'));
	$("#new_person_addressId").val($('#'+target.id).attr('addressId'));
	$("#new_person_line1").val($('#'+target.id).attr('line1'));
	$("#new_person_line2").val($('#'+target.id).attr('line2'));
	$("#new_person_line3").val($('#'+target.id).attr('line3'));
	$("#new_person_postal").val($('#'+target.id).attr('postal'));
	$("#village-list").val($('#'+target.id).attr('villageId'));
	$("#ward-list").val($('#'+target.id).attr('wardId'));
	$("#city-list").val($('#'+target.id).attr('cityId'));
	$("#district-list").val($('#'+target.id).attr('districtId'));
	$("#state-list").val($('#'+target.id).attr('stateId'));
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
		new google.maps.LatLng(-33.8902, 151.1759),
		new google.maps.LatLng(-33.8474, 151.2631)
		);
	map.fitBounds(defaultBounds);
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
	});

	// Bias the SearchBox results towards places that are within the bounds of the
	// current map's viewport.
	google.maps.event.addListener(map, 'bounds_changed', function() {
		var bounds = map.getBounds();
		searchBox.setBounds(bounds);
	});

	//map end
	$("#searchButton").click(function() {		
		var s = $('#person_search').val();
		$.ajax({
			type: "GET",
			url:"/ajax/person/search/name/"+s,
			data: "",
			contentType: "application/json; charset=utf-8",
			dataType: "JSON",
			success: function(data){
				for(var i=0; i<data.length; i++) {
					$('#users tbody').append("<tr>" +
						"<td>" + data[i].name + "</td>" +
						"<td>" + data[i].email + "</td>" +
						"<td>" + data[i].line1 + "</td>" +
						"<td>" + "<a id='update" + i + "' class='btn blue' href='#new-person' data-toggle='modal' onClick='setNodeId(event);'" +"pid='"+data[i].id+"' pname='"+data[i].name+"' biodata='"+data[i].biodata+"' dob='"+data[i].dob+"' gender='"+data[i].gender+"' photo='"+data[i].photo+"' email='"+data[i].email+"' ll1='"+data[i].landlineNumber1+"' ll2='"+data[i].landlineNumber2+"' mobile1='"+data[i].mobileNumber1+"' mobile2='"+data[i].mobileNumber2+"' addressId='"+data[i].personAddress.id+"' line1='"+data[i].personAddress.line1+"' line2='"+data[i].personAddress.line2+"' line3='"+data[i].personAddress.line3+"' value='"+data[i].id+"' postal='"+data[i].personAddress.postalCode+"' villageId='"+data[i].personAddress.villageId+"' wardId='"+data[i].personAddress.wardId+"' cityId='"+data[i].personAddress.cityId+"' districtId='"+data[i].personAddress.districtId+"' stateId='"+data[i].personAddress.stateId+"' countryId='"+data[i].personAddress.countryId+"'"  + '>Update</a>' + "</td>" +
						"</tr>" 
						);
				}
			}
		});
	});

	$("#form-save").click(function() {		
		person.id = $('#new_person_id').val();
		person.name = $('#new_person_name').val();
		person.biodata = $('#new_person_biodata').val();
		person.dob = $('#new_person_dob').val();
		person.photo = $('#new_person_photo').val();
		person.gender = $('#new_person_gender').val();
		person.email = $('#new_person_email').val();
		person.landlineNumber1 = $('#new_person_ll1').val();
		person.landlineNumber2 = $('#new_person_ll2').val();
		person.mobileNumber1 = $('#new_person_mobile1').val();
		person.mobileNumber2 = $('#new_person_mobile2').val();
		person.personAddress.id = $('#new_person_addressId').val();
		person.personAddress.line1 = $('#new_person_line1').val();
		person.personAddress.line2 = $('#new_person_line2').val();
		person.personAddress.line3 = $('#new_person_line3').val();
		person.personAddress.postalCode = $('#new_person_postal').val();
		person.personAddress.villageId = $('#village-list').val();
		person.personAddress.wardId = $('#ward-list').val();
		person.personAddress.cityId = $('#city-list').val();
		person.personAddress.districtId = $('#district-list').val();
		person.personAddress.stateId = $('#state-list').val();
		person.personAddress.countryId = $('#country-list').val();
		alert("Final Step:Pick your location on map and then save the person");
	});

	$("#submit").click(function() {		
		//Call the get api with lat/long from the box. Use it to populate rest of the fields in person variable and then call post api to save person
		alert("Person saved");
	});
});
