'use strict';

/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.directives = angular.module("lumens-directives", []);
Lumens.directives.directive("dynamicPropertyForm", function () {
    return {
        restrict: 'A',
        link: function ($scope, element, attr) {
            $scope.$watch(function () {
                return $scope[attr.dynamicPropertyForm];
            }, function (compiledTmpl) {
                element.empty();
                if (compiledTmpl) {
                    element.append(compiledTmpl);
                }
            });
        }
    };
});
Lumens.directives.directive("dynamicFormatList", function () {
    return {
        restrict: 'A',
        link: function ($scope, element, attr) {
            $scope.$watch(function () {
                return $scope[attr.dynamicFormatList];
            }, function (component) {
                element.empty();
                buildDataFormatList(element, component);
            });
        }
    };
});
Lumens.directives.directive("dynamicTransformationList", function () {
    return {
        restrict: 'A',
        link: function ($scope, element, attr) {
            $scope.$watch(function () {
                return $scope[attr.dynamicTransformationList];
            }, function (component) {
                element.empty();
                buildTransformationList(element, component);
            });
        }
    };
});
Lumens.directives.directive("formatList", ['FormatBuilder', function (FormatBuilder) {
        return {
            restrict: 'A',
            link: function ($scope, element, attr) {
                $scope.$watch(function () {
                    return $scope[attr.formatList];
                }, function (formatList) {
                    element.empty();
                    FormatBuilder.build(element, formatList);
                });
            }
        };
    }]);
Lumens.directives.directive("ruleTree", function (RuleBuilder) {
    return {
        restrict: 'E',
        replace: true,
        template: "<div></div>",
        link: function ($scope, element, attr) {
            element.droppable({
                greedy: true,
                accept: ".lumens-tree-node",
                drop: function (event, ui) {
                    var node = $.data(ui.draggable.get(0), "tree-node-data");
                    LumensLog.log("Dropped", node);
                    // TODO compare root
                    // TODO append to exist correctly
                    if (element.children().length > 0) {
                        // TODO try to append to the rule tree
                        console.log("Mapping editing, rule should be appended to existing rule tree:", $scope[attr.ruleData]);
                        RuleBuilder.appendFromData($scope, element, node);
                        //$scope.$broadcast("NewRule", null);
                        // TODO if the root name doesn't match, renew the rule tree
                    } else {
                        RuleBuilder.buildFromData($scope, element, node);
                    }
                }
            });
            $scope.$watch(attr.ruleData, function (ruleData) {
                RuleBuilder.buildTreeFromRuleEntry($scope, element, ruleData);
            });
        }
    };
});

Lumens.directives.directive("scriptEditor", function () {
    return {
        restrict: 'E',
        link: function ($scope, element, attr) {
            if (element.find("#scriptEditor").length === 0)
                element.append('<div id="scriptEditor" class="lumens-rule-script" />');
            $scope[attr.scriptEditorHolder] = CodeMirror(element.find("#scriptEditor").get(0), {
                mode: "javascript",
                lineNumbers: true,
                dragDrop: true,
                theme: "eclipse"
            });

            var unbind = $scope.$watch(function () {
                return $scope[attr.scriptVar];
            }, function (scriptEditorText) {
                console.log("scriptEditorText:", scriptEditorText);
                if (!scriptEditorText)
                    scriptEditorText = "";
                $scope[attr.scriptEditorHolder].setValue(scriptEditorText);
            });
            $scope.$on('$destroy', function () {
                console.log("$destroy");
                unbind();
            });
        }
    };
});