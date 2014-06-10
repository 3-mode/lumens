/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

function buildDataFormatList($parent, formatList) {
    if (formatList) {
        var formatTree = new Lumens.Tree($parent).configure({
            handler: function(parentNode) {
                var entryList = [];
                $.each(formatList, function() {
                    console.log("entry name:", this.format);
                    $.each(this.format, function() {
                        entryList.push({
                            label: this.type === "None" ? this.name : this.name + "&nbsp;&nbsp;[" + this.type + "]",
                            name: this.name,
                            nodeType: this.form === "Field" ? "file" : "folder",
                            data: this
                        });
                    });
                });
                parentNode.addEntryList(entryList);
            },
            dblclick: function(parent, current) {
                if (current.hasContent() || !current.isFolder())
                    return;
                var nodeList = [];
                $.each(current.data.format, function(i) {
                    console.log("format:", this);
                    nodeList[i] = {
                        label: this.type === "None" ? this.name : this.name + "&nbsp;&nbsp;[" + this.type + "]",
                        name: this.name,
                        nodeType: this.form === "Field" ? "file" : "folder",
                        data: this
                    };
                });
                current.addChildList(nodeList);
            },
            draggable: true
        });

        return formatTree;
    }
    return null;
}

function buildTransformRuleTree($parent, transformRuleEntry) {
    if (transformRuleEntry) {
        var transformRuleTree = new Lumens.Tree($parent).configure({
            handler: function(parentNode) {
                var transformRuleItem = transformRuleEntry.transform_rule.transform_rule_item;
                var entryList = [];
                entryList.push({
                    label: transformRuleItem.format_name,
                    name: transformRuleItem.format_name,
                    nodeType: transformRuleItem.transform_rule_item && transformRuleItem.transform_rule_item.length > 0 ? "folder" : "file",
                    data: transformRuleItem
                });
                parentNode.addEntryList(entryList);
            },
            dblclick: function(parent, current) {
                if (current.hasContent() || !current.isFolder())
                    return;
                var nodeList = [];
                $.each(current.data.transform_rule_item, function() {
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
            draggable: true
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

    $.each(category.property, function() {
        var category_property = this;
        componentProps[category_property.name] = {
            label: category.i18n[category_property.name],
            name: category_property.name,
            type: category_property.type
        }
        $.each(compinfo.property, function() {
            if (this.name === category_property.name) {
                componentProps[category_property.name].value = this.value;
                return false;
            }
        })
    })
    return componentProps;
}

function applyProperty(componentProps, currentComponent) {
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
        $.each(transformationList, function() {
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
    for (var i = 0; i < formatEntryList.length; ++i)
        if (formatEntryList[i].name === formatName)
            return true;
    return false;
}

function getTargetComponentFormatList(componentList, selectTargetName) {
    for (var i = 0; i < componentList.length; ++i)
        if (componentList[i].configure.short_desc === selectTargetName)
            return componentList[i].configure.component_info.format_list;
    return undefined;
}