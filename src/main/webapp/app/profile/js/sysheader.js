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

    Hrcms.SysToolbar = {};
    Hrcms.SysToolbar.create = function(containerObj) {
        var tThis = {};
        var container = containerObj;
        var toolbar = $('<div class="hrcms-nav-toolbar"/>').appendTo(container);
        var toolbarContent = $('<ul/>').appendTo(toolbar);
        var configuration = null;
        var buttonList = {};
        var activeItem = null;
        function activeButton(event) {
            if (activeItem)
                activeItem.toggleClass("hrcms-v-active");
            activeItem = $(this);
            activeItem.toggleClass("hrcms-v-active");
            toolbarContent.trigger(jQuery.Event(configuration.eventType, {
                moduleID: activeItem.attr("module-id")
            }));
        }
        tThis.activeButton = function(moduleID) {
            buttonList[moduleID].trigger("click");
        }
        tThis.onButtonClick = function(buttonClick) {
            toolbarContent.on(configuration.eventType, buttonClick);
        }
        tThis.configure = function(config) {
            configuration = config;
            var buttons = config.buttons;
            for (var i = 0; i < buttons.length; ++i) {
                var button = $('<li><a><span class="hrcms-toolbar-button-text"></span></a></li>').appendTo(toolbarContent);
                button.attr("module-id", buttons[i].moduleID);
                button.find('span').html(buttons[i].title);
                button.on('click', activeButton);
                buttonList[buttons[i].moduleID] = button;
            }
        }
        // end
        return tThis;
    }
});