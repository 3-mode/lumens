/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
Lumens.services.factory('RuleTreeService', ['FormatService', function (FormatService) {
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
                    if (entry.for_each)
                        currentRuleItem.for_each = entry.for_each;
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
                        var ruleTree = __this.transformRuleTree = new Lumens.Tree(parent).configure({
                            handler: function (parentNode) {
                                var rootRuleItem = __this.getRootRuleItem(ruleEntry);
                                __this.checkRuleWithFormat(rootRuleItem.format_name, formatEntry.name);
                                var entryList = __this.buildRootRuleItem(rootRuleItem, formatEntry);
                                parentNode.addEntryList(entryList);
                                __this.buildRuleItemChildren(parentNode, rootRuleItem, formatEntry);
                            },
                            click: function (current, parent) {
                                $scope.selectRuleItem = current;
                                $scope.$broadcast("SelectRuleItem", current);
                            },
                            drop: function (node, current, parent) {
                                if (node.direction === "OUT")
                                    current.setScript('@' + __this.getFullPath(node));
                            },
                            droppable: true
                        });
                        $scope.$on("ApplyScript", function (evt, script) {
                            var selectNode = ruleTree.getSelectNode();
                            if (selectNode)
                                selectNode.setScript(script);
                        })
                        return __this.transformRuleTree;
                    } else if (parent.children().length > 0) {
                        // TODO find the parent node for the drag object and append it to the parent node
                        console.log("appended");
                    }
                }
                return null;
            },
            buildRootRuleItem: function (rootRuleItem, formatEntry) {
                var root = {
                    label: rootRuleItem.format_name,
                    name: rootRuleItem.format_name,
                    nodeType: FormatService.isField(formatEntry.form) ? "file" : "folder",
                    data: formatEntry
                };
                if (rootRuleItem && rootRuleItem.script)
                    root.script = rootRuleItem.script;
                if (rootRuleItem && rootRuleItem.for_each)
                    root.for_each = rootRuleItem.for_each;
                return [root];
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
                    nodeType: FormatService.isField(format.form) ? "file" : (FormatService.isArray(format.form) ? "folderset" : "folder"),
                    data: format
                };
                if (ruleItem && ruleItem.script)
                    node.script = ruleItem.script;
                if (ruleItem && ruleItem.for_each)
                    node.for_each = ruleItem.for_each;
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
Lumens.services.factory('TransformMapperStorageService', ['RuleTreeService', function (RuleTreeService) {
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
                required.ruleScriptList = RuleTreeService.getRuleScriptList();

                if ($scope.ruleRegName)
                    required.ruleEntry.name = $scope.ruleRegName;
                if ($scope.displaySourceFormatList && $scope.displaySourceFormatList.component_id)
                    required.ruleEntry.source_id = $scope.displaySourceFormatList.component_id;
                if ($scope.displayTargetFormatList && $scope.displayTargetFormatList.component_id)
                    required.ruleEntry.target_id = $scope.displayTargetFormatList.component_id;
                required.ruleEntry.source_format_name = $scope.inputFormatRegName ? $scope.inputFormatRegName : "";
                required.ruleEntry.target_format_name = $scope.outputFormatRegName ? $scope.outputFormatRegName : "";
                required.ruleEntry.transform_rule = RuleTreeService.buildTransformRuleTree();
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
                var result_format = RuleTreeService.buildTargetFormatTree();
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


