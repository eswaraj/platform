var partyApp = angular.module('partyApp', []);

partyApp.controller('partyController', function($scope, $http) {
    $scope.party = {};
    $scope.partyList = [];
    $scope.selectedParty = {};
    $scope.openForm = function(event) {
        $scope.party = $.extend(true,$scope.party,$scope.partyList[event.target.attributes.index.value]);
        $scope.selectedParty = $scope.partyList[event.target.attributes.index.value];
    };
    $scope.saveForm = function() {
        var saveRequest = $http({
            method: "POST",
            url:"/ajax/party/save",
            data: angular.toJson($scope.party),
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });
        saveRequest.success(function(data){
            var error = data.message.length > 0;
            if(error) {
                alert("Error in node creation: " + data.message);
            }
            else {
                if($scope.selectedParty.id) {
                    $scope.selectedParty = $.extend(true, $scope.selectedParty, data);
                    $scope.selectedParty = {};
                }
                else {
                    $scope.partyList.push(data);
                }
            }
        });
        saveRequest.error(function() {
            alert("Request failed");
        });
    };
    $scope.resetForm = function() {
        $scope.party = {};
        $scope.selectedParty = {};
    };
    
    $("#menu_new").load("../ui/menu.html");
    var request = $http({
        mrthod: "GET",
        url:"/ajax/party/getall",
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });

    request.success(function(data) {
        $scope.partyList = $.extend(true, $scope.partyList, data);
    });

    request.error(function() {
        alert("Request failed.");
    });
});//controller end