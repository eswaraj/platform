var complaintsApp = angular.module('complaintsApp', []);

complaintsApp.controller('complaintsController', function ($scope, $http) {
    var getCount = 10;
    var total = 0;
    var current = 0;
    var allComplaints = [];
    $scope.positions = {};
    $scope.complaints = [];
    $scope.statuses = ['Pending', 'Viewed', 'Duplicate', 'Assigned', 'InProgress', 'InReview', 'Done', 'Unfinished', 'Esclated'];
    $scope.selectedPosition = {};
    $scope.selectedStatus = {};
    $scope.newComment = {};
    $scope.addComment = function (complaint) {
        var commentRequest = $http({
            method: "POST",
            url:"/ajax/complaint/leader/comment",
            data: {
                'politicalAdminId' : $scope.selectedPosition.id,
                'complaintId' : complaint.id,
                'commentText' : complaint.commentText
            },
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });
        commentRequest.success(function (data) {
            //complaint.commentText = $scope.selectedStatus;
        });
        commentRequest.error(function () {
            console.error('Request failed for /ajax/complaint/leader/comment');
        });
    };
    $scope.showTab = function (event) {
        var element = $(event.currentTarget);
        element.tab('show');
    };
    $scope.saveStatus = function (complaint) {
        var statusRequest = $http({
            method: "POST",
            url:"/ajax/complaint/leader/status",
            data: {
                'politicalAdminId' : $scope.selectedPosition.id,
                'complaintId' : complaint.id,
                'status' : $scope.selectedStatus
            },
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });
        statusRequest.success(function (data) {
            complaint.politicalAdminStatus = $scope.selectedStatus;
        });
        statusRequest.error(function () {
            console.error('Request failed for /ajax/complaint/leader/status');
        });
    };
    $scope.showDetailsAndMarkViewed = function (event, complaint) {
        var element = $(event.currentTarget);
        var innerdiv = $(element.next( ".innerdiv-list-row" ));
        innerdiv.toggleClass("innerdiv-box-shadow").fadeToggle(500);
        element.find(".innerblock .glyph_right_float" ).toggleClass("glyphicon-collapse-up");
        
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
    $scope.onPositionSelected = function () {
        total = 0;
        current = 0;
        $scope.complaints = [];
        allComplaints = [];
        $scope.getNext();
    };
    $scope.getNext = function () {
        if(current == total) {
            var complaintRequest = $http({
                method: "GET",
                url:'/ajax/complaint/leader/' + $scope.selectedPosition.id + '/?page=' + (current+1),
                headers: {'Content-Type': 'application/json; charset=utf-8'}
            });
            complaintRequest.success(function (data) {
                allComplaints = allComplaints.concat(data);
                total = total + 1;
                current = current + 1;
                $scope.complaints = allComplaints.slice((current-1)*getCount, current*getCount);

            });
            complaintRequest.error(function () {
                console.error("Complaint request failed");
            });
        }
        else {
            current = current + 1;
            $scope.complaints = allComplaints.slice((current-1)*getCount, current*getCount);
        }
    };
    $scope.getPrevious = function () {
        if (current == 1) {
            return;
        }
        current = current - 1;
        $scope.complaints = allComplaints.slice((current-1)*getCount, current*getCount);
    };
    //Get all political positions
    var positionRequest = $http({
        method: "GET",
        url:'/ajax/leader/positions/',
        headers: {'Content-Type': 'application/json; charset=utf-8'}
    });
    positionRequest.success(function (data) {
        $scope.positions = data;
    });
    positionRequest.error(function () {
        console.error('Could not get positions for the leader');
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
        out = date.toString();
        return out;
    };
});

complaintsApp.directive('textcollapse', function () {
    return {
        restrict : 'E',
        transclude : true,
        scope : {},
        template : '<p class="desc elipsis text-content short-text" ng-transclude></p><p class="show-more"><a href="#">Show more</a></p>',
        link : function (scope, element, attrs) {
            var content = element.find(".text-content");
            var link = element.find("a");
            var visibleHeight = content.clientHeight;
            var actualHeight = content.scrollHeight - 1;
            if (actualHeight < visibleHeight) {
                link.show();
            } else {
                link.hide();
            }
            
            link.bind('click', function () {
                content.toggleClass("short-text, full-text", 100);
                var text = link.text() == "Show More" ? "Show less" : "Show More";
                link.text(text);
            });
        }
    };
});