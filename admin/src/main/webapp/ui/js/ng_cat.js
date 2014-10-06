var categoryApp = angular.module('categoryApp',[]);

categoryApp.controller('categoryController', function ($scope, $http) {
    "use strict";
    $scope.root = {
        'id' : "",
        'name' : "",
        'root' : true,
        'parentCategoryId' : null,
        'description' : "",
        'imageUrl' : "",
        'headerImageUrl' : "",
        'videoUrl' : ""
    };
    $scope.child = {
        'id' : "",
        'name' : "",
        'root' : false,
        'parentCategoryId' : null,
        'description' : "",
        'imageUrl' : "",
        'headerImageUrl' : "",
        'videoUrl' : ""
    };
    $scope.cat = {
        'id' : "",
        'name' : "",
        'root' : "",
        'parentCategoryId' : null,
        'description' : "",
        'imageUrl' : "",
        'headerImageUrl' : "",
        'videoUrl' : ""
    };
    var root_node;
    var hash = {};

    $("#menu_new").load("../ui/menu.html");

    var r = $http({
        method: "GET",
        url:"/ajax/categories/getroot",
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });

    r.success( function(data){
        var root_node_array = [];
        for(var i=0; i< data.length;i++){
            new_node = {
                'text':data[i].name+'-'+data[i].id,
                'id':data[i].id,
                'li_attr':{
                    'id':data[i].id,
                    'name':data[i].name,
                    'parentCategoryId':data[i].parentCategoryId,
                    'description':data[i].description,
                    'imageUrl':data[i].imageUrl,
                    'headerImageUrl':data[i].headerImageUrl,
                    'videoUrl':data[i].videoUrl,
                    'root':data[i].root
                }
            };
            root_node_array.push(new_node);
        }

        var tree = $('#js_tree').jstree(
            { 
                'core' : 
                {
                    "check_callback" :true,
                    'data' :root_node_array,
                    "plugins" : [ "types","contextmenu"]
                }
            });

        tree.bind("select_node.jstree", function (e, data) {  
            var parent = $('#js_tree').jstree('get_selected');	
            $scope.cat.id = $('#'+parent).attr('id');
            $scope.cat.parentCategoryId = $('#'+parent).attr('parentCategoryId');
            $scope.cat.root = $('#'+parent).attr('root');
            $scope.cat.name = $('#'+parent).attr('name');
            $scope.cat.description = $('#'+parent).attr('description');
            $scope.cat.imageUrl = $('#'+parent).attr('imageUrl');
            $scope.cat.headerImageUrl = $('#'+parent).attr('headerImageUrl');
            $scope.cat.videoUrl = $('#'+parent).attr('videoUrl');

            $scope.child.id = $('#'+parent).attr('id');
            $scope.$apply();

            if($('#'+parent).attr('img')!= null){
                $('#image').css("display","block");
                $('#image').attr('src',$('#'+parent).attr('img'));
            }
            if($('#'+parent).attr('headerImg') != null){
                $('#headerImg').css("display","block");
                $('#headerImg').attr('src',$('#'+parent).attr('headerImg'));
            }
            if($('#'+parent).attr('videoUrl') != null){
                $('#video').css("display","block");
                $('#video').attr('src',$('#'+parent).attr('videoUrl'));
            }

            if(!($('#'+parent).hasClass('jstree-open')) && !hash.hasOwnProperty('fake_node'+$('#'+parent).attr('id'))){
                new_node = {'text':'fake','id':'fake_node'+$('#'+parent).attr('id')};
                $('#js_tree').jstree(true).create_node(parent, new_node);
                hash['fake_node'+$('#'+parent).attr('id')] = 1;
            }
            $scope.$apply();
        }); //select_node binding ends

        tree.bind("open_node.jstree",function(e,data){
            var parent = data.node.id;
            $("#js_tree").jstree("delete_node", $('#fake_node'+$('#'+parent).attr('id')));
            if($('#'+parent).closest("li").children("ul").length ==0){
                var selected_node =  data.node.id;
                var new_node;
                var sel;

                var request = $http({
                    method: "GET",
                    url:"/ajax/categories/getchild/"+data.node.id,
                    headers: {'Content-Type': 'application/json; charset=utf-8'}
                });

                request.success( function(data) {
                    if(data.length == 0) {
                        alert("No Children found");
                    } 
                    else {
                        if($('#'+selected_node).closest("li").children("ul").length ==0) {
                            for(var i=0; i< data.length;i++){
                                new_node = {
                                    'text':data[i].name+'-'+data[i].id,
                                    'id':data[i].id,
                                    'li_attr':{
                                        'id' :data[i].id,
                                        'name':data[i].name,
                                        'parentCategoryId':data[i].parentCategoryId,
                                        'description':data[i].description,
                                        'imageUrl':data[i].imageUrl,
                                        'headerImageUrl':data[i].headerImg,
                                        'videoUrl':data[i].videoUrl,
                                        'root':data[i].root
                                    }
                                };
                                sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);
                            }
                        }
                        else { 
                            alert('No New Nodes');
                        };
                    }
                });

                request.error(function () {
                    alert("Request failed.");
                });
            }
        }); //open_node binding ends
        
        $('#js_tree').jstree("select_node",root_node_array[0].id);
    }); //r.success() ends

    r.error(function () {
        alert("Request failed.");
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
                    'text':data.name+'-'+data.id,
                    'id':data.id,
                    'li_attr':{
                        'id':data.id,
                        'name':data.name,
                        'parentCategoryId':data.parentCategoryId,
                        'description':data.description,
                        'imageUrl':data.imageUrl,
                        'headerImageUrl':data.headerImageUrl,
                        'videoUrl':data.videoUrl,
                        'root':data.root
                    }
                };
                var sel = $('#js_tree').jstree(true).create_node('#', new_node);
                $('#js_tree').jstree("select_node",data.id);
                $scope.$apply();
            }
        }).error(function () {
            alert("Request failed.");
        });
    };

    $scope.addChildNode = function () {
        var selected_node =  $('#js_tree').jstree('get_selected');
        //$scope.child.parentCategoryId = selected_node[0];
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
                    'text':data.name+'-'+data.id,
                    'id':data.id,
                    'li_attr':{
                        'id':data.id,
                        'name':data.name,
                        'parentCategoryId':data.parentCategoryId,
                        'description':data.description,
                        'imageUrl':data.imageUrl,
                        'headerImageUrl':data.headerImageUrl,
                        'videoUrl':data.videoUrl,
                        'root':data.root
                    }
                };
                $scope.$apply();
                var sel = $('#js_tree').jstree(true).create_node(selected_node, new_node);
                $('#js_tree').jstree("select_node",data.id);
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
            data: angular.toJson($scope.cat),
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        }).success(function (data) {
            if(data.message){
                alert("Error: Check POST response");
            }
            else{
                alert("Category Updated: "+data.name);
                $('#js_tree').jstree('set_text',data.id, data.name);
                $('#'+data.id).attr('id',data.id);
                $('#'+data.id).attr('name',data.name);
                $('#'+data.id).attr('parentCategoryId',data.parentCategoryId);
                $('#'+data.id).attr('imageUrl',data.imageUrl);
                $('#'+data.id).attr('description',data.description);  
                $('#'+data.id).attr('root',data.root);
                $('#'+data.id).attr('headerImageUrl',data.headerImageUrl);
                $('#'+data.id).attr('videoUrl',data.videoUrl);
            }
        }).error(function () {
            alert("Request failed.");
        });
    };
});
