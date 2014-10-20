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