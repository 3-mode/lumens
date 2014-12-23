/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
function createGetTemplateObject($http, $q, url) {
    return {
        get: function (onResponse) {
            var deferred = $q.defer();
            $http.get(url).then(function (response) {
                deferred.resolve(response.data);
                if (onResponse)
                    onResponse(response.data);
            });
            return deferred.promise;
        }
    };
}

function getAppendTransformRuleItems(first, second) {
    var temp = [];
    $.each(second, function () {
        var second_item = this;
        var isDuplicated = false;
        $.each(first, function () {
            if (second_item.format_name === this.format_name) {
                isDuplicated = true;
                return false;
            }
        })
        if (!isDuplicated)
            temp.push(this);
    });
    return temp;
}

function buildTransformRuleItemStructure(parentRuleItem, formats) {
    if (!formats || !formats.length)
        return;
    parentRuleItem.transform_rule_item = [];
    $.each(formats, function () {
        var transformRuleItem = {
            format_name: this.name
        };
        parentRuleItem.transform_rule_item.push(transformRuleItem);
        buildTransformRuleItemStructure(transformRuleItem, this.format);
    });
}

function buildTransformRuleItemPathStructure(parentNode, path, index, childTransformRuleItem) {
    var curParentData = parentNode.data;
    var entryData = [];
    while (index < path.length) {
        if (!curParentData.transform_rule_item)
            curParentData.transform_rule_item = [];
        var transformRuleItem = {
            format_name: path[index].name
        };
        curParentData.transform_rule_item.push(transformRuleItem);
        if (entryData.length === 0)
            entryData.push(transformRuleItem);
        curParentData = transformRuleItem;
        ++index;
    }
    // Put child into its correct location
    if (!curParentData.transform_rule_item)
        curParentData.transform_rule_item = [];
    if (parentNode.children.map[childTransformRuleItem.format_name]) {
        parentNode = parentNode.children.map[childTransformRuleItem.format_name];
        curParentData = parentNode.data;
        var appended = getAppendTransformRuleItems(curParentData.transform_rule_item, childTransformRuleItem.transform_rule_item);
        curParentData.transform_rule_item = curParentData.transform_rule_item.concat(appended);
        if (entryData.length === 0)
            entryData = entryData.concat(appended);
    }
    else {
        curParentData.transform_rule_item.push(childTransformRuleItem);
        if (entryData.length === 0)
            entryData.push(childTransformRuleItem);
    }
    var nodeList = [];
    $.each(entryData, function () {
        nodeList.push({
            label: this.format_name,
            name: this.format_name,
            nodeType: this.transform_rule_item && this.transform_rule_item.length > 0 ? "folder" : "file",
            data: this
        });
    });
    parentNode.addChildList(nodeList);
}

function buildTransformRuleTree($parent, ruleEntity) {
    if (ruleEntity && ruleEntity.transformRuleEntry) {
        var transformRuleTree = new Lumens.Tree($parent).configure({
            handler: function (parentNode) {
                var transformRuleItem = ruleEntity.transformRuleEntry.transform_rule.transform_rule_item;
                var entryList = [];
                entryList.push({
                    label: transformRuleItem.format_name,
                    name: transformRuleItem.format_name,
                    nodeType: transformRuleItem.transform_rule_item && transformRuleItem.transform_rule_item.length > 0 ? "folder" : "file",
                    data: transformRuleItem
                });
                parentNode.addEntryList(entryList);
            },
            drop: function (data, current, parent) {
                console.log("Dropped 2", data);
                // TODO
                var Root = current.getRoot();
                var path = data.location;
                if (Root.name !== path[0].name) {
                    alert("Root node name '" + Root.name + "' does not match with '" + path[0].name + "'");
                    return;
                }
                // Build children transform rule items from child list which is dropped
                var transformRuleItem = {
                    format_name: data.child.name
                };
                buildTransformRuleItemStructure(transformRuleItem, data.child.format);

                current = Root;
                for (var i = 1; i < path.length; ++i) {
                    var next = current.children.map[path[i].name];
                    if (!next)
                        break;
                    current = next;
                }
                buildTransformRuleItemPathStructure(current, path, i, transformRuleItem);
            },
            click: function (current, parent) {
                if (ruleEntity.clickCallBack)
                    ruleEntity.clickCallBack(current);
                if (current.hasContent() || !current.isFolder())
                    return;
                var nodeList = [];
                $.each(current.data.transform_rule_item, function () {
                    console.log("transform_rule_item:", this);
                    nodeList.push({
                        label: this.format_name,
                        name: this.format_name,
                        nodeType: this.transform_rule_item && this.transform_rule_item.length > 0 ? "folder" : "file",
                        data: this
                    });
                });
                current.addChildList(nodeList);
            },
            droppable: true
        });

        return transformRuleTree;
    }
    return null;
}

function ComponentPropertyList(config) {
    var category = config.category_info;
    var compinfo = config.component_info;
    var componentProps = {
        "Description": {label: category.i18n.Description, value: (compinfo && compinfo.description) ? compinfo.description : "", type: "String"},
        "Name": {label: category.i18n.Name, value: (compinfo && compinfo.name) ? compinfo.name : "", type: "String"}
    };

    if (!category.property)
        return componentProps;

    $.each(category.property, function () {
        var category_property = this;
        componentProps[category_property.name] = {
            label: category.i18n[category_property.name],
            name: category_property.name,
            type: category_property.type
        }

        for (var i in compinfo.property) {
            var prop = compinfo.property[i];
            if (prop.name === category_property.name) {
                componentProps[category_property.name].value = prop.value;
                break;
            }
        }
    });
    return componentProps;
}

function applyProperty(componentProps, currentUIComponent) {
    var currentComponent = currentUIComponent.getCompData();
    currentComponent.property = [];
    currentComponent.name = componentProps.Name.value;
    currentComponent.description = componentProps.Description.value;
    for (var propName in componentProps) {
        if (propName === "Description" || propName === "Name")
            continue;
        var prop = componentProps[propName];
        currentComponent.property.push({
            name: propName,
            type: prop.type,
            value: prop.value
        });
    }
}

function buildTransformationList($parent, component) {
    console.log("Transformation List:", component);
    var transformationList = component.transform_rule_entry;
    if (transformationList) {
        var IdTitleList = [];
        var contentList = [];
        $.each(transformationList, function () {
            IdTitleList.push(this.name);
            contentList.push($('<div style="padding-left:30px;"><b>' + this.source_name + "--->" + this.target_name + '</b></div>'));
        });
        new Lumens.List($parent).configure({
            IdList: IdTitleList,
            titleList: IdTitleList,
            contentList: contentList
        });
    }
}

function isFormatOf(formatEntryList, formatName) {
    for (var i in formatEntryList)
        if (formatEntryList[i].name === formatName)
            return true;
    return false;
}

function getTargetComponentFormatList(componentList, selectTargetName) {
    for (var i in componentList)
        if (componentList[i].configure.short_desc === selectTargetName)
            return componentList[i].configure.component_info.format_list;
    return undefined;
}