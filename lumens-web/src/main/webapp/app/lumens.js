/* 
 * Application
 */
Lumens.Application = Class.$extend({
    __init__: function(ngAppName) {
        this.$ngApp = angular.module(ngAppName, []);
    },
    run: function() {
        var __this = this;
        this.$ngApp.controller('MainPageCtrl', ['$scope', '$http', function($scope, $http) {
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
                    $.get("app/mock/nav_menu_mock.json", function(menu) {
                        // Load data source category
                        $.get("app/mock/data_source_category.json", function(data_source_items) {
                            //Load instrument category
                            $.get("app/mock/instrument_category.json", function(instrument_items) {
                                menu.sections[0].items = data_source_items.items;
                                menu.sections[1].items = instrument_items.items;
                                __this.compCagegory = {};
                                $.each(data_source_items.items, function() {
                                    __this.compCagegory[this.id] = this;
                                });
                                $.each(instrument_items.items, function() {
                                    __this.compCagegory[this.id] = this;
                                });
                                // Create desgin workspace panel
                                __this.designPanel = new Lumens.ComponentPanel(__this.workspaceLayout.getPart2Element()).configure({width: "100%", height: "100%"});
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
                                __this.projectImporter = new Lumens.ProjectImporter(__this.compCagegory, __this.designPanel).importById();
                            });
                        });
                    });
                }
                // <******* Design View ------------------------------------------------------------
            }]
        )
        .config(function($controllerProvider) {
            $controllerProvider.register('DataCompCtrl', ['$scope', '$http', function($scope, $http) {
                    console.log("DataCompCtrl");
                }]
            );
        });
        return this;
    }
});

window.lumensApp = new Lumens.Application('lumens-app').run();