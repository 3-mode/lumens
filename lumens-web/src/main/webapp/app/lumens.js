/* 
 * Application
 */
Lumens.Application = Class.$extend({
    __init__: function(ngAppName) {
        this.$ngApp = angular.module(ngAppName, []);
    },
    run: function() {
        var __this = this;
        this.$ngApp.controller('MainViewCtrl', function($scope, $http, $compile) {
            // Set the default page view as dashboard view
            // TODO or get localstorge to display last page view
            // TODO Dashboad View
            // TODO Manage View                
            // TODO Design View
            // ******* Design View ------------------------------------------------------------>
            if (true)
            {
                __this.rootLayout = new Lumens.RootLayout($('#id-main-view')).configure();
                __this.theLayout = new Lumens.SplitLayout(__this.rootLayout).configure({
                    mode: "vertical",
                    part1Size: 52
                });
                __this.sysHeader = new Lumens.Header(__this.theLayout.getPart1Element()).setSysTitle("JAMES");
                __this.navToolbar = new Lumens.NavToolbar(__this.sysHeader.getElement()).configure(Lumens.SysToolbar_Config);
                __this.workspaceLayout = new Lumens.SplitLayout(__this.theLayout.getPart2Element()).configure({
                    mode: "horizontal",
                    part1Size: 220
                });
                __this.leftPanel = new Lumens.Panel(__this.workspaceLayout.getPart1Element())
                .configure({
                    panelClass: ["lumens-menu-container"],
                    panelStyle: {width: "100%", height: "100%"}
                });
                // Load menu category
                $.get("app/config/desgin_nav_menu.json", function(menu) {
                    // Load data source category
                    $.get("app/mock/data_source_category.json", function(data_source_items) {
                        //Load instrument category
                        $.get("app/mock/instrument_category.json", function(instrument_items) {
                            menu.sections[0].items = data_source_items.items;
                            menu.sections[1].items = instrument_items.items;
                            // Create a dictionary to find the correct icon
                            __this.compCagegory = {};
                            $.each(data_source_items.items, function() {
                                __this.compCagegory[this.id] = this;
                            });
                            $.each(instrument_items.items, function() {
                                __this.compCagegory[this.id] = this;
                            });
                            __this.designAndInfoPanel = new Lumens.ResizableVSplitLayoutExt(__this.workspaceLayout.getPart2Element()).configure({
                                mode: "vertical",
                                useRatio: true,
                                part1Size: "60%"
                            });
                            // Create desgin workspace panel
                            __this.designPanel = new Lumens.ComponentPanel(__this.designAndInfoPanel.getPart1Element(), $scope).configure({width: "100%", height: "100%"});
                            // Create left menu
                            __this.navMenu = new Lumens.NavMenu({
                                container: __this.leftPanel.getElement(),
                                width: "100%",
                                height: "auto"
                            }).configure(menu, function(item, data) {
                                $.data(item.find("a").addClass("data-comp-node").draggable({
                                    appendTo: $("#id-data-comp-container"),
                                    helper: "clone"
                                }).get(0), "item-data", data);
                            });

                            // Create info form panel
                            __this.designAndInfoPanel.getTitleElement().append($compile('<b>Project: {{project.name}}</b>')($scope));
                            var tabsContainer = new Lumens.Panel(__this.designAndInfoPanel.getPart2Element()).configure({
                                panelStyle: {"height": "100%", "width": "100%", "overflow": "auto"}
                            });
                            function tabDescription($tabContent) {
                                $http.get("app/templates/project_summary_tmpl.html").success(function(project_summary_tmpl) {
                                    $tabContent.append($compile(project_summary_tmpl)($scope));
                                });
                            }
                            function tabProperties($tabContent) {
                                $http.get("app/templates/comp_props_form_tmpl.html").success(function(comp_props_tmpl) {
                                    $tabContent.append($compile(comp_props_tmpl)($scope));
                                });
                            }
                            __this.tabs = new Lumens.TabPanel(tabsContainer.getElement())
                            .configure({
                                tab: [
                                    {id: "id-project-info", label: "Summary", content: tabDescription},
                                    {id: "id-component-selected-props", label: "Properties", content: tabProperties},
                                    {id: "id-component-format-list", label: "Formats", content: undefined}
                                ]
                            });

                            __this.projectImporter = new Lumens.ProjectImporter(__this.compCagegory, __this.designPanel, $scope).importById();
                        });
                    });
                });
            }
            // <******* Design View ------------------------------------------------------------
        });
        return this;
    }
});

window.lumensApp = new Lumens.Application('lumens-app').run();