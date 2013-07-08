$(function() {
    var I18N = Hrcms.I18N;
    Hrcms.TableEditor = {};
    Hrcms.TableEditor.create = function(config) {
        var tThis = {};
        var SyncGet = Hrcms.SyncGet;
        var SyncUUID = Hrcms.SyncUUID;
        var TABLE_UUID = "table-uuid";
        var container = config.container;
        var tableEditorContainer = $('<div class="hrcms-teditor-container" />').appendTo(container);
        var workspaceHeader = Hrcms.NavIndicator.create(tableEditorContainer);
        var tableContainer = $('<div class="hrcms-teditor-table-container"/>').appendTo(tableEditorContainer);
        // TODO need a row column selector
        var tableHolder = $(
        '<table class="hrcms-move-active">' +
        '  <tr><td style="height:20px;"></td></tr>' +
        '  <tr><td><div id="hrcms-table-holder"/></td></tr>' +
        '</table>').appendTo(tableContainer);
        var tableBox = tableHolder.find("#hrcms-table-holder");
        var tdHtmlTpl = '<td style="width:50px;height:20px;border-width:1px;border-style:solid;border-color:rgb(163, 163, 163);"></td>';
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
        function getColspan(td) {
            var colspan = td.attr("colspan");
            return colspan ? parseInt(colspan) : 1;
        }
        function getRowspan(td) {
            var rowspan = td.attr("rowspan");
            return rowspan ? parseInt(rowspan) : 1;
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
                            var box = $('<div class="hrcms-table-holder-box" style="padding-left:20px;"><table class="hrcms-report-table"></div>').appendTo(tableBox);
                            var table = box.find('table');
                            table.selectable({
                                start: function(evt, ui) {
                                    tableBox.find('.ui-selected').removeClass('ui-selected');
                                },
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
                create: function() {
                    // Only keep first cell is selected
                    var selectedCells = getSelectedCells();
                    if (selectedCells.length > 0) {
                        for (var i = 1; i < selectedCells.length; ++i)
                            $(selectedCells[i].cell).removeClass("ui-selected");
                    }
                },
                buttons: [
                    {
                        text: I18N.Widget.Button_Ok,
                        click: function() {
                            var row = message.find("#rowCount").val();
                            row = row ? parseInt(row) : 1;
                            var rowPosition = message.find('input[name="rowPosition"]:checked').val();
                            if (Hrcms.debugEnabled)
                                console.log(row + ", " + rowPosition);
                            var selectedCells = getSelectedCells();
                            if (selectedCells.length > 0 && row > 0) {
                                var selectedRow = $(selectedCells[0].cell.parentElement);
                                var tdList = selectedRow.find('td');
                                var col = tdList.length;
                                var prevRow = selectedRow[0];
                                var offset = 1;
                                var effectedRowspanCells = [];
                                var topEffectedRowspanCells = [];
                                var bottomEffectedRowspanCells = [];
                                tdList.each(function(i, cell) {
                                    var td = $(cell);
                                    var rowspan = getRowspan(td);
                                    var colspan = getColspan(td);
                                    colspan = colspan ? parseInt(colspan) : 1;
                                    if (colspan > 1 || rowspan > 1)
                                        topEffectedRowspanCells.push({
                                            colspan: colspan,
                                            cell: cell
                                        });
                                });
                                while (prevRow) {
                                    if (Hrcms.debugEnabled)
                                        console.log(prevRow);
                                    $(prevRow).find('td').each(function(i, cell) {
                                        var td = $(cell);
                                        var rowspan = getRowspan(td);
                                        var colspan = getColspan(td);
                                        if (colspan > 1 && rowspan === offset) {
                                            bottomEffectedRowspanCells.push({
                                                colspan: colspan,
                                                cell: cell
                                            });
                                        }
                                        if (rowspan > 1 && 1 === offset && rowPosition === "Below")
                                            // the cell is at top edge
                                            effectedRowspanCells.push(td);
                                        else if (rowspan > 1 && rowspan === offset && rowPosition === "Above")
                                            // the cell is at bottom edge
                                            effectedRowspanCells.push(td);
                                        else if (rowspan > 1 && rowspan > offset && offset !== 1)
                                            effectedRowspanCells.push(td);
                                    });
                                    prevRow = prevRow.previousElementSibling;
                                    ++offset;
                                }

                                if (rowPosition === "Above") {
                                    col -= topEffectedRowspanCells.length;
                                    for (var i = 0; i < topEffectedRowspanCells.length; ++i) {
                                        col += topEffectedRowspanCells[i].colspan;
                                    }
                                } else if (rowPosition === "Below") {
                                    col -= topEffectedRowspanCells.length;
                                    for (var i = 0; i < bottomEffectedRowspanCells.length; ++i) {
                                        col += bottomEffectedRowspanCells[i].colspan;
                                    }
                                }
                                for (var i = 0; i < row; ++i) {
                                    var tr = $('<tr/>');
                                    rowPosition === "Above" ? selectedRow.before(tr) : selectedRow.after(tr);
                                    for (var j = 0; j < col; ++j) {
                                        $(tdHtmlTpl).appendTo(tr);
                                    }
                                }
                                for (var i = 0; i < effectedRowspanCells.length; ++i) {
                                    var rowspan = effectedRowspanCells[i].attr("rowspan");
                                    rowspan = rowspan ? parseInt(rowspan) : 1;
                                    effectedRowspanCells[i].attr("rowspan", rowspan + row);
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

        function getTDbyColspan(tr, cellIndex) {
            var tdList = tr.find("td");
            var td;
            var index = 0;
            for (var i = 0; i < tdList.length && index < cellIndex; ++i) {
                td = $(tdList[i]);
                var colspan = getColspan(td);
                index += colspan;
            }
            return td;
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
                create: function() {
                    // Only keep first cell is selected
                    var selectedCells = getSelectedCells();
                    if (selectedCells.length > 0) {
                        for (var i = 1; i < selectedCells.length; ++i)
                            $(selectedCells[i].cell).removeClass("ui-selected");
                    }
                },
                buttons: [
                    {
                        text: I18N.Widget.Button_Ok,
                        click: function() {
                            var col = message.find("#colCount").val();
                            col = col ? parseInt(col) : 1;
                            var colPosition = message.find('input[name="colPosition"]:checked').val();
                            if (Hrcms.debugEnabled)
                                console.log(col + ", " + colPosition);
                            var selectedCells = getSelectedCells();
                            if (selectedCells.length > 0 && col > 0) {
                                var cell = $(selectedCells[0].cell);
                                var currentTable = cell.closest("table");
                                var trList = currentTable.find('tr');
                                var colNum = selectedCells[0].cell.cellIndex;
                                for (var j = 0; j < col; ++j) {
                                    for (var i = 0; i < trList.length; ++i) {
                                        var td = getTDbyColspan($(trList[i]), colNum + 1);
                                        var colspan = getColspan(td);
                                        var colRange = (td[0].cellIndex + colspan - 1);
                                        if (colspan === 1) {
                                            if (colPosition === "Left")
                                                $(tdHtmlTpl).insertBefore(td);
                                            else
                                                $(tdHtmlTpl).insertAfter(td);
                                        }
                                        else if (colspan > 1) {
                                            if (colNum < colRange && colNum > td[0].cellIndex) {
                                                td.attr("colspan", (colspan + 1));
                                            } else if (colNum === td[0].cellIndex) {
                                                if (colPosition === "Left")
                                                    $(tdHtmlTpl).insertBefore(td);
                                                else
                                                    td.attr("colspan", (colspan + 1));
                                            } else if (colNum === colRange) {
                                                if (colPosition === "Left")
                                                    td.attr("colspan", (colspan + 1));
                                                else
                                                    $(tdHtmlTpl).insertAfter(td);
                                            }
                                        }
                                    }
                                    if (colPosition === "Left")
                                        colNum++;
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
        function getSelectedCells() {
            var selectedCells = tableContainer.find(".ui-selected");
            // Normalized cell array ------------------------------>
            var normalizedCells = [];
            selectedCells.each(function(index, cell) {
                var td = $(cell);
                var colspan = getColspan(td);
                var rowspan = getRowspan(td);
                if (Hrcms.debugEnabled) {
                    console.log("colspan: " + colspan + "; rowspan: " + rowspan);
                }
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

            return normalizedCells;
        }
        function joinTableCells(event) {
            var normalizedCells = getSelectedCells();
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
                create: function() {
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
                                var borderWidthCSS;
                                if (border.Top)
                                    borderWidthCSS = "1px";
                                else
                                    borderWidthCSS = "0px";
                                if (border.Right)
                                    borderWidthCSS += " 1px";
                                else
                                    borderWidthCSS += " 0px";
                                if (border.Bottom)
                                    borderWidthCSS += " 1px";
                                else
                                    borderWidthCSS += " 0px";
                                if (border.Left)
                                    borderWidthCSS += " 1px";
                                else
                                    borderWidthCSS += " 0px";
                                selectedCells.css("border-width", borderWidthCSS);
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
