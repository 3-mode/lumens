$(function() {
    var I18N = Hrcms.I18N;
    Hrcms.TableEditor = {};
    Hrcms.TableEditor.create = function(config) {
        var tThis = {};
        var SyncGet = Hrcms.SyncGet;
        var SyncUUID = Hrcms.SyncUUID;
        var TABLE_UUID = "table-uuid";
        var container = config.container;
        var tableEditorContainer = $('<div class="hrcms-teditor-container"/>').appendTo(container);
        var workspaceHeader = Hrcms.NavIndicator.create(tableEditorContainer);
        var tableContainer = $('<div class="hrcms-teditor-table-container"/>').appendTo(tableEditorContainer);
        var editingTable = {};
        for (var i = 0; i < config.navigator.length; ++i)
            workspaceHeader.goTo(config.navigator[i]);

        // TODO this is a MOCK
        function click(event) {
        }
        function tdDbClick(event) {
            $(this).toggleClass("hrcms-selected");
        }
        function updateRowStatus(currentRow) {
            if (currentRow && currentRow.hasClass("hrcms-selected")) {
                currentRow.toggleClass("hrcms-selected");
                editingTable.selectedRow = undefined;
            } else if (currentRow) {
                if (editingTable.selectedRow)
                    editingTable.selectedRow.toggleClass("hrcms-selected");
                currentRow.toggleClass("hrcms-selected");
                if (currentRow.hasClass("hrcms-selected"))
                    editingTable.selectedRow = currentRow;
            }
        }
        function updateColumnStatus(currentColumn) {
            if (currentColumn) {
                if (currentColumn.selectedColumnIndex >= 0) {
                    var localTable = tableContainer.find('table[table-uuid="' + currentColumn.tableUUID + '"]');
                    var tr = localTable.find('tr');
                    var filter = 'td:nth-child(' + (currentColumn.selectedColumnIndex + 1) + ')';
                    for (var i = 0; i < tr.length; ++i) {
                        $(tr[i]).find(filter).toggleClass("hrcms-selected");
                    }
                }
                if (editingTable.selectedColumn &&
                currentColumn.tableUUID === editingTable.selectedColumn.tableUUID &&
                currentColumn.selectedColumnIndex === editingTable.selectedColumn.selectedColumnIndex) {
                    editingTable.selectedColumn = undefined;
                }
                else {
                    editingTable.selectedColumn = currentColumn;
                }
            }
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
            message.find("#RowsLabel").html(I18N.Widget.Table_RowCount);
            message.find("#ColumnsLabel").html(I18N.Widget.Table_ColCount);
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
                            '<table class="hrcms-move-active">' +
                            '  <tr><td id="hrcms-move-bar"></td><td class="hrcms-column-select-area"></td></tr>' +
                            '  <tr><td class="hrcms-row-select-area"></td><td><div id="tableBox"></div></td></tr>' +
                            '</table>').appendTo(tableContainer);
                            var tableBox = tableHolder.find("#tableBox");
                            var table = $('<table class="hrcms-report-table">').appendTo(tableBox);
                            table.selectable({
                                stop: function(evt, ui) {
                                    table.find('tbody[class~="ui-selected"]').removeClass("ui-selected");
                                    table.find('tr[class~="ui-selected"]').removeClass("ui-selected");
                                    table.find('th[class~="ui-selected"]').removeClass("ui-selected");
                                }
                            });//*/
                            table.attr(TABLE_UUID, SyncUUID("table"));
                            for (var i = 0; i < row; ++i) {
                                var tr = $('<tr></tr>').appendTo(table);
                                for (var j = 0; j < col; ++j) {
                                    $('<td style="width:50px;height:20px;"></td>').appendTo(tr).dblclick(tdDbClick);
                                }
                            }
                            function rowSelect(event) {
                                updateColumnStatus(editingTable.selectedColumn);
                                var tr = table.find('tr');
                                for (var i = 0; i < tr.length; ++i) {
                                    var cur = tr[i];
                                    if (event.offsetY < (cur.offsetTop + cur.offsetHeight)) {
                                        cur = $(cur);
                                        updateRowStatus(cur);
                                        break;
                                    }
                                }
                            }
                            function columnSelect(event) {
                                updateRowStatus(editingTable.selectedRow);
                                var oldSelectedClolumn = editingTable.selectedColumn;
                                updateColumnStatus(editingTable.selectedColumn);
                                var tr = table.find('tr');
                                var firstTR = $(tr[0]);
                                var td = firstTR.find('td');
                                // Find the selected column number
                                var localSelectedColumnIndex = -1;
                                for (var i = 0; i < td.length; ++i) {
                                    var cur = td[i];
                                    if (event.offsetX < (cur.offsetLeft + cur.offsetWidth)) {
                                        localSelectedColumnIndex = i;
                                        break;
                                    }
                                }
                                if (!oldSelectedClolumn ||
                                oldSelectedClolumn.tableUUID !== table.attr(TABLE_UUID) ||
                                (oldSelectedClolumn.tableUUID === table.attr(TABLE_UUID) &&
                                oldSelectedClolumn.selectedColumnIndex !== localSelectedColumnIndex))
                                    updateColumnStatus({
                                        selectedColumnIndex: localSelectedColumnIndex,
                                        tableUUID: table.attr(TABLE_UUID)
                                    });
                            }
                            tableHolder.find(".hrcms-row-select-area").click(rowSelect);
                            tableHolder.find(".hrcms-column-select-area").click(columnSelect);
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
            message.find("#RowsLabel").html(I18N.Widget.Table_RowCount);
            message.find("#AboveLabel").html(I18N.Widget.Table_RowAbove);
            message.find("#BelowLabel").html(I18N.Widget.Table_RowBelow);
            message.dialog({
                modal: true,
                resizable: false,
                buttons: [
                    {
                        text: I18N.Widget.Button_Ok,
                        click: function() {
                            var row = message.find("#rowCount").val();
                            var rowPosition = message.find('input[name="rowPosition"]:checked').val();
                            if (Hrcms.debugEnabled)
                                console.log(row + ", " + rowPosition);
                            // TODO need to refine
                            var selectedRow = editingTable.selectedRow;
                            if (selectedRow && row > 0) {
                                var col = 0;
                                selectedRow.find('td').each(function(i, cell) {
                                    var jcell = $(cell);
                                    var colspan = jcell.attr("colspan");
                                    colspan = colspan ? parseInt(colspan) : 1;
                                    col += colspan;
                                });
                                var insert = rowPosition === "Above" ? selectedRow.before : selectedRow.after;
                                for (var i = 0; i < row; ++i) {
                                    var tr = $('<tr/>');
                                    insert(tr);
                                    // TODO need to check the colspan when adding td cell
                                    for (var j = 0; j < col; ++j) {
                                        var td = $('<td style="width:50px;height:20px;"></td>').appendTo(tr);
                                        if (td && td.dblclick)
                                            td.dblclick(tdDbClick);
                                    }
                                }
                                updateRowStatus(selectedRow);
                            }
                            //table.find('td[row="0"][column="1"]').html("test");
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
        function insertTableCol(event) {
            var form = SyncGet({
                url: "html/table/insert-column.html",
                dataType: "html"
            });
            var message = $('<div>' + form + '<div>');
            message.find("#ColumnsLabel").html(I18N.Widget.Table_ColCount);
            message.find("#LeftLabel").html(I18N.Widget.Table_ColLeft);
            message.find("#RightLabel").html(I18N.Widget.Table_ColRight);
            message.dialog({
                modal: true,
                resizable: false,
                buttons: [
                    {
                        text: I18N.Widget.Button_Ok,
                        click: function() {
                            var col = message.find("#colCount").val();
                            var colPosition = message.find('input[name="colPosition"]:checked').val();
                            if (Hrcms.debugEnabled)
                                console.log(col + ", " + colPosition);
                            var selectedColumn = editingTable.selectedColumn;
                            var localTable = tableContainer.find('table[table-uuid="' + selectedColumn.tableUUID + '"]');
                            if (selectedColumn && col > 0) {
                                var tr = localTable.find('tr');
                                for (var j = 0; j < col; ++j) {
                                    var filter = 'td:nth-child(' + (selectedColumn.selectedColumnIndex + 1) + ')';
                                    for (var i = 0; i < tr.length; ++i) {
                                        var td = $(tr[i]).find(filter);
                                        if (colPosition === "Left")
                                            $('<td style="width:50px;height:20px;"></td>').insertBefore(td);
                                        else
                                            $('<td style="width:50px;height:20px;"></td>').insertAfter(td);
                                    }
                                    if (colPosition === "Left")
                                        selectedColumn.selectedColumnIndex++;
                                }
                            }
                            updateColumnStatus(selectedColumn);
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
        function joinTableCells(event) {
            //TODO must to make sure selected a rectangle cells

            var selectedCells = tableContainer.find(".ui-selected");
            selectedCells.toggleClass("ui-selected");
            // Normalized cell array ------------------------------>
            var normalizedCells = [];
            selectedCells.each(function(index, cell) {
                var jcell = $(cell);
                var colspan = jcell.attr("colspan");
                var rowspan = jcell.attr("rowspan");
                if (Hrcms.debugEnabled) {
                    console.log("colspan: " + colspan + "; rowspan: " + rowspan);
                }
                colspan = colspan ? parseInt(colspan) : 1;
                rowspan = rowspan ? parseInt(rowspan) : 1;
                normalizedCells.push({
                    cell: cell,
                    rowspan: rowspan,
                    colspan: colspan,
                    cellIndex: cell.cellIndex,
                    rowIndex: cell.parentElement.rowIndex
                })
            })
            normalizedCells.sort(function(a, b) {
                if (Hrcms.debugEnabled) {
                    console.log("--------->");
                    console.log(a);
                    console.log(b);
                    console.log("<---------");
                }
                if (a.rowIndex < b.rowIndex) {
                    return -1;
                } else if (a.rowIndex > b.rowIndex) {
                    return 1;
                } else {
                    if (a.cellIndex < b.cellIndex) {
                        return -1;
                    }
                    else if (a.cellIndex > b.cellIndex) {
                        return 1;
                    }
                }
                return 0;
            });
            // Normalized cell array <------------------------------
            var cellMatrix = [];
            var rowCounter = 0, colCounter = 0;
            var firstCell = normalizedCells[0];
            for (var index = 0; index < normalizedCells.length; ++index) {
                var cell = normalizedCells[index];
                var row = cell.rowIndex - firstCell.rowIndex;
                var rowCount = cell.rowspan;
                for (var i = 0; i < rowCount; ++i) {
                    var rowIndex = row + i;
                    if (rowIndex >= cellMatrix.length) {
                        // New row
                        cellMatrix.push([]);
                    }
                    for (var j = 0; j < cell.colspan; ++j)
                        cellMatrix[rowIndex].push(cell);
                }
            }

            if (Hrcms.debugEnabled)
                console.log(cellMatrix);

            for (var i = 1; i < cellMatrix.length; ++i) {
                if (cellMatrix[i].length !== cellMatrix[i - 1].length) {
                    var message = $('<div title="错误">不能合并非法单元格</div>');
                    message.dialog({
                        modal: true,
                        resizable: false,
                        buttons: [{
                                text: I18N.Widget.Button_Ok,
                                click: function() {
                                    removeDialog($(this));
                                }
                            }]
                    });
                    return;
                }
            }

            colCounter = cellMatrix[0].length;
            rowCounter = cellMatrix.length;
            if (firstCell && firstCell.cell)
                $(firstCell.cell).attr("colspan", colCounter).attr("rowspan", rowCounter)
                .css("width", colCounter * 50)
                .css("height", rowCounter * 20);
            for (var index = 0; index < normalizedCells.length; ++index) {
                if (index !== 0)
                    $(normalizedCells[index].cell).remove();
            }
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
                insertTableColumn: insertTableCol,
                deleteTableColumn: click,
                joinTableCell: joinTableCells
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
