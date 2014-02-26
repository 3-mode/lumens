/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
Lumens.NavToolbar = Class.$extend({
    __init__: function(containerObj) {
        var __this = this;
        this.$container = containerObj;
        this.$toolbar = $('<div class="lumens-nav-toolbar"/>').appendTo(this.$container);
        var toolbarContent = this.$toolbarContent = $('<ul/>').appendTo(this.$toolbar);
        var configuration = null;
        var buttonList = {};
        var activeItem = null;
        function activeButton(event) {
            if (activeItem)
                activeItem.toggleClass("lumens-v-active");
            activeItem = $(this);
            activeItem.toggleClass("lumens-v-active");
            toolbarContent.trigger(jQuery.Event(configuration.event_type, {
                module_id: activeItem.attr("module-id")
            }));
        }
        this.activeButton = function(module_id) {
            buttonList[module_id].trigger("click");
        };
        this.onButtonClick = function(buttonClick) {
            toolbarContent.on(configuration.event_type, buttonClick);
        };
        this.configure = function(config) {
            configuration = config;
            var buttons = config.buttons;
            for (var i = 0; i < buttons.length; ++i) {
                var button = $('<li><a><span class="lumens-toolbar-button-text"></span></a></li>').appendTo(toolbarContent);
                button.attr("module-id", buttons[i].module_id);
                button.find('span').html(buttons[i].name);
                button.on('click', activeButton);
                buttonList[buttons[i].module_id] = button;
            }
            __this.activeButton(config.default_active);
            return __this;
        };
        this.remove = function() {
            __this.$toolbar.remove();
        };
    }
});

