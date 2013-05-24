$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    var SyncGet = Hrcms.SyncGet;
    Hrcms.Toolbar = {};
    Hrcms.Toolbar.create = function(config) {
        var tThis = {};
        var container = config.container;
        var htmlTpl = SyncGet({
            url: "html/toolbar-tpl.html",
            dataType: "html"
        });
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
                    buttons: {
                        Ok: function() {
                            $(this).dialog("close");
                        }
                    }
                });
            }
            buttons.on("click", button_click);
        }
        // end
        return tThis;
    }
});