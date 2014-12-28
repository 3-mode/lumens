/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
function createGetTemplateObject($http, $q, url) {
    return {
        get: function (onResponse) {
            var deferred = $q.defer();
            $http.get(url).then(function (response) {
                deferred.resolve(response.data);
                if (onResponse)
                    onResponse(response.data);
            });
            return deferred.promise;
        }
    };
}

function ComponentPropertyList(config) {
    var category = config.category_info;
    var compinfo = config.component_info;
    var componentProps = {
        "Description": {label: category.i18n.Description, value: (compinfo && compinfo.description) ? compinfo.description : "", type: "String"},
        "Name": {label: category.i18n.Name, value: (compinfo && compinfo.name) ? compinfo.name : "", type: "String"}
    };

    if (!category.property)
        return componentProps;

    $.each(category.property, function () {
        var category_property = this;
        componentProps[category_property.name] = {
            label: category.i18n[category_property.name],
            name: category_property.name,
            type: category_property.type
        }

        for (var i in compinfo.property) {
            var prop = compinfo.property[i];
            if (prop.name === category_property.name) {
                componentProps[category_property.name].value = prop.value;
                break;
            }
        }
    });
    return componentProps;
}

function applyProperty(componentProps, currentUIComponent) {
    var currentComponent = currentUIComponent.getCompData();
    currentComponent.property = [];
    currentComponent.name = componentProps.Name.value;
    currentComponent.description = componentProps.Description.value;
    for (var propName in componentProps) {
        if (propName === "Description" || propName === "Name")
            continue;
        var prop = componentProps[propName];
        currentComponent.property.push({
            name: propName,
            type: prop.type,
            value: prop.value
        });
    }
}

function buildTransformationList($parent, component) {
    console.log("Transformation List:", component);
    var transformationList = component.transform_rule_entry;
    if (transformationList) {
        var IdTitleList = [];
        var contentList = [];
        $.each(transformationList, function () {
            IdTitleList.push(this.name);
            contentList.push($('<div style="padding-left:30px;"><b>' + this.source_name + "--->" + this.target_name + '</b></div>'));
        });
        new Lumens.List($parent).configure({
            IdList: IdTitleList,
            titleList: IdTitleList,
            contentList: contentList
        });
    }
}

function isFormatOf(formatEntryList, formatName) {
    for (var i in formatEntryList)
        if (formatEntryList[i].name === formatName)
            return true;
    return false;
}