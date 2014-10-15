var locationTypeApp = angular.module('locationTypeApp', ['customDirectives']);

locationTypeApp.factory('postService', function ($http) {
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

locationTypeApp.controller('locationTypeController', function ($scope, postService) {
    $("#menu_new").load("../ui/ng_menu.html");
    $scope.selectedNode = $scope.selectedNode || {};
    $scope.child = {};
    $scope.loc = {};
    $scope.$watch('selectedNode', function() {
        $scope.child.parentLocationTypeId = $scope.selectedNode.id;
        //$.extend(true, $scope.loc, $scope.selectedNode);
        $scope.loc.id = $scope.selectedNode.id;
        $scope.loc.name = $scope.selectedNode.name;
        $scope.loc.parentLocationTypeId = $scope.selectedNode.parentLocationTypeId;
        $('#location_type_name').prop("disabled", true);
        $("#edit_btn").css('display','block');
        $("#save_btn").css('display','none');
    });
    $scope.editNode = function() {
        $("#save_btn").css('display','block');
        $("#edit_btn").css('display','none');
        $('#location_type_name').prop("disabled", false);
    };
    $scope.updateNode = function() {
        $("#edit_btn").css('display','block');
        $("#save_btn").css('display','none');
        $('#location_type_name').prop("disabled", true);
        postService.run($scope, '/ajax/locationtype/save', $scope.loc, false, 'updateNode');
    };
    $scope.deleteNode = function() {
        //Get the selected node,, remove from tree and send delete request to server
        var x = confirm("Are you sure you want to delete this node?");
        if (x)
            return true;
        else
            return false;
    };
    $scope.addChildNode = function() {
        postService.run($scope, '/ajax/locationtype/save', $scope.child, true, 'addChild');
    };
});
