var pos = angular.module('eSwaraj', [
  'ngRoute'
]);

var module = angular.module('eSwaraj',
['ngResource']).
config(['$routeProvider', function ($routeProvider) {
$routeProvider.
when('/location',
{templateUrl: '/location.html'}).
when('/profile-add',
{templateUrl: '/profile-add.html'}).
when('/profile-edit',
{templateUrl: '/profile-edit.html'}). 
otherwise({redirectTo: '/index.html'});
}]);
