var posControllers = angular.module('posControllers', []);

posControllers.controller('RootCategories', ['$scope', '$http', function($scope, $http) {
  $http.get('/ajax/categories/getroot').success(function(data) {
    $scope.rootCategories = data;
    
  });
}]);

posControllers.controller('GetLocation', ['$scope', '$http', function($scope, $http) {
  $http.get('/api/location/78340/info').success(function(data) {
    $scope.location = data;
    
  });
}]);

posControllers.controller('GetTotalComplaintsForLocation', ['$scope', '$http', function($scope, $http) {
  $http.get('/api/location/78340/complaintcounts/last30').success(function(data) {
    $scope.totalComplaintsForLocation = data;
    
  });
}]);

posControllers.controller('GetAllComplaints', ['$scope', '$http', function($scope, $http) {
  $http.get('/api/complaint/location/78340').success(function(data) {
    $scope.allComplaints = data;
    
  });
}]);

