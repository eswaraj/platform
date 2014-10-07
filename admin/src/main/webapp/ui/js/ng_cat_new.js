var categoriesApp = angular.module('categoriesApp',['customDirectives']);

categoriesApp.controller('categoriesController', function ($scope, $http) {
    "use strict";
    $scope.root = {};
    $scope.child = {};
    $scope.selectedNode = $scope.selectedNode || {};
    var root_node;
    var hash = {};

    $("#menu_new").load("../ui/menu.html");
    
    $scope.$watch('selectedNode.li_attr', function() {
        $scope.child.parentCategoryId = $scope.selectedNode.id;
    });

    $scope.addRootNode = function () {
        $http({
            method: 'POST',
            url: "/ajax/categories/save",
            data: angular.toJson($scope.root),
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        }).success(function (data) {
            if(data.message){
                alert("Error: Check POST response");
            }
            else{
                alert("Category Added"+data.name);
                $scope.root = {};
                $scope.$broadcast('addRoot',{id:"js_tree",child:data});
            }
        }).error(function () {
            alert("Request failed.");
        });
    };

    $scope.addChildNode = function () {
        $http({
            method: 'POST',
            url: "/ajax/categories/save",
            data: angular.toJson($scope.child),
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        }).success(function (data) {
            if(data.message){
                alert("Error: Check POST response");
            }
            else{
                alert("Category Added"+data.name);
                $scope.child = {};
                $scope.$broadcast('addChild',{id:"js_tree",child:data});
            }
        }).error(function () {
            alert("Request failed.");
        });
    };

    $scope.updateCategory = function () {
        $http({
            method: 'POST',
            url: "/ajax/categories/save",
            data: angular.toJson($scope.selectedNode),
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        }).success(function (data) {
            if(data.message){
                alert("Error: Check POST response");
            }
            else{
                alert("Category Updated: "+data.name);
                $scope.$broadcast('updateNode',{id:"js_tree",child:data});
            }
        }).error(function () {
            alert("Request failed.");
        });
    };
});
