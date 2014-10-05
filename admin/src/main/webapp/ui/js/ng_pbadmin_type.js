var pbAdminTypeApp = angular.module("pbAdminTypeApp", []);

pbAdminTypeApp.controller('pbAdminTypeController', function($scope, $http) {
    $scope.locationOptions = [];
    $scope.pbtypeList = [];
    $scope.formData = {};
    $scope.openForm = function (event) {
        $scope.formData = $.extend(true,$scope.formData,$scope.pbtypeList[event.target.attributes.index.value]);
        $scope.selectedData = $scope.pbtypeList[event.target.attributes.index.value];
    };
    $scope.resetForm = function() {
        $scope.formData = {};
        $scope.selectedData = {};
    };
    $scope.saveForm = function() {
        var saveRequest = $http({
            method: "POST",
            url:"/ajax/pbtype/save",
            data: angular.toJson($scope.formData),
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });
        saveRequest.success(function(data){
            var error = data.message.length > 0;
            if(error) {
                alert("Error in node creation: " + data.message);
            }
            else {
                if($scope.selectedData.id) {
                    $scope.selectedData = $.extend(true, $scope.selectedData, data);
                    $scope.selectedData = {};
                }
                else {
                    $scope.pbtypeList.push(data);
                }
            }
        });
        saveRequest.error(function() {
            alert("Request failed");
        });
    };
    var addLocation = function (node)
    {
        $scope.locationOptions.push({
            'id' : node.id,
            'name' : node.name});
        if(node.children)
        {   
            for(var i=0; i<node.children.length; i++)
            {   
                addLocation(node.children[i]);
            }   
        }   
    };
    $("#menu_new").load("../ui/menu.html");
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

    var pbtypeRequest = $http({
        mrthod: "GET",
        url:"/ajax/pbtype/get",
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });

    pbtypeRequest.success(function(data) {
        $scope.pbtypeList = $.extend(true, $scope.pbtypeList, data);
    });

    pbtypeRequest.error(function() {
        alert("Request failed.");
    });
}); //controller ends