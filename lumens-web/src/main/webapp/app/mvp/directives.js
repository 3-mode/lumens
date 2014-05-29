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
                element.empty();
                if (compiledTmpl) {
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
                element.empty();
                buildDataFormatList(element, component);
            });
        }
    };
});
Lumens.directives.directive("dynamicTransformationList", function() {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            var bindVar = attr.dynamicTransformationList;
            function getStringValue() {
                return scope[bindVar];
            }
            scope.$watch(getStringValue, function(component) {
                element.empty();
                buildTransformationList(element, component);
            });
        }
    };
});