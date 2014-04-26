/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
Lumens.List = Class.$extend({
    __init__: function(container) {
        this.$container = container;
        this.$accordionHolder = $('<div class="lumens-accordion"></div>').appendTo(this.$container);
        this.$container.resize(this.layout);

        this.sectionHTMLTemplate =
        '<div class="lumens-accordion-item">' +
        '  <div class="lumens-accordion-title">' +
        '    <div style="padding-top: 5px; padding-left: 10px;">' +
        '      <div class="lumens-accordion-icon icon-collapse">' +
        '         <b></b>' +
        '      </div>' +
        '    </div>' +
        '  </div>' +
        '  <ul class="lumens-accordion-content"><li id="content-holder"></li></ul>' +
        '</div>';
    },
    remove: function() {
        this.$accordionHolder.unbind();
        this.$accordionHolder.remove();
        this.$container.unbind("resize", this.layout);
    },
    layout: function(e) {
        if (e && e.target !== this)
            return;
        if (this.$accordionHolder)
            this.$accordionHolder.find(".lumens-accordion-content").trigger("resize");
    },
    doExpandCollapse: function(accordion) {
        accordion.find("ul").toggle(200);
        accordion.find(".lumens-accordion-icon").toggleClass("icon-expand").toggleClass("icon-collapse");
    },
    addAccordion: function(config, i) {
        var __this = this;
        var accordion = $(this.sectionHTMLTemplate).appendTo(this.$accordionHolder);
        if (config.titleList && config.titleList.length > i)
            accordion.find("b").html(config.titleList[i]);
        if (config.contentList && config.contentList.length > i)
            accordion.find("#content-holder").append(config.contentList[i]);

        var accordionTitle = accordion.find(".lumens-accordion-title");
        var form = accordion.find("ul");
        form.toggle(200);
        accordionTitle.attr("id", config.IdList[i])
        .click(function() {
            __this.doExpandCollapse(accordion);
            // Load data
            if (config.buildContent) {
                __this.buildContent(config, accordion);
            }
        });
    },
    configure: function(config) {
        for (var i = 0; i < config.IdList.length; ++i)
            this.addAccordion(config, i);

        var accordions = this.$accordionHolder.find(".lumens-accordion-item");
        if (accordions.length) {
            var accordion = $(accordions[0]);
            this.doExpandCollapse($(accordions[0]));
            this.buildContent(config, accordion);
        }
        return this;
    },
    buildContent: function(config, accordion) {
        var contentHolder = accordion.find("#content-holder");
        if (config.buildContent && contentHolder.children().length === 0)
            config.buildContent(contentHolder, accordion.find(".icon-expand").length > 0, accordion.find(".lumens-accordion-title"), accordion.find(".lumens-accordion-title").find('b'));
    }
});


