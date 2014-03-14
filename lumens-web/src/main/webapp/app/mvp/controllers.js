/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.controllers = angular.module("lumens-controllers", ["ngRoute"]);
Lumens.controllers.controller("MainViewCtrl", function($scope, $route, $http, $compile) {
    // Set the default page view as dashboard view
    Lumens.system.rootLayout = new Lumens.RootLayout($('#id-main-view')).configure();
    Lumens.system.theLayout = new Lumens.SplitLayout(Lumens.system.rootLayout).configure({
        mode: "vertical",
        part1Size: 52
    });
    Lumens.system.sysHeader = new Lumens.Header(Lumens.system.theLayout.getPart1Element()).setSysTitle("JAMES");
    Lumens.system.navToolbar = new Lumens.NavToolbar(Lumens.system.sysHeader.getElement()).configure(Lumens.SysToolbar_Config);
    Lumens.system.theLayout.getPart2Element().append($compile('<div ng-view />')($scope));
})
.controller("DashboardViewCtrl", function($scope, $route, $http, $compile) {
    console.log("in DashboardViewCtrl");
    Lumens.system.navToolbar.active($route.current.$$route.originalPath.substring(1));
    if (Lumens.system.designView.workspaceLayout)
        Lumens.system.designView.workspaceLayout.getElement().hide();
})
.controller("ManageViewCtrl", function($scope, $route, $http, $compile) {
    console.log("in ManageViewCtrl");
    Lumens.system.navToolbar.active($route.current.$$route.originalPath.substring(1));
    if (Lumens.system.designView.workspaceLayout)
        Lumens.system.designView.workspaceLayout.getElement().hide();
})
.controller("DesignViewCtrl", function($scope, $route, $http, $compile) {
    // Set the default page view as dashboard view
    // ******* Design View ------------------------------------------------------------>
    Lumens.system.navToolbar.active($route.current.$$route.originalPath.substring(1));
    if (!Lumens.system.designView.scope)
        Lumens.system.designView.scope = $scope;
    else
        $scope = Lumens.system.designView.scope;

    if (Lumens.system.designView.workspaceLayout) {
        Lumens.system.designView.workspaceLayout.getElement().show();
        Lumens.system.designView.workspaceLayout.getElement().trigger("resize");
        return;
    }
    Lumens.system.designView.workspaceLayout = new Lumens.SplitLayout(Lumens.system.theLayout.getPart2Element()).configure({
        mode: "horizontal",
        part1Size: 220
    });
    Lumens.system.designView.leftPanel = new Lumens.Panel(Lumens.system.designView.workspaceLayout.getPart1Element())
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
                Lumens.system.designView.designAndInfoPanel = new Lumens.ResizableVSplitLayoutExt(Lumens.system.designView.workspaceLayout.getPart2Element()).configure({
                    mode: "vertical",
                    useRatio: true,
                    part1Size: "55%"
                });
                // Create desgin workspace panel
                $scope.currentComponent = {name: "to select"};
                Lumens.system.designView.designPanel = new Lumens.ComponentPanel(Lumens.system.designView.designAndInfoPanel.getPart1Element())
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
                Lumens.system.designView.navMenu = new Lumens.NavMenu({
                    container: Lumens.system.designView.leftPanel.getElement(),
                    width: "100%",
                    height: "auto"
                }).configure(menu, function(item, data) {
                    item.find("a").addClass("data-comp-node").draggable({
                        appendTo: $("#id-data-comp-container"),
                        helper: "clone"
                    }).data("item-data", data);
                });

                // Create info form panel
                Lumens.system.designView.designAndInfoPanel.getTitleElement().append($compile('<b>Name: {{project.name}}</b>')($scope));
                Lumens.system.designView.tabsContainer = new Lumens.Panel(Lumens.system.designView.designAndInfoPanel.getPart2Element())
                .configure({
                    panelStyle: {"height": "100%", "width": "100%", "overflow": "auto"}
                });
                function tabSummary($tabContent) {
                    Lumens.system.designView.tabs.projSummaryList = new Lumens.List($tabContent).configure({
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
                    Lumens.system.designView.tabs.compPropsList = new Lumens.List($tabContent).configure({
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
                var buttonBar = $('<div class="lumens-button-bar"><input type=button value="Save" /><input type=button value="Delete" /></div>').appendTo(Lumens.system.designView.tabsContainer.getElement());
                buttonBar.find('input[value="Save"]').click(function() {
                    console.log($scope);
                });
                Lumens.system.designView.tabs = new Lumens.TabPanel(Lumens.system.designView.tabsContainer.getElement());
                Lumens.system.designView.tabs.configure({
                    tab: [
                        {id: "id-project-info", label: "Project Summary", content: tabSummary},
                        {id: "id-component-selected-props", label: "Component Properties", content: tabProperties},
                        {id: "id-component-format-list", label: "Data Formats", content: undefined}
                    ]
                });

                $scope.projectImporter = new Lumens.ProjectImporter($scope.compCagegory, Lumens.system.designView.designPanel, $scope).importById();
            });
        });
    });
    // <******* Design View ------------------------------------------------------------
});