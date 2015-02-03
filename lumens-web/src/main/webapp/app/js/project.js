/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.ProjectOperator = Class.$extend({
    __init__: function (compCagegory, componentPanel, $scope) {
        this.compCagegory = compCagegory;
        this.componentPanel = componentPanel;
        this.$scope = $scope;
    },
    isValid: function () {
        return this.$scope.project
    },
    close: function () {
        this.componentPanel.emptyComponent();
        this.projectId = undefined;
        this.$scope.componentForm = undefined;
        this.$scope.componentProps = {Name: {value: "to select"}};
        this.$scope.categoryInfo = {name: "to select"};
        this.$scope.currentComponent = {name: "to select"};
    },
    create: function (name, description) {
        this.componentPanel.emptyComponent();
        this.projectId = undefined;
        this.$scope.project = {
            name: name,
            description: description,
            datasource: [],
            transformer: [],
            start_entry: []
        };
        this.$scope.componentForm = null;
        this.$scope.componentProps = {Name: {value: "to select"}};
        this.$scope.categoryInfo = {name: "to select"};
        this.$scope.currentComponent = {name: "to select"};
        this.$scope.$broadcast("InitProject");
    },
    get: function () {
        return {
            projectId: this.projectId,
            project: this.$scope.project
        };
    },
    add: function (component) {
        var project = this.$scope.project;
        if (component.isDataSource())
            project.datasource.push(component.getCompData());
        else if (component.isTransformer())
            project.transformer.push(component.getCompData())
        else
            console.error("Not supported type", component);
    },
    sync: function () {
        // Sync data from client before saving the project
        console.log("Sync data from web client before saving the project '" + this.$scope.project.name + "'");
        var componentList = this.componentPanel.getComponentList();
        $.each(componentList, function () {
            var curComp = this;
            curComp.getCompData().position = curComp.getXY();
            // Reset the target list
            curComp.getCompData().target = [];
            $.each(this.getToLinkList(), function () {
                var targetComp = this.getTo();
                curComp.getCompData().target.push({id: targetComp.getCompData().id});
            });
        });
        this.$scope.project.start_entry = this.getStartEntryList();
    },
    getContainedStartEntry: function (entry, startEntryList) {
        for (var i in startEntryList) {
            // Check if saved start entry is valid still.
            if (entry.component_id === startEntryList[i].component_id
            && entry.format_name === startEntryList[i].format_name) {
                return startEntryList[i];
            }
        }
    },
    discoverSavedValidStartEntry: function (discoverStartEntryList) {
        var validStartEntryList = [];
        var projectStartEntryList = this.$scope.project.start_entry;
        for (var i in projectStartEntryList) {
            var entry = this.getContainedStartEntry(projectStartEntryList[i], discoverStartEntryList);
            if (entry)
                validStartEntryList.push(entry);
        }
        return validStartEntryList;
    },
    updateStartEntryListFromDiscoverStartEntryList: function (saveValidStartEntryList, discoverStartEntryList) {
        for (var i in discoverStartEntryList) {
            var entry = this.getContainedStartEntry(discoverStartEntryList[i], saveValidStartEntryList)
            if (!entry)
                saveValidStartEntryList.push(entry);
        }
    },
    getStartEntryList: function () {
        var discoverStartEntryList = [];
        var componentList = this.componentPanel.getComponentList();
        // Get all avialiable start entries
        for (var i in componentList) {
            var comp = componentList[i];
            if (comp.isTransformer()) {
                var ruleList = comp.getRuleEntryList();
                for (var j in ruleList) {
                    var rule = ruleList[j];
                    if (!rule.source_id || rule.source_id === '0' || rule.source_id === comp.getId())
                        discoverStartEntryList.push({
                            component_id: comp.getId(),
                            component_name: comp.getShortDescription(),
                            format_name: rule.name
                        });
                }
            }
            else if (comp.isDataSource()) {
                var outFmtList = comp.getFormatEntryList("OUT");
                for (var j in outFmtList) {
                    var outFmt = outFmtList[j];
                    if (!comp.getInputFormat(outFmt.name))
                        discoverStartEntryList.push({
                            component_id: comp.getId(),
                            component_name: comp.getShortDescription(),
                            format_name: outFmt.name
                        });
                }
            }
        }
        // Check loaded start entries if they are avialiable
        var saveValidStartEntryList = this.discoverSavedValidStartEntry(discoverStartEntryList);
        // Add the new start entries into the start entry list
        this.updateStartEntryListFromDiscoverStartEntryList(saveValidStartEntryList, discoverStartEntryList);
        return saveValidStartEntryList;
    },
    setId: function (projectId) {
        this.projectId = projectId;
    },
    import: function (projectData) {
        console.log("<============= Opening a project ==============>");
        var __this = this;
        __this.close();
        this.componentPanel.emptyComponent();
        if (projectData.content && projectData.content.project && projectData.content.project.length > 0) {
            var compDict = {};
            __this.projectId = projectData.content.project[0].id;
            var project = this.$scope.project = $.parseJSON(projectData.content.project[0].data).project;
            console.log("Opened project:", project);
            // ==================== Update angular JS data model ====================
            $.each(project.datasource, function () {
                compDict[this.id] = __this.componentPanel.addComponent(this.position, __this.compCagegory[this.type], this);
            });
            $.each(project.transformer, function () {
                compDict[this.id] = __this.componentPanel.addComponent(this.position, __this.compCagegory[this.type], this);
            });
            $.each(project.datasource, function () {
                if (compDict[this.id]) {
                    for (var i = 0; i < this.target.length; ++i) {
                        if (compDict[this.target[i].id])
                            new Lumens.Link().from(compDict[this.id]).to(compDict[this.target[i].id]).draw();
                    }
                }
            });
            $.each(project.transformer, function () {
                if (compDict[this.id]) {
                    for (var i = 0; i < this.target.length; ++i) {
                        if (compDict[this.target[i].id])
                            new Lumens.Link().from(compDict[this.id]).to(compDict[this.target[i].id]).draw();
                    }
                }
            });

            var compList = __this.componentPanel.getComponentList();
            $.each(compList, function () {
                if (this.isDataSource()) {
                    this.changeStatus(function () {
                        console.log("open or close");
                        return true;
                    });
                }
            });
        }
        this.$scope.$broadcast("InitProject");
    }
});