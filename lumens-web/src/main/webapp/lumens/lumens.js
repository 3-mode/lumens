/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 * Author: shaofeng wang (shaofeng.cjpw@gmail.com)
 */
var Lumens = {
    version: 1.0
};

Lumens.create = function(parentId) {
    var lumensApp = {};
    var parent = $(parentId);
    // Build the web header
    var header = Header.create(parent);
    var navigator = Navigator.create("navigator", parent);
    navigator.setText("Welcome: Guest");
    // Initialize the splitter pane of the workspace
    $(parent).append('<div id="SplitterPane" class="layout-content splitter-pane-container" style="overflow:hidden;"></div>');
    var splitterPane = $('#SplitterPane');
    splitterPane.append('<div id="LeftPane" style="position: absolute; z-index: 1; overflow-x: hidden; overflow-y: auto; left: 0px; width: 300px; height: 100%;"/>');
    splitterPane.append('<div id="RightPane" style="position: absolute; z-index: 1; width: 100%; height: 100%; overflow: hidden"/>');
    splitterPane.splitter({
        splitVertical: true,
        sizeLeft: true
    });

    var rightPane = $("#RightPane");
    rightPane.append('<div id="TopPane" style="position: absolute; z-index: 1; overflow: auto; top: 0px; width: 100%; height: 85%;"/>');
    rightPane.append('<div id="BottomPane" style="position: absolute; z-index: 1; width: 100%; height:15%; overflow: hidden"/>');
    rightPane.splitter({
        splitHorizontal: true,
        sizeBottom: true
    });

    // Load these settings from server ?
    var accordian = Accordian.create(splitterPane.find("#LeftPane"), "Toolbox", "toolbar",
        ["Datasource", "Project", "Settings"]);
    // Build compononet tree UI
    var componentTree = ComponentTree.create(accordian.item(0), "componentTree", "SplitterPane");
    var topPane = $("#TopPane");
    var componentPane = ComponentPane.create(topPane, "100%", "100%");
    // Add three demo component here
    /*
    var c1 = componentPane.addComponent("SOAP", "source", 100, 100);
    var c2 = componentPane.addComponent("Transform", "transform", 400, 200);
    var c3 = componentPane.addComponent("Database", "target", 700, 100);
    var p = c1.getPosition();
    var link = c1.link(c2).target.link(c3);//*/
    lumensApp.run = function() {
        // Load the mandory data from server first
        // Do some settings on the web page, such status and so on
        // Load the component tree by AJAX
        componentTree.loadData();
    }
    return lumensApp;
};