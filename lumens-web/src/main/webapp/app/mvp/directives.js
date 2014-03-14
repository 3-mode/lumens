/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.directives = angular.module("lumens-directives", []);
Lumens.directives.directive("dynamicPropertyForm", function() {
    return function(scope, element, attr) {
        var bindVar = attr.dynamicPropertyForm;
        function getStringValue() {
            return scope[bindVar];
        }
        scope.$watch(getStringValue, function bindComponentFormTmplWatchAction(compiledTmpl) {
            if (compiledTmpl) {
                element.empty();
                element.append(compiledTmpl);
            }
        });
    };
});