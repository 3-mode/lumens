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

                    // Create desgin workspace panel
                    __this.designPanel = new Lumens.Panel(__this.workspaceLayout.getPart2Element())
                    .configure({
                        panelClass: ["data-comp-container"],
                        panelStyle: {width: "100%", height: "100%"}
                    });
                    // Initialize the business logical panel drop event
                    __this.componentList = [];
                    var $designPanelElement = __this.designPanel.getElement()
                    .attr("id", "id-data-comp-container")
                    .droppable({
                        accept: ".data-comp-node",
                        drop: function(event, ui) {
                            event.preventDefault();
                            var data = $.data(ui.draggable.get(0), "item-data");
                            var pos = ui.position;
                            __this.componentList.push(new Lumens.DataComponent($designPanelElement, {"x": pos.left, "y": pos.top, "data": data, "short_desc": "To configure"}));
                        }
                    });

                    // Loading nav menu
                    $.get("app/mock/nav_menu_mock.json").success(function(data) {
                        __this.navMenu = new Lumens.NavMenu({
                            container: __this.leftPanel.getElement(),
                            width: "100%",
                            height: "auto"
                        }).configure(data, function(item, data) {
                            $.data(item.find("a").addClass("data-comp-node").draggable({
                                appendTo: $("#id-data-comp-container"),
                                helper: "clone"
                            }).get(0), "item-data", data);
                        });
                        /*
                         var c0 = new Lumens.DataComponent($designPanelElement, {"x": 50, "y": 50, "product_name": "Oracle", "data": data.sections[0].items[0], "short_desc": "jdbc:oracle:thin:@localhost:1521:orcl"});
                         var c1 = new Lumens.DataComponent($designPanelElement, {"x": 500, "y": 100, "product_name": "MySQL", "data": data.sections[0].items[0], "short_desc": "jdbc:mysql:thin:@localhost:1521:mysql"});
                         var c2 = new Lumens.DataComponent($designPanelElement, {"x": 300, "y": 300, "product_name": "Oracle", "data": data.sections[0].items[0], "short_desc": "jdbc:oracle:thin:@localhost:1521:orcl"});
                         var c3 = new Lumens.DataComponent($designPanelElement, {"x": 500, "y": 500, "product_name": "Database", "data": data.sections[0].items[0], "short_desc": "jdbc:oracle:thin:@localhost:1521:orcl"});
                         new Lumens.Link().from(c0).to(c1).draw();
                         new Lumens.Link().from(c0).to(c2).draw();
                         new Lumens.Link().from(c2).to(c3).draw();//*/
                    });
                }
                // <******* Design View ------------------------------------------------------------
            }]
        );
        return this;
    }
});

window.lumensApp = new Lumens.Application('lumens-app').run();