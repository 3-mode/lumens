$(function() {
    var I18N = Hrcms.I18N;
    Hrcms.TableEditor = {};
    Hrcms.TableEditor.create = function(config) {
        var tThis = {};
        var SyncGet = Hrcms.SyncGet;
        var container = config.container;
        var tableEditorContainer = $('<div class="hrcms-teditor-container"/>').appendTo(container);
        var workspaceHeader = Hrcms.NavIndicator.create(tableEditorContainer);
        var tableContainer = $('<div class="hrcms-teditor-table-container"/>').appendTo(tableEditorContainer);
        var selectedRow;
        var selectedColumn = -1;
        for (var i = 0; i < config.navigator.length; ++i)
            workspaceHeader.goTo(config.navigator[i]);

        // TODO this is a MOCK
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
                        text: I18N.Widget.Button_Ok,
                        click: function() {
                            var row = message.find("#rowCount").val();
                            var col = message.find("#colCount").val();
                            var tableHolder = $(
                            '<table>' +
                            '  <tr><td id="hrcms-move-bar"></td><td class="hrcms-column-select-area"></td></tr>' +
                            '  <tr><td class="hrcms-row-select-area"></td><td id="tableBox"></td></tr>' +
                            '</table>').appendTo(tableContainer);
                            var tableMoveBar = tableHolder.find("#hrcms-move-bar");
                            var mouseUp = true, isDraggable = false;
                            tableMoveBar.mousedown(function(event) {
                                mouseUp = false;
                            }).mouseup(function(event) {
                                mouseUp = true;
                            }).mouseenter(function(event) {
                                if (!isDraggable) {
                                    isDraggable = true;
                                    tableMoveBar.toggleClass("hrcms-move-bar-active");
                                    tableHolder.draggable({
                                        stop: function(event) {
                                            var p = tableHolder.position();
                                            var tp = tableContainer.position();
                                            if (p.left < tp.left)
                                                tableHolder.css("left", 0);
                                            if (p.top < tp.top)
                                                tableHolder.css("top", 0);
                                        }
                                    });
                                }
                            }).mouseleave(function(event) {
                                if (mouseUp) {
                                    isDraggable = false;
                                    tableHolder.draggable("destroy");
                                    tableMoveBar.toggleClass("hrcms-move-bar-active");
                                }
                            });
                            function updateRowStatus(currentRow) {
                                if (currentRow && currentRow.hasClass("hrcms-selected")) {
                                    currentRow.toggleClass("hrcms-selected");
                                    selectedRow = undefined;
                                } else if (currentRow) {
                                    if (selectedRow)
                                        selectedRow.toggleClass("hrcms-selected");
                                    currentRow.toggleClass("hrcms-selected");
                                    if (currentRow.hasClass("hrcms-selected"))
                                        selectedRow = currentRow;
                                }
                            }
                            function updateColumnStatus(currentColumn) {
                                if (currentColumn >= 0) {
                                    var tr = table.find('tr');
                                    var filter = 'td:nth-child(' + (currentColumn + 1) + ')';
                                    for (var i = 0; i < tr.length; ++i) {
                                        $(tr[i]).find(filter).toggleClass("hrcms-selected");
                                    }
                                }
                                selectedColumn = (currentColumn === selectedColumn) ? -1 : currentColumn;
                            }
                            tableHolder.find(".hrcms-row-select-area").click(function(event) {
                                updateColumnStatus(selectedColumn);
                                var tr = table.find('tr');
                                for (var i = 0; i < tr.length; ++i) {
                                    var cur = tr[i];
                                    if (event.offsetY < (cur.offsetTop + cur.offsetHeight)) {
                                        cur = $(cur);
                                        updateRowStatus(cur);
                                        break;
                                    }
                                }
                            });
                            tableHolder.find(".hrcms-column-select-area").click(function(event) {
                                updateRowStatus(selectedRow);
                                var oldSelectedClolumn = selectedColumn;
                                updateColumnStatus(selectedColumn);
                                var tr = table.find('tr');
                                var firstTR = $(tr[0]);
                                var td = firstTR.find('td');
                                // Find the selected column number
                                var localSelectedColumn = -1;
                                for (var i = 0; i < td.length; ++i) {
                                    var cur = td[i];
                                    if (event.offsetX < (cur.offsetLeft + cur.offsetWidth)) {
                                        localSelectedColumn = i;
                                        break;
                                    }
                                }
                                if (oldSelectedClolumn !== localSelectedColumn)
                                    updateColumnStatus(localSelectedColumn);
                            });

                            var tableBox = tableHolder.find("#tableBox");
                            var table = $('<table class="hrcms-report-table">').appendTo(tableBox);
                            for (var i = 0; i < row; ++i) {
                                var tr = $('<tr></tr>').appendTo(table);
                                for (var j = 0; j < col; ++j) {
                                    $('<td style="width:50px;height:20px;"></td>').appendTo(tr);
                                }
                                // tr.find('td:nth-child(2)').css("background-color", "red");
                            }
                            removeDialog($(this));
                        }
                    },
                    {
                        text: I18N.Widget.Button_Cancel,
                        click: function() {
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
                            if (selectedRow && row > 0) {
                                for (var i = 0; i < row; ++i) {
                                    var tr = $('<tr/>');

                                    if (rowPosition === "Above")
                                        selectedRow.before(tr);
                                    else
                                        selectedRow.after(tr);

                                    var col = selectedRow.find('td').length;
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
