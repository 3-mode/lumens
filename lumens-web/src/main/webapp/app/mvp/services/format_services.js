/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
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

