$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.Header = {};
    Hrcms.Header.create = function(containerObj) {
        var tThis = {};
        var header = $('<div class="hrcms-header"/>').appendTo(containerObj);
        header.append('<table><tr><td><div class="hrcms-logo"/></td><td><div id="hrcms-system-title"></div></td></tr></table>');
        var sysTitle = $("#hrcms-system-title");
        tThis.setSysTitle = function(text) {
            sysTitle.html(text);
        }
        // end
        return tThis;

    }

    Hrcms.NavToolbar = {};
    Hrcms.NavToolbar.create = function(containerObj) {
        var tThis = {};
        var container = containerObj;
        var toolbar = $('<div class="hrcms-toolbar"/>').appendTo(container);
        var toolbarContent = $('<ul/>').appendTo(toolbar);
        var buttonList = [];
        var activeItem = null;
        function activeButton(event) {
            if (activeButton !== null)
                activeItem.toggleClass("hrcms-v-active");
            activeItem = $(this);
            activeItem.toggleClass("hrcms-v-active");
        }
        tThis.configure = function(config) {
            var callback = config.event_callback;
            var buttons = config.settings;
            for (var i = 0; i < buttons.length; ++i) {
                var button = $('<li><a><span class="hrcms-toolbar-button-text"></span></a></li>').appendTo(toolbarContent);
                button.find('span').html(buttons[i].title);
                button.on('click', activeButton);
                if (callback)
                    button.on('click', callback);
                buttonList.push(button);
            }
            activeItem = buttonList[0];
            activeItem.toggleClass("hrcms-v-active");
        }
        // end
        return tThis;
    }
});