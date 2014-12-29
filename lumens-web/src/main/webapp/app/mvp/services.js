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
Lumens.services.factory('Notifier', function () {
    return {
        message: function (type, title, text) {
            new PNotify({
                type: type,
                title: title,
                addclass: "lumens-bootstrap",
                text: text,
                delay: 2000,
                buttons: {
                    closer: true
                },
                nonblock: {
                    nonblock: true
                }
            });
        }
    }
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
                    formatPath += pathTokens[i].name.indexOf('.') > 0 ? ("'" + pathTokens[i].name + "'") : pathTokens[i].name;
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
                                        nodeType: __this.isField(formatItem.form) ? "file" : (__this.isArray(formatItem.form) ? "folderset" : "folder"),
                                        label: formatItem.type === "None" ? formatItem.name : formatItem.name + "&nbsp;&nbsp;[" + formatItem.type + "]",
                                        name: formatItem.name,
                                        direction: direction,
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
                                            nodeType: __this.isField(formatItem.form) ? "file" : (__this.isArray(formatItem.form) ? "folderset" : "folder"),
                                            label: formatItem.type === "None" ? formatItem.name : formatItem.name + "&nbsp;&nbsp;[" + formatItem.type + "]",
                                            name: formatItem.name,
                                            direction: direction,
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
            clear: function () {
                if (this.transformRuleTree) {
                    this.transformRuleTree.remove();
                    this.transformRuleTree = null;
                }
            },
            getFullPath: function (node) {
                var formatPath = "";
                var pathTokens = node.getPath();
                for (var i = 0; i < pathTokens.length; ++i) {
                    if (formatPath.length > 0)
                        formatPath += '.';
                    formatPath += pathTokens[i].name.indexOf('.') > 0 ? ("'" + pathTokens[i].name + "'") : pathTokens[i].name;
                }
                return formatPath;
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
            getRuleScriptList: function () {
                var scriptList = [];
                var entryList = this.transformRuleTree.getEntryList();
                var childKeys = Object.keys(entryList.map);
                if (childKeys.length > 1)
                    throw "Wrong transform rule configruation it must equal 1";
                var rootRuleItemNode = entryList.map[childKeys[0]];
                if (rootRuleItemNode.script)
                    scriptList.push(rootRuleItemNode.script);
                this.getRuleScriptListFromChildren(scriptList, rootRuleItemNode.getChildList());
                return scriptList;
            },
            getRuleScriptListFromChildren: function (scriptList, entryList) {
                var entryListKeys = Object.keys(entryList.map);
                for (var i in entryListKeys) {
                    var entry = entryList.map[entryListKeys[i]];
                    if (entry.script)
                        scriptList.push(entry.script);
                    this.getRuleScriptListFromChildren(scriptList, entry.getChildList());
                }
            },
            buildTargetFormatTree: function () {
                if (!this.transformRuleTree)
                    throw "No transform rule is configured";
                var entryList = this.transformRuleTree.getEntryList();
                var childKeys = Object.keys(entryList.map);
                if (childKeys.length > 1)
                    throw "Wrong transform rule configruation it must equal 1";
                var rootRuleItemNode = entryList.map[childKeys[0]];
                var rootFormat = this.duplicateFormat(rootRuleItemNode.data);
                var childFormat = this.buildTargetFormatTreeChildren(rootRuleItemNode.getChildList());
                if (childFormat)
                    rootFormat.format = childFormat;
                return rootFormat;

            },
            buildTargetFormatTreeChildren: function (entryList) {
                var formatList = [];
                var entryListKeys = Object.keys(entryList.map);
                for (var i in entryListKeys) {
                    var entry = entryList.map[entryListKeys[i]];
                    var currentFormat = this.duplicateFormat(entry.data);
                    var childFormat = this.buildTargetFormatTreeChildren(entry.getChildList());
                    if (childFormat.length > 0)
                        currentFormat.format = childFormat;
                    formatList.push(currentFormat);
                }
                return formatList;
            },
            buildTransformRuleTree: function () {
                if (!this.transformRuleTree)
                    throw "No transform rule is configured";
                var entryList = this.transformRuleTree.getEntryList();
                var childKeys = Object.keys(entryList.map);
                if (childKeys.length > 1)
                    throw "Wrong transform rule configruation it must equal 1";
                var rootRuleItemNode = entryList.map[childKeys[0]];
                var rootRuleItem = {format_name: rootRuleItemNode.data.name};
                // Build child item rules
                var transform_rule_item = this.buildTransformRuleTreeChildren(rootRuleItemNode.getChildList());
                if (transform_rule_item.length > 0)
                    rootRuleItem.transform_rule_item = transform_rule_item;
                console.log("Root rule node: ", rootRuleItemNode);
                console.log("Root rule item: ", rootRuleItem);
                return {
                    name: rootRuleItemNode.data.name,
                    transform_rule_item: rootRuleItem
                };
            },
            buildTransformRuleTreeChildren: function (entryList) {
                var ruleItemList = [];
                var entryListKeys = Object.keys(entryList.map);
                for (var i in entryListKeys) {
                    var entry = entryList.map[entryListKeys[i]];
                    var currentRuleItem = {format_name: entry.data.name};
                    if (entry.script)
                        currentRuleItem.script = entry.script;
                    var transform_rule_item = this.buildTransformRuleTreeChildren(entry.getChildList());
                    if (transform_rule_item.length > 0)
                        currentRuleItem.transform_rule_item = transform_rule_item;
                    ruleItemList.push(currentRuleItem);
                }
                return ruleItemList;
            },
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
                    }
                    entryNode.expandContent();
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
                            drop: function (node, current, parent) {
                                if (node.direction === "OUT")
                                    current.setScript('@' + __this.getFullPath(node));
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
                    if (nodeList.length)
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
                    data: format
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
Lumens.services.factory('TransformMapperStorageService', ['RuleTreeBuilder', function (RuleTreeBuilder) {
        return {
            pathEnding: "+-*/ &|!<>\n\r\t^%=;:?,",
            findRootFormat: function (formatList, formatName) {
                if (formatList) {
                    for (var i = 0; i < formatList.length; ++i)
                        if (formatList[i].format.name === formatName)
                            return formatList[i].format;
                }
                return null;
            },
            buildRequiredInfo: function ($scope) {
                var required = {ruleEntry: {}};
                // Begin ***************************************************************************
                if ($scope.displaySourceFormatList && $scope.displaySourceFormatList.format_entity && $scope.inputSelectedFormatName)
                    required.selectedSourceFormat = this.findRootFormat($scope.displaySourceFormatList.format_entity, $scope.inputSelectedFormatName);
                if ($scope.displayTargetFormatList && $scope.displayTargetFormatList.format_entity && $scope.outputSelectedFormatName)
                    required.selectedTargetFormat = this.findRootFormat($scope.displayTargetFormatList.format_entity, $scope.outputSelectedFormatName);
                if ($scope.orignalInputFormatRegName)
                    required.orginalInRegName = $scope.orignalInputFormatRegName;
                if ($scope.orignalOutputFormatRegName)
                    required.orginalOutRegName = $scope.orignalOutputFormatRegName;
                if ($scope.orignalRuleRegName)
                    required.orginalRuleRegName = $scope.orignalRuleRegName;
                required.ruleScriptList = RuleTreeBuilder.getRuleScriptList();

                if ($scope.ruleRegName)
                    required.ruleEntry.name = $scope.ruleRegName;
                if ($scope.displaySourceFormatList && $scope.displaySourceFormatList.component_id)
                    required.ruleEntry.source_id = $scope.displaySourceFormatList.component_id;
                if ($scope.displayTargetFormatList && $scope.displayTargetFormatList.component_id)
                    required.ruleEntry.target_id = $scope.displayTargetFormatList.component_id;
                required.ruleEntry.source_format_name = $scope.inputFormatRegName ? $scope.inputFormatRegName : "";
                required.ruleEntry.target_format_name = $scope.outputFormatRegName ? $scope.outputFormatRegName : "";
                required.ruleEntry.transform_rule = RuleTreeBuilder.buildTransformRuleTree();
                // End ******************************************************************************
                return required;
            },
            buildSourcePathList: function (required) {
                var pathList = [];
                if (required.ruleScriptList.length > 0) {
                    for (var i in required.ruleScriptList)
                        this.parseScriptFindSourceFormat(pathList, required.ruleScriptList[i]);
                }
                return pathList;
            },
            handleChildSourceFormat: function (currentNewFormat, childSourceFormat, newChildSourceFormat) {
                if (childSourceFormat && !newChildSourceFormat) {
                    if (!currentNewFormat.format)
                        currentNewFormat.format = [];
                    currentNewFormat.format.push(this.duplicateFormat(childSourceFormat));
                }
            },
            findChildFormat: function (formatList, formatName) {
                if (formatList) {
                    for (var i = 0; i < formatList.length; ++i)
                        if (formatList[i].name === formatName)
                            return formatList[i];
                }
                return null;
            },
            buildSourceRegFormat: function (required) {
                var pathList = this.buildSourcePathList(required);
                var selectedSourceFormat = required.selectedSourceFormat;
                if (selectedSourceFormat) {
                    var rootSourceFormat = this.duplicateFormat(selectedSourceFormat);
                    for (var i in pathList) {
                        var path = new Lumens.FormatPath(pathList[i]);
                        var tokenCount = path.tokenCount();
                        var currentFormat = selectedSourceFormat;
                        var currentNewFormat = rootSourceFormat;
                        var tindex = 0, childSourceFormat, newChildSourceFormat;
                        if (currentFormat.name === path.token(tindex)) {
                            while (++tindex < tokenCount) {
                                console.log("Processing:", currentFormat);
                                var token = path.token(tindex);
                                childSourceFormat = this.findChildFormat(currentFormat.format, token);
                                newChildSourceFormat = this.findChildFormat(currentNewFormat.format, token);
                                if (childSourceFormat) {
                                    currentFormat = childSourceFormat;
                                    this.handleChildSourceFormat(currentNewFormat, childSourceFormat, newChildSourceFormat);
                                }
                            }
                        }
                    }
                    console.log("Final source format:", selectedSourceFormat, rootSourceFormat);
                    return rootSourceFormat;
                }
            },
            discoverSourceFormat: function (required) {
                var result_format = this.buildSourceRegFormat(required);
                if (result_format) {
                    return {
                        orginal_name: required.orginalInRegName,
                        format_entry: {
                            name: required.ruleEntry.source_format_name,
                            format: result_format,
                            direction: "OUT"
                        }
                    }
                }
            },
            discoverTargetFormat: function (required) {
                var result_format = RuleTreeBuilder.buildTargetFormatTree();
                if (result_format) {
                    return {
                        orignal_name: required.orginalOutRegName,
                        format_entry: {
                            name: required.ruleEntry.target_format_name,
                            format: result_format,
                            direction: "IN"
                        }
                    };
                }
            },
            discoverRule: function (required) {
                if (required.ruleEntry) {
                    return {
                        orignal_name: required.orginalRuleRegName,
                        rule_entry: required.ruleEntry
                    };
                }
            },
            save: function ($scope) {
                var required = this.buildRequiredInfo($scope);
                var sourceFormatInfo = this.discoverSourceFormat(required);
                var targetFormatInfo = this.discoverTargetFormat(required);
                var ruleInfo = this.discoverRule(required);
                this.saveToFormatList($scope, sourceFormatInfo);
                this.saveToFormatList($scope, targetFormatInfo);
                this.saveToTransformList($scope, ruleInfo);
            },
            isValidEndComponent: function ($scope, formatInfo) {
                if (!$scope.currentUIComponent || !formatInfo || !formatInfo.format_entry.name)
                    return false;
                if (formatInfo.format_entry.direction === "IN")
                    return $scope.currentUIComponent.hasTo() && $scope.currentUIComponent.getTo(0).getCompData();
                else if (formatInfo.format_entry.direction === "OUT")
                    return $scope.currentUIComponent.hasFrom() && $scope.currentUIComponent.getFrom(0).getCompData();
            },
            findRuleEntryIndex: function (ruleEntryList, ruleInfo) {
                var name = ruleInfo.orignal_name ? ruleInfo.orignal_name : ruleInfo.rule_entry.name;
                if (ruleEntryList) {
                    for (var i in ruleEntryList)
                        if (ruleEntryList[i].name === name)
                            return i;
                }
                return -1;
            },
            // Private memeber functions
            saveToTransformList: function ($scope, ruleInfo) {
                if (!$scope.currentUIComponent || !ruleInfo)
                    throw "In valid component is selected or transform mapping rule is not valid !";
                var ruleEntryList = $scope.currentUIComponent.getRuleEntryList();
                if (ruleEntryList) {
                    var index = this.findRuleEntryIndex(ruleEntryList, ruleInfo);
                    index = index >= 0 ? index : ruleEntryList.length;
                    ruleEntryList[index] = ruleInfo.rule_entry;
                } else {
                    ruleEntryList = [ruleInfo.rule_entry];
                }
                $scope.currentUIComponent.setRuleEntryList(ruleEntryList);
            },
            findFormatEntryIndex: function (formatEntryList, formatInfo) {
                var name = formatInfo.orignal_name ? formatInfo.orignal_name : formatInfo.format_entry.name;
                if (formatEntryList) {
                    for (var i in formatEntryList)
                        if (formatEntryList[i].name === name)
                            return i;
                }
                return -1;
            },
            saveToFormatList: function ($scope, formatInfo) {
                if (!this.isValidEndComponent($scope, formatInfo))
                    return;
                var direction = formatInfo.format_entry.direction;
                var endComponent = (direction === "IN") ? $scope.currentUIComponent.getTo(0) : $scope.currentUIComponent.getFrom(0);
                var formatEntryList = endComponent.getFormatEntryList(direction);
                if (formatEntryList) {
                    var index = this.findFormatEntryIndex(formatEntryList, formatInfo);
                    index = index >= 0 ? index : formatEntryList.length;
                    formatEntryList[index] = formatInfo.format_entry;
                } else {
                    formatEntryList = [formatInfo.format_entry];
                }
                endComponent.setFormatEntryList(direction, formatEntryList);
            },
            duplicateFormat: function (format) {
                if (!format)
                    return;
                var duplicate = {
                    name: format.name,
                    form: format.form,
                    type: format.type
                };
                if (format.property)
                    duplicate.property = format.property;
                return duplicate;
            },
            parseScriptFindSourceFormat: function (pathList, strScript) {
                // The same algorithm to see lumens-processor module's JavaScriptBuilder.java
                LumensLog.log("Parsing script:", strScript);
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
                                    this.addPathToList(pathList, path);
                                    break;
                                } else {
                                    path += c;
                                    ++i;
                                    if (i === strScript.length)
                                        this.addPathToList(pathList, path);
                                }
                            }
                        }
                    }
                }
            },
            addPathToList: function (pathList, path) {
                for (var i in pathList)
                    if (pathList[i] === path)
                        return;
                pathList.push(path);
            }
        }
    }]
);
