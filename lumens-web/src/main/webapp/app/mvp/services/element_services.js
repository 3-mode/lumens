/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.services.factory('ElementService', ['FormatService', function (FormatService) {
        return {
            build: function ($scope, attrName, $element, elementData) {
                if (!elementData || !elementData.log_data || !elementData.log_data.element)
                    return;

                var __this = this;
                var elementData = elementData.log_data.element;
                var elementTree = __this.elementTree = new Lumens.Tree($element).configure({
                    handler: function (parentNode) {
                        var entryList = __this.buildRootElement(elementData);
                        parentNode.addEntryList(entryList);
                        __this.buildElementChildren(parentNode, elementData);
                    }
                });
                return elementTree;
            },
            buildRootElement: function (element) {
                return [{
                        label: element.name,
                        name: element.name,
                        nodeType: FormatService.isField(element.form) ? "file" : "folder",
                        data: element,
                        disableSelectStatus: true
                    }];
            },
            buildElementChildren: function (parentNode, parentElement) {
                var entryList = parentNode.getEntryList ? parentNode.getEntryList() : parentNode.getChildList();
                parentNode = entryList.map[parentElement.name];
                var nodeList = [];
                for (var i in parentElement.element) {
                    nodeList.push(this.buildItemNode(parentElement.element[i]));
                }
                if (nodeList.length > 0)
                    parentNode.addChildList(nodeList);

                for (var i in parentElement.element) {
                    this.buildElementChildren(parentNode, parentElement.element[i]);
                }
            },
            buildItemNode: function (element) {
                var node = {
                    label: element.name,
                    name: element.name,
                    nodeType: FormatService.isField(element.form) ? "file" : (FormatService.isArray(element.form) ? "folderset" : "folder"),
                    data: element,
                    disableSelectStatus: true
                };
                if (element && element.value)
                    node.script = element.value;
                return node;
            },
        };
    }
]);

