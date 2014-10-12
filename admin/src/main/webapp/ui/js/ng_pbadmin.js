var pbadminApp = angular.module('pbadminApp', ['typeAhead','customDirectives']);

pbadminApp.controller('pbadminController', function($scope, $http) {
    "use strict";
    //window.scope = $scope;
    $scope.acData = {};
    $scope.locationSearchText = "";
    $scope.loc_hash = {};
    $scope.parties = {};
    $scope.pbAdminTypeList = {};
    $scope.pbAdminListCurrent = {};
    $scope.pbAdminListAll = {};
    $scope.selectedNode = $scope.selectedNode || {};
    $scope.onLocationSelected = function (index) {
        var locId = $scope.acData.node_searchData[index].id;
        var locTypeId = $scope.acData.node_searchData[index].locationTypeId;
        $scope.pbAdminTypeList = getPbAdminTypeForLocationType(all_pbtype, locTypeId);
        $scope.pbAdminTypeList.forEach(function (value, index, array) {
            var currentRequest = $http({
                method: "GET",
                url:"/ajax/pbadmin/getcurrent/"+locId+"/"+value.id,
                headers: {'Content-Type': 'application/json; charset=utf-8'}
            });
            currentRequest.success(function (data) {
                $scope.pbAdminListCurrent[value.shortName] = data;
            });
            currentRequest.error(function () {
                console.log("Current request failed");
            });
            
            var allRequest = $http({
                method: "GET",
                url:"/ajax/pbadmin/get/"+locId+"/"+value.id,
                headers: {'Content-Type': 'application/json; charset=utf-8'}
            });
            allRequest.success(function (data) {
                $scope.pbAdminListAll[value.shortName] = data;
            });
            allRequest.error(function () {
                console.log("All request failed");
            });
        });
    };
    var all_pbtype = {};
    var addLocation = function (node) {
        $scope.loc_hash[node.id] = node.name;
        if(node.children)
        {
            for(var i=0; i<node.children.length; i++)
            {
                addLocation(node.children[i]);
            }
        }
    };
    var  getPbAdminTypeForLocationType = function (pbtypes,selected_loc_typeid){
        var result = [];
        for (var i=0; i < pbtypes.length; i++){
            if(pbtypes[i].locationTypeId == selected_loc_typeid) {
                var node = {
                    'id' : pbtypes[i].id,
                    'shortName' : pbtypes[i].shortName 
                };
                result.push(node);
            }
        } 
        return result;
    };
    $("#menu_new").load("../ui/menu.html"); 
    
    var locTypeRequest = $http({
        method: "GET",
        url:"/ajax/locationtype/get",
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });
    locTypeRequest.success(function (data) {
        addLocation(data);
    });
    locTypeRequest.error(function () {
        console.error('Location type get request failed');
    });
    
    var pbTypeRequest = $http({
        method: "GET",
        url:"/ajax/pbtype/get",
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });
    pbTypeRequest.success(function (data) {
        all_pbtype = data;
    });
    pbTypeRequest.error(function () {
        console.error('Pb type get request failed');
    });
    
    var partyRequest = $http({
        method: "GET",
        url:"/ajax/party/getall",
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });
    partyRequest.success(function (data) {
        $scope.parties = data;
    });
    partyRequest.error(function () {
        console.error('Party get request failed');
    });
});