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
        var selecctedRow;
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
                url: "html/table/create.html",
                dataType: "html"
            });
            var message = $('<div>' + form + '<div>');
            message.dialog({
                modal: true,
                resizable: false,
                buttons: [
                    {
                        text: "Ok", click: function() {
                            var row = message.find("#rowCount").val();
                            var col = message.find("#colCount").val();
                            var table = $('<table class="hrcms-report-table">').appendTo(tableContainer);
                            for (var i = 0; i < row; ++i) {
                                var tr = $('<tr></tr>').appendTo(table);
                                tr.dblclick(function(event) {
                                    var cur = $(this);
                                    if (cur.hasClass("hrcms-selected")) {
                                        cur.toggleClass("hrcms-selected");
                                        selecctedRow = undefined;
                                    } else {
                                        if (selecctedRow)
                                            selecctedRow.toggleClass("hrcms-selected");
                                        cur.toggleClass("hrcms-selected");
                                        if (cur.hasClass("hrcms-selected"))
                                            selecctedRow = cur;
                                    }
                                });
                                for (var j = 0; j < col; ++j) {
                                    $('<td style="width:50px;height:20px;"></td>').appendTo(tr);
                                }
                                // tr.find('td:nth-child(2)').css("background-color", "red");
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
        function insertTableRow(event) {
            var form = SyncGet({
                url: "html/table/insert-row.html",
                dataType: "html"
            });
            var message = $('<div>' + form + '<div>');
            message.dialog({
                modal: true,
                resizable: false,
                buttons: [
                    {
                        text: "Ok", click: function() {
                            var row = message.find("#rowCount").val();
                            var rowPosition = message.find('input[name="rowPosition"]:checked').val();
                            if (Hrcms.debugEnabled)
                                console.log(row + ", " + rowPosition);
                            // TODO need to refine
                            if (selecctedRow && row > 0) {
                                for (var i = 0; i < row; ++i) {
                                    var tr = $('<tr/>');

                                    if (rowPosition === "Above")
                                        selecctedRow.before(tr);
                                    else
                                        selecctedRow.after(tr);

                                    tr.dblclick(function(event) {
                                        var cur = $(this);
                                        if (selecctedRow)
                                            selecctedRow.toggleClass("hrcms-selected");
                                        cur.toggleClass("hrcms-selected");
                                        if (cur.hasClass("hrcms-selected"))
                                            selecctedRow = cur;
                                    });
                                    var col = selecctedRow.find('td').length;
                                    for (var j = 0; j < col; ++j) {
                                        $('<td style="width:50px;height:20px;"></td>').appendTo(tr);
                                    }
                                }
                            }
                            //table.find('td[row="0"][column="1"]').html("test");
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
                editTable: click,
                saveTable: click,
                insertTableRow: insertTableRow,
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
