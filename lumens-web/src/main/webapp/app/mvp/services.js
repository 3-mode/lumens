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
Lumens.services.factory('ProjectListModal', function($http, $q) {
    return {
        get: function(onResponse) {
            var deferred = $q.defer();
            $http.get("app/templates/projects_modal_tmpl.html").then(function(response) {
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
