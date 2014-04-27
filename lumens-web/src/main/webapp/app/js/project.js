/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.ProjectImporter = Class.$extend({
    __init__: function(compCagegory, componentPanel, $scope) {
        this.projectServiceUrl = "app/mock/json/get_project.json";
        this.compCagegory = compCagegory;
        this.componentPanel = componentPanel;
        this.$scope = $scope;
    },
    importById: function(projectId) {
        var __this = this;
        $.getJSON(this.projectServiceUrl, function(projectData) {
            if (projectData.content && projectData.content.project && projectData.content.project.length > 0) {
                var compDict = {};
                var project = $.parseJSON(projectData.content.project[0].data).project;
                // ==================== Update angular JS data model ====================
                __this.$scope.$apply(function() {
                    __this.$scope.project = project;
                });
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
        });
        return this;
    }
});

Lumens.ProjectExporter = Class.$extend({
    __init__: function() {

    }
});