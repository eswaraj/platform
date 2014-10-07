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
        var selected_node =  $('#js_tree').jstree('get_selected');
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
                var new_node = {
                    'text': data.name+'-'+data.id,
                    'id': data.id,
                    'li_attr': data
                };
                $scope.$broadcast('addRoot',{id:"js_tree",child:new_node});
            }
        }).error(function () {
            alert("Request failed.");
        });
    };

    $scope.addChildNode = function () {
        var selected_node =  $('#js_tree').jstree('get_selected');
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
                var new_node = {
                    'text': data.name+'-'+data.id,
                    'id': data.id,
                    'li_attr': data
                };
                $scope.$broadcast('addChild',{id:"js_tree",child:new_node});
            }
        }).error(function () {
            alert("Request failed.");
        });
    };

    $scope.updateCategory = function () {
        var selected_node =  $('#js_tree').jstree('get_selected');
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
                var new_node = {
                    'text': data.name,
                    'id': data.id,
                    'li_attr': data
                };
                $scope.$broadcast('updateNode',{id:"js_tree",child:new_node});
            }
        }).error(function () {
            alert("Request failed.");
        });
    };
});
