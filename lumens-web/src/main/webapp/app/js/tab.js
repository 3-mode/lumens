/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.TabPanel = Class.$extend({
    __init__: function(container) {
        this.$parentContainer = container;
        this.$tabHolder = $('<div class="lumens-tab-panel"></div>').appendTo(this.$parentContainer);
        this.$tabHeaderHolder = $('<div style="overflow:hidden;"><ul class="lumens-tab-header"></ul></div>').appendTo(this.$tabHolder).find('.lumens-tab-header');
        this.$tabContentHolder = $('<div class="lumens-tab-content"></div>').appendTo(this.$tabHolder);
    },
    addTab: function(tabConfig) {
        var __this = this;
        var $tab = $('<li class="lumens-tab"><div></div></li>').appendTo(this.$tabHeaderHolder)
        .click(function() {
            if (__this.currentTab === this)
                return;

            if (__this.currentTab) {
                var inactive = $(__this.currentTab).find("div").removeClass("active");
                __this.$tabContentHolder.find(inactive.attr("content-id")).hide();
            }
            var $actived = $(this).find("div").addClass("active");
            var $activedContent = __this.$tabContentHolder.find($actived.attr("content-id")).show(200);
            if (__this.theConfig && __this.theConfig.activate) {
                __this.theConfig.activate($actived, $activedContent);
            }
            __this.currentTab = this;
        });
        $tab.find('div').attr("content-id", "#" + tabConfig.id).html(tabConfig.label);
        var $contentPage = $('<div class="lumens-tab-content-page"></div>').appendTo(this.$tabContentHolder).attr("id", tabConfig.id).hide();
        if (tabConfig.content)
            tabConfig.content($contentPage);
        return $tab;
    },
    active: function(tab) {
        tab.trigger("click");
    },
    /**
     * config = {
     *   tab:[
     *     { id: "id1", label: "test1"},
     *     { id: "id2", label: "test2"},
     *     { id: "id3", label: "test3"}
     *   ],
     *   activate: function(actived, content) {}
     * }
     */
    configure: function(config) {
        this.theConfig = config;
        this.active(this.addTab(config.tab[0]));
        for (var i = 1; i < config.tab.length; ++i)
            this.addTab(config.tab[i]);
        return this;
    }
});