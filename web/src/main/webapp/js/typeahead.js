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
            scope.model = scope.model || {};
            scope.model[scope.title] = scope.model[scope.title] || "";
            scope.$watch('model[title]', function () {
                if (scope.pendingPromise) { 
                    $timeout.cancel(scope.pendingPromise); 
                }
                scope.pendingPromise = $timeout( function () {
                    if(scope.model[scope.title].length == scope.min && !scope.selected) {
                        dataFactory.get(scope.url, scope.model[scope.title], scope.querystring).then(function(resp){
                            scope.items=resp.data;
                        });
                    }
                }, 300); //wait 300ms before processing. This is to avoid search on quick typing
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
        //templateUrl: 'templates/typeahead.html'
        template : '<input type="text" ng-model="model[title]" placeholder="{{prompt}}" ng-keydown="selected=false" class="tainput"/><br/>\
<div class="taitems" ng-hide="!model[title].length || selected">\
<div class="taitem" ng-repeat="item in items | filter:model[title] track by $index" ng-click="handleSelection(item)" style="cursor:pointer" ng-class="{taactive:isCurrent($index)}" ng-mouseenter="setCurrent($index)">\
<img class="taimage" src="{{item[img]}}" ng-show="item[img]"></img>\
<p class="tatitle">{{item[title]}}</p>\
<p class="tasubtitle">{{item[subtitle]}}</p>\
</div>\
</div>'
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
