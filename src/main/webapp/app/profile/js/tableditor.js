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
        // TODO need a row column selector
        var tableHolder = $(
        '<table class="hrcms-move-active">' +
        '  <tr><td style="height:20px;"></td></tr>' +
        '  <tr><td><div id="hrcms-table-holder"/></td></tr>' +
        '</table>').appendTo(tableContainer);
        var tableBox = tableHolder.find("#hrcms-table-holder");
        var tdHtmlTpl = '<td style="width:50px;height:20px;border-left:1px solid rgb(163, 163, 163);border-bottom:1px solid rgb(163, 163, 163);border-right:1px solid rgb(163, 163, 163);border-top:1px solid rgb(163, 163, 163);"></td>';
        tableBox.sortable();

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
            message.find("#RowsLabel").html(I18N.Widget.Table_RowCount);
            message.find("#ColumnsLabel").html(I18N.Widget.Table_ColCount);
            message.dialog({
                modal: true,
                resizable: false,
                buttons: [
                    {
                        text: I18N.Widget.Button_Ok,
                        click: function() {
                            // Editing one table once
                            var row = message.find("#rowCount").val();
                            var col = message.find("#colCount").val();
                            var box = $('<div id="hrcms-table-holder-box" style="padding-left:20px;"><table class="hrcms-report-table"></div>').appendTo(tableBox);
                            var table = box.find('table');
                            table.selectable({
                                stop: function(evt, ui) {
                                    table.find('tbody[class~="ui-selected"]').removeClass("ui-selected");
                                    table.find('tr[class~="ui-selected"]').removeClass("ui-selected");
                                    table.find('th[class~="ui-selected"]').removeClass("ui-selected");
                                }
                            });
                            table.attr(TABLE_UUID, SyncUUID("table"));
                            for (var i = 0; i < row; ++i) {
                                var tr = $('<tr></tr>').appendTo(table);
                                for (var j = 0; j < col; ++j) {
                                    $(tdHtmlTpl).appendTo(tr);
                                }
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
                                for (var i = 0; i < row; ++i) {
                                    var tr = $('<tr/>');
                                    rowPosition === "Above" ? selectedRow.before(tr) : selectedRow.after(tr);
                                    // TODO need to check the colspan when adding td cell
                                    for (var j = 0; j < col; ++j) {
                                        $(tdHtmlTpl).appendTo(tr);
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
                                            $(tdHtmlTpl).insertBefore(td);
                                        else
                                            $(tdHtmlTpl).insertAfter(td);
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
            var selectedCells = tableContainer.find(".ui-selected");
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

            rowCounter = cellMatrix ? cellMatrix.length : 1;
            colCounter = cellMatrix.length ? cellMatrix[0].length : 1;
            if (firstCell && firstCell.cell)
                $(firstCell.cell).attr("colspan", colCounter).attr("rowspan", rowCounter)
                .css("width", colCounter * 50)
                .css("height", rowCounter * 20);
            for (var index = 0; index < normalizedCells.length; ++index) {
                if (index !== 0)
                    $(normalizedCells[index].cell).remove();
            }
        }
        function editTable(event) {
            var form = SyncGet({
                url: "html/table/table-edit.html",
                dataType: "html"
            });
            var message = $('<div>' + form + '<div>');
            var checkboxSet = Hrcms.CellStyleChechboxSet.create(message.find("#CellBorderStyle"));
            checkboxSet.configure();
            message.find("#CellTextLabel").html(I18N.Widget.TableCellText);
            message.find("#CellTextColorLabel").html(I18N.Widget.TextColor);
            message.find("#FontSizeLabel").html(I18N.Widget.FontSize);
            message.find("#FontStyleLabel").html(I18N.Widget.FontStyle);
            message.find("#BackgroundColorLabel").html(I18N.Widget.BackgroundColor);
            message.find("#CellBoderStyleLabel").html(I18N.Widget.Border);
            message.dialog({
                title: I18N.Widget.TableCellSettings,
                modal: true,
                height: "auto",
                width: "auto",
                resizable: false,
                create: function(event) {
                    var selectedCells = tableContainer.find(".ui-selected");
                    if (selectedCells.length === 1)
                        message.find('#CellText').val(selectedCells.html());
                    selectedCells.removeClass("ui-selected").addClass("hrcms-ui-selected");
                    var bgcolor = selectedCells.css("background-color");
                    if (bgcolor !== "" && bgcolor)
                        message.find('#BackgroundColor').val(Hrcms.Rgb2Hex(bgcolor).substring(1));
                    jscolor.init();
                },
                buttons: [{
                        text: I18N.Widget.Button_Ok,
                        click: function() {
                            // TODO Setting the table cell styles
                            var cellText = message.find('#CellText').val();
                            var fontSize = parseInt(message.find('#FontSize').val());
                            var fontStyle = message.find('#FontStyle').val();
                            var backgroundColor = '#' + message.find('#BackgroundColor').val();
                            var border = checkboxSet.values();
                            if (Hrcms.debugEnabled) {
                                console.log(fontSize);
                                console.log(fontStyle);
                                console.log(backgroundColor);
                                console.log(border);
                            }
                            var selectedCells = tableContainer.find(".hrcms-ui-selected");
                            selectedCells.toggleClass("hrcms-ui-selected");
                            if (selectedCells.length === 1)
                                selectedCells.html(cellText).css("font-weight", "bold");
                            if (fontSize > 0)
                                selectedCells.css("font-size", fontSize)
                            if (backgroundColor !== "#000000")
                                selectedCells.css("background-color", backgroundColor);
                            else if (backgroundColor === "#000000")
                                selectedCells.css("background-color", "");
                            if (border) {
                                if (border.Left)
                                    selectedCells.css("border-left", "1px solid rgb(163, 163, 163)");
                                else
                                    selectedCells.css("border-left", "0px solid rgb(163, 163, 163)");
                                if (border.Bottom)
                                    selectedCells.css("border-bottom", "1px solid rgb(163, 163, 163)");
                                else
                                    selectedCells.css("border-bottom", "0px solid rgb(163, 163, 163)");
                                if (border.Right)
                                    selectedCells.css("border-right", "1px solid rgb(163, 163, 163)");
                                else
                                    selectedCells.css("border-right", "0px solid rgb(163, 163, 163)");
                                if (border.Top)
                                    selectedCells.css("border-top", "1px solid rgb(163, 163, 163)");
                                else
                                    selectedCells.css("border-top", "0px solid rgb(163, 163, 163)");
                            }
                            removeDialog($(this));
                        }
                    },
                    {
                        text: I18N.Widget.Button_Cancel,
                        click: function() {
                            removeDialog($(this));
                        }
                    }]
            });
        }

        function saveTable(event) {
            if (Hrcms.debugEnabled)
                console.log(tableBox.html())
        }
        workspaceHeader.configure({
            goBack: config.goBack,
            toolbar: {
                barType: Hrcms.TableEditorbar,
                addTable: addTable,
                deleteTable: click,
                editTable: editTable,
                saveTable: saveTable,
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
