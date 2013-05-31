$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.TableEditor = {};
    Hrcms.TableEditor.create = function(config) {
        var tThis = {};
        var SyncGet = Hrcms.SyncGet;
        var container = config.container;
        var tableEditorContainer = $('<div class="hrcms-teditor-container"/>').appendTo(container);
        var workspaceHeader = Hrcms.NavIndicator.create(tableEditorContainer);
        var tableContainer = $('<div class="hrcms-teditor-table-container"/>').appendTo(tableEditorContainer);

        for (var i = 0; i < config.navigator.length; ++i)
            workspaceHeader.goTo(config.navigator[i]);

        function click(event) {
        }
        function removeDialog(dialog) {
            dialog.dialog("close");
            dialog.remove();
        }
        function addTable(event) {
            var form = SyncGet({
                url: "html/tableDefinition.html",
                dataType: "html"
            });
            var message = $('<div>' + form + '<div>');
            message.dialog({
                modal: true,
                resizable: false,
                buttons: [
                    {
                        text: "Ok", click: function() {
                            var row = $("#rowCount").val();
                            var col = $("#colCount").val();
                            var table = $('<table class="hrcms-report-table">').appendTo(tableContainer);
                            for (var i = 0; i < row; ++i) {
                                var tr = $('<tr></tr>').appendTo(table);
                                for (var j = 0; j < col; ++j) {
                                    var td = $('<td style="width:50px;height:20px;"></td>').appendTo(tr);
                                    td.attr("row", i).attr("column", j);
                                }
                            }
                            table.draggable({stop: function() {
                                    var p = table.position();
                                    var tp = tableContainer.position();
                                    if (p.left < tp.left)
                                        table.css("left", 0);
                                    if (p.top < tp.top)
                                        table.css("top", 0);
                                }}
                            );
                            table.find('td[row="0"][column="1"]').html("test");
                            removeDialog($(this));
                        }
                    },
                    {
                        text: "Cancel", click: function() {
                            removeDialog($(this));
                        }
                    }
                ]
            });
        }
        workspaceHeader.configure({
            goBack: config.goBack,
            toolbar: {
                barType: Hrcms.TableEditorbar,
                addTable: addTable,
                deleteTable: click,
                insertTableRow: click,
                deleteTableRow: click,
                insertTableColumn: click,
                deleteTableColumn: click,
                joinTableCell: click

            }
        });
        var offsetHeight = config.offsetHeight ? config.offsetHeight : 0;
        var offsetWidth = config.offsetWidth ? config.offsetWidth : 0;
        var indicatorSize = workspaceHeader.size();
        function resize(event) {
            tableEditorContainer.css("width", container.width() - offsetWidth);
            tableEditorContainer.css("height", container.height() - offsetHeight);
            tableContainer.css("width", tableEditorContainer.width());
            tableContainer.css("height", tableEditorContainer.height() - indicatorSize.height);
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
