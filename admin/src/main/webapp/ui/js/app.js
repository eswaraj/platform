var pos = angular.module('pos', [
  'ngRoute',
  'posControllers'
]);

/* pos.config(['$routeProvider', function($routeProvider) {
  $routeProvider.
  when('/list', {
    templateUrl: 'template/list.html',
    controller: 'ListController'
  }).
  otherwise({
    redirectTo: '/list'
  });
}]); */