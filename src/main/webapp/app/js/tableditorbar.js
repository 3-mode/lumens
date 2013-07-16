$(function() {
    var I18N = Hrcms.I18N;
    Hrcms.TableEditorbar = {};
    Hrcms.TableEditorbar.create = function(config) {
        var tThis = {};
        var container = config.container;
        var toolbar = $(htmlTpl).appendTo(container);
        toolbar.find('#add_table').attr("title", I18N.Widget.TableAdding);
        toolbar.find('#edit_table').attr("title", I18N.Widget.TableCellSettings);
        toolbar.find('#join_table_cell').attr("title", I18N.Widget.TableCellJoin);
        toolbar.find('#insert_table_row').attr("title", I18N.Widget.TableRowAdding);
        toolbar.find('#join_table_cell').attr("title", I18N.Widget.TableCellJoin);
        tThis.configure = function(config) {
            var navTarget = config.tableNavTarget;
            // TODO need to use navTarget to call target object function
            var buttons = toolbar.find('.hrcms-toolbar-button');
            buttons.on("click", function(event) {
                var id = $(this).attr("id");
                if (id === "insert_table_row" && config.insertTableRow)
                    config.insertTableRow(event);
                else if (id === "delete_table_row" && config.deleteTableRow)
                    config.deleteTableRow(event);
                else if (id === "insert_table_column" && config.insertTableColumn)
                    config.insertTableColumn(event);
                else if (id === "delete_table_column" && config.deleteTableColumn)
                    config.deleteTableColumn(event);
                else if (id === "join_table_cell" && config.joinTableCell)
                    config.joinTableCell(event);
                else if (id === "add_table" && config.addTable)
                    config.addTable(event);
                else if (id === "delete_table" && config.deleteTable)
                    config.deleteTable(event);
                else if (id === "edit_table" && config.editTable)
                    config.editTable(event);
                else if (id === "add_chart" && config.addChart)
                    config.addChart(event);
                else if (id === "save_table" && config.saveTable)
                    config.saveTable(event);
            });
            return this;
        }
        // end
        return tThis;
    }

    var htmlTpl = '<div style="padding-left:10px;position:relative;">' +
    '<table cellspacing="0" cellpadding="0" border="0" style="table-layout:auto;" class="hrcms-toolbar"><tbody><tr>' +
    '  <td><div id="add_table" class="hrcms-toolbar-button hrcms-tableditor-add-table ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="delete_table" class="hrcms-toolbar-button hrcms-tableditor-delete-table ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="insert_table_row" class="hrcms-toolbar-button hrcms-tableditor-insert-row ui-corner-all"/></td>' +
    /*'  <td style="width:5px;"></td>' +
     '  <td><div id="delete_table_row" class="hrcms-toolbar-button hrcms-tableditor-delete-row ui-corner-all"/></td>' + */
    '  <td style="width:5px;"></td>' +
    '  <td><div id="insert_table_column" class="hrcms-toolbar-button hrcms-tableditor-insert-column ui-corner-all"/></td>' +
    /*'  <td style="width:5px;"></td>' +
     '  <td><div id="delete_table_column" class="hrcms-toolbar-button hrcms-tableditor-delete-column ui-corner-all"/></td>' + */
    '  <td style="width:5px;"></td>' +
    '  <td><div id="join_table_cell" class="hrcms-toolbar-button hrcms-tableditor-join-cell ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="edit_table" class="hrcms-toolbar-button hrcms-tableditor-edit-table ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="add_chart" class="hrcms-toolbar-button hrcms-tableditor-chart ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="data_table_bind" class="hrcms-toolbar-button hrcms-tableditor-data-bind ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="save_table" class="hrcms-toolbar-button hrcms-tableditor-save-table ui-corner-all"/></td>' +
    '</tr></tbody></table></div>';
});