var eSwarajMod = angular.module('eSwaraj', []);

eSwarajMod.directive('header', function () {
return {
restrict: 'A', //This menas that it will be used as an attribute and NOT as an element. I don't like creating custom HTML elements
replace: true,
templateUrl: "../angularjs/header.html",
controller: ['$scope', '$filter', function ($scope, $filter) {

}]
}
});

eSwarajMod.directive('footer', function () {
return {
restrict: 'A', //This menas that it will be used as an attribute and NOT as an element. I don't like creating custom HTML elements
replace: true,
templateUrl: "../angularjs/footer.html",
controller: ['$scope', '$filter', function ($scope, $filter) {

}]
}
});

eSwarajMod.controller('RootCategories', ['$scope', '$http', function($scope, $http) {
  $http.get('/ajax/categories/getroot').success(function(data) {
    $scope.rootCategories = data;
	for(var i=0; i< data.length ; i++){
	$scope.items.push({ label: data[i].name,value: data[i].id })
	}
    
  });
}]);

eSwarajMod.controller('GetLocation', ['$scope', '$http', function($scope, $http) {
  $http.get('http://dev.admin.eswaraj.com/eswaraj-web/api/location/78340/info').success(function(data) {
    $scope.location = data;
    
  });
}]);

eSwarajMod.controller('GetTotalComplaintsForLocation', ['$scope', '$http', function($scope, $http) {
  $http.get('http://dev.admin.eswaraj.com/eswaraj-web/api/location/78340/complaintcounts/last30').success(function(data) {
    $scope.totalComplaintsForLocation = data;
    
  });
}]);

eSwarajMod.controller('GetAllComplaints', ['$scope', '$http', function($scope, $http) {
  $http.get('http://dev.admin.eswaraj.com/eswaraj-web/api/complaint/location/78340').success(function(data) {
    $scope.allComplaints = data;
    
  });
}]);

