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
DesignNavMenu, SuccessTemplate, WarningTemplate, ErrorTemplate, PropFormTemplate, TransformListTemplate,
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
    // Init variable
    $scope.categoryInfo = {name: "to select"};
    $scope.componentProps = {Name: {value: "to select"}};
    // Load templates
    $scope.messageBox = new Lumens.Message({success: SuccessTemplate, warning: WarningTemplate, error: ErrorTemplate});
    TransformListTemplate.get(function(templ) {
        $scope.transformListTemplate = templ;
    });
    $scope.onApplyProperty = function(event) {
        console.log("Apply:", event);
        console.log("Current editing props:", $scope.componentProps);
        console.log("Current editing component:", $scope.currentComponent);
        // *** TODO shortDescription changed then the related linked component target name should be changed also
        $scope.currentUIComponent.setShortDescription($scope.componentProps.Name.value);
        applyProperty($scope.componentProps, $scope.currentComponent);
    };

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
                    desgin.designPanel = new Lumens.ComponentPanel(desgin.designAndInfoPanel.getPart1Element())
                    .configure({
                        panelStyle: {width: "100%", height: "100%"},
                        onComponentDblclick: function(component) {
                            console.log("Dblclick:", component);
                            if ($scope.currentUIComponent === component)
                                return;
                            $scope.currentUIComponent = component;
                            var config = component.configure;
                            $scope.$apply(function() {
                                $scope.componentForm = $compile(config.category_info.html)($scope);
                                $scope.categoryInfo = config.category_info;
                                $scope.componentProps = ComponentPropertyList(config);
                                $scope.currentComponent = config.component_info ? config.component_info : $scope.currentComponent
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
                                $compile('<span data-bind="categoryInfo.name">{{categoryInfo.name}}</span>')($scope)
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
                    function tabTransformationList($tabContent) {
                        $tabContent.append($compile($scope.transformListTemplate)($scope));

                    }
                    desgin.tabs = new Lumens.TabPanel(desgin.tabsContainer.getElement());
                    desgin.tabs.configure({
                        tab: [
                            {id: "id-project-info", label: "Project Summary", content: tabSummary},
                            {id: "id-component-selected-props", label: "Component Properties", content: tabConfiguration},
                            {id: "id-component-transformation-list", label: "Transformations", content: tabTransformationList}
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
    var i18n = $scope.i18n;
    var projectOperator = $scope.projectOperator;
    var messageBoxParent = $scope.desgin.designPanel.getElement();
    var messageBox = $scope.messageBox;
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
            if (projectOperator.isValid()) {
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
                    else
                        messageBox.showError(i18n.id_save_proj_err.format(project.name), messageBoxParent);
                });
            }
        }
        else
            console.log("Clicked:", id);
    };
})
.controller("ProjectListCtrl", function($scope, $element, ProjectList, ProjectById) {
    var i18n = $scope.i18n;
    var projectOperator = $scope.projectOperator;
    var messageBox = $scope.messageBox;
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
                    projectOperator.import(projectData);
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
    console.log("In ProjectCreateCtrl", $element);
    var i18n = $scope.$parent.i18n;
    var messageBoxParent = $scope.desgin.designPanel.getElement();
    var projectInfoContent = $element.find(".modal-body");
    var messageBox = $scope.messageBox;
    var projectOperator = $scope.projectOperator;
    $scope.createProject = function() {
        if ($scope.projectName) {
            projectOperator.create($scope.projectName, $scope.projectDescription);
            messageBox.showSuccess(i18n.id_new_project_successfully, messageBoxParent);
            $element.modal("hide");
        }
        else
            messageBox.showWarning(i18n.id_no_project_name, projectInfoContent);
    };
})
.controller("TransformListCtrl", function($scope, $compile, $element, TransformEditTemplate, ScriptEditTemplate) {
    console.log("In TransformListCtrl", $element);
    var componentList = $scope.desgin.designPanel.getComponentList();

    function showRuleEditor() {
        Lumens.system.designView.workspaceLayout.hide();
        //TODO show to the transform editing
        TransformEditTemplate.get(function(transformEditTemplate) {
            $scope.transformEditPanel = new Lumens.ResizableVSplitLayoutExt(Lumens.system.theLayout.getPart2Element())
            .configure({
                mode: "vertical",
                useRatio: true,
                part1Size: "40%"
            });
            $compile(transformEditTemplate)($scope).appendTo($scope.transformEditPanel.getPart1Element());
            $(window).resize(function(evt) {
                if (evt.target !== this)
                    return;
                $scope.transformEditPanel.getElement().trigger("resize");
                $scope.transformEditPanel.getElement().find(".lumens-format-panel").trigger("resize");
            });
            // Load script editing panel
            ScriptEditTemplate.get(function(scriptEditTemplate) {
                $compile(scriptEditTemplate)($scope).appendTo($scope.transformEditPanel.getPart2Element());
            })
        });
    }

    $scope.onRuleCommand = function(id_btn) {
        if (id_btn === "id_rule_new") {
            showRuleEditor();
        } else if (id_btn === "id_rule_delete") {
        }
    };

    $scope.openTransformEditing = function(rule) {
        console.log("Opening rule: ", rule);
        $scope.currentTransformRule = rule;
        showRuleEditor();
    };

    $scope.backFromTransformEditing = function() {
        $scope.transformEditPanel.remove();
        Lumens.system.designView.workspaceLayout.show();
    };

    // Init the trnasformation rule list to show all rules
    $scope.$watch(function() {
        return $scope.currentComponent;
    }, function(currentComponent) {
        $scope.theTargetNameList = [];
        if (currentComponent && currentComponent.target) {
            $.each(currentComponent.target, function() {
                $scope.theTargetNameList.push(this.name);
            });
            if (currentComponent.transform_rule_entry) {
                $.each(currentComponent.transform_rule_entry, function() {
                    if ($scope.selectTargetName === "All") {
                        $scope.transformRuleEntryList.push(this);
                    }
                });
            }
        }
    });

    // if the select a target then only list the related rules
    $scope.$watch(function() {
        return $scope.selectTargetName;
    }, function(selectTargetName) {
        $scope.transformRuleEntryList = [];
        if ($scope.currentComponent && $scope.currentComponent.transform_rule_entry) {
            $.each($scope.currentComponent.transform_rule_entry, function() {
                if (selectTargetName === "All") {
                    $scope.transformRuleEntryList.push(this);
                }
                else {
                    var targetComponentFormatList = getTargetComponentFormatList(componentList, selectTargetName);
                    if (targetComponentFormatList) {
                        if (targetComponentFormatList.length > 0 && targetComponentFormatList[0].direction === "IN") {
                            var formatINList = targetComponentFormatList[0];
                            if (formatINList.format_entry && isFormatOf(formatINList.format_entry, this.target_format_name)) {
                                $scope.transformRuleEntryList.push(this);
                            }
                        }
                    }
                }
            });
        }
    });
})
.controller("TransformEditCtrl", function($scope, $element, ProjectById, FormatList) {
    console.log("In TransformEditCtrl");
    $scope.$parent.scriptNodeScope = $scope;
    var projectOperator = $scope.projectOperator;
    var currentScriptNode;
    if ($scope.currentComponent.id === "id-transformator") {
        function buildTransformRuleEntry(ruleEntry) {
            return {
                // TODO
                transformRuleEntry: ruleEntry,
                clickCallBack: function(target) {
                    currentScriptNode = target;
                    $scope.$apply(function() {
                        $scope.$parent.transformRuleScript = target.getScript();
                    });
                }
            };
        }
        function buildTransformRuleTree(location_tokens) {
            var rootRuleItem, currentRuleItem;
            $.each(location_tokens, function() {
                if (!rootRuleItem) {
                    currentRuleItem = rootRuleItem = {
                        "format_name": this.name
                    };
                } else {
                    currentRuleItem.transform_rule_item = [{
                            "format_name": this.name
                        }];
                    currentRuleItem = currentRuleItem.transform_rule_item[0];
                }
            });
            return {root: rootRuleItem, last: currentRuleItem};
        }
        var sourceComponentName, targetComponentName;
        if ($scope.currentUIComponent.$from_list && $scope.currentUIComponent.$from_list.length > 0 && $scope.currentUIComponent.$from_list[0].$from)
            sourceComponentName = $scope.currentUIComponent.$from_list[0].$from.configure.component_info.name;
        if ($scope.currentUIComponent.$to_list && $scope.currentUIComponent.$to_list.length > 0 && $scope.currentUIComponent.$to_list[0].$to)
            targetComponentName = $scope.currentUIComponent.$to_list[0].$to.configure.component_info.name;
        console.log("transform source:" + sourceComponentName + ", target:" + targetComponentName);
        $element.find("div[rule-entity]").droppable({
            greedy: true,
            accept: ".lumens-tree-node",
            drop: function(event, ui) {
                var data = $.data(ui.draggable.get(0), "tree-node-data");
                console.log("Dropped", data);
                // TODO compare root
                // TODO append to exist correctly
                var tree = buildTransformRuleTree(data.location);
                tree.last.transform_rule_item = [{
                        "format_name": data.child.name
                    }];
                buildTransformRuleItemStructure(tree.last, data.child.format);
                var transformRuleEntry = [
                    {
                        "name": "new",
                        "source_name": "s1",
                        "target_name": "t1",
                        "transform_rule": {
                            "name": "new",
                            "transform_rule_item": tree.root
                        }
                    }
                ];
                if ($scope.currentComponent.transform_rule_entry) {
                    $scope.currentComponent.transform_rule_entry.concat(transformRuleEntry);
                    $scope.$apply(function() {
                        $scope.transformRuleEntity = buildTransformRuleEntry(transformRuleEntry[0]);
                    });
                }
            }
        });
        $scope.transformRuleEntity = buildTransformRuleEntry($scope.currentComponent.transform_rule_entry[0]);
        var unbindWatch = $scope.$watch(function() {
            return $scope.transformRuleScript;
        }, function(scriptEditorText) {
            console.log("Changed script: ", scriptEditorText);
            if (currentScriptNode)
                currentScriptNode.setScript(scriptEditorText);
        });
        $scope.$on('$destroy', function() {
            unbindWatch();
        });

        if (sourceComponentName) {
            ProjectById.operate({project_id: projectOperator.get().projectId}, {action: 'active'}, function(result) {
                console.log(result);
                FormatList.getIN({project_id: projectOperator.get().projectId, component_name: sourceComponentName}, function(result) {
                    $scope.sourceFormatList = result.content.format_list;
                });
            });
        }
        if (targetComponentName) {
            ProjectById.operate({project_id: projectOperator.get().projectId}, {action: 'active'}, function(result) {
                console.log(result);
                FormatList.getIN({project_id: projectOperator.get().projectId, component_name: targetComponentName}, function(result) {
                    $scope.targetFormatList = result.content.format_list;
                });
            });
        }
    }
})
.controller("RuleScriptCtrl", function($scope) {
    $scope.$parent.scriptEditorScope = $scope;
    $scope.onCommand = function(id_script_btn) {
        if (id_script_btn === "id_script_apply") {
            $scope.$parent.transformRuleScript = $scope.transformRuleScriptEditor.getValue();
        }
        else if (id_script_btn === "id_script_validate") {
            console.log("Validate transform_rule_entity:", $scope.transformRuleEntity.transformRuleEntry);
        }
        else if (id_script_btn === "id_script_back") {
            $scope.transformEditPanel.remove();
            $scope.scriptNodeScope.$destroy();
            $scope.scriptEditorScope.$destroy();
            Lumens.system.designView.workspaceLayout.show();
        }
    }
    console.log("In RuleScriptCtrl");
});
