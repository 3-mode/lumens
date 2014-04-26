'use strict';

/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.services = angular.module('lumens-services', ['ngResource']);
// Services
Lumens.services.factory('ProjectList', function($resource) {
    return $resource("../rest/projects", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('ProjectById', function($resource) {
    return $resource("../rest/projects/:project_id", {}, {
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
