$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.TableEditor = {};
    Hrcms.TableEditor.create = function(config) {
        var tThis = {};
        var container = config.container;
        var tableEditorContainer = $('<div class="hrcms-teditor-container"/>').appendTo(container);
        var tableContainer = $('<div class="hrcms-teditor-table-container" />').appendTo(tableContainer);
        var workspaceHeader = Hrcms.NavIndicator.create(tableEditorContainer);
        for (var i = 0; i < config.navigator.length; ++i)
            workspaceHeader.goTo(config.navigator[i]);
        function click(event) {
            var text = "Hi, you click me !!! " + $(event.target).attr("id");
            var message = $('<div><b>' + text + '</b><div>');
            message.dialog({
                modal: true,
                resizable: false,
                buttons: [
                    {
                        text: "确定", click: function() {
                            $(this).dialog("close");
                        }
                    }
                ]
            });
        }
        workspaceHeader.configure({
            goBack: config.goBack,
            toolbar: {
                barType: Hrcms.TableEditorbar,
                insertTableRow: click,
                deleteTableRow: click,
                insertTableColumn: click,
                deleteTableColumn: click,
                joinTableCell: click,
                addTable: click,
                deleteTable: click
            }});
        var offsetHeight = config.offsetHeight ? config.offsetHeight : 0;
        var offsetWidth = config.offsetWidth ? config.offsetWidth : 0;
        function resize(event) {
            tableEditorContainer.css("width", container.width() - offsetWidth);
            tableEditorContainer.css("height", container.height() - offsetHeight);
        }
        container.bind("resize", resize);
        resize();

        // Member methods
        tThis.configure = function(config) {
            return this;
        }
        tThis.remove = function() {
            tableEditorContainer.remove();
        }
        // end
        return tThis;
    }
});
