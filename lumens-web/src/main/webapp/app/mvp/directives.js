'use strict';

/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.directives = angular.module("lumens-directives", []);
Lumens.directives.directive("dynamicPropertyForm", function() {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            function getStringValue() {
                return scope[attr.dynamicPropertyForm];
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
            function getStringValue() {
                return scope[attr.dynamicFormatList];
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
            function getStringValue() {
                return scope[attr.dynamicTransformationList];
            }
            scope.$watch(getStringValue, function(component) {
                element.empty();
                buildTransformationList(element, component);
            });
        }
    };
});
Lumens.directives.directive("formatList", function() {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            function getStringValue() {
                return scope[attr.formatList];
            }
            scope.$watch(getStringValue, function(formatList) {
                element.empty();
                if (formatList) {
                    console.log("Format list:", formatList);
                    buildDataFormatList(element, formatList);
                }
            });
        }
    };
});

Lumens.directives.directive("ruleEntry", function() {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            function getStringValue() {
                return scope[attr.ruleEntry];
            }
            scope.$watch(getStringValue, function(ruleEntry) {
                element.empty();
                if (ruleEntry) {
                    console.log("Rule list:", ruleEntry);
                    buildTransformRuleTree(element, ruleEntry);
                }
            });
        }
    };
});