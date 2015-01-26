'use strict';

/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.directives.directive("dynamicPropertyForm", function () {
    return {
        restrict: 'A',
        link: function ($scope, $element, attr) {
            $scope.$watch(function () {
                return $scope[attr.dynamicPropertyForm];
            }, function (compiledTmpl) {
                $element.empty();
                if (compiledTmpl) {
                    $element.append(compiledTmpl);
                }
            });
        }
    };
});
Lumens.directives.directive("dynamicFormatList", function () {
    return {
        restrict: 'A',
        link: function ($scope, $element, attr) {
            $scope.$watch(function () {
                return $scope[attr.dynamicFormatList];
            }, function (component) {
                $element.empty();
                buildDataFormatList($element, component);
            });
        }
    };
});
Lumens.directives.directive("dynamicTransformationList", function () {
    return {
        restrict: 'A',
        link: function ($scope, $element, attr) {
            $scope.$watch(function () {
                return $scope[attr.dynamicTransformationList];
            }, function (component) {
                $element.empty();
                buildTransformationList($element, component);
            });
        }
    };
});
Lumens.directives.directive("formatList", function (FormatService) {
    return {
        restrict: 'A',
        link: function ($scope, $element, attr) {
            $scope.$watch(function () {
                return $scope[attr.formatList];
            }, function (formatList) {
                $element.empty();
                if (formatList)
                    formatList.formatTree = FormatService.build($scope, attr.formatList, $element, formatList);
            });
        }
    };
});
Lumens.directives.directive("elementData", function (ElementService) {
    return {
        restrict: 'A',
        link: function ($scope, $element, attr) {
            $scope.$watch(function () {
                return $scope[attr.elementData];
            }, function (elementData) {
                $element.empty();
                if (elementData)
                    elementData.elementTree = ElementService.build($scope, attr.elementData, $element, elementData);
            });
        }
    };
});
Lumens.directives.directive("ruleTree", function (RuleTreeService) {
    return {
        restrict: 'E',
        replace: true,
        template: "<div></div>",
        link: function ($scope, $element, attr) {
            $element.droppable({
                greedy: true,
                accept: ".lumens-tree-node",
                drop: function (event, ui) {
                    var node = $.data(ui.draggable.get(0), "tree-node-data");
                    if (!node || !node.direction || node.direction === "OUT")
                        return;
                    if ($element.children().length > 0)
                        RuleTreeService.appendFromData($scope, $element, node);
                    else
                        RuleTreeService.buildFromData($scope, $element, node);
                }
            });
            $scope.$on("RuleChanged", function (evt, data) {
                $scope.$apply(function () {
                    console.log("In RuleChanged apply", data);
                    $scope[attr.ruleData] = data;
                });
            });
            $scope.$watch(attr.ruleData, function (ruleData) {
                RuleTreeService.clear();
                if (ruleData)
                    ruleData.ruleTree = RuleTreeService.buildTreeFromRuleEntry($scope, $element, ruleData);
            });
        }
    };
});
Lumens.directives.directive("scriptEditor", function (RuleTreeService) {
    return {
        restrict: 'E',
        replace: true,
        template: "<div></div>",
        link: function ($scope, $element, attr) {
            var codeMirror = CodeMirror($element.get(0), {
                mode: "javascript",
                lineNumbers: true,
                dragDrop: true,
                theme: "eclipse"
            });
            $element.droppable({
                drop: function (evt, ui) {
                    console.log("CodeMirror drop: ", codeMirror, evt, ui)
                    var node = $.data(ui.draggable.get(0), "tree-node-data");
                    if (!node || !node.direction || node.direction !== "OUT")
                        return;
                    codeMirror.replaceSelection('@' + RuleTreeService.getFullPath(node));
                }
            });
            $scope.$on("SelectRuleItem", function (evt, currentRuleItem) {
                console.log("SelectRuleItem", currentRuleItem);
                var script = currentRuleItem.getScript() ? currentRuleItem.getScript() : "";
                codeMirror.setValue(script);
            });
            $scope.$on("ApplyScriptToRuleItem", function () {
                $scope.$parent.$broadcast("ApplyScript", codeMirror.getValue());
            });
            $scope.$on("ScriptEditorDisplay", function (evt, mode) {
                console.log("RuleEditorDisplay", $element);
                if (mode === "show") {
                    $element.show();
                    if ($scope.selectRuleItem) {
                        var currentRuleItem = $scope.selectRuleItem;
                        var script = currentRuleItem.getScript() ? currentRuleItem.getScript() : "";
                        codeMirror.setValue(script);
                    }
                }
                else if (mode === "hide")
                    $element.hide();
            });

            var unbind = $scope.$watch(function () {
                return $scope[attr.scriptVar];
            }, function (scriptEditorText) {
                console.log("scriptEditorText:", scriptEditorText);
                if (!scriptEditorText)
                    scriptEditorText = "";
                codeMirror.setValue(scriptEditorText);
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
        link: function ($scope, $element, attr) {
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
                $element.empty();
                console.log("Current format prop", propData);
                if (propData) {
                    var propHtml = '<table class="table table-bordered">'
                    for (var i in propData) {
                        propHtml += '<tr><td><b>' + propData[i].name + '</b></td><td>' + propData[i].value + '</td></tr>';
                    }
                    propHtml += '</table>';
                    $element.append(propHtml);
                }
                else {
                    $element.append('<div class="lumens-format-prop-value"><b>No property</b></div>');
                }
            });
        }
    };
});
Lumens.directives.directive("scriptPanel", function ($compile, TemplateService, RuleTreeService) {
    return {
        restrict: 'E',
        replace: true,
        template: '<div></div>',
        link: function ($scope, $element, attr) {
            $scope.scriptConfigList = new Lumens.List($element).configure({
                activeIndex: 1,
                IdList: [
                    "id_foreach",
                    "id_rule_script"
                ],
                titleList: [
                    '<i class="lumens-icon-rule-config-name lumens-icon-gap"></i>' + $scope.i18n.id_rule_foreach,
                    '<i class="lumens-icon2-script-config-name lumens-icon-gap"></i>' + $scope.i18n.id_rule_script
                ],
                buildContent: function (itemContent, id, isExpand, title) {
                    if (isExpand) {
                        if (id === "id_foreach") {
                            $compile($(TemplateService.get("app/templates/designer/foreach_tmpl.html")).appendTo(itemContent))($scope);
                            // #Workaround It is used to resolve the bind variable not update value
                            $scope.$apply();
                        }
                        else if (id === "id_rule_script") {
                            $compile($('<script-editor class="lumens-rule-script-panel"></script-editor>').appendTo(itemContent))($scope);
                        }
                    }
                }
            });
            $scope.$on("SelectRuleItem", function (evt, currentRuleItem) {
                console.log("on SelectRuleItem", currentRuleItem);
                $scope.$apply(function () {
                    $scope.forEachList = RuleTreeService.getForeachList(currentRuleItem);
                });
            });
            $scope.$on("ApplyScriptToRuleItem", function () {
               $scope.$parent.$broadcast("ApplyForeach", $scope.forEachList);
            })
        }
    };
});