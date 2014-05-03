'use strict';

/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.services = angular.module('lumens-services', ['ngResource']);
// Services
Lumens.services.factory('DesignNavMenu', function($resource) {
    return $resource("app/config/desgin_nav_menu.json", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('DesignButtons', function($http, $q) {
    return {
        get: function(onResponse) {
            var deferred = $q.defer();
            $http.get("app/templates/design_command_tmpl.html").then(function(response) {
                deferred.resolve(response.data);
                if (onResponse)
                    onResponse(response.data);
            });
            return deferred.promise;
        }};
});
Lumens.services.factory('WarningTemplate', function($http, $q) {
    return {
        get: function(onResponse) {
            var deferred = $q.defer();
            $http.get("app/templates/warning_tmpl.html").then(function(response) {
                deferred.resolve(response.data);
                if (onResponse)
                    onResponse(response.data);
            });
            return deferred.promise;
        }};
});
Lumens.services.factory('SuccessTemplate', function($http, $q) {
    return {
        get: function(onResponse) {
            var deferred = $q.defer();
            $http.get("app/templates/success_tmpl.html").then(function(response) {
                deferred.resolve(response.data);
                if (onResponse)
                    onResponse(response.data);
            });
            return deferred.promise;
        }};
});
Lumens.services.factory('PropFormTemplate', function($http, $q) {
    return {
        get: function(onResponse) {
            var deferred = $q.defer();
            $http.get("app/templates/property_form_tmpl.html").then(function(response) {
                deferred.resolve(response.data);
                if (onResponse)
                    onResponse(response.data);
            });
            return deferred.promise;
        }};
});
Lumens.services.factory('ProjectListModal', function($http, $q) {
    return {
        get: function(onResponse) {
            var deferred = $q.defer();
            $http.get("app/templates/project_list_modal_tmpl.html").then(function(response) {
                deferred.resolve(response.data);
                if (onResponse)
                    onResponse(response.data);
            });
            return deferred.promise;
        }};
});

Lumens.services.factory('ProjectCreateModal', function($http, $q) {
    return {
        get: function(onResponse) {
            var deferred = $q.defer();
            $http.get("app/templates/project_create_modal_tmpl.html").then(function(response) {
                deferred.resolve(response.data);
                if (onResponse)
                    onResponse(response.data);
            });
            return deferred.promise;
        }};
});
Lumens.services.factory('DatasourceCategory', function($resource) {
    return $resource("rest/category/component", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('InstrumentCategory', function($resource) {
    return $resource("app/mock/json/instrument_category.json", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('ProjectList', function($resource) {
    return $resource("rest/project", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('ProjectById', function($resource) {
    return $resource("rest/project/:project_id", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('ProjectSave', function($http) {
    return {
        save: function(projectJSON, onResponse) {
            var projectData = {
                action: projectJSON.projectId ? "update" : "create",
                content: JSON.stringify({project: projectJSON.project})
            };
            console.log("Saved data:", projectData);
            $http.post(projectJSON.projectId ? "rest/project/" + projectJSON.projectId : "rest/project", projectData).success(onResponse);
        }
    };
});
Lumens.services.factory('FormatList', function($resource) {
    return $resource("app/mock/json/db_format_list_response.json?component_name=:component_name&direction=:direction", {}, {
        getIN: {method: 'GET', params: {direction: 'IN'}, isArray: false},
        getOUT: {method: 'GET', params: {direction: 'OUT'}, isArray: false}
    });
});
Lumens.services.factory('FormatByPath', function($resource) {
    return $resource("app/mock/json/db_format_list_response.json?component_name=:component_name&format_name=:format_name&format_path=:format_path&direction=:direction", {}, {
        getIN: {method: 'GET', params: {direction: 'IN'}, isArray: false},
        getOUT: {method: 'GET', params: {direction: 'OUT'}, isArray: false}
    });
});
