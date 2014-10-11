var pbadminStaff = angular.module('pbadminStaff', ['typeAhead']);

pbadminStaff.controller('pbadminStaffController',function($scope, $http){
    $scope.selected = {};
    $scope.new = {};
    $scope.staffs = {};
    $scope.positions = {};
    $scope.onPositionSelected = function (index) {
        $scope.new.politicalAdminId = $scope.positions[index].id;
        var staffRequest = $http.get('/ajax/leader/staff/' + $scope.positions[index].id);
        staffRequest.success(function (data) {
            $scope.staffs = data;
        });
    };
    $scope.onPersonSelected = function() {
        console.log('selected='+$scope.selected);
        $scope.new.personId = $scope.selected.id;
    };
    $scope.addStaff = function () {
        var addRequest = $http({
            method: "POST",
            url:"/ajax/leader/staff",
            data: angular.toJson($scope.new),
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });
        addRequest.success(function (data) {
            $scope.staffs = data;
        });
        addRequest.error(function () {
            console.error('Request failed for /ajax/leader/staff');
        });
    };
    $scope.deleteStaff = function (index) {
        var deleteRequest = $http.delete('/ajax/leader/staff/' + $scope.staffs[index].politicalAdminStaffId);
        deleteRequest.success(function (data) {
            if($scope.staffs[index].politicalAdminStaffId == data.politicalAdminStaffId) {
                $scope.staffs.splice(index, 1);
            }
            else {
                console.error('Wrong staff member deleted');
            }
        });
        deleteRequest.error(function () {
            console.error('Delete request failed');
        });
    };
    
    //Get all political positions
    var positionRequest = $http.get('/ajax/complaint/leader/positions');
    positionRequest.success(function (data) {
        $scope.positions = data;
    });
    positionRequest.error(function () {
        console.error('Could not get positions for the leader');
    });
});