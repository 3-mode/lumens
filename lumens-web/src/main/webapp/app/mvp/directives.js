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
Lumens.directives.directive("formatList", function (FormatBuilder) {
    return {
        restrict: 'A',
        link: function ($scope, element, attr) {
            $scope.$watch(function () {
                return $scope[attr.formatList];
            }, function (formatList) {
                element.empty();
                if (formatList)
                    formatList.formatTree = FormatBuilder.build($scope, attr.formatList, element, formatList);
            });
        }
    };
});
Lumens.directives.directive("ruleTree", function (RuleTreeBuilder) {
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
                    if (!node || !node.direction || node.direction === "OUT")
                        return;
                    if (element.children().length > 0)
                        RuleTreeBuilder.appendFromData($scope, element, node);
                    else
                        RuleTreeBuilder.buildFromData($scope, element, node);
                }
            });
            $scope.$on("RuleChanged", function (evt, data) {
                $scope.$apply(function () {
                    console.log("In RuleChanged apply", data);
                    $scope[attr.ruleData] = data;
                });
            });
            $scope.$watch(attr.ruleData, function (ruleData) {
                RuleTreeBuilder.clear();
                if (ruleData)
                    ruleData.ruleTree = RuleTreeBuilder.buildTreeFromRuleEntry($scope, element, ruleData);
            });
        }
    };
});
Lumens.directives.directive("scriptEditor", function (RuleTreeBuilder) {
    return {
        restrict: 'E',
        replace: true,
        template: "<div></div>",
        link: function ($scope, element, attr) {
            var codeMirror = $scope[attr.scriptEditorHolder] = CodeMirror(element.get(0), {
                mode: "javascript",
                lineNumbers: true,
                dragDrop: true,
                theme: "eclipse"
            });
            element.droppable({
                drop: function (evt, ui) {
                    console.log("CodeMirror drop: ", codeMirror, evt, ui)
                    var node = $.data(ui.draggable.get(0), "tree-node-data");
                    if (!node || !node.direction || node.direction !== "OUT")
                        return;
                    codeMirror.replaceSelection('@' + RuleTreeBuilder.getFullPath(node));
                }
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
Lumens.directives.directive("formatProp", function () {
    return {
        restrict: 'E',
        replace: true,
        template: '<div></div>',
        link: function ($scope, element, attr) {
            $scope.$on("ClickFormatItem", function (evt, info) {
                $scope.$apply(function () {
                    $scope[info.name].select_format_prop = info.node.data.property;
                    console.log("In ClickFormatItem apply", info);
                });
            });
            $scope.$watch(function () {
                var accessString = attr.propData.split(".");
                var value = accessString[0] ? $scope[accessString[0]] : null;
                return value ? value[accessString[1]] : null;
            }, function (propData) {
                element.empty();
                console.log("Current format prop", propData);
                if (propData) {
                    var propHtml = '<table class="table table-bordered">'
                    for (var i in propData) {
                        propHtml += '<tr><td><b>' + propData[i].name + '</b></td><td>' + propData[i].value + '</td></tr>';
                    }
                    propHtml += '</table>';
                    element.append(propHtml);
                }
                else {
                    element.append('<div class="lumens-format-prop-value"><b>No property</b></div>');
                }
            });
        }
    };
});