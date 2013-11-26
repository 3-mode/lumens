Lumens.Tree = {}; // right indent or left indent
Lumens.Tree.create = function(config) {
    var tThis = {};
    var parentContainer = config.container;
    var tree = $('<div class="lumens-tree"/>').appendTo(parentContainer);
    Lumens.forEach(config.classes, function(clazz) {
        tree.addClass(clazz);
    });
    var entryHolder = {size: 0, map: {}};
    var dblclickHandler;

    var Node = {};
    Node.create = function(type, name, draggable, droppable, level, index, parentElem, containerElem) {
        var tThis = {};
        var children = {size: 0, map: {}};
        var parent = parentElem;
        var container = containerElem;
        var levelNumber = level;
        var indexNumber = index;
        var nodeType = type;
        var folder = $('<div class="lumens-tree-folder" style="display: block;"></div>').appendTo(container).attr('id', '(' + levelNumber + ',' + indexNumber + ')');
        // ----------- Build folder header ----------------
        var fheader;
        var fcontent;
        if (nodeType === "file") {
            fheader = $('<div class="lumens-tree-folder-header"><i class="lumens-icon-file-node"></i><div class="lumens-tree-folder-name"></div></div>').appendTo(folder);
        }
        else {
            fheader = $('<div class="lumens-tree-folder-header"><i id="folder-status" class="lumens-icon-folder-close"></i><i class="lumens-icon-folder-node"></i><div class="lumens-tree-folder-name"></div></div>').appendTo(folder);
            fcontent = $('<div class="lumens-tree-folder-content"></div>').appendTo(folder);
        }
        //-------------------------------------------------
        fheader.find('.lumens-tree-folder-name').text(name);
        if (draggable)
            fheader.draggable({
                helper: "clone",
                appendTo: "body"
            });
        if (droppable)
            ;
        fheader.dblclick(function(evt) {
            evt.stopPropagation();
            if (dblclickHandler)
                dblclickHandler(parent, tThis);
            tThis.toggleContent();
        });

        tThis.getElement = function() {
            return folder;
        };

        tThis.isFolder = function() {
            return nodeType !== "file";
        }

        tThis.hasContent = function() {
            return  tThis.isFolder() && fcontent.children().length > 0;
        }

        tThis.toggleContent = function() {
            if (tThis.isFolder() && tThis.hasContent() > 0) {
                if (fcontent.hasClass('lumens-tree-content-show')) {
                    fheader.find('#folder-status').removeClass('lumens-icon-folder-open').addClass('lumens-icon-folder-close');
                    fcontent.removeClass('lumens-tree-content-show').addClass('lumens-tree-content-hide');
                }
                else {
                    fcontent.removeClass('lumens-tree-content-hide').addClass('lumens-tree-content-show');
                    fheader.find('i').removeClass('lumens-icon-folder-close').addClass('lumens-icon-folder-open');
                }
            }
        }

        tThis.getId = function() {
            return '(' + levelNumber + ',' + indexNumber + ')';
        };

        tThis.getLevel = function() {
            return levelNumber;
        };

        tThis.addChildList = function(nodes) {
            if (!tThis.isFolder())
                throw "Current node is not folder type, no child";
            var p = this;
            Lumens.forEach(nodes, function(child) {
                var entry = Node.create(child.type, child.name, child.draggable, child.droppable, levelNumber + 1, children.size, p, fcontent);
                children.map[entry.getId()] = entry;
                children.size++;
            });
            return this;
        }

        //end
        return tThis;
    };

    tThis.getElement = function() {
        return tree;
    };

    tThis.addEntryList = function(entryList) {
        var p = this;
        Lumens.forEach(entryList, function(entryInfo) {
            var entry = Node.create(entryInfo.type, entryInfo.name, entryInfo.draggable, entryInfo.droppable,  0, entryHolder.size, p, p.getElement());
            entryHolder.map[entry.getId()] = entry;
            entryHolder.size++;
        });
        return this;
    };

    tThis.configure = function(config) {
        dblclickHandler = config.dblclick;
        config.init(this);
        return this;
    }

    //end
    return tThis;
}
