/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.TreeNode = Class.$extend({
    __init__: function (root, node, parent, classes) {
        var __this = this;
        this.children = {size: 0, map: {}};
        this.root = root;
        this.$parent = parent;
        this.$container = parent.getElement();
        this.clickHandler = parent.clickHandler;
        this.dblclickHandler = parent.dblclickHandler;
        this.dropHandler = parent.dropHandler;
        this.levelNumber = parent.levelNumber + 1;
        this.indexNumber = parent.children.size;
        this.draggable = parent.draggable;
        this.droppable = parent.droppable;
        // ******* Begin Init the all attributes of node to this ********************
        var nodeKeys = Object.keys(node);
        for (var i in nodeKeys)
            this[nodeKeys[i]] = node[nodeKeys[i]];
        // ******* End Init the all attributes of node to this **********************

        this.$folder = $('<div class="lumens-tree-folder" style="display: block;"></div>').appendTo(this.$container).attr('id', '(' + this.levelNumber + ',' + this.indexNumber + ')');
        if (classes)
            this.$folder.addClass(classes);
        // ----------- Build folder header ----------------
        if (this.nodeType === "file") {
            this.$fHeader = $('<div class="lumens-tree-folder-header"><div class="lumens-tree-node lumens-file-node-offset" ><i class="lumens-icon-file-node"></i><div class="lumens-tree-node-name"></div><div class="lumens-tree-node-script"></div></div></div>').appendTo(this.$folder);
        }
        else if (this.nodeType === "folder") {
            this.$fHeader = $('<div class="lumens-tree-folder-header"><i id="folder-status" class="lumens-icon-folder-close"/><div class="lumens-tree-node"><i class="lumens-icon-folder-node"/><div class="lumens-tree-node-name"></div><div class="lumens-tree-node-script"></div></div></div>').appendTo(this.$folder);
            this.$fContent = $('<div class="lumens-tree-folder-content" style="display:none;"></div>').appendTo(this.$folder);
        }
        else {
            this.$fHeader = $('<div class="lumens-tree-folder-header"><i id="folder-status" class="lumens-icon-folder-close"/><div class="lumens-tree-node"><i class="lumens-icon-folderset-node"/><div class="lumens-tree-node-name"></div><div class="lumens-tree-node-script"></div></div></div>').appendTo(this.$folder);
            this.$fContent = $('<div class="lumens-tree-folder-content" style="display:none;"></div>').appendTo(this.$folder);
        }
        //-------------------------------------------------
        this.$fHeader.find('.lumens-tree-node-name').html(this.label);
        if (this.script)
            this.$fHeader.find('.lumens-tree-node-script').html(this.script);
        if (this.draggable)
            this.$fHeader.find(".lumens-tree-node").draggable({
                appendTo: "body",
                helper: "clone"
            }).data("tree-node-data", this);
        if (this.droppable) {
            this.$fHeader.find(".lumens-tree-node").droppable({
                greedy: true,
                hoverClass: "lumens-tree-node-hover",
                accept: ".lumens-tree-node",
                drop: function (event, ui) {
                    var data = $.data(ui.draggable.get(0), "tree-node-data");
                    if (__this.dropHandler)
                        __this.dropHandler(data, __this, __this.$parent);
                }
            });
        }
        this.$fHeader.click(function (evt) {
            evt.stopPropagation();
            if (__this.clickHandler)
                __this.clickHandler(__this, __this.$parent);
            __this.updateSelectStatus();
        });
        this.$fHeader.dblclick(function (evt) {
            evt.stopPropagation();
            if (__this.dblclickHandler)
                __this.dblclickHandler(__this, __this.$parent);
            __this.toggleContent();
        });
    },
    remove: function () {
        if (this.root.selectNode === this)
            this.root.selectNode = undefined;
        this.$folder.remove();
        delete this.$parent.children.map[this.getName()];
        --this.$parent.children.size;
    },
    updateSelectStatus: function () {
        if (this.root.selectNode)
            this.root.selectNode.$fHeader.find(".lumens-tree-node").toggleClass("lumens-tree-node-select").find(".lumens-tree-node-script").toggleClass("lumens-tree-node-script-select");
        this.$fHeader.find(".lumens-tree-node").toggleClass("lumens-tree-node-select").find(".lumens-tree-node-script").toggleClass("lumens-tree-node-script-select");;
        this.root.selectNode = this;
    },
    getPath: function () {
        var path = [];
        var node = this;
        // TODO need to handle new tree root node
        node = node.$parent;
        while (node && node.data) {
            path.push(node.data);
            node = node.$parent;
        }
        path.reverse();
        path.push(this.data);
        return path;
    },
    getRoot: function () {
        return this.root;
    },
    getScript: function () {
        return this.script;
    },
    setScript: function (script) {
        this.script = script;
        this.$fHeader.find('.lumens-tree-node-script').html(this.script);
    },
    getElement: function () {
        return this.$fContent ? this.$fContent : this.$folder;
    },
    isFolder: function () {
        return this.nodeType !== "file";
    },
    hasContent: function () {
        return  this.isFolder() && this.$fContent.children().length > 0;
    },
    removeContent: function () {
        this.$fContent.empty();
    },
    getLabel: function () {
        return this.$fHeader.find('.lumens-tree-node-name').text();
    },
    getName: function () {
        return this.name;
    },
    getIndex: function () {
        return this.indexNumber;
    },
    getLevel: function () {
        return this.levelNumber;
    },
    expandContent: function () {
        if (this.isFolder() && this.hasContent() > 0) {
            var $status = this.$fHeader.find('#folder-status');
            if (!$status.hasClass('lumens-icon-folder-open')) {
                this.$fContent.show("blind", 200);
                $status.removeClass('lumens-icon-folder-close').addClass('lumens-icon-folder-open');
            }
        }
    },
    toggleContent: function (expandFirstLevel) {
        if (this.isFolder()) {
            var $status = this.$fHeader.find('#folder-status');
            if ($status.hasClass('lumens-icon-folder-open')) {
                $status.removeClass('lumens-icon-folder-open').addClass('lumens-icon-folder-close');
                this.$fContent.hide("blind", 200);
            }
            else {
                if (expandFirstLevel && this.children && this.dblclickHandler)
                    this.dblclickHandler(this, this.$parent);
                this.$fContent.show("blind", 200);
                $status.removeClass('lumens-icon-folder-close').addClass('lumens-icon-folder-open');
            }
        }
    },
    addChildList: function (nodes) {
        if (!this.isFolder())
            throw "Current node is not folder type, no child";
        var parent = this;
        for (var i in nodes) {
            var node = nodes[i];
            var entry = new Lumens.TreeNode(this.root, node, parent);
            parent.children.map[entry.getName()] = entry;
            parent.children.size++;
        }
        return this;
    },
    getChildList: function () {
        return this.children;
    }
});

