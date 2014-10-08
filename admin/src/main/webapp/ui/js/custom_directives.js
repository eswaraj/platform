/*global angular, autocomplete, console, $*/

var app = angular.module('customDirectives', []);

//autocomplete directive using JQuery's autocomplete. It will populate acData.{id}Data in the parent's scope(mostly controller's scope)
app.directive('ngAutocomplete', function ($timeout) {
    "use strict";
    return {
        restrict: 'A',
        scope: false,
        link: function (scope, element, attrs) {
            element.autocomplete({
                source: attrs.url,
                minLength: 3,
                response: function (event, ui) {
                    scope.acData = scope.acData || {};
                    scope.acData[attrs.id + 'Data'] = ui.content;
                    scope.$apply();
                },
                open: function (event, ui) {
                    $(".ui-autocomplete").hide();
                }
            });
        }
    };
});

//jstree directive. To use, do following:
//<div jstree id="tree1" ng-model="text" root-url="http://demo5303989.mockable.io/person/" child-url="http://demo5303989.mockable.io/getchild"></div>
//To programmatically add a new root node, child node:
//$scope.$broadcast('addRoot',{id:"tree1",child:{id:100,name:'Vaibhav',li_attr:{id:100,name:'Vaibhav',someField:'someValue'}}});
//$scope.$broadcast('addChild',{id:"tree1",child:{id:100,name:'Vaibhav',li_attr:{id:100,name:'Vaibhav',someField:'someValue'}}});
//$scope.$broadcast('updateNode',{id:"tree1",child:{id:100,name:'Vaibhav',li_attr:{id:100,name:'Vaibhav',someField:'someValue'}}});
//'tree1' above is the id of the jstree to which you want to add the new node. Also, the payload has to be in 'child'. The child node is added to currently selected node.
//parent scope will get a "selectedNode" field which will have the node object
app.directive('jstree', function($timeout, $http) {
    "use strict";
    return {
        restrict: 'A',
        //scope: {selectedNode : '='},
        scope: false,
        link: function (scope, element, attrs) {
            var root_node_array = [];

            scope.$on('addRoot', function(event, data) {
                if(data.id === attrs.id) {
                    element.jstree(true).create_node('#', {
                        id : data.child.id,
                        text : data.child.name,
                        li_attr : data.child
                    });
                }
            });

            scope.$on('addChild', function(event, data) {
                if(data.id === attrs.id) {
                    var n = element.jstree('get_selected', true);
                    n = n[0];
                    element.jstree(true).create_node(n, {
                        id : data.child.id,
                        text : data.child.name,
                        li_attr : data.child
                    });
                }
            });

            scope.$on('updateNode', function(event, data) {
                if(data.id === attrs.id) {
                    element.jstree('set_text',data.child.id, data.child.name);
                    for (var key in data.child) {
                        $('#'+data.child.id).attr(key,data.child[key]);
                    }
                }
            });

            var request = $http({
                method: "GET",
                url:attrs.rootUrl,
                headers: {'Content-Type': 'application/json; charset=utf-8'}
            });
            request.success(function(data) {
                if(data instanceof Array) {
                    for(var i=0; i< data.length;i++){
                        var new_node = {
                            'text': data[i].name,
                            'id': data[i].id,
                            'li_attr': ""
                        };
                        new_node.li_attr = $.extend(true, new_node.li_attr, data[i]);
                        root_node_array.push(new_node);
                    }
                }
                else {
                    var new_node = {
                        'text': data.name,
                        'id': data.id,
                        'li_attr': ""
                    };
                    new_node.li_attr = $.extend(true, new_node.li_attr, data);
                    root_node_array.push(new_node);
                }
                var tree = element.jstree(
                    { 
                        'core' : 
                        {
                            "check_callback" :true,
                            'data' :root_node_array,
                            "plugins" : [ "types","contextmenu"]
                        }
                    });
                tree.bind('select_node.jstree', function() {
                    $timeout(function() {
                        var n = element.jstree('get_selected', true);
                        if(n) {
                            n = n[0];
                            scope.selectedNode = n.li_attr;
                            //scope.selectedNode.id = n.id;
                            //scope.selectedNode.li_attr = n.li_attr;
                            //scope.selectedNode.text = n.text;
                        }
                        if($('#'+n.id).closest("li").children("ul").length == 0) {
                            var childRequest = $http({
                                method: "GET",
                                url:attrs.childUrl+'/'+n.id,
                                headers: {'Content-Type': 'application/json; charset=utf-8'}
                            });
                            childRequest.success(function (data) {
                                for(var i=0; i< data.length;i++){
                                    var new_node = {
                                        'text': data[i].name,
                                        'id': data[i].id,
                                        'li_attr': ""
                                    };
                                    new_node.li_attr = $.extend(true, new_node.li_attr, data[i]);
                                    element.jstree(true).create_node(n, new_node);
                                }
                                element.jstree('open_node', n);
                            });
                            childRequest.error(function () {
                                console.error('jstree directive child url request failed. Url = ' + attrs.childUrl);
                            });
                        }
                    });
                });
            });
            request.error(function() {
                console.error('jstree directive root url request failed. Url = ' + attrs.rootUrl);
            });
        }
    };
});