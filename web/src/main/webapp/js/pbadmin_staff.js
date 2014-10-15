var pbadminStaff = angular.module('pbadminStaff', ['typeAhead']);

pbadminStaff.controller('pbadminStaffController',function($scope, $http){
    $scope.selected = {};
    $scope.new = {};
    $scope.staffs = {};
    $scope.positions = {};
    $scope.selectedPosition = {};
    $scope.addMode = {};
    $scope.showForm = function () {
        $scope.addMode = true;
    };
    $scope.closeForm = function () {
        $scope.addMode = false;
    };
    $scope.label = function (positionType, locationName) {
        return positionType + " of " + locationName;
    };
    $scope.onPositionSelected = function () {
        //$scope.new.politicalAdminId = $scope.positions[index].id;
        $scope.addMode = false;
        $scope.new.politicalAdminId = $scope.selectedPosition.id;
        var staffRequest = $http({
                method: "GET",
		url:'/ajax/leader/staff/' + $scope.positions[index].id,
		headers: {'Content-Type': 'application/json; charset=utf-8'}
	});
        staffRequest.success(function (data) {
            $scope.staffs = data;
        });
    };
    $scope.onPersonSelected = function() {
        console.log('selected='+$scope.selected.name);
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
        $scope.addMode = false;
    };
    $scope.deleteStaff = function (staff) {
        var deleteRequest = $http({
            method: "DELETE",
            //url:"/ajax/leader/staff/" + $scope.staffs[index].id,
            url:"/ajax/leader/staff/" + staff.id,
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });
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
