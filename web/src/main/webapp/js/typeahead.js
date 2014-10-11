var typeAhead = angular.module('typeAhead', []);

typeAhead.directive('typeahead', function($timeout, dataFactory) {
    return {
        restrict: 'AEC',
        scope: {
            items: '=',
            prompt:'@',
            title: '@',
            subtitle:'@',
            img: '@',
            min: '@',
            url: '@',
            querystring: '@',
            model: '=',
            onSelect:'&'
        },
        link:function(scope,elem,attrs){
            scope.$watch('model', function () {
                if(scope.model.length >= scope.min) {
                    dataFactory.get(scope.url, scope.model, scope.querystring).then(function(resp){
                        scope.items=resp.data;
                    });
                }
            });
            scope.handleSelection=function(selectedItem){
                scope.model=selectedItem;
                scope.current=0;
                scope.selected=true;        
                $timeout(function(){
                    scope.onSelect();
                },200);
            };
            scope.current=0;
            scope.selected=true;
            scope.isCurrent=function(index){
                return scope.current==index;
            };
            scope.setCurrent=function(index){
                scope.current=index;
            };
        },
        templateUrl: 'templates/typeahead.html'
    }
});

typeAhead.factory('dataFactory', function ($http) {
    "use strict";
    return {
        get: function (url, query, querystring) {
            var request = $http({
                method: "GET",
                url: url + (querystring == '' ? '/' + query : '?' + querystring + '=' + query),
                headers: {'Content-Type': 'application/json; charset=utf-8'}
            });
            return request;
        }
    };
});