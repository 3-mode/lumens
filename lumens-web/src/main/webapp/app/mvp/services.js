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
    return createGetTemplateObject($http, $q, "app/templates/design_command_tmpl.html");
});
Lumens.services.factory('ErrorTemplate', function($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/error_tmpl.html");
});
Lumens.services.factory('WarningTemplate', function($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/warning_tmpl.html");
});
Lumens.services.factory('SuccessTemplate', function($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/success_tmpl.html");
});
Lumens.services.factory('SmallMessageTmplate', function($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/small_message_tmpl.html");
});
Lumens.services.factory('PropFormTemplate', function($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/property_form_tmpl.html");
});
Lumens.services.factory('TransformListTemplate', function($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/transform_list_tmpl.html");
});
Lumens.services.factory('TransformEditTemplate', function($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/transform_edit_tmpl.html");
});
Lumens.services.factory('ProjectListModal', function($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/project_list_modal_tmpl.html");
});
Lumens.services.factory('ProjectCreateModal', function($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/project_create_modal_tmpl.html");
});
Lumens.services.factory('FormatRegistryModal', function($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/format_registry_modal_tmpl.html");
});
Lumens.services.factory('RuleRegistryModal', function($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/rule_registry_modal_tmpl.html");
});
Lumens.services.factory('ScriptEditTemplate', function($http, $q) {
    return createGetTemplateObject($http, $q, "app/templates/script_edit_tmpl.html");
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
    return $resource("rest/project/:project_id", {project_id: '@project_id'}, {
        get: {method: 'GET', isArray: false},
        operate: {method: 'POST', isArray: false}
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
    return $resource("rest/project/:project_id/format?component_name=:component_name&direction=:direction", {}, {
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
Lumens.services.factory('RuleEditorService', function() {
    return {
        create: function() {
            var scopes = [];
            return {
                addScope: function(scope) {
                    scopes.push(scope);
                },
                destroy: function() {
                    $.each(scopes, function(i) {
                        scopes[i].$destroy();
                    });
                    scopes = [];
                },
                sync: function(script) {
                    $.each(scopes, function(i) {
                        scopes[i].setScript(script);
                    });
                }
            }
        }
    }
});
Lumens.services.factory('RuleRegister', function() {
    return {
        build: function($scope, message) {
            console.log(message, $scope);
        }
    }
});
Lumens.services.factory('FormatRegister', function() {
    return {
        build: function($scope) {
            var inRegName = $scope.inputFormatRegName;
            var inSelectedName = $scope.inputSelectedFormatName;
            var outRegName = $scope.outputFormatRegName;
            var outSelectedName = $scope.outputSelectedFormatName;
            console.log("Rule entity:", $scope.transformRuleEntity);
            var tranformRuleEntry = $scope.transformRuleEntity.transformRuleEntry;
            var selectedSourceFormat = this.findFormat($scope.displaySourceFormatList, inSelectedName);
            var selectedTargetFormat = this.findFormat($scope.displayTargetFormatList, outSelectedName);
            this.rootSourceFormat = this.duplicateFormat(selectedSourceFormat);
            this.rootTargetFormat = this.duplicateFormat(selectedTargetFormat);
            if ($scope.displayTargetFormatList) {
                // TODO Build the registedformat tree
                if (tranformRuleEntry.transform_rule.transform_rule_item) {
                    buildRegistedFormat(tranformRuleEntry.transform_rule.transform_rule_item, $scope.displayTargetFormatList, selectedSourceFormat)
                }
            }
            if (selectedSourceFormat) {
                // TODO Build the registedformat tree
            }
        },
        // Private memeber functions
        duplicateFormat: function(format) {
            return  {
                form: format.form,
                name: format.name,
                property: format.property,
                type: format.type
            };
        },
        buildRegistedFormat: function(ruleItem, targetFormats, sourceFormat) {
            var refTargetFormat = this.findFormat(targetFormats, ruleItem.format_name);
            if (ruleItem.script) {
                var sourceFormatPaths = parseScriptFindSourceFormat(ruleItem.script);
                buildRegistedSourceFormat(sourceFormat, sourceFormatPaths);
            }
            var ruleItems = ruleItem.transform_rule_item;
            for (var i = 0; i < ruleItems.length; ++i)
                buildRegistedFormat(ruleItems[i], refTargetFormat, sourceFormat);
        },
        buildRegistedSourceFormat: function(sourceFormat, sourceFormatPaths) {
            if (this.rootSourceFormat) {
                // TODO parse the format paths to build required format for source
            }
        },
        findFormat: function(formatList, formatName) {
            for (var i = 0; i < formatList.length; ++i)
                if (formatList[i].format[0].name === formatName)
                    return formatList[i].format[0];
            return null;
        }
    }
});
