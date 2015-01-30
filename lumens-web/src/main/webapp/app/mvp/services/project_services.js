/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.services.factory('ProjectService', ['FormatByPath', 'ViewUtils', function (FormatByPath, ViewUtils) {
        return {
            save: function (projectJSON, onResponse) {
                var projectData = {
                    action: projectJSON.projectId ? "update" : "create",
                    content: angular.toJson({project: projectJSON.project})
                };
                LumensLog.log("Saved data:", projectData);
                $http.post(projectJSON.projectId ? "rest/project/" + projectJSON.projectId : "rest/project", projectData).success(onResponse);
            }
        };
    }
]);
