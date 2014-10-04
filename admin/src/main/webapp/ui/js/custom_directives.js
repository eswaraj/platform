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
            }
                );
        }
    };
}
            );