Lumens.Tree = Class.$extend({
    __init__: function (container) {
        this.$parentContainer = container;
        this.$tree = $('<div class="lumens-tree"/>').appendTo(container);
        this.children = {size: 0, map: {}};
        this.levelNumber = -1;
    },
    getSelectNode: function () {
        return this.selectNode;
    },
    remove: function () {
        this.$parentContainer.unbind();
        this.$parentContainer.remove();
    },
    getElement: function () {
        return this.$tree;
    },
    addEntryList: function (entryList) {
        var parent = this;
        $.each(entryList, function () {
            var entry = new Lumens.TreeNode(parent, this, parent, parent.children.size ? "lumens-0n-level" : "lumens-00-level");
            parent.children.map[entry.getName()] = entry;
            parent.children.size++;
        });
        return this;
    },
    getEntryList: function () {
        return this.children;
    },
    configure: function (config) {
        if (config) {
            this.draggable = config.draggable;
            this.droppable = config.droppable;
            if (config.classes)
                this.$tree.addClass(config.classes);
            if (config.click)
                this.clickHandler = config.click;
            if (config.dblclick)
                this.dblclickHandler = config.dblclick;
            if (config.drop)
                this.dropHandler = config.drop;
            if (config.handler)
                config.handler(this);
        }
        return this;
    }
});
