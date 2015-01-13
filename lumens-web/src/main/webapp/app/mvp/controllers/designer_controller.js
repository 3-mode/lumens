/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.controllers.controller("DesignViewCtrl", function ($scope, $route, $http, $compile,
DesignNavMenu, SuccessTemplate, WarningTemplate, ErrorTemplate, PropFormTemplate, TransformListTemplate,
DatasourceCategory, InstrumentCategory, TemplateService, DesignButtons, ProjectById) {
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
        applyProperty($scope.componentProps, $scope.currentUIComponent);
        $scope.currentUIComponent.setShortDescription($scope.componentProps.Name.value);
        $scope.$broadcast("ApplyProperty", {UI: $scope.currentUIComponent});
    };

    // Load menu category
    DesignNavMenu.get(function (menu) {
        // Load data source category
        DatasourceCategory.get(function (data_source_items) {
            //Load instrument category
            InstrumentCategory.get(function (instrument_items) {
                menu.sections[0].items = data_source_items.items;
                menu.sections[1].items = instrument_items.items;
                TemplateService.getItems(instrument_items.items);
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
                        if ($scope.currentUIComponent)
                            $scope.currentUIComponent.updateSelect(false);
                        $scope.currentUIComponent = component;
                        $scope.currentUIComponent.updateSelect(true);
                        var config = component.configure;
                        $scope.$apply(function () {
                            $scope.componentForm = $compile(component.getFormHtml())($scope);
                            $scope.categoryInfo = config.category_info;
                            $scope.componentProps = ComponentPropertyList(config);
                            $scope.currentComponent = config.component_info ? config.component_info : $scope.currentComponent
                        });
                        $scope.$broadcast("UIComponentSelect", {UI: component});
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
                // **************************************************************************************************************
                // Try to load the project on local when refresh the currrent view if already there is a project is opened.
                if (sessionStorage.local_project_storage) {
                    var local_project_storage = angular.fromJson(sessionStorage.local_project_storage);
                    ProjectById.get({project_id: local_project_storage.id}, function (projectData) {
                        if ($scope.projectOperator)
                            $scope.projectOperator.import(projectData);
                        if (local_project_storage.is_active)
                            ProjectById.operate({project_id: local_project_storage.id}, {action: 'active'}, function (result) {
                                LumensLog.log(result);
                            });
                    });
                }
                // **************************************************************************************************************
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
                var nameTmpl = '<span class="lumens-icon-project lumens-icon-gap"></span><b>{{project.name}}</b>';
                desgin.designAndInfoPanel.getTitleElement().append($compile(nameTmpl)($scope));
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
                            "<i class='lumens-icon-desc lumens-icon-gap'></i>Description",
                            "<i class='lumens-icon-resource lumens-icon-gap'></i>Resources",
                            "<i class='lumens-icon-instrucment lumens-icon-gap'></i>Instruments"
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
                            $compile('<span data-bind="categoryInfo.name"><i class="lumens-icon-props lumens-icon-gap"></i>{{categoryInfo.name}}</span>')($scope)
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
                function tabExecOnceResult($tabContent) {
                    $tabContent.append($compile(TemplateService.get("app/templates/designer/exec_log_tmpl.html"))($scope));
                }
                desgin.tabs = new Lumens.TabPanel(desgin.tabsContainer.getElement());
                desgin.tabs.configure({
                    tab: [
                        {id: "id-project-info", label: "Project Summary", content: tabSummary},
                        {id: "id-component-selected-props", label: "Component Properties", content: tabConfiguration},
                        {id: "id-component-transformation-list", label: "Transformations", content: tabTransformationList},
                        {id: "id-component-exec-once-result", label: "Execute Once Result", content: tabExecOnceResult}
                    ]
                });
            })
        });
    });
    // <******* Design View ------------------------------------------------------------
})
.controller("DesginCmdCtrl", function ($scope, $element, $compile, Notifier, ProjectListModal, ProjectCreateModal, ProjectSave, ProjectById) {
    // Handle desgin command button event
    LumensLog.log("In DesginCmdCtrl", $element);
    var i18n = $scope.i18n;
    var projectOperator = $scope.projectOperator;
    function Message(response) {
        LumensLog.log(response);
        if (response && response.status === "OK")
            Notifier.message("info", "Success", response.result_content);
        else
            Notifier.message("error", "Error", response.error_message);
    }
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
                        Notifier.message("info", "Success", i18n.id_save_project.format(project.name));
                    }
                    else
                        Notifier.message("error", "Error", i18n.id_save_project.format(project.name));
                });
            }
        }
        else if ("id_active" === id) {
            ProjectById.operate({project_id: projectOperator.get().projectId}, {action: 'active'}, function (response) {
                LumensLog.log(response);
                Message(response);
                sessionStorage.local_project_storage = angular.toJson({
                    id: projectOperator.get().projectId,
                    is_active: true
                });
            });
        }
        else if ("id_deploy" === id) {
            ProjectById.operate({project_id: projectOperator.get().projectId}, {action: 'deploy'}, function (response) {
                Message(response);
            });
        }
        else if ("id_execute" === id) {
            ProjectById.operate({project_id: projectOperator.get().projectId}, {action: 'execute'}, function (response) {
                Message(response);
            });
        }
        else if ("id_delete" === id) {
            ProjectById.operate({project_id: projectOperator.get().projectId}, {action: 'delete'}, function (response) {
                if (response.status === "OK")
                    projectOperator.close();
                Message(response);
            });
        }
        LumensLog.log("Clicked:", id);
    };
})
.controller("ProjectListCtrl", function ($scope, $element, Notifier, ProjectList, ProjectById) {
    var i18n = $scope.i18n;
    var projectOperator = $scope.projectOperator;
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
                sessionStorage.local_project_storage = angular.toJson({
                    id: projectOperator.get().projectId
                });
            });
        }
        else {
            Notifier.message("notice", "Warning", i18n.id_no_project_select_warning)
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
.controller("ProjectCreateCtrl", function ($scope, $element, Notifier) {
    LumensLog.log("In ProjectCreateCtrl", $element);
    var i18n = $scope.i18n;
    var projectInfoContent = $element.find(".modal-body");
    var projectOperator = $scope.projectOperator;
    $scope.createProject = function () {
        if ($scope.projectName) {
            projectOperator.create($scope.projectName, $scope.projectDescription);
            $element.modal("hide");
            Notifier.message("info", "Success", "Created a new project '" + $scope.projectName + "'");
        }
        else
            Notifier.message("notice", "Warning", i18n.id_no_project_name, projectInfoContent);
    };
})
.controller("TransformListCtrl", function ($scope, $compile, $element, TransformEditTemplate) {
    LumensLog.log("In TransformListCtrl", $element);
    $scope.onSelectRow = function (index) {
        $scope.selectIndex = index;
    };
    function showRuleEditor() {
        Lumens.system.workspaceLayout.hide();
        //TODO show to the transform editing
        TransformEditTemplate.get(function (transformEditTemplate) {
            $scope.ruleTreeEditor = $compile(transformEditTemplate)($scope).appendTo(Lumens.system.theLayout.getPart2Element());
            $scope.transformEditPanel = new Lumens.ResizableVSplitLayoutExt($scope.ruleTreeEditor.find("#rulePanelId"))
            .configure({
                mode: "vertical",
                useRatio: true,
                part1Size: "40%"
            });
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
            $scope.currentRuleEntry = null;
            $scope.inputFormatRegName = null;
            $scope.outputFormatRegName = null;
            $scope.ruleRegName = null;
            $scope.inputSelectedFormatName = null;
            $scope.outputSelectedFormatName = null;
            $scope.ruleData = null;
            showRuleEditor();
        } else if (id_btn === "id_rule_delete") {
            // TODO delete transform rulle
            // TODO delete register format from source and target components
            console.log("Delete rule: ", $scope.transformRuleEntryList[$scope.selectIndex]);
            if ($scope.currentUIComponent) {
                $scope.currentUIComponent.removeInputFormat($scope.transformRuleEntryList[$scope.selectIndex].source_format_name);
                $scope.currentUIComponent.removeOutputFormat($scope.transformRuleEntryList[$scope.selectIndex].target_format_name);
                $scope.currentUIComponent.removeRuleEntry($scope.transformRuleEntryList[$scope.selectIndex].name);
            }
        }
    };

    $scope.openTransformEditing = function (ruleEntry) {
        LumensLog.log("Opening rule: ", ruleEntry);
        $scope.currentRuleEntry = ruleEntry;
        $scope.inputFormatRegName = ruleEntry.source_format_name;
        $scope.outputFormatRegName = ruleEntry.target_format_name;
        $scope.ruleRegName = ruleEntry.name;
        var inFormat = $scope.currentUIComponent.hasFrom() ? $scope.currentUIComponent.getInputFormat($scope.inputFormatRegName) : null;
        $scope.inputSelectedFormatName = inFormat ? inFormat.name : null;
        var outFormat = $scope.currentUIComponent.hasTo() ? $scope.currentUIComponent.getOutputFormat($scope.outputFormatRegName) : null;
        $scope.outputSelectedFormatName = outFormat ? outFormat.name : null;
        $scope.ruleData = {rule_entry: ruleEntry, format_entry: outFormat};
        showRuleEditor();
    };

    $scope.backFromTransformEditing = function () {
        $scope.transformEditPanel.remove();
        Lumens.system.workspaceLayout.show();
    };

    // Init the trnasformation rule list to show all rules
    $scope.$on("ApplyProperty", function (evt, data) {
        if (!data.UI || !data.UI.configure)
            return;
        $scope.transformRuleEntryList = data.UI.getRuleEntryList();
        $scope.theSourceNameList = data.UI.hasFrom() ? [data.UI.getFrom(0).getCompData().name] : [data.UI.getCompData().name];
        $scope.theTargetNameList = data.UI.hasTo() ? [data.UI.getTo(0).getCompData().name] : [];
    });
    $scope.$on("UIComponentSelect", function (evt, data) {
        if (!data.UI || !data.UI.configure)
            return;
        $scope.$apply(function () {
            $scope.transformRuleEntryList = data.UI.getRuleEntryList();
            $scope.theSourceNameList = data.UI.hasFrom() ? [data.UI.getFrom(0).getCompData().name] : [data.UI.getCompData().name];
            $scope.theTargetNameList = data.UI.hasTo() ? [data.UI.getTo(0).getCompData().name] : [];
        });
    })
})
.controller("TransformEditCtrl", function ($scope, $element, $compile,
FormatList, RuleEditTemplate, ScriptEditTemplate, FormatRegistryModal, RuleRegistryModal, ViewUtils, Notifier) {
    LumensLog.log("In TransformEditCtrl");
    var i18n = $scope.i18n;
    // Load script editing panel
    RuleEditTemplate.get(function (ruleEditTemplate) {
        $compile(ruleEditTemplate)($scope).appendTo($scope.transformEditPanel.getPart1Element());
    })
    ScriptEditTemplate.get(function (scriptEditTemplate) {
        $compile(scriptEditTemplate)($scope).appendTo($scope.transformEditPanel.getPart2Element());
    })
    $scope.setScript = function (script) {
        if ($scope.currentScriptNode)
            $scope.currentScriptNode.setScript(script);
    }
    var projectOperator = $scope.projectOperator;
    if ($scope.currentComponent && $scope.currentComponent.type === "type-transformer") {
        if ($scope.currentUIComponent.hasFrom()) {
            var sourceComponentID = $scope.currentUIComponent.getFrom(0).getId();
            if (sourceComponentID) {
                FormatList.getOUT({project_id: projectOperator.get().projectId, component_id: sourceComponentID}, function (result) {
                    if (result.status === "OK") {
                        $scope.sourceFormatList = {
                            project_id: projectOperator.get().projectId,
                            component_id: sourceComponentID,
                            direction: 'OUT',
                            format_entity: result.content.format_entity
                        };
                        $scope.displaySourceFormatList = $scope.sourceFormatList;
                        $scope.onCommand("id_format_reg_filter_btn", "left");
                    }
                    else
                        Notifier.message("error", "Error", result.error_message);
                });
            }
        }
        if ($scope.currentUIComponent.hasTo()) {
            var targetComponentID = $scope.currentUIComponent.getTo(0).getId();
            if (targetComponentID) {
                FormatList.getIN({project_id: projectOperator.get().projectId, component_id: targetComponentID}, function (result) {
                    if (result.status === "OK") {
                        $scope.targetFormatList = {
                            project_id: projectOperator.get().projectId,
                            component_id: targetComponentID,
                            direction: 'IN',
                            format_entity: result.content.format_entity
                        };
                        $scope.displayTargetFormatList = $scope.targetFormatList;
                        $scope.onCommand("id_format_reg_filter_btn", "right");
                    } else
                        Notifier.message("error", "Error", result.error_message);
                });
            }
        }

        $scope.onCommand = function (btn_id, side, evt) {
            LumensLog.log(btn_id, side, evt);
            $scope.selectedSide = side;
            if ("id_format_reg_expand_btn" === btn_id) {
                ViewUtils.updateExpandStatus(evt, side === "left" ? $scope.displaySourceFormatList.formatTree : $scope.displayTargetFormatList.formatTree);
            }
            else if ("id_rule_reg_expand_btn" === btn_id) {
                ViewUtils.updateExpandStatus(evt, $scope.ruleData.ruleTree);
            }
            else if ("id_rule_reg_delete_btn" === btn_id) {
                ViewUtils.removeRuleNode($scope.ruleData.ruleTree);
            }
            else if (side && "id_format_reg_edit_btn" === btn_id) {
                if ("left" === side)
                    $scope.currentFormatList = $scope.sourceFormatList ? $scope.sourceFormatList.format_entity : null;
                else if ("right" === side)
                    $scope.currentFormatList = $scope.targetFormatList.format_entity;

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
                if ("left" === side && $scope.displaySourceFormatList) {
                    var formatEntity = $scope.displaySourceFormatList.format_entity;
                    if (formatEntity.length > 1 &&
                    $scope.inputSelectedFormatName &&
                    $scope.inputSelectedFormatName !== "") {
                        var validDisplayFmtList = [];
                        for (var i = 0; i < formatEntity.length; ++i) {
                            if (formatEntity[i].format.name === $scope.inputSelectedFormatName) {
                                validDisplayFmtList.push(formatEntity[i]);
                                $scope.displaySourceFormatList = ViewUtils.updateDisplayFormatList($scope.displaySourceFormatList, validDisplayFmtList);
                                break;
                            }
                        }
                    } else {
                        $scope.displaySourceFormatList = $scope.sourceFormatList;
                    }
                } else if ("right" === side && $scope.displayTargetFormatList) {
                    var formatEntity = $scope.displayTargetFormatList.format_entity;
                    if (formatEntity.length > 1 &&
                    $scope.outputSelectedFormatName &&
                    $scope.outputSelectedFormatName !== "") {
                        var validDisplayFmtList = [];
                        for (var i = 0; i < formatEntity.length; ++i) {
                            if (formatEntity[i].format.name === $scope.outputSelectedFormatName) {
                                validDisplayFmtList.push(formatEntity[i]);
                                $scope.displayTargetFormatList = ViewUtils.updateDisplayFormatList($scope.displayTargetFormatList, validDisplayFmtList);
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
.controller("RuleScriptCtrl", function ($scope, TransformMapperStorageService, Notifier) {
    $scope.onCommand = function (id_script_btn) {
        if (id_script_btn === "id_script_apply") {
            $scope.$broadcast("ApplyScriptToRuleItem");
        }
        else if (id_script_btn === "id_script_validate") {
            LumensLog.log("Validate transform_rule_entry:", $scope.currentRuleEntry);
        }
        else if (id_script_btn === "id_script_back") {
            $scope.transformEditPanel.remove();
            $scope.ruleTreeEditor.remove();
            Lumens.system.workspaceLayout.show();
        }
        else if (id_script_btn === "id_rule_fmt_save") {
            try {
                TransformMapperStorageService.save($scope);
                Notifier.message("info", "Success", "Apply the rule and format configuration successfully");
            } catch (e) {
                Notifier.message("error", "Error", "Failed to apply the rule and format configuration");
            }
        }
        else if (id_script_btn === "id_rule_script") {
            $scope.$broadcast("ScriptEditorDisplay", "show");
            $scope.$broadcast("ScriptConfigDispaly", "hide");
        }
        else if (id_script_btn === "id_rule_configuration") {
            $scope.$broadcast("ScriptEditorDisplay", "hide");
            $scope.$broadcast("ScriptConfigDispaly", "show");
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
            $scope.$parent.orginalInputFormatRegName = $scope.inputFormatRegName
            $scope.$parent.inputFormatRegName = $scope.registeredFormatName;
            $scope.$parent.inputSelectedFormatName = $scope.selectedFormatName;
        }
        else if ($scope.selectedSide === 'right') {
            $scope.$parent.orginalOutputFormatRegName = $scope.outputFormatRegName
            $scope.$parent.outputFormatRegName = $scope.registeredFormatName;
            $scope.$parent.outputSelectedFormatName = $scope.selectedFormatName;
        }
        $element.modal("hide");
    }
})
.controller("RuleRegistryCtrl", function ($scope, $element) {
    $scope.saveRuleRegistry = function () {
        $scope.$parent.orignalRuleRegName = $scope.ruleRegName;
        $scope.$parent.ruleRegName = $scope.registeredRuleName;
        $element.modal("hide");
    }
})
.controller("ExecLogCtrl", function ($scope, $element, TestExecLogService, Notifier) {
    function getCurrentComponentLog() {
        TestExecLogService.get({
            project_id: $scope.projectOperator.get().projectId,
            component_id: $scope.currentUIComponent.getId()
        }, function (resp) {
            $scope.logItemList = resp.result_content.log_items;
            for (var i in $scope.logItemList) {
                $scope.logItemList[i].log_data = $.parseJSON($scope.logItemList[i].log_data);
            }
        });
    }
    $scope.$on("UIComponentSelect", function () {
        if (!$scope.currentUIComponent || $element.is(':hidden'))
            return;
        getCurrentComponentLog();
    });
    $scope.onCommand = function (id_btn) {
        if ("id_exec_log_refresh" === id_btn) {
            getCurrentComponentLog();
        }
        else if ("id_exec_log_clear" === id_btn) {
            TestExecLogService.delete({project_id: $scope.projectOperator.get().projectId}, function (response) {
                if (response.status === 'OK') {
                    Notifier.message("info", "Success", "Clean project execution result log");
                    $scope.logItemList = null;
                }
                else
                    Notifier.message("error", "Error", "Error to clean project execution result log");
            })
        }
    }
});