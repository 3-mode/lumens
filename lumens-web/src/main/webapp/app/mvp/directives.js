'use strict';

/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.directives = angular.module("lumens-directives", []);
Lumens.directives.directive("dynamicPropertyForm", function() {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            var bindVar = attr.dynamicPropertyForm;
            function getStringValue() {
                return scope[bindVar];
            }
            scope.$watch(getStringValue, function(compiledTmpl) {
                if (compiledTmpl) {
                    element.empty();
                    element.append(compiledTmpl);
                }
            });
        }
    };
});
Lumens.directives.directive("dynamicFormatList", function() {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            var bindVar = attr.dynamicFormatList;
            function getStringValue() {
                return scope[bindVar];
            }
            scope.$watch(getStringValue, function(component) {
                console.log("dynamicFormatList: ", element.parent().attr("display"));
                element.empty();
                buildDataFormatList(element, component);
                console.log("Current component: ", component, element);
            });
        }
    };
});