/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
Lumens.controllers = angular.module("lumens-controllers", []);
Lumens.controllers.controller("MainViewCtrl", function($scope, $http, $compile) {
    // Set the default page view as dashboard view
    Lumens.system.rootLayout = new Lumens.RootLayout($('#id-main-view')).configure();
    Lumens.system.theLayout = new Lumens.SplitLayout(Lumens.system.rootLayout).configure({
        mode: "vertical",
        part1Size: 52
    });
    Lumens.system.sysHeader = new Lumens.Header(Lumens.system.theLayout.getPart1Element()).setSysTitle("JAMES");
    Lumens.system.navToolbar = new Lumens.NavToolbar(Lumens.system.sysHeader.getElement()).configure(Lumens.SysToolbar_Config);
    Lumens.system.navToolbar.onButtonClick(function(event) {
        var curSysModuleID = event.moduleID;
    });
})
.controller("DashboardViewCtrl", function($scope, $http, $compile) {
    console.log("in DashboardViewCtrl")
})
.controller("ManageViewCtrl", function($scope, $http, $compile) {

})
.controller("DesignViewCtrl", function($scope, $http, $compile) {
    // Set the default page view as dashboard view
    // ******* Design View ------------------------------------------------------------>
    $scope.workspaceLayout = new Lumens.SplitLayout(Lumens.system.theLayout.getPart2Element()).configure({
        mode: "horizontal",
        part1Size: 220
    });
    $scope.leftPanel = new Lumens.Panel($scope.workspaceLayout.getPart1Element())
    .configure({
        panelClass: ["lumens-menu-container"],
        panelStyle: {width: "100%", height: "100%"}
    });
    // Load menu category
    $.get("app/config/desgin_nav_menu.json", function(menu) {
        // Load data source category
        $.get("app/mock/json/data_source_category.json", function(data_source_items) {
            //Load instrument category
            $.get("app/mock/json/instrument_category.json", function(instrument_items) {
                menu.sections[0].items = data_source_items.items;
                menu.sections[1].items = instrument_items.items;
                // Create a dictionary to find the correct icon
                $scope.compCagegory = {};
                $.each(data_source_items.items, function() {
                    $scope.compCagegory[this.id] = this;
                });
                $.each(instrument_items.items, function() {
                    $scope.compCagegory[this.id] = this;
                });
                $scope.designAndInfoPanel = new Lumens.ResizableVSplitLayoutExt($scope.workspaceLayout.getPart2Element()).configure({
                    mode: "vertical",
                    useRatio: true,
                    part1Size: "55%"
                });
                // Create desgin workspace panel
                $scope.currentComponent = {name: "to select"};
                $scope.designPanel = new Lumens.ComponentPanel($scope.designAndInfoPanel.getPart1Element())
                .configure({
                    componentDblclick: function(category_info, component_info) {
                        console.log(component_info);
                        $scope.$apply(function() {
                            $scope.currentComponent = component_info;
                            $scope.componentForm = $compile(category_info.html)($scope);
                            $scope.componentI18N = category_info.i18n;
                            $scope.componentProps = {"Description": component_info.description};
                            if ($scope.currentComponent && $scope.currentComponent.property) {
                                $.each($scope.currentComponent.property, function() {
                                    $scope.componentProps[this.name] = this.value;
                                });
                            }
                        });
                    },
                    panelStyle: {width: "100%", height: "100%"}
                });
                // Create left menu
                $scope.navMenu = new Lumens.NavMenu({
                    container: $scope.leftPanel.getElement(),
                    width: "100%",
                    height: "auto"
                }).configure(menu, function(item, data) {
                    item.find("a").addClass("data-comp-node").draggable({
                        appendTo: $("#id-data-comp-container"),
                        helper: "clone"
                    }).data("item-data", data);
                });

                // Create info form panel
                $scope.designAndInfoPanel.getTitleElement().append($compile('<b>Name: {{project.name}}</b>')($scope));
                var tabsContainer = new Lumens.Panel($scope.designAndInfoPanel.getPart2Element()).configure({
                    panelStyle: {"height": "100%", "width": "100%", "overflow": "auto"}
                });
                function tabSummary($tabContent) {
                    $scope.tabs.projSummaryList = new Lumens.List($tabContent).configure({
                        IdList: [
                            "Description",
                            "Resources",
                            "Instruments"
                        ],
                        titleList: [
                            "Description",
                            "Resources",
                            "Instruments"
                        ],
                        buildContent: function(itemContent, isExpand, title) {
                            if (isExpand) {
                                var itemID = title.attr("id");
                                if (itemID === "Description") {
                                    $http.get("app/templates/project_desc_tmpl.html").success(function(project_desc_tmpl) {
                                        itemContent.append($compile(project_desc_tmpl)($scope));
                                    });
                                }
                                else if (itemID === "Resources") {
                                    $http.get("app/templates/resources_tmpl.html").success(function(resources_tmpl) {
                                        itemContent.append($compile(resources_tmpl)($scope));
                                    });
                                }
                                else if (itemID === "Instruments") {
                                    $http.get("app/templates/instruments_tmpl.html").success(function(instruments_tmpl) {
                                        itemContent.append($compile(instruments_tmpl)($scope));
                                    });
                                }
                            }
                        }
                    });
                }
                function tabProperties($tabContent) {
                    $scope.tabs.compPropsList = new Lumens.List($tabContent).configure({
                        IdList: [
                            "ComponentProps"
                        ],
                        buildContent: function(itemContent, isExpand, title, titleText) {
                            if (isExpand) {
                                var itemID = title.attr("id");
                                if (itemID === "ComponentProps") {
                                    titleText.append($compile('<span ng-model="currentComponent.name">{{currentComponent.name}}</span>')($scope));
                                    $http.get("app/templates/comp_props_form_tmpl.html").success(function(comp_props_form_tmpl) {
                                        itemContent.append($compile(comp_props_form_tmpl)($scope));
                                    });
                                }
                            }
                        }
                    });
                }
                var buttonBar = $('<div class="lumens-button-bar"><input type=button value="Save" /><input type=button value="Delete" /></div>').appendTo(tabsContainer.getElement());
                buttonBar.find('input[value="Save"]').click(function() {
                    console.log($scope);
                });
                $scope.tabs = new Lumens.TabPanel(tabsContainer.getElement());
                $scope.tabs.configure({
                    tab: [
                        {id: "id-project-info", label: "Project Summary", content: tabSummary},
                        {id: "id-component-selected-props", label: "Component Properties", content: tabProperties},
                        {id: "id-component-format-list", label: "Data Formats", content: undefined}
                    ]
                });

                $scope.projectImporter = new Lumens.ProjectImporter($scope.compCagegory, $scope.designPanel, $scope).importById();
            });
        });
    });
    // <******* Design View ------------------------------------------------------------
});