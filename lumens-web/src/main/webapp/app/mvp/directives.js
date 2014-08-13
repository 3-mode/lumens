'use strict';

/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.directives = angular.module("lumens-directives", []);
Lumens.directives.directive("dynamicPropertyForm", function() {
    return {
        restrict: 'A',
        link: function($scope, element, attr) {
            function getStringValue() {
                return $scope[attr.dynamicPropertyForm];
            }
            $scope.$watch(getStringValue, function(compiledTmpl) {
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
        link: function($scope, element, attr) {
            function getStringValue() {
                return $scope[attr.dynamicFormatList];
            }
            $scope.$watch(getStringValue, function(component) {
                element.empty();
                buildDataFormatList(element, component);
            });
        }
    };
});
Lumens.directives.directive("dynamicTransformationList", function() {
    return {
        restrict: 'A',
        link: function($scope, element, attr) {
            function getStringValue() {
                return $scope[attr.dynamicTransformationList];
            }
            $scope.$watch(getStringValue, function(component) {
                element.empty();
                buildTransformationList(element, component);
            });
        }
    };
});
Lumens.directives.directive("formatList", function() {
    return {
        restrict: 'A',
        link: function($scope, element, attr) {
            function getStringValue() {
                return $scope[attr.formatList];
            }
            $scope.$watch(getStringValue, function(formatList) {
                element.empty();
                if (formatList) {
                    console.log("Format list:", formatList);
                    buildDataFormatList(element, formatList);
                }
            });
        }
    };
});

Lumens.directives.directive("ruleEntity", function() {
    return {
        restrict: 'A',
        link: function($scope, element, attr) {
            function getStringValue() {
                return $scope[attr.ruleEntity];
            }
            $scope.$watch(getStringValue, function(ruleEntity) {
                element.empty();
                if (ruleEntity) {
                    console.log("Rule list:", ruleEntity);
                    buildTransformRuleTree(element, ruleEntity);
                }
            });
        }
    };
});

Lumens.directives.directive("scriptEditor", function() {
    return {
        restrict: 'E',
        link: function($scope, element, attr) {
            if (element.find("#scriptEditor").length === 0)
                element.append('<div id="scriptEditor" class="lumens-rule-script" />');
            $scope[attr.scriptEditorHolder] = CodeMirror(element.find("#scriptEditor").get(0), {
                mode: "javascript",
                lineNumbers: true,
                dragDrop: true,
                theme: "eclipse"
            });
            function getStringValue() {
                return $scope[attr.scriptVar];
            }
            var unbind = $scope.$watch(getStringValue, function(scriptEditorText) {
                console.log("scriptEditorText:", scriptEditorText);
                if (!scriptEditorText)
                    scriptEditorText = "";
                $scope[attr.scriptEditorHolder].setValue(scriptEditorText);
            });
            $scope.$on('$destroy', function() {
                console.log("$destroy");
                unbind();
            });
        }
    };
});