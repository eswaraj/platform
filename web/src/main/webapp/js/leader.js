/* ng-infinite-scroll - v1.0.0 - 2013-02-23 */
var mod;mod=angular.module("infinite-scroll",[]),mod.directive("infiniteScroll",["$rootScope","$window","$timeout",function(i,n,e){return{link:function(t,l,o){var r,c,f,a;return n=angular.element(n),f=0,null!=o.infiniteScrollDistance&&t.$watch(o.infiniteScrollDistance,function(i){return f=parseInt(i,10)}),a=!0,r=!1,null!=o.infiniteScrollDisabled&&t.$watch(o.infiniteScrollDisabled,function(i){return a=!i,a&&r?(r=!1,c()):void 0}),c=function(){var e,c,u,d;return d=n.height()+n.scrollTop(),e=l.offset().top+l.height(),c=e-d,u=n.height()*f>=c,u&&a?i.$$phase?t.$eval(o.infiniteScroll):t.$apply(o.infiniteScroll):u?r=!0:void 0},n.on("scroll",c),t.$on("$destroy",function(){return n.off("scroll",c)}),e(function(){return o.infiniteScrollImmediateCheck?t.$eval(o.infiniteScrollImmediateCheck)?c():void 0:c()},0)}}}]);

var complaintsApp = angular.module('complaintsApp', ['infinite-scroll']);

complaintsApp.controller('complaintsController', function ($scope, $http) {
    var allComplaints = [];
    var current = 0;
	var scrollbusy = false;
    $scope.positions = {};
    $scope.complaints = [];
    $scope.selectedPosition = {};
    $scope.selectedCategory = null;
    $scope.categories = {};
	$scope.showDetailsAndMarkViewed = function (event, complaint) {
        //var element = $(event.currentTarget);
        //var innerdiv = $(element.next( ".innerdiv-list-row" ));
        //innerdiv.toggleClass("innerdiv-box-shadow").fadeToggle(500);
        //element.find(".innerblock .glyph_right_float" ).toggleClass("glyphicon-collapse-up");
        complaint.showMode = !complaint.showMode;

        if (!complaint.viewed) {
            var viewedRequest = $http({
                method: "POST",
                url:"/ajax/complaint/leader/view",
                data: {
                    'politicalAdminId' : $scope.selectedPosition.id,
                    'complaintId' : complaint.id
                },
                headers: {'Content-Type': 'application/json; charset=utf-8'}
            });
            viewedRequest.success(function (data) {
                complaint.viewed = true;
            });
            viewedRequest.error(function () {
                console.error('Request failed for /ajax/complaint/leader/view');
            });
        }
    };
    $scope.label = function (positionType, locationName) {
        return positionType + " of " + locationName;
    };
    $scope.onStatusSelected = function () {};
    $scope.onCategorySelected = function (category) {
        $scope.selectedCategory = category;
        current = 0;
        $scope.complaints = [];
        allComplaints = [];
        $scope.getNext();
    };
    $scope.onPositionSelected = function () {
        current = 0;
        $scope.complaints = [];
        allComplaints = [];
        $scope.getNext();
    };
    $scope.onRefresh = function () {
        current = 0;
        $scope.complaints = [];
        allComplaints = [];
        $scope.getNext();
    };
    $scope.getNext = function () {
		if (scrollbusy) return;
		scrollbusy = true;
        var categoryString = $scope.selectedCategory ? "/" + $scope.selectedCategory.id : "";
                var complaintRequest = $http({
                    method: "GET",
                    url:'/ajax/complaint/leader/' + $scope.selectedPosition.id + categoryString + '/?page=' + (current+1),
                    headers: {'Content-Type': 'application/json; charset=utf-8'}
                });
                complaintRequest.success(function (data) {
                    if(data.length > 0) {
                        //get comments for all fetched complaints
                        data.forEach(function (value, index, array) {
                            value.showMode = false;
                            var commentRequest = $http({
                                method: "GET",
                                url:'/ajax/complaint/' + value.id + '/comments?order=DESC',
                                headers: {'Content-Type': 'application/json; charset=utf-8'}
                            });
                            commentRequest.success(function (resp) {
                                array[index].comments = resp;
                            });
                            commentRequest.error(function () {
                                console.error("/ajax/complaint/'" + value.id + "'/comments?count=5 failed");
                            });
                        });
                        current = current + 1;
						scrollbusy = false;
						allComplaints = allComplaints.concat(data);
                        $scope.complaints = allComplaints;
                    }
                });
                complaintRequest.error(function () {
                    console.error("Complaint request failed");
                });
    };    
    //Get all political positions
    var positionRequest = $http({
        method: "GET",
        url:'/ajax/leader/positions/',
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });
    positionRequest.success(function (data) {
        $scope.positions = data;
        if($scope.positions.length == 1) {
            $scope.selectedPosition = $scope.positions[0];
            $scope.onPositionSelected();
        }
    });
    positionRequest.error(function () {
        console.error('Could not get positions for the leader');
    });
    
    //Get all categories for filter
    var categoryRequest = $http({
        method: "GET",
        url:'/ajax/categories/',
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });
    categoryRequest.success(function (data) {
        $scope.categories = data;
    });
    categoryRequest.error(function () {
        console.error('Could not get categories');
    });
});

complaintsApp.filter('rootCategory', function () {
    return function (categories) {
        var out = "";
        categories.forEach(function (value, index, array) {
            if (value.root) {
                out = value.name;
            }
        });
        return out;
    };
});

complaintsApp.filter('subCategory', function () {
    return function (categories) {
        var out = "";
        categories.forEach(function (value, index, array) {
            if (!value.root) {
                out = value.name;
            }
        });
        return out;
    };
});

complaintsApp.filter('dateFormatter', function () {
    return function (input) {
        var date = new Date(input);
        out = date.toLocaleString();
        return out;
    };
});

complaintsApp.directive('textcollapse', function () {
    return {
        restrict : 'E',
        transclude : true,
        scope : {},
        template : '<p class="desc elipsis text-content short-text" ng-transclude></p><p class="show-more"><a href="#!">Show more</a></p>',
        link : function (scope, element, attrs) {
            var content = element.find(".text-content");
            var link = element.find("a");
            var visibleHeight = content[0].clientHeight;
            var actualHeight = content[0].scrollHeight - 1;
            if (actualHeight < visibleHeight) {
                link.show();
            } else {
                link.hide();
            }

            link.bind('click', function () {
                content.toggleClass("short-text, full-text", 100);
                var text = link.text() == "Show more" ? "Show less" : "Show more";
                link.text(text);
            });
        }
    };
});

complaintsApp.directive('scrollOnClick', function() {
  return {
    restrict: 'A',
    link: function(scope, $elm) {
      $elm.on('click', function() {
        $("body").animate({scrollTop: $elm.offset().top}, "slow");
      });
    }
  }
});