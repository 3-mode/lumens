$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.Curdbar = {};
    Hrcms.Curdbar.create = function(config) {
        var tThis = {};
        var container = config.container;
        var toolbar = $(htmlTpl).appendTo(container);
        tThis.configure = function(config) {
            var navTarget = config.tableNavTarget;
            // TODO need to use navTarget to call target object function
            var buttons = toolbar.find('.hrcms-toolbar-button');
            function button_click(event) {
                var text = null;
                if ($(this).attr("id") === "find_table_record") {
                    text = "Search button Not implemented !!!";
                }
                else {
                    text = "Hi, you click me !!!";
                }
                var message = $('<div><p>' + text + '</p><div>');
                message.dialog({
                    position: {my: "center top", at: "center bottom", of: $(this)},
                    modal: true,
                    resizable: false,
                    buttons: [
                        {
                            text: "确定", click: function() {
                                $(this).dialog("close");
                            }
                        }
                    ]
                });
            }
            buttons.on("click", button_click);
            return this;
        }
        // end
        return tThis;
    }

    var htmlTpl = '<div style="padding-left:10px;position:relative;">' +
    '<table cellspacing="0" cellpadding="0" border="0" style="table-layout:auto;" class="hrcms-toolbar"><tbody><tr>' +
    '  <td><div id="add_table_record" class="hrcms-toolbar-button hrcms-toolbar-button-add ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="delete_table_record" class="hrcms-toolbar-button hrcms-toolbar-button-delete ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="reload_table_record" class="hrcms-toolbar-button hrcms-toolbar-button-reload ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="find_table_record" class="hrcms-toolbar-button hrcms-toolbar-button-find ui-corner-all"/></td>' +
    '</tr></tbody></table></div>';
});