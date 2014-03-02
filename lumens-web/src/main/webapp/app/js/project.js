/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.ProjectImporter = Class.$extend({
    __init__: function(compCagegory, componentPanel) {
        this.projectServiceUrl = "app/mock/get_project.json";
        this.compCagegory = compCagegory;
        this.componentPanel = componentPanel;
    },
    importById: function(projectId) {
        var __this = this;
        $.get(this.projectServiceUrl, function(projectData) {
            if (projectData.content && projectData.content.project && projectData.content.project.length > 0) {
                var compDict = {};
                var project = $.parseJSON(projectData.content.project[0].data).project;
                $.each(project.datasource, function() {
                    compDict[this.name] = __this.componentPanel.addComponent(this.position, __this.compCagegory[this.id]);
                });
                $.each(project.transformator, function() {
                    compDict[this.name] = __this.componentPanel.addComponent(this.position, __this.compCagegory[this.id]);
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