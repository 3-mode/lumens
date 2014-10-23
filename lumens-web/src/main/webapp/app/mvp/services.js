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
        pathEnding: "+-*/ &|!<>\n\r\t^%=;:?,",
        build: function($scope) {
            var inRegName = $scope.inputFormatRegName;
            var inSelectedName = $scope.inputSelectedFormatName;
            var outRegName = $scope.outputFormatRegName;
            var ruleRegName = $scope.ruleRegName;
            var outSelectedName = $scope.outputSelectedFormatName;
            console.log("Rule entity:", $scope.transformRuleEntity);
            var tranformRuleEntry = $scope.transformRuleEntity.transformRuleEntry;
            tranformRuleEntry.name = ruleRegName;
            tranformRuleEntry.source_name = inRegName;
            tranformRuleEntry.target_name = outRegName;
            var selectedSourceFormat = this.findRootFormat($scope.displaySourceFormatList, inSelectedName);
            var selectedTargetFormat = this.findRootFormat($scope.displayTargetFormatList, outSelectedName);
            this.rootSourceFormat = this.duplicateFormat(selectedSourceFormat);
            this.rootTargetFormat = this.duplicateFormat(selectedTargetFormat);
            if ($scope.displayTargetFormatList) {
                // TODO Build the registedformat tree
                if (tranformRuleEntry.transform_rule.transform_rule_item) {
                    this.buildRegistedFormat(tranformRuleEntry.transform_rule.transform_rule_item, $scope.displayTargetFormatList, selectedSourceFormat)
                }
            }
            console.log("Completed source and target building:\n", this.rootSourceFormat, this.rootTargetFormat);
            return {
                sourceFormat: this.rootSourceFormat,
                targetFormat: this.rootTargetFormat,
                ruleEntry: tranformRuleEntry
            };
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
            if (!ruleItem || !targetFormats || !sourceFormat)
                return;
            var refTargetFormat = this.findRootFormat(targetFormats, ruleItem.format_name);
            if (ruleItem.script) {
                var sourceFormatPaths = this.parseScriptFindSourceFormat(ruleItem.script);
                console.log("Source format paths:", sourceFormatPaths);
                this.buildRegistedSourceFormat(this.rootSourceFormat, sourceFormat, sourceFormatPaths);
            }
            var ruleItems = ruleItem.transform_rule_item;
            if (ruleItems) {
                for (var i = 0; i < ruleItems.length; ++i)
                    this.buildRegistedTargetFormat(this.rootTargetFormat, ruleItems[i], refTargetFormat.format, this.rootSourceFormat, sourceFormat);
            }
        },
        buildRegistedTargetFormat: function(registedParentFormat, ruleItem, targetFormats, registedSourceParentFormat, sourceFormat) {
            if (!ruleItem || !targetFormats || !registedParentFormat)
                return false;
            if (ruleItem.script) {
                var sourceFormatPaths = this.parseScriptFindSourceFormat(ruleItem.script);
                console.log("Source format paths:", sourceFormatPaths);
                this.buildRegistedSourceFormat(registedSourceParentFormat, sourceFormat, sourceFormatPaths);
            }
            var childFormat1 = this.findChildFormat(this.rootTargetFormat.format, ruleItem.format_name);
            var childFormat2 = this.findChildFormat(targetFormats, ruleItem.format_name);
            if (!childFormat2)
                return false;
            if (!childFormat1) {
                childFormat1 = {
                    form: childFormat2.form,
                    name: childFormat2.name,
                    property: childFormat2.property,
                    type: childFormat2.type
                };
                if (!registedParentFormat.format)
                    registedParentFormat.format = [childFormat1];
                else
                    registedParentFormat.format.push(childFormat1);
            }
            var ruleItems = ruleItem.transform_rule_item;
            if (ruleItems) {
                for (var i = 0; i < ruleItems.length; ++i)
                    this.buildRegistedTargetFormat(childFormat1, ruleItems[i], childFormat2.format, registedSourceParentFormat, sourceFormat);
            }
            return true;
        },
        buildRegistedSourceFormat: function(registedParentFormat, sourceParentFormat, sourceFormatPaths) {
            if (registedParentFormat && sourceParentFormat) {
                // TODO parse the format paths to build required format for source
                for (var i = 0; i < sourceFormatPaths.length; ++i) {
                    var path = new Lumens.FormatPath(sourceFormatPaths[i]);
                    if (!this.buildRegisterSourceChildFormatFromOrignalFormat(registedParentFormat, sourceParentFormat, path, 0))
                        continue;
                }
            }
        },
        buildRegistedSourceChildFormat: function(registedParentFormat, sourceParentFormat, path, tokenIdx) {
            for (var i = tokenIdx; i < path.tokenCount(); ++i) {
                if (!this.buildRegisterSourceChildFormatFromOrignalFormat(registedParentFormat, sourceParentFormat, path, tokenIdx))
                    continue;
            }
        },
        buildRegisterSourceChildFormatFromOrignalFormat: function(registedParentFormat, sourceParentFormat, path, tokenIdx) {
            var childFormat1 = this.findChildFormat(registedParentFormat.format, path.token(tokenIdx));
            var childFormat2 = this.findChildFormat(sourceParentFormat.format, path.token(tokenIdx));
            if (!childFormat2)
                return false;
            if (!childFormat1) {
                childFormat1 = {
                    form: childFormat2.form,
                    name: childFormat2.name,
                    property: childFormat2.property,
                    type: childFormat2.type
                };
                if (!registedParentFormat.format)
                    registedParentFormat.format = [childFormat1];
                else
                    registedParentFormat.format.push(childFormat1);
            }
            if (childFormat1)
                this.buildRegistedSourceChildFormat(childFormat1, childFormat2, path, tokenIdx + 1);
            return true;
        },
        parseScriptFindSourceFormat: function(strScript) {
            // The same algorithm to see lumens-processor module's JavaScriptBuilder.java
            console.log("Parsing script:", strScript);
            var paths = [];
            var bQuote = false;
            for (var i = 0; i < strScript.length; ++i) {
                var c = strScript.charAt(i);
                if (c === '\"')
                    bQuote = !bQuote;
                if (bQuote || c !== '@') {
                    continue;
                } else if (c === '@') { // find all @a.b.c format line in this strScript
                    i++;
                    if (i < strScript.length) {
                        var singleQuote = false;
                        var path = "";
                        while (i < strScript.length) {
                            c = strScript.charAt(i);
                            if (c === '\'')
                                singleQuote = !singleQuote;
                            if (!singleQuote && this.pathEnding.indexOf(c) > -1) {
                                paths.push(path);
                                break;
                            } else {
                                path += c;
                                ++i;
                                if (i === strScript.length)
                                    paths.push(path);
                            }
                        }
                    }
                }
            }
            return paths;
        },
        findRootFormat: function(formatList, formatName) {
            if (formatList) {
                for (var i = 0; i < formatList.length; ++i)
                    if (formatList[i].format[0].name === formatName)
                        return formatList[i].format[0];
            }
            return null;
        },
        findChildFormat: function(formatList, formatName) {
            if (formatList) {
                for (var i = 0; i < formatList.length; ++i)
                    if (formatList[i].name === formatName)
                        return formatList[i];
            }
            return null;
        }
    }
});
