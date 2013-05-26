$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.DataGrid = {}
    Hrcms.DataGrid.create = function(config) {
        var tThis = {};
        var container = config.container;
        var gridContainer = $('<div class="hrcms-datagrid-container" />').appendTo(container);
        var table = $('<table class="hrcms-datagrid"/>').appendTo(gridContainer);
        var tableBody = null;
        var currentSortTh = null;
        var columns = null;
        var event = null;
        var offsetHeight = (config.offsetHeight ? config.offsetHeight : 0);
        var offsetWidth = (config.offsetWidth ? config.offsetWidth : 0);
        gridContainer.css("width", container.width() - offsetWidth);
        gridContainer.css("height", container.height() - offsetHeight);
        container.resize(function(event) {
            if (event.target !== this)
                return;
            gridContainer.css("width", container.width() - offsetWidth);
            gridContainer.css("height", container.height() - offsetHeight);
        });
        tThis.configure = function(config) {
            columns = config.columns;
            event = config.event ? config.event : {};
            var thead = $('<thead><tr></tr></thead>').appendTo(table);
            thead = thead.find('tr');
            dataFieldNames = [];
            function sort() {
                var clickedTh = $(this);
                if (currentSortTh !== null) {
                    if (clickedTh.get(0) === currentSortTh.get(0)) {
                        if (currentSortTh.hasClass("sortdown")) {
                            currentSortTh.removeClass("sortdown");
                            currentSortTh.addClass("sortup");
                            if (event.sortup)
                                event.sortup({tbody: tableBody, column: currentSortTh});
                        }
                        else if (currentSortTh.hasClass("sortup")) {
                            currentSortTh.removeClass("sortup");
                            currentSortTh.addClass("sortdown");
                            if (event.sortdown)
                                event.sortdown({tbody: tableBody, column: currentSortTh});
                        }
                    }
                    else {
                        if (currentSortTh.hasClass("sortdown")) {
                            currentSortTh.removeClass("sortdown");
                            clickedTh.addClass("sortdown");
                            if (event.sortdown)
                                event.sortdown({tbody: tableBody, column: currentSortTh});
                        }
                        else if (currentSortTh.hasClass("sortup")) {
                            currentSortTh.removeClass("sortup");
                            clickedTh.addClass("sortup");
                            if (event.sortup)
                                event.sortup({tbody: tableBody, column: currentSortTh});
                        }
                        currentSortTh = clickedTh;
                    }
                }
                else {
                    currentSortTh = clickedTh;
                    currentSortTh.addClass("sortdown");
                    if (event.sortdown)
                        event.sortdown({tbody: tableBody, column: currentSortTh});
                }
            }
            var th = $('<th style="padding-left:6px;" width="1"> <input type="checkbox"></th>').appendTo(thead);
            for (var i = 0; i < columns.length; ++i) {
                th = $('<th/>').appendTo(thead);
                th.addClass("hrcms-datagrid-header sortable");
                th.css("padding-left", "8px");
                th.css("padding-right", "20px");
                th.attr("field-name", columns[i].field);
                th.on('click', sort);
                var div = $('<div class="hrcms-datagrid-header-text"></div>').appendTo(th);
                div.html(columns[i].name);
            }
            tableBody = $('<tbody/>').appendTo(table);
            table.append('<tfoot><tr><td colspan="' + (columns.length + 1) + '"><div style="height:20px;"></div></td></tr></tfoot>');
        }
        tThis.data = function(records) {
            for (var i = 0; i < records.length; ++i) {
                var tr = $('<tr/>').appendTo(tableBody);
                tr.addClass("hrcms-datagrid-row");
                tr.attr('row-number', i);
                if (event.rowclick)
                    tr.click(event.rowclick);
                var th = $('<th width="1"><input type="checkbox"></th>').appendTo(tr);
                var field = records[i].field_value;
                for (var j = 0; j < columns.length; ++j) {
                    td = $('<td/>').appendTo(tr);
                    td.attr('field-name', columns[j].field);
                    if (j < field.length)
                        td.html(field[j]);
                }
            }
        }
        tThis.remove = function() {
            gridContainer.remove();
        }
        // end
        return tThis;
    }
});

