'use strict';

/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
// Services
Lumens.services.factory('DesignNavMenu', function ($resource) {
    return $resource("app/config/json/desgin_nav_menu.json", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('DesignButtons', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/design_command_tmpl.html");
});
Lumens.services.factory('ErrorTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/error_tmpl.html");
});
Lumens.services.factory('WarningTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/warning_tmpl.html");
});
Lumens.services.factory('SuccessTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/success_tmpl.html");
});
Lumens.services.factory('SmallMessageTmplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/small_message_tmpl.html");
});
Lumens.services.factory('PropFormTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/property_form_tmpl.html");
});
Lumens.services.factory('TransformListTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/transform_list_tmpl.html");
});
Lumens.services.factory('TransformEditTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/transform_edit_tmpl.html");
});
Lumens.services.factory('ProjectListModal', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/project_list_modal_tmpl.html");
});
Lumens.services.factory('ProjectCreateModal', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/project_create_modal_tmpl.html");
});
Lumens.services.factory('FormatRegistryModal', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/format_registry_modal_tmpl.html");
});
Lumens.services.factory('RuleRegistryModal', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/rule_registry_modal_tmpl.html");
});
Lumens.services.factory('ScriptEditTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/script_edit_tmpl.html");
});
Lumens.services.factory('RuleEditTemplate', function ($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/designer/rule_edit_tmpl.html");
});
Lumens.services.factory('DatasourceCategory', function ($resource) {
    return $resource("rest/category/component", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('InstrumentCategory', function ($resource) {
    return $resource("app/config/json/instrument_category.json", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('ProjectList', function ($resource) {
    return $resource("rest/project", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('ProjectById', function ($resource) {
    return $resource("rest/project/:project_id", {project_id: '@project_id'}, {
        get: {method: 'GET', isArray: false},
        operate: {method: 'POST', isArray: false}
    });
});
Lumens.services.factory('ProjectSave', function ($http) {
    return {
        save: function (projectJSON, onResponse) {
            var projectData = {
                action: projectJSON.projectId ? "update" : "create",
                content: JSON.stringify({project: projectJSON.project})
            };
            LumensLog.log("Saved data:", projectData);
            $http.post(projectJSON.projectId ? "rest/project/" + projectJSON.projectId : "rest/project", projectData).success(onResponse);
        }
    };
});
Lumens.services.factory('FormatList', function ($resource) {
    return $resource("rest/project/:project_id/format?component_id=:component_id&direction=:direction", {}, {
        get: {method: 'GET', isArray: false},
        getIN: {method: 'GET', params: {direction: 'IN'}, isArray: false},
        getOUT: {method: 'GET', params: {direction: 'OUT'}, isArray: false}
    });
});
Lumens.services.factory('JobList', function ($resource) {
    return $resource("app/mock/json/job_list.json?pagesize=:pagesize", {}, {
        get: {method: 'GET', params: {pagesize: '50'}, isArray: false}
    });
});
Lumens.services.factory('JobConfig', function ($resource) {
    return $resource("app/config/json/job_config.json?pagesize=:pagesize", {});
});
Lumens.services.factory('ManageNavMenu', function ($resource) {
    return $resource("app/config/json/manage_nav_menu.json", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('CpuCount', function ($resource) {
    return $resource("rest/server_resources/cpu_perc", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('CpuPerc', function ($resource) {
    return $resource("rest/server_resources/cpu_perc", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('CpuCount', function ($resource) {
    return $resource("rest/server_resources/cpu_count", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('MemPerc', function ($resource) {
    return $resource("rest/server_resources/mem_perc", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('Disk', function ($resource) {
    return $resource("rest/server_resources/disk", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('SyncGet', function () {
    return {
        get: function (url, contentType) {
            var result;
            $.ajax({
                async: false,
                url: url,
                contentType: contentType
            }).done(function (data) {
                result = data;
            });
            return result;
        }
    };
});
Lumens.services.factory('TemplateService', function () {
    return {
        getItems: function (items) {
            for (var i = 0; i < items.length; ++i) {
                if (items[i].html_url) {
                    $.ajax({
                        async: false,
                        url: items[i].html_url,
                        contentType: "plain/text"
                    }).done(function (data) {
                        items[i].html = data;
                    });
                }
            }
        },
        get: function (templateUrl) {
            var templateText = "";
            $.ajax({
                async: false,
                url: templateUrl,
                contentType: "plain/text"
            }).done(function (data) {
                templateText = data;
            });
            return templateText;
        }
    };
});
Lumens.services.factory('FormatByPath', function ($resource) {
    return $resource("rest/project/:project_id/format?component_id=:component_id&format_name=:format_name&format_path=:format_path&direction=:direction", {}, {
        get: {method: 'GET', isArray: false}
    });
});
Lumens.services.factory('TestExecLogService', function ($resource) {
    return $resource("rest/project/testexec/log?project_id=:project_id&component_id=:component_id", {}, {
        get: {method: 'GET', isArray: false},
        delete: { method: "DELETE", isArray: false }
    });
});
