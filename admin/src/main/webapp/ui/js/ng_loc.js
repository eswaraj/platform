var locationNodeApp = angular.module('locationNodeApp', []);

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

    $scope.node = {
        'id': "",
        'name' : "",
        'locationTypeId' : "",
        'parentLocationId' : "",
        'latitude' : "",
        'longitude' : "",
        'urlIdentifier' : "",
        'boundaryFile' : ""
    };
    $scope.newNode = {
        'id': "",
        'name' : "",
        'locationTypeId' : "",
        'parentLocationId' : "",
        'latitude' : "",
        'longitude' : "",
        'urlIdentifier' : "",
        'boundaryFile' : ""
    };
    var addLocation = function (node)
    {
        loc_hash[node.id] = node.name;
        //console.log(node.name);
        if(node.children)
        {   
            //console.log("Error:"+node.name);
            for(var i=0; i<node.children.length; i++)
            {   
                addLocation(node.children[i]);
            }   
        }   
    };
    $scope.update_map = function (kml_path) {
        if (typeof layer !== 'undefined')
        {
            layer.setMap(null);
        }
        layer = new google.maps.KmlLayer(kml_path + '?' + urlSuffix );
        layer.setMap(map);
    };
    $scope.addChildNode = function() {
        var selected_node =  $('#js_tree').jstree('get_selected');
        var locationRequest = $http({
            method: "POST",
            url:"/ajax/location/save",
            data: angular.toJson($scope.newNode),
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });
        locationRequest.success(function(data){
            var new_node = {
                'text':data.name+" Type :"+data.locationTypeId,
                'id':data.id,
                'li_attr':{
                    'id' : data.id,
                    'name' : data.name,
                    'locationTypeId' : data.locationTypeId,
                    'parentLocationId' : data.parentLocationId,
                    'latitude' : data.latitude,
                    'longitude' : data.longitude,
                    'boundaryFile' : data.boundaryFile,
                    'urlIdentifier' : data.urlIdentifier
                }
            };
            var error = data.message.length > 0;
            if(error) {
                alert("Error in node creation: " + data.message);
            }
            else {
                var sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);
            }
        }
                               );
        locationRequest.error(function() {
            alert("Request failed");
        });
    };
    $scope.updateNode = function() {
        var saveRequest = $http({
            method: "POST",
            url:"/ajax/location/save",
            data: angular.toJson($scope.node),
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });
        saveRequest.success(function(data){
            $('#js_tree').jstree('set_text',data.id, data.name);
            $('#'+data.id).attr('id',data.id);
            $('#'+data.id).attr('name',data.name);
            $('#'+data.id).attr('latitude',data.latitude);
            $('#'+data.id).attr('longitude',data.longitude);
            $('#'+data.id).attr('locationTypeId',data.locationTypeId);
            $('#'+data.id).attr('parentLocationId',data.parentLocationId);
            $('#'+data.id).attr('urlIdentifier',data.urlIdentifier);
            $('#'+data.id).attr('boundaryFile',data.boundaryFile);
            var error = data.message.length > 0;
            if(error) {
                alert("Error in node creation: " + data.message);
            }
        });
        saveRequest.error(function() {
            alert('Request failed');
        });
    };
    $scope.setLocationTypeId = function(event) {
        $scope.newNode.parentLocationId = $scope.buttonData[event.target.attributes['index'].value].id;
        $scope.$apply();
    };
    $scope.setCurrent = function(filepath) {
        console.log("Set file "+filepath+" as current by API call");
    };

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

    var request = $http({
        mrthod: "GET",
        url:"/ajax/locationtype/get",
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });

    request.success(function(data) {
        addLocation(data);
    });

    request.error(function() {
        alert("Request failed.");
    });

    var r = $http({
        method: "GET",
        url:"/ajax/location/getroot",
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });

    r.success( function(data) {
        var root_node = {		
            'text' : data.name + " Type: Country",
            'id' : data.id,								   
            'li_attr':{
                'id' : data.id,
                'name' : data.name,
                'locationTypeId' : data.locationTypeId,
                'parentLocationId' : data.parentLocationId,
                'latitude' : data.latitude,
                'longitude' : data.longitude,
                'boundaryFile' : data.boundaryFile,
                'urlIdentifier' : data.urlIdentifier
            }  
        };

        var tree = $('#js_tree').jstree({ 
            'core' : {
                "check_callback" : true,
                'data' : root_node ,
                "plugins" : [ "types","contextmenu"]
            }
        });

        tree.bind("select_node.jstree", function (e, data) {  

            var parent = $('#js_tree').jstree('get_selected');

            $scope.node.id = $('#'+parent).attr('id');
            $scope.node.name = $('#'+parent).attr('name');
            $scope.node.latitude = $('#'+parent).attr('latitude');
            $scope.node.longitude = $('#'+parent).attr('longitude');
            $scope.node.locationTypeId = $('#'+parent).attr('locationTypeId');
            $scope.node.parentLocationId = $('#'+parent).attr('parentLocationId');
            $scope.node.urlIdentifier = $('#'+parent).attr('urlIdentifier');
            $scope.node.boundaryFile = $('#'+parent).attr('boundaryFile');
            $scope.$apply();

            if($('#'+parent).attr('boundaryFile') != 'null') {
                $('#kml_status').html("<p>KML File exists</p>");	
            } 
            else { 
                $('#kml_status').html("<p>KML File does not exist</p>");	
            }

            /****************************Load KML Layer on Google Map*****************************/

            $scope.update_map($scope.node.boundaryFile);

            /*************************End KML Load****************************************************/

            var selected_loc_typeid =  $scope.node.locationTypeId;

            var getchildRequest = $http({
                method: "GET",
                url:"/ajax/locationtype/getchild/"+selected_loc_typeid,
                headers: {'Content-Type': 'application/json; charset=utf-8'}
            });

            getchildRequest.success(function(data){
                $scope.buttonData = data;
                $scope.$apply();
            });

            getchildRequest.error(function() {
                alert('Request Failed.');
            });

            var getkmlRequest = $http({
                method: "GET",
                url:"/ajax/location/"+$scope.node.id+"/kmlfiles",
                headers: {'Content-Type': 'application/json; charset=utf-8'}
            });

            getkmlRequest.success(function(data){
                $scope.kmlList = data;
                $scope.$apply();
            });

            getkmlRequest.error(function() {
                alert('Request Failed.');
            });

            if(!($('#'+parent).hasClass('jstree-open')) && !hash.hasOwnProperty('fake_node'+$('#'+parent).attr('id'))){
                new_node = {'text':'fake','id':'fake_node'+$('#'+parent).attr('id')};
                $('#js_tree').jstree(true).create_node(parent, new_node);
                hash['fake_node'+$('#'+parent).attr('id')] = 1;
            }
        });

        tree.bind("open_node.jstree",function(e,data){
            var parent = data.node.id;
            $("#js_tree").jstree("delete_node", $('#fake_node'+$('#'+parent).attr('id')));
            if($('#'+parent).closest("li").children("ul").length ==0){
                var selected_node = data.node.id;
                var new_node;
                var sel;
                var getchildRequest = $http({
                    type: "GET",
                    url:"/ajax/location/getchild/"+selected_node,
                    headers: {'Content-Type': 'application/json; charset=utf-8'}
                });

                getchildRequest.success(function(data){
                    if(data.length ==0) {
                        alert("No Children found");
                    }		   			   
                    for(var i=0; i< data.length;i++) {
                        new_node = {
                            'text' : data[i].name+" Type :"+loc_hash[data[i].locationTypeId],
                            'id' : data[i].id,
                            'li_attr' : {
                                'id' : data[i].id,
                                'name' : data[i].name,
                                'locationTypeId' : data[i].locationTypeId,
                                'parentLocationId' : data[i].parentLocationId,
                                'latitude' : data[i].latitude,
                                'longitude' : data[i].longitude,
                                'boundaryFile' : data[i].boundaryFile,
                                'urlIdentifier' : data[i].urlIdentifier
                            }
                        };
                        sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);
                    }
                });

                getchildRequest.error(function() {
                    alert('Request Failed.');
                });

                $("#js_tree").jstree("open_node", selected_node);
            }
        });    

        $('#js_tree').jstree("select_node",root_node.id);
    });

    $('#form1').submit(function(event){
        event.preventDefault();
        //grab all form data  
        //var formData = new FormData($('#form1'));
        var formData = new FormData(this);
        var selected_node =  $('#js_tree').jstree('get_selected');
        var  kml_url= '/ajax/location/'+selected_node[0]+'/upload';

        $.ajax({
            url: kml_url,
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (returndata) {
                $('#'+selected_node).attr('boundaryFile',returndata);
                $('#kml_status').html("<p>KML File exists</p>");
                update_map(returndata);
            }
        });

        return false;
    });
}); //controller ends
