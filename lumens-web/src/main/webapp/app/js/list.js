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
    doExpandCollapse: function (config, accordion) {
        var __this = this;
        accordion.find("ul").toggle({
            duration: 200,
            complete: function () {
                __this.buildContent(config, accordion);
            }
        });
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
            __this.doExpandCollapse(config, accordion);
        });
    },
    addItem: function (config, pos, isAddToHead) {
        this.addAccordion(config, 0, isAddToHead);
        var accordion = this.$accordionHolder.find("#" + config.IdList[0]);
        if (pos === 0)
            this.doExpandCollapse(accordion);
        return this;
    },
    configure: function (config) {
        for (var i = 0; i < config.IdList.length; ++i)
            this.addAccordion(config, i);

        var accordions = this.$accordionHolder.find(".lumens-accordion-item");
        if (accordions.length > 0) {
            this.doExpandCollapse(config, $(accordions[config.activeIndex ? config.activeIndex : 0]));
        }
        return this;
    },
    buildContent: function (config, accordion) {
        var contentHolder = accordion.find("#content-holder");
        if (config.buildContent && contentHolder.children().length === 0)
            config.buildContent(contentHolder, accordion.attr("id"), accordion.find(".icon-expand").length > 0, accordion.find(".lumens-accordion-title"), accordion.find(".lumens-accordion-title").find('b'));
    }
});