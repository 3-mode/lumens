$(function() {
    Hrcms.DataGrid = {}
    Hrcms.DataGrid.create = function(config) {
        var tThis = {};
        var container = config.container;
        var gridContainer = $('<div class="hrcms-datagrid hrcms-datagrid-container"/>').appendTo(container);
        var fixedHeaderContainer = $('<div style="position:relative;"/>').appendTo(gridContainer);
        var fxiedHeaderTable = $('<table class="hrcms-datagrid-header"/>').appendTo(fixedHeaderContainer);
        var dataContainer = $('<div class="hrcms-data-container"/>').appendTo(gridContainer);
        var table = $('<table class="hrcms-datagrid-table"/>').appendTo(dataContainer);
        var tableHeaderFixedHeight = 30;
        var tableBody;
        var fixedTableHeader;
        var currentSortTh;
        var configuration;
        var offsetHeight = (config.offsetHeight ? config.offsetHeight + tableHeaderFixedHeight : tableHeaderFixedHeight);
        var offsetWidth = (config.offsetWidth ? config.offsetWidth : 0);
        dataContainer.css("width", container.width() - offsetWidth);
        dataContainer.css("height", container.height() - offsetHeight);
        container.resize(function(event) {
            if (event.target !== this)
                return;
            dataContainer.css("width", container.width() - offsetWidth);
            dataContainer.css("height", container.height() - offsetHeight);
        });
        dataContainer.scroll(function(event) {
            if (Hrcms.debugEnabled)
                console.log(event);
            fixedHeaderContainer.css("left", -event.target.scrollLeft);
        })
        function buildColumns(config) {
            var columns = config.columns;
            var event = config.event ? config.event : {};
            function sort() {
                var clickedTh = $(this);
                if (currentSortTh) {
                    if (clickedTh[0] === currentSortTh[0]) {
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
            var thead = fixedTableHeader = $('<thead><tr></tr></thead>').appendTo(fxiedHeaderTable);
            thead = thead.find('tr');
            var th = $('<th width="1" style="padding-left:6px;"> <input type="checkbox"></th>').appendTo(thead);
            for (var i = 0; i < columns.length; ++i) {
                th = $('<th style="padding-left: 8px; padding-right: 20px;"/>').appendTo(thead);
                th.addClass("hrcms-datagrid-header sortable");
                th.attr("field-name", columns[i].field);
                th.on('click', sort);
                var htxt = $('<div class="hrcms-datagrid-header-text"></div>').appendTo(th);
                htxt.html(columns[i].name);
            }
        }
        // Member methods
        tThis.toggle = function() {
            gridContainer.toggle();
        }
        tThis.configure = function(config) {
            configuration = config
            buildColumns(config);
            var columns = config.columns;
            tableBody = $('<tbody/>').appendTo(table);
            table.append('<tfoot><tr><td colspan="' + (columns.length + 1) + '"><div style="height:20px;"></div></td></tr></tfoot>');
            return this;
        }
        tThis.data = function(records) {
            var columns = configuration.columns;
            for (var i = 0; i < records.length; ++i) {
                var tr = $('<tr/>').appendTo(tableBody);
                tr.addClass("hrcms-datagrid-row");
                tr.attr('row-number', i);
                if (configuration.event.rowclick)
                    tr.click(configuration.event.rowclick);
                // TODO need to add event handler for this th
                var th = $('<th width="1" style="padding-left:6px; padding-right:8px;"><input type="checkbox"></th>').appendTo(tr);
                var field = records[i].field_value;
                for (var j = 0; j < columns.length; ++j) {
                    td = $('<td style="padding-left: 8px; padding-right: 20px;"><div></div></td>').appendTo(tr);
                    td.attr('field-name', columns[j].field);
                    if (j < field.length)
                        td.find("div").html(field[j]);
                }
            }
            // Update header width
            var fixedHeaders = fixedTableHeader.find("div");
            var firstTr = tableBody.find("tr:first");
            var firstTdList = firstTr.find("div");
            for (var i = 0; i < fixedHeaders.length; ++i) {
                var jElem = $(firstTdList[i]);
                var jFixed = $(fixedHeaders[i]);
                var oldWidth = jFixed.attr("init-width");
                if (!oldWidth) {
                    oldWidth = jFixed.width();
                    jFixed.attr("init-width", oldWidth);
                }
                else {
                    oldWidth = parseInt(oldWidth);
                }
                if (jElem.width() < oldWidth)
                    jElem.css("width", oldWidth);
                else
                    jFixed.css("width", jElem.width());
            }
            return this;
        }
        tThis.remove = function() {
            fixedHeaderContainer.remove();
            gridContainer.remove();
        }
        // end
        return tThis;
    }
});

