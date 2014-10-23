var locationNodeApp = angular.module('locationNodeApp', ['customDirectives']);

locationNodeApp.controller('locationNodeController', function($scope, $http) {
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
    var root_node,new_node,sel;
    var hash = {};
    var loc_hash = {};

    $scope.node = {};
    $scope.newNode = {};
    $scope.selectedNode = {};
    var addLocation = function (node)
    {
        loc_hash[node.id] = node.name;
        if(node.children) {
            for(var i=0; i<node.children.length; i++) {
                addLocation(node.children[i]);
            }   
        }   
    };
    $scope.update_map = function (kml_path) {
        if (typeof layer !== 'undefined') {
            layer.setMap(null);
        }
        layer = new google.maps.KmlLayer(kml_path + '?' + urlSuffix );
        layer.setMap(map);
    };
    $scope.addChildNode = function() {
        postService.run($scope, '/ajax/location/save', $scope.newNode, true, 'addChild');
    };
    $scope.updateNode = function() {
        postService.run($scope, '/ajax/location/save', $scope.node, false, 'updateNode');
    };
    $scope.setLocationTypeId = function(button) {
        $scope.newNode.locationTypeId = button.id;
    };
    $scope.setCurrent = function(filepath) {
        console.log("Set file "+filepath+" as current by API call");
    };
    $scope.showTypeWithLocation = function (obj) {
        if (locationsAdded) {
            return obj.name + " Type: " + loc_hash[obj.locationTypeId];
        }
        else {
            return obj.name + " Type: Country";
        }
    };
    $scope.$watch('selectedNode', function() {
        $scope.newNode.parentLocationId = $scope.selectedNode.id;
        $.extend(true, $scope.node, $scope.selectedNode);
        if ($scope.node.boundaryFile == null) {
            $scope.kmlStatus = "KML does not exist";
        }
        else {
            $scope.kmlStatus = "KML exists";
        }
        $scope.update_map($scope.node.boundaryFile);

        var getChildRequest = $http({
            method: "GET",
            url: "/ajax/locationtype/getchild/" + $scope.node.locationTypeId,
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });

        getChildRequest.success(function(data){
            $scope.buttonData = data;
        });

        getChildRequest.error(function() {
            alert('Request Failed.');
        });

        var getkmlRequest = $http({
            method: "GET",
            url: "/ajax/location/" + $scope.node.id + "/kmlfiles",
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });

        getkmlRequest.success(function(data){
            $scope.kmlList = data;
        });

        getkmlRequest.error(function() {
            alert('Request Failed.');
        });
    });

    $("#menu_new").load("../ui/ng_menu.html");

    //Create map
    var myLatlng = new google.maps.LatLng( mylocation.latitude, mylocation.longitude );
    var mapOptions = {
        zoom: 5,
        center: myLatlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
    c = map.getCenter();
    myMarker = new google.maps.Marker({
        position: c,
        draggable: true
    });
    myMarker.setMap(map);
    urlSuffix = (new Date).getTime().toString(); //Will be used for KML layer

    google.maps.event.addListener(myMarker, 'dragend', function(evt){
        $scope.node.latitude = evt.latLng.lat();
        $scope.node.longitude = evt.latLng.lng();
        $scope.$apply();
    });
    google.maps.event.addListener(map, "center_changed", function() {
        c = map.getCenter();
        myMarker.setPosition(c);
        $scope.node.latitude = c.lat();
        $scope.node.longitude = c.lng();
        $scope.$apply();
    });
    //map end
    
    var locationsAdded = false;
    
    var request = $http({
        mrthod: "GET",
        url:"/ajax/locationtype/get",
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });

    request.success(function(data) {
        addLocation(data);
        locationsAdded = true;
    });

    request.error(function() {
        alert("Request failed.");
    });

    $('#form1').submit(function(event){
        event.preventDefault();
        var formData = new FormData(this);
        var  kml_url = '/ajax/location/' + $scope.selectedNode.id + '/upload';

        $.ajax({
            url: kml_url,
            type: 'POST',
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                var child = {};
                $scope.selectedNode.boundaryFile = data;
                $.extend(true, child, $scope.selectedNode);
                $scope.$broadcast(updateNode, {id:"js_tree", child:child});
                $scope.kmlStatus = "KML exists";
                update_map(data);
                $scope.$apply();
            }
        });

        return false;
    });
}); //controller ends

locationNodeApp.factory('postService', function ($http) {
    return {
        run : function(scope, url, obj, clearObj, event) {
            $http({
                method: 'POST',
                url: url,
                data: angular.toJson(obj),
                headers: {'Content-Type': 'application/json; charset=utf-8'}
            }).success(function (data) {
                if(data.message){
                    alert("Error: Check POST response");
                }
                else{
                    alert("Category Added"+data.name);
                    if(clearObj) {
                        obj = {};
                    }
                    scope.$broadcast(event,{id:"js_tree",child:data});
                }
            }).error(function () {
                alert("Request failed.");
            });
        }
    };
});
