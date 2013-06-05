$(function() {
    Hrcms.Tablebar = {};
    Hrcms.Tablebar.create = function(config) {
        var tThis = {};
        var container = config.container;
        var toolbar = $(htmlTpl).appendTo(container);
        tThis.configure = function(config) {
            var navTarget = config.tableNavTarget;
            // TODO need to use navTarget to call target object function
            var buttons = toolbar.find('.hrcms-toolbar-button');
            function button_click(event) {
                var text = null;
                if ($(this).attr("id") === "find_pagernav") {
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
    '  <td><div id="first_pagernav" class="hrcms-toolbar-button hrcms-toolbar-button-begin ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="prev_pagernav" class="hrcms-toolbar-button hrcms-toolbar-button-prev ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td id="info_pagernav" colspan="2" style="padding-left:1px; padding-right:6px;">' +
    '    <input id="current_records" type="text" value="122" size="2" maxlength="10" style="border:0px;height:18px;"/>' +
    '    <span id="total_number" style="height:20px;"> / 23223</span></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td> <div id="next_pagernav" class="hrcms-toolbar-button hrcms-toolbar-button-next ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="last_pagernav" class="hrcms-toolbar-button hrcms-toolbar-button-end ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="add_pagernav" class="hrcms-toolbar-button hrcms-toolbar-button-add ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="delete_pagernav" class="hrcms-toolbar-button hrcms-toolbar-button-delete ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="reload_pagernav" class="hrcms-toolbar-button hrcms-toolbar-button-reload ui-corner-all"/></td>' +
    '  <td style="width:5px;"></td>' +
    '  <td><div id="find_pagernav" class="hrcms-toolbar-button hrcms-toolbar-button-find ui-corner-all"/></td>' +
    '</tr></tbody></table></div>';
});