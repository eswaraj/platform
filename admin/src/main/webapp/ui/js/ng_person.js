var personApp = angular.module('personApp', ['customDirectives']);

personApp.controller('personController', function ($scope, $http, $timeout) {
    "use strict";
    //window.scope = $scope;
    $scope.acData = {};
    $scope.searchText = "";
    $scope.person = {
        'id' : "",
        'name' : "",
        'biodata' : "",
        'dob' : "",
        'profilePhoto' : "",
        'gender' : "",
        'email' : "",
        'landlineNumber1' : "",
        'landlineNumber2' : "",
        'mobileNumber1' : "",
        'mobileNumber2' : "",
        'personAddress' : {
            'id' : "",
            'line1' : "",
            'line2' : "",
            'line3' : "",
            'postalCode' : "",
            'villageId' : "",
            'wardId' : "",
            'cityId' : "",
            'districtId' : "",
            'stateId' : "",
            'countryId' : ""
        }
    };
    $scope.lat = "";
    $scope.lng = "";
    
    //Google Maps
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
    $("#menu_new").load("../ui/ng_menu.html");
    
    $ = $.noConflict();
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
        $scope.lat = evt.latLng.lat();
        $scope.lng = evt.latLng.lng();
        $scope.$apply();
    });
    google.maps.event.addListener(map, "center_changed", function() {
        c = map.getCenter();
        myMarker.setPosition(c);
        $scope.lat = c.lat();
        $scope.lng = c.lng();
        $scope.$apply();
    });
    google.maps.event.addListener(map, "zoom_changed", function() {
        c = map.getCenter();
        myMarker.setPosition(c);
        $scope.lat = c.lat();
        $scope.lng = c.lng();
        $scope.$apply();
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

    
    //Form handling
    $scope.selectPerson = function (event) {
        var model = $scope.acData[event.target.attributes['input-id'].value + 'Data'];
        $scope.person = $.extend(true,$scope.person,model[event.target.attributes.index.value]);
        $scope.selectedPerson = model[event.target.attributes.index.value];
        //for(var k in model[event.target.attributes.index.value]) $scope.person.k = model[event.target.attributes.index.value].k;
    };
    
    $scope.savePerson = function () {
        $http({
            method: 'POST',
            url: "/ajax/person/save",
            data: angular.toJson($scope.person),
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        }).success(function (data) {
            if(data.message){
                alert("Error: Check POST response");
            }
            else{
                alert("Person Added"+data.name);
                //var oldText = $scope.searchText;
                //$scope.searchText = "";
                //$timeout(function() {
                    //$scope.searchText = oldText;
                //},500);
                //$("person_search").autocomplete('search', $scope.searchText);
                $scope.selectedPerson = $.extend(true,$scope.selectedPerson,data);
            }
        }).error(function () {
            alert("Request failed.");
        });
    };
    
    $scope.pickLocation = function () {
        //Call the get api with lat/long from the box. Use it to populate rest of the fields in person variable and then call post api to save person
        //Show reverse geocoded value
        var latlng = new google.maps.LatLng($scope.lat, $scope.lng);
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
    };
});
