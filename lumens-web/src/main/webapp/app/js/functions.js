/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

function buildDataFormatList($parent, component) {
    /*var titleContainer = new Lumens.SplitLayout($parent).configure({
     mode: "horizontal",
     disabeAutoChildrenSize: true,
     useRatio: true,
     width: "100%",
     part1Size: "45%",
     part2Size: "55%"
     });
     titleContainer.getElement().css({"min-height": "30px", "border-bottom": "1px solid rgb(214, 214, 214)"});
     var titleLeftPanel = new Lumens.Panel(titleContainer.getPart1Element()).configure({
     panelStyle: {width: "100%", "height": "30px", overflow: "hidden", "border-right": "1px solid rgb(214, 214, 214)"}
     });
     titleLeftPanel.getElement().append('<div style="padding-left:20px;">Meta Format</div>');
     var titleRightPanel = new Lumens.Panel(titleContainer.getPart2Element()).configure({
     panelStyle: {width: "100%", "height": "25px", overflow: "hidden"}
     });
     titleRightPanel.getElement().append('<div style="padding-left:20px;">Registered Format</div>');
     //*/
    var panelContainer = new Lumens.SplitLayout($parent).configure({
        mode: "horizontal",
        disabeAutoChildrenSize: true,
        useRatio: true,
        width: "100%",
        part1Size: "45%",
        part2Size: "55%"
    });
    panelContainer.getElement().css({"min-height": "100px", "height": "350px", "border-bottom": "2px solid rgb(214, 214, 214)"});
    panelContainer.getPart1Element().addClass("formatResize");
    panelContainer.getPart2Element().addClass("formatResize");
    var leftPanel = new Lumens.Panel(panelContainer.getPart1Element()).configure({
        panelStyle: {width: "100%", "height": "100%", "min-height": "100px", overflow: "auto", "border-right": "1px solid rgb(214, 214, 214)"}
    });
    var rightPanel = new Lumens.Panel(panelContainer.getPart2Element()).configure({
        panelStyle: {width: "100%", "height": "100%", "min-height": "100px", overflow: "auto"}
    });
    panelContainer.getElement().resizable({
        handles: 's',
        alsoResize: '.formatResize'
    });
    if (component) {
        if (component.format_list && component.format_list.length > 0) {
            var formatList_IN = component.format_list[0];
            var formatList_OUT = component.format_list[1];
            var tree2 = new Lumens.Tree(rightPanel.getElement()).configure({
                handler: function(parentNode) {
                    var entryList = [];
                    $.each(formatList_IN.format_entry, function(i) {
                        console.log("entry name:", this.format);
                        entryList[i] = {
                            name: this.format.type === "None" ? this.format.name : this.format.name + "[" + this.format.type + "]",
                            nodeType: this.format.form === "Field" ? "file" : "folder",
                            data: this.format
                        };
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
                            name: this.type === "None" ? this.name : this.name + "[" + this.type + "]",
                            nodeType: this.form === "Field" ? "file" : "folder",
                            data: this
                        };
                    });
                    current.addChildList(nodeList);
                },
                draggable: true
            });

            var tree1 = new Lumens.Tree(leftPanel.getElement()).configure({
                dblclick: function(parent, current) {
                    if (current.hasContent() || !current.isFolder())
                        return;
                    current.addChildList(current.getLevel() > 3 ?
                    [
                        {
                            name: "[string]test" + current.getId(),
                            nodeType: "file"
                        },
                        {
                            name: "[int]test2" + current.getId(),
                            nodeType: "file"
                        }
                    ] :
                    [
                        {
                            name: "test" + current.getId(),
                            nodeType: "folder"
                        },
                        {
                            name: "test2" + current.getId(),
                            nodeType: "folder"
                        }
                    ]);
                },
                handler: function(parentNode) {
                    parentNode.addEntryList([{
                            name: "test",
                            nodeType: "folder"
                        },
                        {
                            name: "test2",
                            nodeType: "folder"
                        }
                    ]);
                },
                draggable: true
            });
        }
    }
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