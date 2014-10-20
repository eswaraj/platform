var complaintsApp = angular.module('complaintsApp', []);

complaintsApp.controller('complaintsController', function ($scope, $http) {
    $scope.positions = {};
    $scope.complaints = [];
    $scope.statuses = ['Pending', 'Viewed', 'Duplicate', 'Assigned', 'InProgress', 'InReview', 'Done', 'Unfinished', 'Esclated'];
    $scope.selectedPosition = {};
    $scope.selectedStatus = {};
    $scope.newComment = {};
    $scope.addComment = function () {};
    $scope.saveStatus = function () {};
    $scope.label = function (positionType, locationName) {
        return positionType + " of " + locationName;
    };
    $scope.onPositionSelected = function () {
        //$scope.new.politicalAdminId = $scope.positions[index].id;
        //$scope.addMode = false;
        //$scope.new.politicalAdminId = $scope.selectedPosition.id;
        var complaintRequest = $http({
            method: "GET",
            url:'/ajax/complaint/leader/' + $scope.selectedPosition.id,
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });
        complaintRequest.success(function (data) {
            $scope.complaints = data;
        });
        complaintRequest.error(function () {
            console.error("Complaint request failed");
        });
    };
    //Get all political positions
    var positionRequest = $http({
        method: "GET",
        url:'/ajax/leader/positions/',
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });
    positionRequest.success(function (data) {
        $scope.positions = data;
    });
    positionRequest.error(function () {
        console.error('Could not get positions for the leader');
    });
});
complaintsApp.filter('rootCategory', function () {
    return function (categories) {
        var out = "";
        categories.forEach(function (value, index, array) {
            if (value.root) {
                out = value.name;
            }
        });
        return out;
    };
});
complaintsApp.filter('subCategory', function () {
    return function (categories) {
        categories.forEach(function (value, index, array) {
            if (!value.root) {
                return value.name;
            }
        });
    };
});