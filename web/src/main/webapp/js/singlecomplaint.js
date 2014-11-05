var complaintsApp = angular.module('complaintsApp', []);

complaintsApp.controller('complaintsController', function ($scope, $http) {
    //complaintId, loggedIn, totalComments will be populated by server. Can be directly used here.
    var getCount = 10;
    var totalCount = 0;
    $scope.loggedIn = loggedIn;
    $scope.totalComments = totalComments;
    $scope.commentText = "";
    $scope.comments = [];
    $scope.getNext = function () {
        totalCount = totalCount + getCount;
        var commentRequest = $http({
            method: "GET",
            url:'/ajax/complaint/' + complaintId + '/comments?count=' + totalCount + '&order=DESC',
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });
        commentRequest.success(function (resp) {
            $scope.comments = resp;
            totalCount = totalCount - getCount + resp.length;
        });
        commentRequest.error(function () {
            console.error("/ajax/complaint/'" + complaintId + "'/comments?count=5 failed");
        });
    };
    $scope.saveComment = function () {
        var commentRequest = $http({
            method: "POST",
            url:"/ajax/complaint/user/comment",
            data: {
                'complaintId' : complaintId,
                'commentText' : $scope.commentText
            },
            headers: {'Content-Type': 'application/json; charset=utf-8'}
        });
        commentRequest.success(function (data) {
            $scope.commentText = "";
            $scope.comments = $scope.comments || [];
            $scope.comments.unshift(data);
            totalCount = totalCount + 1;
            $scope.totalComments = $scope.totalComments + 1;
        });
        commentRequest.error(function () {
            console.error('Request failed for /ajax/complaint/leader/comment');
        });
    };
    $scope.getNext();
});

complaintsApp.filter('dateFormatter', function () {
    return function (input) {
        var date = new Date(input);
        out = date.toLocaleString();
        return out;
    };
});