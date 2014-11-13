/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.controllers
.controller("DesignViewCtrl", function (
$scope, $route, $http, $compile,
DesignNavMenu, SuccessTemplate, WarningTemplate, ErrorTemplate, PropFormTemplate, TransformListTemplate,
DatasourceCategory, InstrumentCategory, jSyncHtml, DesignButtons, FormatList) {
    Lumens.system.switchTo(Lumens.system.NormalView);
    // Set the default page view as dashboard view
    var i18n = $scope.i18n = Lumens.i18n;
    var desgin = $scope.desgin = {};
    // ******* Design View ------------------------------------------------------------>
    Lumens.system.navToolbar.active($route.current.$$route.originalPath.substring(1));
    Lumens.system.workspaceLayout = new Lumens.SplitLayout(Lumens.system.theLayout.getPart2Element()).configure({
        mode: "horizontal",
        part1Size: 220
    });
    desgin.leftPanel = new Lumens.Panel(Lumens.system.workspaceLayout.getPart1Element())
    .configure({
        panelClass: ["lumens-menu-container"],
        panelStyle: {width: "100%", height: "100%"}
    });
    // Init variable
    $scope.categoryInfo = {name: "to select"};
    $scope.componentProps = {Name: {value: "to select"}};
    // Load templates
    $scope.messageBox = new Lumens.Message({success: SuccessTemplate, warning: WarningTemplate, error: ErrorTemplate});
    TransformListTemplate.get(function (templ) {
        $scope.transformListTemplate = templ;
    });
    $scope.onApplyProperty = function (event) {
        LumensLog.log("Apply:", event);
        LumensLog.log("Current editing props:", $scope.componentProps);
        LumensLog.log("Current editing component:", $scope.currentComponent);
        // *** TODO shortDescription changed then the related linked component target name should be changed also
        $scope.currentUIComponent.setShortDescription($scope.componentProps.Name.value);
        applyProperty($scope.componentProps, $scope.currentUIComponent);
    };

    // Load menu category
    DesignNavMenu.get(function (menu) {
        // Load data source category
        $.getJSON("app/mock/json/data_source_category.json", function (mock_category) {
            DatasourceCategory.get(function (data_source_items) {
                $.each(mock_category.items, function () {
                    data_source_items.items.push(this);
                });
                //Load instrument category
                InstrumentCategory.get(function (instrument_items) {
                    menu.sections[0].items = data_source_items.items;
                    menu.sections[1].items = instrument_items.items;
                    jSyncHtml.get(instrument_items.items);
                    // Create a dictionary to find the correct icon
                    $scope.compCagegory = {};
                    $.each(data_source_items.items, function () {
                        $scope.compCagegory[this.type] = this;
                    });
                    $.each(instrument_items.items, function () {
                        $scope.compCagegory[this.type] = this;
                    });
                    desgin.barPanel = new Lumens.SplitLayout(Lumens.system.workspaceLayout.getPart2Element()).configure({
                        mode: "vertical",
                        part1Size: 48
                    });
                    DesignButtons.get(function (design_command_tmpl) {
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
                        onComponentDblclick: function (component) {
                            LumensLog.log("Dblclick:", component);
                            if ($scope.currentUIComponent === component)
                                return;
                            $scope.currentUIComponent = component;
                            var config = component.configure;
                            $scope.$apply(function () {
                                $scope.componentForm = $compile(component.getFormHtml())($scope);
                                $scope.categoryInfo = config.category_info;
                                $scope.componentProps = ComponentPropertyList(config);
                                $scope.currentComponent = config.component_info ? config.component_info : $scope.currentComponent
                            });
                        },
                        onBeforeComponentAdd: function () {
                            if ($scope.project)
                                return true;
                            $scope.messageBox.showWarning(i18n.id_no_project_warning, desgin.designPanel.getElement());
                            return false;
                        },
                        onAfterComponentAdd: function (component) {
                            LumensLog.log("Added compnoent:", component);
                            $scope.projectOperator.add(component.getCompData(), component.getClassType());
                        }
                    });
                    $scope.projectOperator = new Lumens.ProjectOperator($scope.compCagegory, desgin.designPanel, $scope);
                    // Create left menu
                    desgin.navMenu = new Lumens.NavMenu({
                        container: desgin.leftPanel.getElement(),
                        width: "100%",
                        height: "auto"
                    }).configure(menu, function (item, data) {
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
                            buildContent: function (itemContent, id, isExpand, title) {
                                if (isExpand) {
                                    if (id === "Description") {
                                        $http.get("app/templates/designer/project_desc_tmpl.html").success(function (project_desc_tmpl) {
                                            itemContent.append($compile(project_desc_tmpl)($scope));
                                        });
                                    }
                                    else if (id === "Resources") {
                                        $http.get("app/templates/designer/resources_tmpl.html").success(function (resources_tmpl) {
                                            itemContent.append($compile(resources_tmpl)($scope));
                                        });
                                    }
                                    else if (id === "Instruments") {
                                        $http.get("app/templates/designer/instruments_tmpl.html").success(function (instruments_tmpl) {
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
                            buildContent: function (itemContent, id, isExpand, title) {
                                if (isExpand) {
                                    if (id === "ComponentProps") {
                                        PropFormTemplate.get(function (propFormTmpl) {
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
.controller("DesginCmdCtrl", function ($scope, $element, $compile, ProjectListModal, ProjectCreateModal, ProjectSave, ProjectById) {
    // Handle desgin command button event
    LumensLog.log("In DesginCmdCtrl", $element);
    var i18n = $scope.i18n;
    var projectOperator = $scope.projectOperator;
    var messageBoxParent = $scope.desgin.designPanel.getElement();
    var messageBox = $scope.messageBox;
    $scope.onCommand = function (id) {
        if ("id_open" === id) {
            if ($element.find('#projectListModal').length === 0) {
                ProjectListModal.get(function (project_list_modal_tmpl) {
                    var projectListDialog = $element.find("#project_dialog");
                    projectListDialog.append($compile(project_list_modal_tmpl)($scope));
                    $('#projectListModal').on("hidden.bs.modal", function () {
                        projectListDialog.empty();
                    }).modal({backdrop: "static"});
                });
            }
        }
        else if ("id_new" === id) {
            if ($element.find('#projectCreateModal').length === 0) {
                ProjectCreateModal.get(function (project_create_modal_tmpl) {
                    var projectCreateDialog = $element.find("#project_dialog");
                    projectCreateDialog.append($compile(project_create_modal_tmpl)($scope));
                    $('#projectCreateModal').on("hidden.bs.modal", function () {
                        projectCreateDialog.empty();
                    }).modal({backdrop: "static"});
                });
            }
        }
        else if ("id_save" === id) {
            if (projectOperator.isValid()) {
                projectOperator.sync();
                LumensLog.log("Saving project:", projectOperator.get());
                LumensLog.log("Saveing propery:", $scope.$parent.componentProps);
                ProjectSave.save(projectOperator.get(), function (response) {
                    LumensLog.log("Save project status:", response);
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
        else if ("id_active" === id) {
            ProjectById.operate({project_id: projectOperator.get().projectId}, {action: 'active'}, function (result) {
                LumensLog.log(result);
            });
        }
        else if ("id_deploy" === id) {
            ProjectById.operate({project_id: projectOperator.get().projectId}, {action: 'deploy'}, function (result) {
                LumensLog.log(result);
            });
        }
        else if ("id_execute" === id) {
            ProjectById.operate({project_id: projectOperator.get().projectId}, {action: 'execute'}, function (result) {
                LumensLog.log(result);
            });
        }
        else if ("id_delete" === id) {
            ProjectById.operate({project_id: projectOperator.get().projectId}, {action: 'delete'}, function (result) {
                if (result.status === "OK")
                    projectOperator.close();
                LumensLog.log(result);
            });
        }
        LumensLog.log("Clicked:", id);
    };
})
.controller("ProjectListCtrl", function ($scope, $element, ProjectList, ProjectById) {
    var i18n = $scope.i18n;
    var projectOperator = $scope.projectOperator;
    var messageBox = $scope.messageBox;
    var projectListContent = $element.find(".modal-body");
    $scope.selectProject = function (index, event) {
        $scope.selectIndex = index;
        $scope.currentProjectId = $(event.target).parent().attr("project-id");
    };
    $scope.openProject = function () {
        LumensLog.log("Opening project:", $scope.currentProjectId);
        if ($scope.currentProjectId) {
            ProjectById.get({project_id: $scope.currentProjectId}, function (projectData) {
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
    ProjectList.get(function (projectsResponse) {
        $scope.projects = projectsResponse.content.project;
        projectListContent.find("#projectLoading").remove();
        projectListContent.children().show();
    });
})
.controller("ProjectCreateCtrl", function ($scope, $element) {
    LumensLog.log("In ProjectCreateCtrl", $element);
    var i18n = $scope.$parent.i18n;
    var messageBoxParent = $scope.desgin.designPanel.getElement();
    var projectInfoContent = $element.find(".modal-body");
    var messageBox = $scope.messageBox;
    var projectOperator = $scope.projectOperator;
    $scope.createProject = function () {
        if ($scope.projectName) {
            projectOperator.create($scope.projectName, $scope.projectDescription);
            messageBox.showSuccess(i18n.id_new_project_successfully, messageBoxParent);
            $element.modal("hide");
        }
        else
            messageBox.showWarning(i18n.id_no_project_name, projectInfoContent);
    };
})
.controller("TransformListCtrl", function ($scope, $compile, $element, TransformEditTemplate, RuleEditorService) {
    LumensLog.log("In TransformListCtrl", $element);
    $scope.ruleEditorService = RuleEditorService.create();

    function showRuleEditor() {
        Lumens.system.workspaceLayout.hide();
        //TODO show to the transform editing
        TransformEditTemplate.get(function (transformEditTemplate) {
            $scope.transformEditPanel = new Lumens.ResizableVSplitLayoutExt(Lumens.system.theLayout.getPart2Element())
            .configure({
                mode: "vertical",
                useRatio: true,
                part1Size: "40%"
            });
            var transforEditor = $compile(transformEditTemplate)($scope).appendTo($scope.transformEditPanel.getPart1Element());
            $(window).resize(function (evt) {
                if (evt.target !== this)
                    return;
                $scope.transformEditPanel.getElement().trigger("resize");
                $scope.transformEditPanel.getElement().find(".lumens-format-panel").trigger("resize");
            });
        });
    }

    $scope.onRuleCommand = function (id_btn) {
        if (id_btn === "id_rule_new") {
            LumensLog.log("New rule");
            $scope.currentTransformRule = null;
            $scope.inputFormatRegName = null;
            $scope.outputFormatRegName = null;
            $scope.inputSelectedFormatName = null;
            $scope.outputSelectedFormatName = null;
            $scope.ruleRegName = null;
            showRuleEditor();
        } else if (id_btn === "id_rule_delete") {
        }
    };

    $scope.openTransformEditing = function (ruleEntry) {
        LumensLog.log("Opening rule: ", ruleEntry);
        $scope.currentTransformRule = ruleEntry;
        $scope.inputFormatRegName = ruleEntry.source_format_name;
        $scope.outputFormatRegName = ruleEntry.target_format_name;
        $scope.ruleRegName = ruleEntry.name;
        var inFormat = $scope.currentUIComponent.getRegisterInputFormat($scope.inputFormatRegName);
        $scope.inputSelectedFormatName = inFormat ? inFormat.name : null;
        var outFormat = $scope.currentUIComponent.getRegisterOutputFormat($scope.outputFormatRegName);
        $scope.outputSelectedFormatName = outFormat ? outFormat.name : null;
        showRuleEditor();
    };

    $scope.backFromTransformEditing = function () {
        $scope.transformEditPanel.remove();
        Lumens.system.workspaceLayout.show();
    };

    // Init the trnasformation rule list to show all rules
    $scope.$watch(function () {
        return $scope.currentUIComponent;
    }, function (currentUIComponent) {
        if (!currentUIComponent || !currentUIComponent.configure)
            return;
        if (currentUIComponent.configure.component_info.transform_rule_entry)
            $scope.transformRuleEntryList = currentUIComponent.getTransformRuleEntry();
        else
            $scope.transformRuleEntryList = [];

        if (currentUIComponent.$from_list &&
        currentUIComponent.hasFrom())
            $scope.theSourceNameList = [currentUIComponent.getFrom(0).getConfig().short_desc];
        else
            $scope.theSourceNameList = [currentUIComponent.getConfig().short_desc];
        if (currentUIComponent.hasTo())
            $scope.theTargetNameList = [currentUIComponent.getTo(0).getConfig().short_desc];
        else
            $scope.theTargetNameList = [];
    });
})
.controller("TransformEditCtrl", function ($scope, $element, $compile, FormatList, ScriptEditTemplate, FormatRegistryModal, RuleRegistryModal) {
    LumensLog.log("In TransformEditCtrl");
    $scope.ruleEditorService.addScope($scope);
    // Load script editing panel
    ScriptEditTemplate.get(function (scriptEditTemplate) {
        $compile(scriptEditTemplate)($scope).appendTo($scope.transformEditPanel.getPart2Element());
    })
    $scope.setScript = function (script) {
        if ($scope.currentScriptNode)
            $scope.currentScriptNode.setScript(script);
    }
    var projectOperator = $scope.projectOperator;
    if ($scope.currentComponent.type === "type-transformator") {
        function buildTransformRuleEntry(ruleRegName, ruleEntry, isNew) {
            if (isNew) {
                return {
                    // TODO
                    transformRuleEntry: ruleEntry[0],
                    clickCallBack: function (target) {
                        $scope.currentScriptNode = target;
                        $scope.ruleEditorService.sync(target.getScript());
                    }
                };
            } else {
                for (var i = 0; i < ruleEntry.length; ++i) {
                    if (ruleEntry[i].name === ruleRegName) {
                        return {
                            // TODO
                            transformRuleEntry: ruleEntry[i],
                            clickCallBack: function (target) {
                                $scope.currentScriptNode = target;
                                $scope.ruleEditorService.sync(target.getScript());
                            }
                        };
                    }
                }
            }
        }
        function buildTransformRuleTree(location_tokens) {
            var rootRuleItem, currentRuleItem;
            $.each(location_tokens, function () {
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
        if ($scope.currentUIComponent.hasFrom())
            sourceComponentName = $scope.currentUIComponent.getFrom(0).getCompData().name;
        if ($scope.currentUIComponent.hasTo())
            targetComponentName = $scope.currentUIComponent.getTo(0).getCompData().name;
        LumensLog.log("transform source:" + sourceComponentName + ", target:" + targetComponentName);
        $element.find("div[rule-entity]").droppable({
            greedy: true,
            accept: ".lumens-tree-node",
            drop: function (event, ui) {
                var data = $.data(ui.draggable.get(0), "tree-node-data");
                LumensLog.log("Dropped", data);
                // TODO compare root
                // TODO append to exist correctly
                var tree = buildTransformRuleTree(data.location);
                tree.last.transform_rule_item = [{
                        "format_name": data.child.name
                    }];
                if (data.child.name !== tree.last.format_name)
                    tree.last = tree.last.transform_rule_item[0];
                buildTransformRuleItemStructure(tree.last, data.child.format);
                var transformRuleEntry = [
                    {
                        "name": "Transform-Rule-Define",
                        "source_name": "",
                        "target_name": "",
                        "transform_rule": {
                            "name": tree.root.format_name,
                            "transform_rule_item": tree.root
                        }
                    }
                ];
                if ($scope.currentComponent.transform_rule_entry) {
                    $scope.currentComponent.transform_rule_entry.concat(transformRuleEntry);
                    $scope.$apply(function () {
                        $scope.transformRuleEntity = buildTransformRuleEntry($scope.ruleRegName, transformRuleEntry, true);
                    });
                }
            }
        });

        $scope.transformRuleEntity = buildTransformRuleEntry($scope.ruleRegName, $scope.currentUIComponent.getTransformRuleEntry(), false);

        if (sourceComponentName) {
            FormatList.getIN({project_id: projectOperator.get().projectId, component_name: sourceComponentName}, function (result) {
                $scope.sourceFormatList = result.content.format_list;
                $scope.displaySourceFormatList = $scope.sourceFormatList;
                $scope.onCommand("id_format_reg_filter_btn", "left");
            });
        }
        if (targetComponentName) {
            FormatList.getIN({project_id: projectOperator.get().projectId, component_name: targetComponentName}, function (result) {
                $scope.targetFormatList = result.content.format_list;
                $scope.displayTargetFormatList = $scope.targetFormatList;
                $scope.onCommand("id_format_reg_filter_btn", "right");
            });
        }

        $scope.onCommand = function (btn_id, side) {
            LumensLog.log(btn_id, side);
            $scope.selectedSide = side;
            if (side && "id_format_reg_edit_btn" === btn_id) {
                if ("left" === side)
                    $scope.currentFormatList = $scope.sourceFormatList;
                else if ("right" === side)
                    $scope.currentFormatList = $scope.targetFormatList;

                FormatRegistryModal.get(function (format_reg_edit_tmpl) {
                    var formatRegEditDialog = $element.find("#reg_edit_dialog");
                    formatRegEditDialog.append($compile(format_reg_edit_tmpl)($scope));
                    $('#formatRegistryModal').on("hidden.bs.modal", function () {
                        formatRegEditDialog.empty();
                    }).modal({backdrop: "static"});
                });
            }
            else if ("id_rule_reg_edit_btn" === btn_id) {
                RuleRegistryModal.get(function (rule_reg_edit_tmpl) {
                    var ruleRegEditDialog = $element.find("#reg_edit_dialog");
                    ruleRegEditDialog.append($compile(rule_reg_edit_tmpl)($scope));
                    $('#ruleRegistryModal').on("hidden.bs.modal", function () {
                        ruleRegEditDialog.empty();
                    }).modal({backdrop: "static"});
                });
            }
            else if ("id_format_reg_filter_btn" === btn_id) {
                LumensLog.log("input:", $scope.inputFormatRegName);
                LumensLog.log("input format:", $scope.inputSelectedFormatName);
                LumensLog.log("output:", $scope.outputFormatRegName);
                LumensLog.log("outut format:", $scope.outputSelectedFormatName);
                if ("left" === side) {
                    if ($scope.displaySourceFormatList.length > 1 &&
                    $scope.inputSelectedFormatName &&
                    $scope.inputSelectedFormatName !== "" &&
                    $scope.displaySourceFormatList) {
                        var displaySourceFormatList = [];
                        for (var i = 0; i < $scope.displaySourceFormatList.length; ++i) {
                            if ($scope.displaySourceFormatList[i].format[0].name === $scope.inputSelectedFormatName) {
                                displaySourceFormatList.push($scope.displaySourceFormatList[i]);
                                $scope.displaySourceFormatList = displaySourceFormatList;
                                break;
                            }
                        }
                    } else {
                        $scope.displaySourceFormatList = $scope.sourceFormatList;
                    }
                } else if ("right" === side) {
                    if ($scope.displayTargetFormatList.length > 1 &&
                    $scope.outputSelectedFormatName &&
                    $scope.outputSelectedFormatName !== "" &&
                    $scope.displayTargetFormatList) {
                        var displayTargetFormatList = [];
                        for (var i = 0; i < $scope.displayTargetFormatList.length; ++i) {
                            if ($scope.displayTargetFormatList[i].format[0].name === $scope.outputSelectedFormatName) {
                                displayTargetFormatList.push($scope.displayTargetFormatList[i]);
                                $scope.displayTargetFormatList = displayTargetFormatList;
                                break;
                            }
                        }
                    } else {
                        $scope.displayTargetFormatList = $scope.targetFormatList;
                    }
                }
            }
        }
    }
})
.controller("RuleScriptCtrl", function ($scope, RuleRegister, FormatRegister) {
    $scope.ruleEditorService.addScope($scope);
    $scope.setScript = function (script) {
        if (!script)
            script = "";
        $scope.transformRuleScriptEditor.setValue(script);
    };
    $scope.onCommand = function (id_script_btn) {
        if (id_script_btn === "id_script_apply") {
            $scope.ruleEditorService.sync($scope.transformRuleScriptEditor.getValue());
        }
        else if (id_script_btn === "id_script_validate") {
            LumensLog.log("Validate transform_rule_entity:", $scope.transformRuleEntity.transformRuleEntry);
        }
        else if (id_script_btn === "id_script_back") {
            $scope.transformEditPanel.remove();
            $scope.ruleEditorService.destroy();
            Lumens.system.workspaceLayout.show();
        }
        else if (id_script_btn === "id_rule_fmt_save") {
            FormatRegister.build($scope);
        }
    }
    LumensLog.log("In RuleScriptCtrl");
})
.controller("FormatRegistryCtrl", function ($scope, $element) {
    if ($scope.selectedSide === 'left') {
        $scope.registeredFormatName = $scope.inputFormatRegName;
        $scope.selectedFormatName = $scope.inputSelectedFormatName
    }
    else if ($scope.selectedSide === 'right') {
        $scope.registeredFormatName = $scope.outputFormatRegName;
        $scope.selectedFormatName = $scope.outputSelectedFormatName
    }

    $scope.saveFormatRegistry = function () {
        if ($scope.selectedSide === 'left') {
            $scope.$parent.inputFormatRegName = $scope.registeredFormatName;
            $scope.$parent.inputSelectedFormatName = $scope.selectedFormatName;
        }
        else if ($scope.selectedSide === 'right') {
            $scope.$parent.outputFormatRegName = $scope.registeredFormatName;
            $scope.$parent.outputSelectedFormatName = $scope.selectedFormatName;
        }
        $element.modal("hide");
    }
})
.controller("RuleRegistryCtrl", function ($scope, $element) {
    $scope.saveRuleRegistry = function () {
        $scope.$parent.ruleRegName = $scope.registeredRuleName;
        $element.modal("hide");
    }
})
;