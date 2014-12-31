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
                formatTree: displayFormatList.formatTree,
                format_entity: validDisplayFmtList
            };
        },
        updateExpandStatus: function (evt, treeNode) {
            if (treeNode) {
                console.log(treeNode);
                var target = $(evt.target);
                if (target.hasClass("lumens-icon2-expand") || target.hasClass("lumens-icon2-collapse")) {
                    target.toggleClass("lumens-icon2-expand").toggleClass("lumens-icon2-collapse");
                    var entryList = treeNode.getEntryList();
                    var childKeys = Object.keys(entryList.map);
                    for (var i in childKeys) {
                        var childNode = entryList.map[childKeys[i]];
                        childNode.toggleContent(true);
                        this.updateChildrenExpandStatus(childNode.getChildList());
                    }
                }
            }
        },
        updateChildrenExpandStatus: function (childList) {
            if (!childList || !childList.map)
                return;
            var childKeys = Object.keys(childList.map);
            for (var i in childKeys) {
                var childNode = childList.map[childKeys[i]];
                childNode.toggleContent(true);
                this.updateChildrenExpandStatus(childNode.getChildList());
            }
        }
    };
});