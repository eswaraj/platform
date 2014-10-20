var complaintsApp = angular.module('complaintsApp', []);

complaintsApp.controller('complaintsController', function ($scope, $http) {
    var getCount = 10;
    var total = 0;
    var current = 0;
    var allComplaints = [];
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
    $scope.onStatusSelected = function () {};
    $scope.onPositionSelected = function () {
        total = 0;
        current = 0;
        $scope.complaints = [];
        allComplaints = [];
        $scope.getNext();
    };
    $scope.getNext = function () {
        if(current == total) {
            var complaintRequest = $http({
                method: "GET",
                url:'/ajax/complaint/leader/' + $scope.selectedPosition.id + '/?page=' + (current+1),
                headers: {'Content-Type': 'application/json; charset=utf-8'}
            });
            complaintRequest.success(function (data) {
                allComplaints = allComplaints.concat(data);
                total = total + 1;
                current = current + 1;
                $scope.complaints = allComplaints.slice((current-1)*getCount, current*getCount);

            });
            complaintRequest.error(function () {
                console.error("Complaint request failed");
            });
        }
        else {
            current = current + 1;
            $scope.complaints = allComplaints.slice((current-1)*getCount, current*getCount);
        }
    };
    $scope.getPrevious = function () {
        if (current == 1) {
            return;
        }
        current = current - 1;
        $scope.complaints = allComplaints.slice((current-1)*getCount, current*getCount);
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
        var out = "";
        categories.forEach(function (value, index, array) {
            if (!value.root) {
                out = value.name;
            }
        });
        return out;
    };
});

complaintsApp.filter('dateFormatter', function () {
    return function (input) {
        var date = new Date(input);
        out = date.toString();
        return out;
    };
});