/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
Lumens.services.factory('DesignViewUtils', function ($resource) {
    return {
        updateDisplayFormatList: function (displayFormatList, validDisplayFmtList) {
            return {
                project_id: displayFormatList.project_id,
                component_id: displayFormatList.component_id,
                direction: displayFormatList.direction,
                format_entity: validDisplayFmtList
            };
        }
    };
});