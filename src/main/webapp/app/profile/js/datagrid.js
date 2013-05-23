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
        var dataFieldNames = null;
        var currentSortTh = null;
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
            var sortup = config.sortup;
            var sortdown = config.sortdown;
            var thead = $('<thead><tr></tr></thead>').appendTo(table);
            thead = thead.find('tr');
            var columns = config.columns;
            dataFieldNames = [];
            function sort() {
                var clickedTh = $(this);
                if (currentSortTh !== null) {
                    if (clickedTh.get(0) === currentSortTh.get(0)) {
                        if (currentSortTh.hasClass("sortdown")) {
                            currentSortTh.removeClass("sortdown");
                            currentSortTh.addClass("sortup");
                            if (sortup !== undefined)
                                sortup(tableBody, currentSortTh);
                        }
                        else if (currentSortTh.hasClass("sortup")) {
                            currentSortTh.removeClass("sortup");
                            currentSortTh.addClass("sortdown");
                            if (sortdown !== undefined)
                                sortdown(tableBody, currentSortTh);
                        }
                    }
                    else {
                        if (currentSortTh.hasClass("sortdown")) {
                            currentSortTh.removeClass("sortdown");
                            clickedTh.addClass("sortdown");
                            if (sortdown !== undefined)
                                sortdown(tableBody, clickedTh);
                        }
                        else if (currentSortTh.hasClass("sortup")) {
                            currentSortTh.removeClass("sortup");
                            clickedTh.addClass("sortup");
                            if (sortup !== undefined)
                                sortup(tableBody, currentSortTh);
                        }
                        currentSortTh = clickedTh;
                    }
                }
                else {
                    currentSortTh = clickedTh;
                    currentSortTh.addClass("sortdown");
                    sortdown(tableBody, clickedTh);
                }
            }
            for (var i = 0; i < columns.length; ++i) {
                var th = $('<th/>').appendTo(thead);
                th.addClass("hrcms-datagrid-header sortable");
                th.css("padding-left", "8px");
                th.css("padding-right", "20px");
                th.attr("field-name", columns[i].field);
                th.on('click', sort);
                var div = $('<div class="hrcms-datagrid-header-text"></div>').appendTo(th);
                div.html(columns[i].name);
                dataFieldNames.push(columns[i].field);
            }
            tableBody = $('<tbody/>').appendTo(table);
            table.append('<tfoot><tr><td colspan="' + columns.length + '"><div style="height:25px;"></div></td></tr></tfoot>');
        }
        tThis.data = function(records) {
            for (var i = 0; i < records.length; ++i) {
                var tr = $('<tr/>').appendTo(tableBody);
                tr.addClass("hrcms-datagrid-row");
                tr.attr('row-number', i);
                var field = records[i].field_value;
                for (var j = 0; j < dataFieldNames.length; ++j) {
                    td = $('<td/>').appendTo(tr);
                    td.attr('field-name', dataFieldNames[j]);
                    td.html(field[j]);
                }
            }
        }
        // end
        return tThis;
    }
})

