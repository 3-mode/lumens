/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.ProjectOperator = Class.$extend({
    __init__: function(compCagegory, componentPanel, $scope) {
        this.compCagegory = compCagegory;
        this.componentPanel = componentPanel;
        this.$scope = $scope;
    },
    create: function() {
        console.log("Create a new project !");
        var __this = this;
        this.componentPanel.emptyComponent();
        this.$scope.project = {};
    },
    import: function(projectData) {
        console.log("Open a project !");
        var __this = this;
        this.componentPanel.emptyComponent();
        if (projectData.content && projectData.content.project && projectData.content.project.length > 0) {
            var compDict = {};
            var project = this.$scope.project = $.parseJSON(projectData.content.project[0].data).project;
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