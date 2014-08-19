/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
Lumens.NavToolbar = Class.$extend({
    __init__: function(containerObj) {
        this.$container = containerObj;
        this.$toolbar = $('<div class="lumens-nav-toolbar"/>').appendTo(this.$container);
        this.$toolbarContent = $('<ul/>').appendTo(this.$toolbar);
        this.buttonList = {};
        var __this = this;
        this.click = function(event) {
            if (__this.activeItem)
                __this.activeItem.toggleClass("lumens-v-active");
            __this.activeItem = $(this).data("style-elem");
            __this.activeItem.toggleClass("lumens-v-active");
            __this.$toolbarContent.trigger(jQuery.Event(__this.configuration.event_type, {
                module_id: __this.activeItem.attr("module-id")
            }));
        };
    },
    onButtonClick: function(buttonClick) {
        this.$toolbarContent.on(this.configuration.event_type, buttonClick);
    },
    configure: function(config) {
        this.configuration = config;
        var buttons = config.buttons;
        for (var i = 0; i < buttons.length; ++i) {
            var button = $('<li><a><span class="lumens-toolbar-button-text"></span></a></li>')
            .appendTo(this.$toolbarContent)
            .attr("module-id", buttons[i].module_id);
            button.find("span").html(buttons[i].name);
            this.buttonList[buttons[i].module_id] = button.find("a").attr("href", "#/" + buttons[i].module_id).on("click", this.click).data("style-elem", button);
        }
        if (config.default_active)
            this.buttonList[config.default_active].trigger("click");
        return this;
    },
    remove: function() {
        this.$toolbar.remove();
    },
    active: function(module_id) {
        if (this.buttonList[module_id])
            this.buttonList[module_id].trigger("click");
    }
});

