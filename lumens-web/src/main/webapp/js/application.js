/* 
 * Application
 */
Lumens.Application = Class.$extend({
    __init__: function(ngAppName) {
        this.ngApp = angular.module(ngAppName, []);
        this._nav_side_width = 225;
        this._nav_header_height = 50;
    },
    directives: function() {
        this.ngApp.directive('datsrcDrag', function() {
            return {
                restrict: 'A',
                link: function($scope, $elem, $attrs) {
                    $elem.draggable({
                        appendTo: $('#id-data-comp-container'),
                        helper: "clone"
                    });
                }
            }
        });
        return this;
    },
    run: function() {
        var _ngApp = this.directives().ngApp;
        _ngApp
        .controller('TitleNavbarCtrl', ['$scope', function($scope) {
                $scope.product_name = 'LUMENS';
            }]
        )
        .controller('MainPageCtrl', ['$scope', function($scope) {
                // Set the default page view as dashboard view
                // TODO or get localstorge to display last page view
                $scope.templates = [{"name": "dashboard template", "url": ""}, {"name": "dashboard content template", "url": "templates/dashboard_tmpl.html"}];
            }]
        )
        .config(function($controllerProvider) {
            _ngApp.controllerProvider = $controllerProvider;
            $controllerProvider.register('TitleNavFunctionsCtrl', ['$scope', '$http', function($scope, $http) {
                    $http.get('config/modules_headerbar.json').success(function(data) {
                        $scope.modules = data.modules;
                        $scope.onTitleNavFunctionsCtrl = function(moduleID) {
                            var moduleBtn = $('#' + moduleID);
                            moduleBtn.parent().find('li').removeClass('active');
                            moduleBtn.addClass('active');
                            // TODO need to refine
                            if ("id_dashboard" === moduleID) {
                                var scope = angular.element('#ID_mainView').scope();
                                scope.templates = [{"name": "dashboard template", "url": ""}, {"name": "dashboard content template", "url": "templates/dashboard_tmpl.html"}];
                            }
                            else if ("id_project_manage" === moduleID) {
                                var scope = angular.element('#ID_mainView').scope();
                                scope.templates = [{"name": "dashboard template", "url": ""}, {"name": "dashboard content template", "url": "templates/project_manage_tmpl.html"}];
                            }
                            else if ("id_business_designer" === moduleID) {
                                var scope = angular.element('#ID_mainView').scope();
                                scope.templates = [{"name": "dashboard template", "url": "templates/designer_sidebar_tmpl.html"}, {"name": "dashboard content template", "url": "templates/designer_workspace_tmpl.html"}];
                            }
                        };
                    });
                }]
            );
            $controllerProvider.register('ServerManageCtrl', ['$scope', '$http', function($scope, $http) {
                    $http.get('mock_data/servers.json').success(function(data) {
                        $scope.servers = data.servers;
                    });
                }]
            );
            $controllerProvider.register('ProjectsCtrl', ['$scope', '$http', function($scope, $http) {
                    $http.get('mock_data/projects.json').success(function(data) {
                        $scope.projects = data.projects;
                    });
                }]
            );
            $controllerProvider.register('DesignerSidebarCtrl', ['$scope', '$http', function($scope, $http) {
                    $scope.sidebar = {};
                    $scope.sidebar.stack = new Array();
                    var _$designerSideBar = $('#ID_Designer_SideBar').css("height", $(window).height() - lumensApp._nav_header_height);
                    $(window).resize(function() {
                        _$designerSideBar.css("height", $(window).height() - lumensApp._nav_header_height);
                    });
                    $http.get('config/component_categories.json').success(function(connectors) {
                        $scope.sidebar.current_menus = connectors;
                        $scope.sidebar.SidebarBackHide = true;
                        $scope.sidebar.onSidebarMenuClick = function(menu) {
                            if (menu.connectors) {
                                $scope.sidebar.stack.push($scope.sidebar.current_menus);
                                $scope.sidebar.current_menus = menu;
                                $scope.sidebar.SidebarBackHide = ($scope.sidebar.stack.length === 0 ? "true" : undefined);
                            }
                        };
                        $scope.sidebar.onMenuBackClick = function() {
                            $scope.sidebar.current_menus = $scope.sidebar.stack.pop();
                            $scope.sidebar.SidebarBackHide = ($scope.sidebar.stack.length === 0 ? "true" : undefined);
                        };
                        $scope.sidebar.hasChildren = function(menu) {
                            return menu.connectors !== null && menu.connectors !== undefined;
                        };
                    });
                }]
            );
            $controllerProvider.register('DesignerWorkspaceCtrl', ['$scope', '$http', function($scope, $http) {
                    // Initalize the workspace panel
                    var _$wrapper = $('#wrapper').css("height", $(window).height() - lumensApp._nav_header_height).css("width", $(window).width() - lumensApp._nav_side_width);
                    var _$component_container = _$wrapper.find('#id-data-comp-container').css("height", _$wrapper.css("height")).css("width", _$wrapper.css("width"));
                    $(window).resize(function() {
                        _$wrapper.css("height", $(window).height() - lumensApp._nav_header_height).css("width", $(window).width() - lumensApp._nav_side_width);
                        _$component_container.css("height", _$wrapper.css("height")).css("width", _$wrapper.css("width"));
                    });
                    $scope.componentList = new Array();
                    _$component_container.droppable({
                        accept: ".designer-sidebar-datanode",
                        drop: function(event, ui) {
                            event.preventDefault();
                            var pos = ui.position;
                            $scope.componentList.push(new Lumens.DataComponent(_$component_container, {"x": pos.left, "y": pos.top, "product_name": "Oracle", "short_desc": "jdbc:oracle:thin:@localhost:1521:orcl"}));
                        }
                    });

                    // mock some components and links
                    /*
                     var _$parent = $('#id-data-comp-container');
                     var c0 = new Lumens.DataComponent(_$parent, {"x": 50, "y": 50, "product_name": "Oracle", "short_desc": "jdbc:oracle:thin:@localhost:1521:orcl"});
                     var c1 = new Lumens.DataComponent(_$parent, {"x": 500, "y": 100, "product_name": "MySQL", "short_desc": "jdbc:mysql:thin:@localhost:1521:mysql"});
                     var c2 = new Lumens.DataComponent(_$parent, {"x": 300, "y": 300, "product_name": "Oracle", "short_desc": "jdbc:oracle:thin:@localhost:1521:orcl"});
                     new Lumens.Link().from(c0).to(c1).draw();
                     new Lumens.Link().from(c0).to(c2).draw();
                     var c3 = new Lumens.DataComponent(_$parent, {"x": 500, "y": 500, "product_name": "Database", "short_desc": "jdbc:oracle:thin:@localhost:1521:orcl"});
                     new Lumens.Link().from(c2).to(c3).draw();//*/
                }]
            );
        });
    }
});

var lumensApp = new Lumens.Application('lumensApp');
lumensApp.run();