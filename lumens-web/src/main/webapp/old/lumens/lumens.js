/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 * Author: shaofeng wang (shaofeng.cjpw@gmail.com)
 */

Lumens.create = function(parentId) {
    var lumensApp = {};
    var parent = $(parentId);
    // Build the web header
    var header = Lumens.Header.create("#header", parent);
    var tabbar = Lumens.Tabbar.create(header.getHtmlElement());
    tabbar.addTabTitles(["Management", "Designer", "Datasource"]);
    tabbar.getTab(0).addEvent("click", function() {
        // TODO Switch to the Management view
        console.log("Switch to Management view");
    });
    tabbar.getTab(1).addEvent("click", function() {
        // TODO Switch to the Designer view
        console.log("Switch to Designer view");
    });
    tabbar.getTab(2).addEvent("click", function() {
        // TODO Switch to the Datasource wizard view
        console.log("Switch to Datasource view");
    });
    tabbar.getTab(1).setActive(true);

    var navigator = Lumens.Navigator.create("#navigator", parent);
    navigator.setText("Welcome: Guest");
    // TODO Create the Management view
    // TODO Create the Datasource wizard view
    // Create the Designer view
    // Initialize the splitter pane of the workspace
    $(parent).append('<div id="SplitterPaneDesignerView" class="SplitterPane layout-content splitter-pane-container" style="overflow:hidden;"></div>');
    var designerView = $('#SplitterPaneDesignerView');
    designerView.append('<div id="LeftPane" style="position: absolute; z-index: 1; overflow-x: hidden; overflow-y: auto; left: 0px; width: 200px; height: 100%;"/>');
    designerView.append('<div id="RightPane" style="position: absolute; z-index: 1; width: 100%; height: 100%; overflow: hidden"/>');
    designerView.splitter({
        splitVertical: true,
        sizeLeft: true
    });

    var rightPane = $("#RightPane");
    rightPane.append('<div id="TopPane" style="position: absolute; z-index: 1; overflow: auto; top: 0px; width: 100%; height: 100%;"/>');
    rightPane.append('<div id="BottomPane" style="position: absolute; z-index: 1; width: 100%; height:200px; overflow: hidden"/>');
    rightPane.splitter({
        splitHorizontal: true,
        sizeBottom: true
    });

    // Load these settings from server ?
    var accordian = Lumens.Accordian.create(designerView.find("#LeftPane"), "Toolbox", "toolbar",
    ["Datasource", "Project", "Settings"]);
    // Build compononet tree UI
    // TODO
    var componentTree = Lumens.ComponentTree.create(accordian.item(0), "#componentTree", "#SplitterPaneDesignerView");
    var topPane = $("#TopPane");
    Lumens.ComponentPane.create(topPane, "100%", "100%");
    
    lumensApp.run = function() {
        // Load the mandory data from server first
        // Do some settings on the web page, such status and so on
        // Load the component tree by AJAX
        // TODO
        componentTree.loadData();
    }
    return lumensApp;
};