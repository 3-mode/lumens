'use strict';

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
        Lumens.system.designView.workspaceLayout.remove();
})
.controller("ManageViewCtrl", function($scope, $route, $http, $compile) {
    console.log("in ManageViewCtrl");
    Lumens.system.navToolbar.active($route.current.$$route.originalPath.substring(1));
    if (Lumens.system.designView.workspaceLayout)
        Lumens.system.designView.workspaceLayout.remove();
})
.controller("DesignViewCtrl", function(
$scope, $route, $http, $compile,
DesignNavMenu, SuccessTemplate, WarningTemplate, PropFormTemplate,
DatasourceCategory, InstrumentCategory, DesignButtons, FormatList) {
    // Test services
    console.log("FormatList: ", FormatList.getIN({component_name: 'Database:@()!- HR'}));
    // Set the default page view as dashboard view
    var i18n = $scope.i18n = Lumens.i18n;
    var desgin = $scope.desgin = {};
    // ******* Design View ------------------------------------------------------------>
    Lumens.system.navToolbar.active($route.current.$$route.originalPath.substring(1));
    Lumens.system.designView.workspaceLayout = new Lumens.SplitLayout(Lumens.system.theLayout.getPart2Element()).configure({
        mode: "horizontal",
        part1Size: 220
    });
    desgin.leftPanel = new Lumens.Panel(Lumens.system.designView.workspaceLayout.getPart1Element())
    .configure({
        panelClass: ["lumens-menu-container"],
        panelStyle: {width: "100%", height: "100%"}
    });
    // Load success/warning template
    $scope.messageBox = new Lumens.Message({success: SuccessTemplate, warning: WarningTemplate});

    // Load menu category
    DesignNavMenu.get(function(menu) {
        // Load data source category
        $.getJSON("app/mock/json/data_source_category.json", function(mock_category) {
            DatasourceCategory.get(function(data_source_items) {
                $.each(mock_category.items, function() {
                    data_source_items.items.push(this);
                });
                //Load instrument category
                InstrumentCategory.get(function(instrument_items) {
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
                    desgin.barPanel = new Lumens.SplitLayout(Lumens.system.designView.workspaceLayout.getPart2Element()).configure({
                        mode: "vertical",
                        part1Size: 48
                    });
                    DesignButtons.get(function(design_command_tmpl) {
                        $compile(design_command_tmpl)($scope).appendTo(desgin.barPanel.getPart1Element());
                    });

                    desgin.designAndInfoPanel = new Lumens.ResizableVSplitLayoutExt(desgin.barPanel.getPart2Element())
                    .configure({
                        mode: "vertical",
                        useRatio: true,
                        part1Size: "40%"
                    });
                    // Create desgin workspace panel
                    $scope.currentComponent = {name: "to select"};
                    desgin.designPanel = new Lumens.ComponentPanel(desgin.designAndInfoPanel.getPart1Element())
                    .configure({
                        panelStyle: {width: "100%", height: "100%"},
                        onComponentDblclick: function(component) {
                            console.log("Dblclick:", component);
                            var config = component.configure;
                            $scope.$apply(function() {
                                $scope.componentForm = $compile(config.category_info.html)($scope);
                                $scope.componentI18N = config.category_info.i18n;
                                $scope.componentProps = {
                                    "Description": {value: (config.component_info && config.component_info.description) ? config.component_info.description : ""},
                                    "Name": {value: (config.component_info && config.component_info.name) ? config.component_info.name : ""}
                                };
                                if (config.component_info) {
                                    $scope.currentCategory = config.category_info;
                                    $.each(config.category_info.property, function() {
                                        $scope.componentProps[this.name] = ComponentProperty(this, config.component_info.property);
                                    });
                                    $scope.currentComponent = config.component_info;
                                }
                                else {
                                    $scope.currentComponent = {name: "to select"};
                                }
                            });
                        },
                        onBeforeComponentAdd: function() {
                            if ($scope.project)
                                return true;
                            $scope.messageBox.showWarning(i18n.id_no_project_warning, desgin.designPanel.getElement());
                            return false;
                        },
                        onAfterComponentAdd: function(component) {
                            console.log("Added compnoent:", component);
                            $scope.projectOperator.add(component.configure.component_info, component.configure.category_info.type);
                        }
                    });
                    $scope.projectOperator = new Lumens.ProjectOperator($scope.compCagegory, desgin.designPanel, $scope);
                    // Create left menu
                    desgin.navMenu = new Lumens.NavMenu({
                        container: desgin.leftPanel.getElement(),
                        width: "100%",
                        height: "auto"
                    }).configure(menu, function(item, data) {
                        item.find("a").addClass("data-comp-node").draggable({
                            appendTo: $("#id-data-comp-container"),
                            helper: "clone"
                        }).data("item-data", data);
                    });

                    // Create info form panel
                    desgin.designAndInfoPanel.getTitleElement().append($compile('<b>Name: {{project.name}}</b>')($scope));
                    desgin.tabsContainer = new Lumens.Panel(desgin.designAndInfoPanel.getPart2Element())
                    .configure({
                        panelStyle: {"height": "100%", "width": "100%", "position": "relative", "overflow-y": "scroll", "overflow-x": "auto"}
                    });
                    function tabSummary($tabContent) {
                        desgin.tabs.projSummaryList = new Lumens.List($tabContent).configure({
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
                    function tabConfiguration($tabContent) {
                        desgin.tabs.compPropsList = new Lumens.List($tabContent).configure({
                            IdList: [
                                "ComponentProps"
                            ],
                            titleList: [
                                $compile('<span data-bind="currentCategory.name">{{currentCategory.name}}</span>')($scope)
                            ],
                            buildContent: function(itemContent, isExpand, title) {
                                if (isExpand) {
                                    var itemID = title.attr("id");
                                    if (itemID === "ComponentProps") {
                                        PropFormTemplate.get(function(propFormTmpl) {
                                            itemContent.append($compile(propFormTmpl)($scope));
                                        })
                                    }
                                }
                            }
                        });
                    }
                    function tabFormatList($tabContent) {
                        $tabContent.append($compile('<div dynamic-format-list="currentComponent"/>')($scope));

                    }
                    desgin.tabs = new Lumens.TabPanel(desgin.tabsContainer.getElement());
                    desgin.tabs.configure({
                        tab: [
                            {id: "id-project-info", label: "Project Summary", content: tabSummary},
                            {id: "id-component-selected-props", label: "Component Properties", content: tabConfiguration},
                            {id: "id-component-format-list", label: "Format List", content: tabFormatList}
                        ]
                    });
                })
            });
        });
    });
    // <******* Design View ------------------------------------------------------------
})
.controller("DesginCmdCtrl", function($scope, $element, $compile, ProjectListModal, ProjectCreateModal, ProjectSave) {
    // Handle desgin command button event
    console.log("In DesginCmdCtrl", $element);
    var i18n = $scope.$parent.i18n;
    var projectOperator = $scope.$parent.projectOperator;
    var messageBoxParent = $scope.$parent.desgin.designPanel.getElement();
    var messageBox = $scope.$parent.messageBox;
    $scope.onCommand = function(id) {
        if ("id_open" === id) {
            if ($element.find('#projectListModal').length === 0) {
                ProjectListModal.get(function(project_list_modal_tmpl) {
                    var projectListDialog = $element.find("#project_dialog");
                    projectListDialog.append($compile(project_list_modal_tmpl)($scope));
                    $('#projectListModal').on("hidden.bs.modal", function() {
                        projectListDialog.empty();
                    }).modal({backdrop: "static"});
                });
            }
        }
        else if ("id_new" === id) {
            if ($element.find('#projectCreateModal').length === 0) {
                ProjectCreateModal.get(function(project_create_modal_tmpl) {
                    var projectCreateDialog = $element.find("#project_dialog");
                    projectCreateDialog.append($compile(project_create_modal_tmpl)($scope));
                    $('#projectCreateModal').on("hidden.bs.modal", function() {
                        projectCreateDialog.empty();
                    }).modal({backdrop: "static"});
                });
            }
        }
        else if ("id_save" === id) {
            projectOperator.sync();
            console.log("Saving project:", projectOperator.get());
            console.log("Saveing propery:", $scope.$parent.componentProps);
            ProjectSave.save(projectOperator.get(), function(response) {
                console.log("Save project status:", response);
                if (response.status === "OK") {
                    var project = response.result_content.project[0];
                    projectOperator.setId(project.id);
                    messageBox.showSuccess(i18n.id_save_project.format(project.name), messageBoxParent);
                }
            });
        }
        else
            console.log("Clicked:", id);
    };
})
.controller("ProjectListCtrl", function($scope, $element, ProjectList, ProjectById) {
    var i18n = $scope.$parent.i18n;
    var projectOperator = $scope.$parent.projectOperator;
    var messageBox = $scope.$parent.messageBox;
    var projectListContent = $element.find(".modal-body");
    $scope.selectProject = function(index, event) {
        $scope.selectIndex = index;
        $scope.currentProjectId = $(event.target).parent().attr("project-id");
    };
    $scope.openProject = function() {
        console.log("Opening project:", $scope.currentProjectId);
        if ($scope.currentProjectId) {
            ProjectById.get({project_id: $scope.currentProjectId}, function(projectData) {
                if (projectOperator)
                    projectOperator.import($scope.currentProjectId, projectData);
            });
        }
        else {
            messageBox.showWarning(i18n.id_no_project_select_warning, projectListContent);
            return;
        }
        $element.modal("hide");
    };
    ProjectList.get(function(projectsResponse) {
        $scope.projects = projectsResponse.content.project;
        projectListContent.find("#projectLoading").remove();
        projectListContent.children().show();
    });
})
.controller("ProjectCreateCtrl", function($scope, $element) {
    // Handle desgin command button event
    console.log("In ProjectCreateCtrl", $element);
    var i18n = $scope.$parent.i18n;
    var messageBoxParent = $scope.$parent.desgin.designPanel.getElement();
    var messageBox = $scope.$parent.messageBox;
    var projectOperator = $scope.$parent.projectOperator;
    $scope.createProject = function() {
        projectOperator.create($scope.projectName, $scope.projectDescription);
        messageBox.showSuccess(i18n.id_new_project_successfully, messageBoxParent);
        $element.modal("hide");
    };
});
