var pbadminApp = angular.module('pbadminApp', ['typeAhead','customDirectives','textAngular']);

pbadminApp.controller('pbadminController', function($scope, $http, $timeout, $q) {
    "use strict";
    //window.scope = $scope;
    $scope.acData = {};
    $scope.locationSearchText = "";
    $scope.loc_hash = {};
    $scope.parties = {};
    $scope.form = {};
    $scope.form.officeAddressDto = {};
    $scope.form.homeAddressDto = {};
    $scope.person = {};
    $scope.person.personAddress = {};
    $scope.pbAdminTypeList = {};
    $scope.pbAdminListCurrent = {};
    $scope.pbAdminListAll = {};
    $scope.selectedNode = $scope.selectedNode || {};
    $scope.selectedLocation = "";
    $scope.selectedPerson = {};
    $scope.showTypeWithLocation = function (obj) {
        //var p = $q.defer(); 
        //deferred.promise.then(function () {
        //    p.resolve(obj.name + " Type: " + $scope.loc_hash[obj.locationTypeId]);
        //});
        //return p.promise;
        
        if (promiseResolved) {
            return obj.name + " Type: " + $scope.loc_hash[obj.locationTypeId];
        }
        else {
            return obj.name + " Type: Country";
        }
    };
    $scope.closeForm = function () {
        $scope.form = {};
        $scope.person = {};
        $( "#add_edit_admin_page" ).hide();
        $( ".wrapper" ).show();
    };
    $scope.savePbAdmin = function () {
        $scope.form.startDate = new Date($scope.form.startDate).getTime();
        $scope.form.endDate = new Date($scope.form.endDate).getTime();
        var saveRequest = $http({
            method: 'POST',
            url: "/ajax/pbadmin/save",
            data: angular.toJson($scope.form),
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });
        saveRequest.success(function (data) {
            var key;
            var updateIndex;
            if(data.message) {
                console.error("Save failed with message: " + data.message);
            }
            else {
                console.info("Pbadmin saved with id = " + data.id);
                data.startDate = isoToHuman(data.startDate);
                data.endDate = isoToHuman(data.endDate);
                $scope.person.personAddress = $scope.person.personAddress || {};
                data.person = data.person || {};
                $.extend(true, data.person, $scope.person);
                $scope.person.dob = new Date($scope.person.dob).getTime();
                var personSaveRequest = $http({
                    method: 'POST',
                    url: "/ajax/person/save",
                    data: angular.toJson($scope.person),
                    headers: {'Content-Type': 'application/json; charset=utf-8'}
                });
                personSaveRequest.success(function (presp) {
                    console.info(presp);
                });
                personSaveRequest.error(function () {
                    console.error("Person save request failed");
                });
                $scope.pbAdminTypeList.forEach(function (value) {
                    if (value.id == data.politicalBodyTypeId) {
                        key = value.shortName;
                    }
                });
                if (!$scope.pbAdminListAll[key]) {
                    $scope.pbAdminListAll[key] = [];
                }
                $scope.pbAdminListAll[key].forEach(function (value, index, array) {
                    if (value.id == data.id) {
                        updateIndex = index;
                    }
                });
                if (typeof updateIndex != 'undefined') {
                    $.extend(true, $scope.pbAdminListAll[key][updateIndex], data);
                }
                else {
                    //var personRequest = $http({
                    //    method: "GET",
                    //    url:"/ajax/person/get/"+data.personId,
                    //    headers: {'Content-Type': 'application/json; charset=utf-8'}
                    //});
                    //personRequest.success(function (resp) {
                    //    data.person = resp;
                    //});
                    //personRequest.error(function () {
                    //    console.error("Person request failed");
                    //});
                    $scope.pbAdminListAll[key].push(data);
                }
            }
            $scope.form = {};
        });
        saveRequest.error(function () {
            console.error("Save request failed");
        });
        $( "#add_edit_admin_page" ).hide();
        $( ".wrapper" ).show();
    };
    $scope.addPbAdmin = function (position) {
        $( "#add_edit_admin_page" ).show();
        $( ".wrapper" ).hide();
        var positionId = "";
        $scope.pbAdminTypeList.forEach(function (value) {
            if (value.shortName == position) {
                positionId = value.id;
            }
        });
        $scope.form.politicalBodyTypeId = positionId;
        $scope.form.locationId = $scope.selectedLocation;
        $scope.person = {};
    };
    $scope.editPbAdmin = function (selected) {
        console.log(selected);
        $( "#add_edit_admin_page" ).show();
        $( ".wrapper" ).hide();
        $.extend(true, $scope.form, selected);
        delete $scope.form.person;
        $.extend(true, $scope.person, selected.person);
        $scope.form.startDate = isoToHuman($scope.form.startDate);
        $scope.form.endDate = isoToHuman($scope.form.endDate);
        //$scope.person.dob = new Date($scope.person.dob).getTime();
    };
    $scope.onPersonSelected = function() {
        console.log($scope.selectedPerson);
        $scope.form.personId = $scope.selectedPerson.id;
        $scope.person = $scope.selectedPerson;
        $scope.person.dob = isoToHuman($scope.person.dob);
    };
    $scope.onLocationSelected = function (index) {
        var locId = $scope.acData.node_searchData[index].id;
        var locTypeId = $scope.acData.node_searchData[index].locationTypeId;
        getPbAdmins(locId, locTypeId);
    }
    $scope.$watch('selectedNode', function () {
        getPbAdmins($scope.selectedNode.id, $scope.selectedNode.locationTypeId);
    });
    var getPbAdmins = function (locId, locTypeId) {
        $scope.form.locationId = locId;
        $scope.selectedLocation = locId;
        $scope.pbAdminTypeList = getPbAdminTypeForLocationType(all_pbtype, locTypeId);
        $scope.pbAdminListAll = {};
        $scope.pbAdminListCurrent = {};
        $scope.pbAdminTypeList.forEach(function (value, index, array) {
            var currentRequest = $http({
                method: "GET",
                url:"/ajax/pbadmin/getcurrent/"+locId+"/"+value.id,
                headers: {'Content-Type': 'application/json; charset=utf-8'}
            });
            currentRequest.success(function (data) {
                $scope.pbAdminListCurrent[value.shortName] = data;
                var personRequest = $http({
                    method: "GET",
                    url:"/ajax/person/get/"+data.personId,
                    headers: {'Content-Type': 'application/json; charset=utf-8'}
                });
                personRequest.success(function (resp) {
                    //data.person = resp;
                    $scope.pbAdminListCurrent[value.shortName].person = resp;
                });
                personRequest.error(function () {
                    console.error("Person request failed");
                });
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
                data.forEach(function (o, idx, a) {
                    a[idx].startDate = isoToHuman(a[idx].startDate);
                    a[idx].endDate = isoToHuman(a[idx].endDate);
                });
                $scope.pbAdminListAll[value.shortName] = data;
                $scope.pbAdminListAll[value.shortName].forEach(function (obj, i, arr) {
                    var personRequest = $http({
                        method: "GET",
                        url:"/ajax/person/get/"+obj.personId,
                        headers: {'Content-Type': 'application/json; charset=utf-8'}
                    });
                    personRequest.success(function (data) {
                        data.dob = isoToHuman(data.dob);
                        arr[i].person = data;
                    });
                    personRequest.error(function () {
                        console.error("Person request failed");
                    });
                });
            });
            allRequest.error(function () {
                console.log("All request failed");
            });
        });
        $timeout(function () {
            $('#pb_admin_tab a:first').tab('show')
        }, 1500);
    };
    $scope.tabToggle = function (event) {
        $(event.target).tab('show');
    };
    var all_pbtype = {};
    var isoToHuman = function (iso) {
        var human = new Date(iso);
        var dd = human.getDate();
        var mm = human.getMonth()+1; //January is 0!
        var yyyy = human.getFullYear();
        if(dd<10){dd='0'+dd} 
        if(mm<10){mm='0'+mm} 
        human = yyyy+'-'+mm+'-'+dd;
        return human;
    }
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
    $("#menu_new").load("../ui/ng_menu.html"); 
    $( "#add_edit_admin_page" ).hide();

    var deferred = $q.defer();
    var promiseResolved = false;
    var locTypeRequest = $http({
        method: "GET",
        url:"/ajax/locationtype/get",
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });
    locTypeRequest.success(function (data) {
        addLocation(data);
        promiseResolved = true;
        deferred.resolve();
    });
    locTypeRequest.error(function () {
        console.error('Location type get request failed');
        deferred.reject();
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
