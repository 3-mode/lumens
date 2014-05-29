/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.ProjectOperator = Class.$extend({
    __init__: function(compCagegory, componentPanel, $scope) {
        this.compCagegory = compCagegory;
        this.componentPanel = componentPanel;
        this.$scope = $scope;
    },
    isValid: function() {
        return this.$scope.project
    },
    close: function() {
        this.componentPanel.emptyComponent();
        this.projectId = undefined;
        this.$scope.componentForm = undefined;
        this.$scope.componentProps = {Name: {value: "to select"}};
        this.$scope.categoryInfo = {name: "to select"};
        this.$scope.currentComponent = {name: "to select"};
    },
    create: function(name, description) {
        this.componentPanel.emptyComponent();
        this.projectId = undefined;
        this.$scope.project = {
            name: name,
            description: description,
            datasource: [],
            transformator: [],
            start_entry: []
        };
        this.$scope.componentForm = undefined;
        this.$scope.componentProps = {Name: {value: "to select"}};
        this.$scope.categoryInfo = {name: "to select"};
        this.$scope.currentComponent = {name: "to select"};
    },
    get: function() {
        return {
            projectId: this.projectId,
            project: this.$scope.project
        };
    },
    add: function(component, type) {
        var project = this.$scope.project;
        if (type === "datasource")
            project.datasource.push(component);
        else if (type === "transformator")
            project.transformator.push(component)
        else
            console.error("Not supported type", component);
    },
    sync: function() {
        // Sync data from client before saving the project
        console.log("Sync data from web client before saving the proejct '" + this.$scope.project.name + "'");
        var componentList = this.componentPanel.getComponentList();
        $.each(componentList, function() {
            var curComp = this;
            curComp.configure.component_info.position = curComp.getXY();
            // Reset the target list
            curComp.configure.component_info.target = [];
            $.each(this.getToLinkList(), function() {
                var targetComp = this.getTo();
                curComp.configure.component_info.target.push({name: targetComp.configure.component_info.name});
            });
        })
    },
    setId: function(projectId) {
        this.projectId = projectId;
    },
    import: function(projectData) {
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
            $.each(project.datasource, function() {
                compDict[this.name] = __this.componentPanel.addComponent(this.position, __this.compCagegory[this.id], this);
            });
            $.each(project.transformator, function() {
                compDict[this.name] = __this.componentPanel.addComponent(this.position, __this.compCagegory[this.id], this);
            });
            $.each(project.datasource, function() {
                if (compDict[this.name]) {
                    for (var i = 0; i < this.target.length; ++i) {
                        if (compDict[this.target[i].name])
                            new Lumens.Link().from(compDict[this.name]).to(compDict[this.target[i].name]).draw();
                    }
                }
            });
            $.each(project.transformator, function() {
                if (compDict[this.name]) {
                    for (var i = 0; i < this.target.length; ++i) {
                        if (compDict[this.target[i].name])
                            new Lumens.Link().from(compDict[this.name]).to(compDict[this.target[i].name]).draw();
                    }
                }
            });
        }
    }
});