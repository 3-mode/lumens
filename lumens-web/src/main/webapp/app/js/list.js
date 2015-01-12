/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
Lumens.List = Class.$extend({
    __init__: function (container) {
        this.$container = container;
        this.$accordionHolder = $('<div class="lumens-accordion"></div>').appendTo(this.$container);
        this.$container.resize(this.layout);

        this.sectionHTMLTemplate =
        '<div class="lumens-accordion-item">' +
        '  <div class="lumens-accordion-title lumens-accordion-border">' +
        '    <div style="padding-top: 5px; padding-left: 10px;">' +
        '      <div class="lumens-accordion-icon icon-collapse">' +
        '         <b id="id-accordion-title"></b>' +
        '      </div>' +
        '    </div>' +
        '  </div>' +
        '  <ul class="lumens-accordion-content"><li id="content-holder"></li></ul>' +
        '</div>';
    },
    remove: function () {
        this.$accordionHolder.unbind();
        this.$accordionHolder.remove();
        this.$container.unbind("resize", this.layout);
    },
    layout: function (e) {
        if (e && e.target !== this)
            return;
        if (this.$accordionHolder)
            this.$accordionHolder.find(".lumens-accordion-content").trigger("resize");
    },
    doExpandCollapse: function (accordion) {
        accordion.find("ul").toggle(200);
        accordion.find(".lumens-accordion-icon").toggleClass("icon-expand").toggleClass("icon-collapse");
    },
    addAccordion: function (config, i, isAddToHead) {
        var __this = this;
        var accordion;
        if (isAddToHead)
            accordion = $(this.sectionHTMLTemplate).prependTo(this.$accordionHolder);
        else
            accordion = $(this.sectionHTMLTemplate).appendTo(this.$accordionHolder);
        if (config.titleList && config.titleList.length > i)
            accordion.find("#id-accordion-title").html(config.titleList[i]);
        if (config.contentList && config.contentList.length > i)
            accordion.find("#content-holder").append(config.contentList[i]);

        var accordionTitle = accordion.find(".lumens-accordion-title");
        accordion.find("ul").hide();
        accordion.attr("id", config.IdList[i])
        accordionTitle.click(function () {
            __this.doExpandCollapse(accordion);
            // Load data
            if (config.buildContent) {
                __this.buildContent(config, accordion);
            }
        });
    },
    addItem: function (config, pos, isAddToHead) {
        this.addAccordion(config, 0, isAddToHead);
        var accordion = this.$accordionHolder.find("#" + config.IdList[0]);
        if (pos === 0)
            this.doExpandCollapse(accordion);
        var contentHolder = accordion.find("#content-holder");
        if (config.buildContent && contentHolder.children().length === 0)
            config.buildContent(contentHolder);
        return this;
    },
    configure: function (config) {
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
    buildContent: function (config, accordion) {
        var contentHolder = accordion.find("#content-holder");
        if (config.buildContent && contentHolder.children().length === 0)
            config.buildContent(contentHolder, accordion.attr("id"), accordion.find(".icon-expand").length > 0, accordion.find(".lumens-accordion-title"), accordion.find(".lumens-accordion-title").find('b'));
    }
});

Lumens.RecordList = Lumens.List.$extend({
    __init__: function (container) {
        this.$super(container);
        this.sectionHTMLTemplate =
        '<div class="lumens-accordion-item">' +
        '  <div class="lumens-accordion-title lumens-record-border">' +
        '    <div style="padding-top: 5px; padding-left: 10px;">' +
        '      <div class="lumens-accordion-icon icon-collapse">' +
        '         <b id="id-accordion-title"></b>' +
        '      </div>' +
        '    </div>' +
        '  </div>' +
        '  <ul class="lumens-accordion-content"><li id="content-holder"></li></ul>' +
        '</div>';
    }
});
