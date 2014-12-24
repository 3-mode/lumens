'use strict';

/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.services = angular.module('lumens-services', ['ngResource']);
// Services
Lumens.services.factory('DesignNavMenu', function ($resource) {
    return $resource("app/config/json/desgin_nav_menu.json", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('DesignButtons', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/design_command_tmpl.html");
});
Lumens.services.factory('ErrorTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/error_tmpl.html");
});
Lumens.services.factory('WarningTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/warning_tmpl.html");
});
Lumens.services.factory('SuccessTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/success_tmpl.html");
});
Lumens.services.factory('SmallMessageTmplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/small_message_tmpl.html");
});
Lumens.services.factory('PropFormTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/property_form_tmpl.html");
});
Lumens.services.factory('TransformListTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/transform_list_tmpl.html");
});
Lumens.services.factory('TransformEditTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/transform_edit_tmpl.html");
});
Lumens.services.factory('ProjectListModal', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/project_list_modal_tmpl.html");
});
Lumens.services.factory('ProjectCreateModal', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/project_create_modal_tmpl.html");
});
Lumens.services.factory('FormatRegistryModal', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/format_registry_modal_tmpl.html");
});
Lumens.services.factory('RuleRegistryModal', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/rule_registry_modal_tmpl.html");
});
Lumens.services.factory('ScriptEditTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/script_edit_tmpl.html");
});
Lumens.services.factory('DatasourceCategory', function ($resource) {
    return $resource("rest/category/component", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('InstrumentCategory', function ($resource) {
    return $resource("app/config/json/instrument_category.json", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('ProjectList', function ($resource) {
    return $resource("rest/project", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('ProjectById', function ($resource) {
    return $resource("rest/project/:project_id", {project_id: '@project_id'}, {
        get: {method: 'GET', isArray: false},
        operate: {method: 'POST', isArray: false}
    });
});
Lumens.services.factory('ProjectSave', function ($http) {
    return {
        save: function (projectJSON, onResponse) {
            var projectData = {
                action: projectJSON.projectId ? "update" : "create",
                content: JSON.stringify({project: projectJSON.project})
            };
            LumensLog.log("Saved data:", projectData);
            $http.post(projectJSON.projectId ? "rest/project/" + projectJSON.projectId : "rest/project", projectData).success(onResponse);
        }
    };
});
Lumens.services.factory('FormatList', function ($resource) {
    return $resource("rest/project/:project_id/format?component_id=:component_id&direction=:direction", {}, {
        get: {method: 'GET', isArray: false},
        getIN: {method: 'GET', params: {direction: 'IN'}, isArray: false},
        getOUT: {method: 'GET', params: {direction: 'OUT'}, isArray: false}
    });
});
Lumens.services.factory('JobList', function ($resource) {
    return $resource("app/mock/json/job_list.json?pagesize=:pagesize", {}, {
        get: {method: 'GET', params: {pagesize: '50'}, isArray: false}
    });
});
Lumens.services.factory('JobConfig', function ($resource) {
    return $resource("app/config/json/job_config.json?pagesize=:pagesize", {});
});
Lumens.services.factory('FormatByPath', function ($resource) {
    return $resource("rest/project/:project_id/format?component_id=:component_id&format_name=:format_name&format_path=:format_path&direction=:direction", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('ManageNavMenu', function ($resource) {
    return $resource("app/config/json/manage_nav_menu.json", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('CpuCount', function ($resource) {
    return $resource("rest/server_resources/cpu_perc", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('CpuPerc', function ($resource) {
    return $resource("rest/server_resources/cpu_perc", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('CpuCount', function ($resource) {
    return $resource("rest/server_resources/cpu_count", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('MemPerc', function ($resource) {
    return $resource("rest/server_resources/mem_perc", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('Disk', function ($resource) {
    return $resource("rest/server_resources/disk", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('SyncGet', function () {
    return {
        get: function (url, contentType) {
            var result;
            $.ajax({
                async: false,
                url: url,
                contentType: contentType
            }).done(function (data) {
                result = data;
            });
            return result;
        }
    };
});
Lumens.services.factory('jSyncHtml', function () {
    return {
        get: function (items) {
            for (var i = 0; i < items.length; ++i) {
                if (items[i].html_url) {
                    $.ajax({
                        async: false,
                        url: items[i].html_url,
                        contentType: "plain/text"
                    }).done(function (data) {
                        items[i].html = data;
                    });
                }
            }
        }
    };
});
Lumens.services.factory('FormatBuilder', ['FormatByPath', function (FormatByPath) {
        return {
            isField: function (form) {
                return form === "Field";
            },
            isArray: function (form) {
                return form === "ArrayOfStruct" || form === "ArrayOfField";
            },
            getPath: function (pathTokens) {
                var formatPath = "";
                for (var i = 1; i < pathTokens.length; ++i) {
                    if (formatPath.length > 0)
                        formatPath += '.';
                    formatPath += pathTokens[i].name;
                }
                return formatPath;
            },
            build: function ($element, formatList) {
                console.log("Format List: ", formatList);
                if (formatList) {
                    var __this = this;
                    var formatEntityList = formatList.format_entity;
                    var projectId = formatList.project_id;
                    var componentId = formatList.component_id;
                    var direction = formatList.direction;
                    var formatTree = new Lumens.Tree($element).configure({
                        handler: function (parentNode) {
                            var entryList = [];
                            for (var index in formatEntityList) {
                                var format = formatEntityList[index].format;
                                entryList.push({
                                    label: format.type === "None" ? format.name : format.name + "&nbsp;&nbsp;[" + format.type + "]",
                                    name: format.name,
                                    nodeType: __this.isField(format.form) ? "file" : (__this.isArray(format.form) ? "folderset" : "folder"),
                                    data: format
                                });
                            }
                            parentNode.addEntryList(entryList);
                        },
                        dblclick: function (current, parent) {
                            if (current.hasContent() || !current.isFolder())
                                return;
                            var currentFormat = current.data;
                            if (currentFormat.format) {
                                var nodeList = [];
                                for (var i in currentFormat.format) {
                                    var formatItem = currentFormat.format[i];
                                    nodeList[i] = {
                                        label: formatItem.type === "None" ? formatItem.name : formatItem.name + "&nbsp;&nbsp;[" + formatItem.type + "]",
                                        name: formatItem.name,
                                        nodeType: __this.isField(formatItem.form) ? "file" : (__this.isArray(formatItem.form) ? "folderset" : "folder"),
                                        data: formatItem
                                    };
                                }
                                current.addChildList(nodeList);
                            } else if (!__this.isField(currentFormat)) {
                                var pathTokens = current.getPath();
                                FormatByPath.get({
                                    project_id: projectId,
                                    component_id: componentId,
                                    format_name: pathTokens[0].name,
                                    direction: direction,
                                    format_path: __this.getPath(pathTokens)
                                }, function (result) {
                                    console.log("Children: ", result);
                                    var nodeList = [];
                                    currentFormat.format = result.content.format_entity[0].format.format;
                                    for (var i in currentFormat.format) {
                                        var formatItem = currentFormat.format[i];
                                        nodeList[i] = {
                                            label: formatItem.type === "None" ? formatItem.name : formatItem.name + "&nbsp;&nbsp;[" + formatItem.type + "]",
                                            name: formatItem.name,
                                            nodeType: __this.isField(formatItem.form) ? "file" : (__this.isArray(formatItem.form) ? "folderset" : "folder"),
                                            data: formatItem
                                        };
                                    }
                                    current.addChildList(nodeList);
                                    current.expandContent();
                                });
                            }
                        },
                        draggable: true
                    });

                    return formatTree;
                }
                return null;
            }
        };
    }]
);
Lumens.services.factory('RuleTreeBuilder', ['FormatBuilder', function (FormatBuilder) {
        return {
            appendFromData: function ($scope, parent, node) {
                console.log("appendFromData::node", node);
                var i = 0, path = node.getPath();
                var entryList = this.transformRuleTree.getEntryList();
                var entryNode = entryList.map[path[i].name];
                this.checkRuleWithFormat(entryNode.getName(), path[i].name);
                while (++i < path.length) {
                    var child = entryNode.getChildList().map[path[i].name];
                    if (!child) {
                        entryNode.addChildList([this.buildItemNode(path[i])]);
                        child = entryNode.getChildList().map[path[i].name];
                        entryNode.expandContent();
                    }
                    entryNode = child;
                }
            },
            buildFromData: function ($scope, parent, node) {
                var path = node.getPath();
                console.log("Path", path);
                // Build rule and root rule item
                var i = 0;
                var rootRuleItem = {
                    format_name: path[i].name,
                    transform_rule_item: []
                };
                var transformRule = {
                    name: path[i].name,
                    transform_rule_item: rootRuleItem
                };
                var currentRuleItem = rootRuleItem.transform_rule_item;
                while (++i < path.length) {
                    currentRuleItem[0] = {format_name: path[i].name};
                    if (i < path.length - 1) {
                        currentRuleItem[0].transform_rule_item = [];
                        currentRuleItem = currentRuleItem[0].transform_rule_item;
                    }
                }
                $scope.$broadcast("RuleChanged", {rule_entry: {transform_rule: transformRule}, format_entry: node.getRoot().data});
            },
            buildTreeFromRuleEntry: function ($scope, parent, ruleData) {
                console.log("Rule list:", ruleData);
                var __this = this;
                if (!ruleData)
                    return null;
                // TODO if parent has children, the ruleEntry should be append to
                // TODO else new rule tree, need to buid rule tree following the format defintion
                if (ruleData.rule_entry && ruleData.format_entry) {
                    var ruleEntry = ruleData.rule_entry;
                    var formatEntry = ruleData.format_entry;
                    if (parent.children().length === 0) {
                        this.transformRuleTree = new Lumens.Tree(parent).configure({
                            handler: function (parentNode) {
                                var rootRuleItem = __this.getRootRuleItem(ruleEntry);
                                __this.checkRuleWithFormat(rootRuleItem.format_name, formatEntry.name);
                                var entryList = __this.buildRootRuleItem(rootRuleItem, formatEntry);
                                parentNode.addEntryList(entryList);
                                __this.buildRuleItemChildren(parentNode, rootRuleItem, formatEntry);
                            },
                            click: function (current, parent) {
                                $scope.$broadcast("ClickRuleItem", current);
                            },
                            drop: function (data, current, parent) {
                            },
                            droppable: true
                        });
                        return this.transformRuleTree;
                    } else if (parent.children().length > 0) {
                        // TODO find the parent node for the drag object and append it to the parent node
                        console.log("appended");
                    }
                }
                return null;
            },
            buildRootRuleItem: function (rootRuleItem, formatEntry) {
                return [{
                        label: rootRuleItem.format_name,
                        name: rootRuleItem.format_name,
                        nodeType: FormatBuilder.isField(formatEntry.form) ? "file" : "folder",
                        data: formatEntry
                    }];
            },
            buildRuleItemChildren: function (parentNode, ruleItem, formatEntry) {
                var entryList = parentNode.getEntryList ? parentNode.getEntryList() : parentNode.getChildList();
                var ruleItemNode = entryList.map[ruleItem.format_name];
                if (ruleItem.transform_rule_item) {
                    var ruleItemChildren = ruleItem.transform_rule_item;
                    var nodeList = [];
                    for (var i in ruleItemChildren) {
                        var format = this.getChildFormat(formatEntry.format, ruleItemChildren[i].format_name);
                        if (format)
                            nodeList.push(this.buildItemNode(format, ruleItemChildren[i]));
                    }
                    ruleItemNode.addChildList(nodeList);
                    ruleItemNode.expandContent();
                    for (var i in ruleItemChildren) {
                        var format = this.getChildFormat(formatEntry.format, ruleItemChildren[i].format_name);
                        this.buildRuleItemChildren(ruleItemNode, ruleItemChildren[i], format);
                    }
                }
            },
            getRootRuleItem: function (ruleEntry) {
                return ruleEntry.transform_rule.transform_rule_item;
            },
            getRuleItemByPath: function (path) {
                var i = 0, length = path.length - 1;
                var ruleItem = this.getRootRuleItem();
                var pathString = path[i].name;
                while (++i < length && ruleItem && ruleItem.transform_rule_item) {
                    pathString += "." + path[i].name;
                    for (var j in ruleItem.transform_rule_item)
                        if (ruleItem.transform_rule_item[j].format_name === path[i].name) {
                            ruleItem = ruleItem.transform_rule_item[j];
                            break;
                        }
                }
                if (!ruleItem)
                    throw ("Not found '" + pathString + "'");
                return ruleItem;
            },
            buildItemNode: function (format, ruleItem) {
                var node = {
                    label: format.name,
                    name: format.name,
                    nodeType: FormatBuilder.isField(format.form) ? "file" : (FormatBuilder.isArray(format.form) ? "folderset" : "folder"),
                };
                if (ruleItem && ruleItem.script)
                    node.script = ruleItem.script;
                return node;
            },
            getChildFormat: function (formatList, formatName) {
                for (var i in formatList)
                    if (formatList[i].name === formatName)
                        return formatList[i];
            },
            hasRuleItem: function (ruleItem, name) {
                if (!ruleItem || !ruleItem.transform_rule_item)
                    return false;
                for (var i in ruleItem.transform_rule_item)
                    if (ruleItem.transform_rule_item[i].format_name === name)
                        return true;
                return false;
            },
            checkRuleWithFormat: function (ruleItemName, formatName) {
                if (ruleItemName !== formatName)
                    throw ("Not match with '" + ruleItemName + "' and '" + formatName + "'");
            }
        };
    }]
);
Lumens.services.factory('RuleWithFormatRegister', function () {
    return {
        pathEnding: "+-*/ &|!<>\n\r\t^%=;:?,",
        build: function ($scope) {
            var inSelectedName = $scope.inputSelectedFormatName;
            var ruleRegName = $scope.ruleRegName;
            var outSelectedName = $scope.outputSelectedFormatName;
            LumensLog.log("Rule entity:", $scope.transformRuleEntity);
            var transformRuleEntry = $scope.transformRuleEntity.transformRuleEntry;
            transformRuleEntry.name = ruleRegName;
            transformRuleEntry.source_id = this.isInValidSource($scope) ? "" : $scope.currentUIComponent.getFrom(0).getId();
            transformRuleEntry.target_id = this.isInValidTarget($scope) ? "" : $scope.currentUIComponent.getTo(0).getId();
            transformRuleEntry.source_format_name = $scope.inputFormatRegName ? $scope.inputFormatRegName : ($scope.outputFormatRegName ? $scope.outputFormatRegName : "");
            transformRuleEntry.target_format_name = $scope.outputFormatRegName ? $scope.outputFormatRegName : "";
            var selectedSourceFormat = this.findRootFormat($scope.displaySourceFormatList, inSelectedName);
            var selectedTargetFormat = this.findRootFormat($scope.displayTargetFormatList, outSelectedName);
            this.rootSourceFormat = this.duplicateFormat(selectedSourceFormat);
            this.rootTargetFormat = this.duplicateFormat(selectedTargetFormat);
            if ($scope.displayTargetFormatList) {
                // TODO Build the registedformat tree
                if (transformRuleEntry.transform_rule.transform_rule_item) {
                    this.buildRegistedFormat(transformRuleEntry.transform_rule.transform_rule_item, $scope.displayTargetFormatList, selectedSourceFormat)
                }
            }
            LumensLog.log("Completed source and target building:\n", this.rootSourceFormat, this.rootTargetFormat);
            var result = {
                sourceFormat: this.rootSourceFormat,
                targetFormat: this.rootTargetFormat,
                ruleEntry: transformRuleEntry
            };
            this.saveToFormatList($scope, result, "IN");
            this.saveToFormatList($scope, result, "OUT");
            this.saveToTransformList($scope, result);
            return result;
        },
        isInValidSource: function ($scope) {
            return !$scope.currentUIComponent.$from_list ||
            !$scope.currentUIComponent.$from_list[0] ||
            !$scope.currentUIComponent.$from_list[0].$from.configure.component_info;
        },
        isInValidTarget: function ($scope) {
            return !$scope.currentUIComponent.$to_list ||
            !$scope.currentUIComponent.$to_list[0] ||
            !$scope.currentUIComponent.$to_list[0].$to.configure.component_info;
        },
        // Private memeber functions
        saveToTransformList: function ($scope, result) {
            var transformRuleEntry = $scope.currentUIComponent.getTransformRuleEntry();
            if (transformRuleEntry) {
                for (var i = 0; i < transformRuleEntry.length; ++i) {
                    var entry = transformRuleEntry[i];
                    if (entry.name === result.ruleEntry.name) {
                        transformRuleEntry[i] = result.ruleEntry;
                        return;
                    }
                }
                transformRuleEntry.push(result.ruleEntry);
            }
        },
        saveToFormatList: function ($scope, result, direction) {
            var formatEntry, formatName, format;
            var saveToComp, UIComponent = $scope.currentUIComponent;
            if (direction === "IN") {
                if (this.isInValidTarget($scope) || !result.targetFormat)
                    return;
                formatName = $scope.outputFormatRegName;
                format = result.targetFormat;
                saveToComp = UIComponent.getTo(0);
            }
            else {
                if (this.isInValidSource($scope) || !result.sourceFormat)
                    return;
                formatName = $scope.inputFormatRegName;
                format = result.sourceFormat;
                saveToComp = UIComponent.getFrom(0);
            }
            formatEntry = saveToComp.getFormatEntry(direction);
            if (!formatEntry) {
                saveToComp.setFormatEntry(direction, {
                    direction: direction,
                    format: [format],
                    name: formatName
                });
            }
            else {
                for (var i = 0; i < formatEntry.length; ++i) {
                    // If found the same reg name, replace it
                    if (formatName === formatEntry[i].name) {
                        formatEntry[i].format = [format];
                        return;
                    }
                }
                formatEntry.push({
                    direction: direction,
                    format: [format],
                    name: formatName
                });
            }
        },
        duplicateFormat: function (format) {
            if (!format)
                return;
            var duplicate = {
                form: format.form,
                name: format.name,
                type: format.type
            };
            if (format.property)
                duplicate.property = format.property;
            return duplicate;
        },
        buildRegistedFormat: function (ruleItem, targetFormats, sourceFormat) {
            if (!ruleItem || !targetFormats)
                return;
            var refTargetFormat = this.findRootFormat(targetFormats, ruleItem.format_name);
            if (ruleItem.script && !sourceFormat) {
                var sourceFormatPaths = this.parseScriptFindSourceFormat(ruleItem.script);
                LumensLog.log("Source format paths:", sourceFormatPaths);
                this.buildRegistedSourceFormat(this.rootSourceFormat, sourceFormat, sourceFormatPaths);
            }
            var ruleItems = ruleItem.transform_rule_item;
            if (ruleItems) {
                for (var i = 0; i < ruleItems.length; ++i)
                    this.buildRegistedTargetFormat(this.rootTargetFormat, ruleItems[i], refTargetFormat.format, this.rootSourceFormat, sourceFormat);
            }
        },
        buildRegistedTargetFormat: function (registedParentFormat, ruleItem, targetFormats, registedSourceParentFormat, sourceFormat) {
            if (!ruleItem || !targetFormats || !registedParentFormat)
                return false;
            if (ruleItem.script) {
                var sourceFormatPaths = this.parseScriptFindSourceFormat(ruleItem.script);
                LumensLog.log("Source format paths:", sourceFormatPaths);
                this.buildRegistedSourceFormat(registedSourceParentFormat, sourceFormat, sourceFormatPaths);
            }
            var childFormat1 = this.findChildFormat(this.rootTargetFormat.format, ruleItem.format_name);
            var childFormat2 = this.findChildFormat(targetFormats, ruleItem.format_name);
            if (!childFormat2)
                return false;
            if (!childFormat1) {
                childFormat1 = {
                    form: childFormat2.form,
                    name: childFormat2.name,
                    type: childFormat2.type
                };
                if (childFormat2.property)
                    childFormat1.property = childFormat2.property;

                if (!registedParentFormat.format)
                    registedParentFormat.format = [childFormat1];
                else
                    registedParentFormat.format.push(childFormat1);
            }
            var ruleItems = ruleItem.transform_rule_item;
            if (ruleItems) {
                for (var i = 0; i < ruleItems.length; ++i)
                    this.buildRegistedTargetFormat(childFormat1, ruleItems[i], childFormat2.format, registedSourceParentFormat, sourceFormat);
            }
            return true;
        },
        buildRegistedSourceFormat: function (registedParentFormat, sourceParentFormat, sourceFormatPaths) {
            if (registedParentFormat && sourceParentFormat) {
                // TODO parse the format paths to build required format for source
                for (var i = 0; i < sourceFormatPaths.length; ++i) {
                    var path = new Lumens.FormatPath(sourceFormatPaths[i]);
                    if (!this.buildRegisterSourceChildFormatFromOrignalFormat(registedParentFormat, sourceParentFormat, path, 0))
                        continue;
                }
            }
        },
        buildRegistedSourceChildFormat: function (registedParentFormat, sourceParentFormat, path, tokenIdx) {
            for (var i = tokenIdx; i < path.tokenCount(); ++i) {
                if (!this.buildRegisterSourceChildFormatFromOrignalFormat(registedParentFormat, sourceParentFormat, path, tokenIdx))
                    continue;
            }
        },
        buildRegisterSourceChildFormatFromOrignalFormat: function (registedParentFormat, sourceParentFormat, path, tokenIdx) {
            var childFormat1 = this.findChildFormat(registedParentFormat.format, path.token(tokenIdx));
            var childFormat2 = this.findChildFormat(sourceParentFormat.format, path.token(tokenIdx));
            if (!childFormat2)
                return false;
            if (!childFormat1) {
                childFormat1 = {
                    form: childFormat2.form,
                    name: childFormat2.name,
                    property: childFormat2.property,
                    type: childFormat2.type
                };
                if (!registedParentFormat.format)
                    registedParentFormat.format = [childFormat1];
                else
                    registedParentFormat.format.push(childFormat1);
            }
            if (childFormat1)
                this.buildRegistedSourceChildFormat(childFormat1, childFormat2, path, tokenIdx + 1);
            return true;
        },
        parseScriptFindSourceFormat: function (strScript) {
            // The same algorithm to see lumens-processor module's JavaScriptBuilder.java
            LumensLog.log("Parsing script:", strScript);
            var paths = [];
            var bQuote = false;
            for (var i = 0; i < strScript.length; ++i) {
                var c = strScript.charAt(i);
                if (c === '\"')
                    bQuote = !bQuote;
                if (bQuote || c !== '@') {
                    continue;
                } else if (c === '@') { // find all @a.b.c format line in this strScript
                    i++;
                    if (i < strScript.length) {
                        var singleQuote = false;
                        var path = "";
                        while (i < strScript.length) {
                            c = strScript.charAt(i);
                            if (c === '\'')
                                singleQuote = !singleQuote;
                            if (!singleQuote && this.pathEnding.indexOf(c) > -1) {
                                paths.push(path);
                                break;
                            } else {
                                path += c;
                                ++i;
                                if (i === strScript.length)
                                    paths.push(path);
                            }
                        }
                    }
                }
            }
            return paths;
        },
        findRootFormat: function (formatList, formatName) {
            if (formatList) {
                for (var i = 0; i < formatList.length; ++i)
                    if (formatList[i].format[0].name === formatName)
                        return formatList[i].format[0];
            }
            return null;
        },
        findChildFormat: function (formatList, formatName) {
            if (formatList) {
                for (var i = 0; i < formatList.length; ++i)
                    if (formatList[i].name === formatName)
                        return formatList[i];
            }
            return null;
        }
    }
});
