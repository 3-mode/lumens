/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
Lumens.services.factory('Notifier', function () {
    return {
        message: function (type, title, text) {
            new PNotify({
                type: type,
                title: title,
                addclass: "lumens-bootstrap",
                text: text,
                delay: 2000,
                buttons: {
                    closer: true
                }
            });
        }
    }
});
Lumens.services.factory('ViewUtils', function ($resource) {
    return {
        removeRuleNode: function (treeNode) {
            var selectNode = treeNode.getSelectNode();
            if (selectNode)
                selectNode.remove();
            console.log(selectNode);
        },
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
                var entryList = treeNode.getEntryList();
                var childKeys = Object.keys(entryList.map);
                for (var i in childKeys) {
                    var childNode = entryList.map[childKeys[i]];
                    childNode.toggleContent(true);
                    this.updateChildrenExpandStatus(childNode.getChildList());
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
        },
        updateExpandIconStatus: function (evt) {
            var target = $(evt.target), iconTarget;
            if (target.hasClass("lumens-icon2-expand") || target.hasClass("lumens-icon2-collapse")) {
                iconTarget = target;
            }
            else {
                iconTarget = target.find(".lumens-icon2-expand");
                if (iconTarget.length === 0) {
                    iconTarget = target.find(".lumens-icon2-collapse");
                    if (iconTarget.length === 0) {
                        return;
                    }
                }
            }
            iconTarget.toggleClass("lumens-icon2-expand").toggleClass("lumens-icon2-collapse");
        }
    };
});