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
    panelContainer.getElement().css({"min-height": "350px", "border-bottom": "2px solid rgb(214, 214, 214)"});
    panelContainer.getPart1Element().addClass("formatResize");
    panelContainer.getPart2Element().addClass("formatResize");
    var leftPanel = new Lumens.Panel(panelContainer.getPart1Element()).configure({
        panelStyle: {width: "100%", "height": "100%", "min-height": "350px", overflow: "auto", "border-right": "1px solid rgb(214, 214, 214)"}
    });
    var rightPanel = new Lumens.Panel(panelContainer.getPart2Element()).configure({
        panelStyle: {width: "100%", "height": "100%", "min-height": "350px", overflow: "auto"}
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
