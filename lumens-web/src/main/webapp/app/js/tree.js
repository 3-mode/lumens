/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.TreeNode = Class.$extend({
    __init__: function(type, name, data, parent, classes) {
        var __this = this;
        this.children = {size: 0, map: {}};
        this.$parent = parent;
        this.nodeType = type;
        this.name = name;
        this.data = data;
        this.dblclickHandler = parent.dblclickHandler;
        this.$container = parent.getElement();
        this.levelNumber = parent.levelNumber + 1;
        this.indexNumber = parent.children.size;
        this.draggable = parent.draggable;
        this.droppable = parent.droppable;
        this.$folder = $('<div class="lumens-tree-folder" style="display: block;"></div>').appendTo(this.$container).attr('id', '(' + this.levelNumber + ',' + this.indexNumber + ')');
        if (classes)
            this.$folder.addClass(classes);
        // ----------- Build folder header ----------------
        if (this.nodeType === "file") {
            this.$fHeader = $('<div class="lumens-tree-folder-header"><div class="lumens-tree-node" ><i class="lumens-icon-file-node"></i><div class="lumens-tree-folder-name"></div></div></div>').appendTo(this.$folder);
        }
        else {
            this.$fHeader = $('<div class="lumens-tree-folder-header"><i id="folder-status" class="lumens-icon-folder-close"/><div class="lumens-tree-node"><i class="lumens-icon-folder-node"/><div class="lumens-tree-folder-name"></div></div></div>').appendTo(this.$folder);
            this.$fContent = $('<div class="lumens-tree-folder-content" style="display:none;"></div>').appendTo(this.$folder);
        }
        //-------------------------------------------------
        this.$fHeader.find('.lumens-tree-folder-name').text(name);
        if (this.draggable)
            this.$fHeader.find(".lumens-tree-node").draggable({
                helper: function() {
                    return $(this).clone();
                },
                appendTo: "body"
            });
        // TODO droppable
        if (this.droppable)
            this.$fHeader.find(".lumens-tree-node").droppable({
                accept: ".lumens-tree-node",
                drop: function(event, ui) {
                    console.log(ui.draggable);
                }
            })

        this.$fHeader.dblclick(function(evt) {
            evt.stopPropagation();
            if (__this.dblclickHandler)
                __this.dblclickHandler(__this.$parent, __this);
            __this.toggleContent();
        });
    },
    getElement: function() {
        return this.$fContent ? this.$fContent : this.$folder;
    },
    isFolder: function() {
        return this.nodeType !== "file";
    },
    hasContent: function() {
        return  this.isFolder() && this.$fContent.children().length > 0;
    },
    getName: function() {
        return this.$fHeader.find('.lumens-tree-folder-name').text();
    },
    getId: function() {
        return '(' + this.levelNumber + ',' + this.indexNumber + ')';
    },
    getLevel: function() {
        return this.levelNumber;
    },
    toggleContent: function() {
        if (this.isFolder() && this.hasContent() > 0) {
            var $status = this.$fHeader.find('#folder-status');
            if ($status.hasClass('lumens-icon-folder-open')) {
                $status.removeClass('lumens-icon-folder-open').addClass('lumens-icon-folder-close');
                this.$fContent.hide("blind", 200);
            }
            else {
                this.$fContent.show("blind", 200);
                $status.removeClass('lumens-icon-folder-close').addClass('lumens-icon-folder-open');
            }
        }
    },
    addChildList: function(nodes) {
        if (!this.isFolder())
            throw "Current node is not folder type, no child";
        var parent = this;
        $.each(nodes, function() {
            var entry = new Lumens.TreeNode(this.nodeType, this.name, this.data, parent);
            parent.children.map[entry.getId()] = entry;
            parent.children.size++;
        });
        return this;
    }
});

Lumens.Tree = Class.$extend({
    __init__: function(container) {
        this.$parentContainer = container;
        this.$tree = $('<div class="lumens-tree"/>').appendTo(container);
        this.children = {size: 0, map: {}};
        this.levelNumber = -1;
    },
    getElement: function() {
        return this.$tree;
    },
    addEntryList: function(entryList) {
        var parent = this;
        $.each(entryList, function() {
            var entry = new Lumens.TreeNode(this.nodeType, this.name, this.data, parent, parent.children.size ? "lumens-0n-level" : "lumens-00-level");
            parent.children.map[entry.getId()] = entry;
            parent.children.size++;
        });
        return this;
    },
    configure: function(config) {
        if (config) {
            this.draggable = config.draggable;
            this.droppable = config.droppable;
            if (config.classes)
                this.$tree.addClass(classes);
            if (config.dblclick)
                this.dblclickHandler = config.dblclick;
            if (config.handler)
                config.handler(this);
        }
        return this;
    }
});

Lumens.FormatTree = Class.$extend({
});

Lumens.TransformRuleTree = Class.$extend({
});
