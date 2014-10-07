var categoriesApp = angular.module('categoriesApp',['customDirectives']);

categoriesApp.factory('postService', function ($http) {
    return {
        run : function(scope, url, obj, clearObj, event) {
            $http({
                method: 'POST',
                url: url,
                data: angular.toJson(obj),
                headers: {'Content-Type': 'application/json; charset=utf-8'}
            }).success(function (data) {
                if(data.message){
                    alert("Error: Check POST response");
                }
                else{
                    alert("Category Added"+data.name);
                    if(clearObj) {
                        obj = {};
                    }
                    scope.$broadcast(event,{id:"js_tree",child:data});
                }
            }).error(function () {
                alert("Request failed.");
            });
        }
    };
});

categoriesApp.controller('categoriesController', function ($scope, postService) {
    "use strict";
    $scope.root = {};
    $scope.child = {};
    $scope.selectedNode = $scope.selectedNode || {};
    var root_node;
    var hash = {};

    $("#menu_new").load("../ui/menu.html");

    $scope.$watch('selectedNode', function() {
        $scope.child.parentCategoryId = $scope.selectedNode.id;
    });

    $scope.addRootNode = function () {
        postService.run($scope, '/ajax/categories/save', $scope.root, true, 'addRoot');
    };

    $scope.addChildNode = function () {
        postService.run($scope, '/ajax/categories/save', $scope.child, true, 'addChild');
    };

    $scope.updateCategory = function () {
        postService.run($scope, '/ajax/categories/save', $scope.selectedNode, false, 'updateNode');
    };
});
