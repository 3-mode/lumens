$(function() {
    // Root layout need to bind to the window resize event
    Hrcms.TabPanel = {};
    Hrcms.TabPanel.create = function(container) {
        var tThis = {};
        var parentContainer = container;
        var tabHolder = $('<div class="hrcms-tab-panel"></div>').appendTo(parentContainer);
        var tabHeaderHolder = $('<ul class="hrcms-tab-header"></ul').appendTo(tabHolder);
        var tabContentHolder = $('<div class="hrcms-tab-content"></div').appendTo(tabHolder);
        var currentTab;
        var theConfig;
        function active(tab) {
            tab.trigger("click");
        }
        function addTab(tab) {
            var t = $('<li class="hrcms-tab"><div></div></li>').appendTo(tabHeaderHolder)
            .click(function(e) {
                if (currentTab) {
                    var inactive = $(currentTab).find("div").removeClass("active");
                    tabContentHolder.find(inactive.attr("content-id")).hide();
                }
                var actived = $(this).find("div").addClass("active");
                var activedContent = tabContentHolder.find(actived.attr("content-id")).show();
                if (theConfig && theConfig.activate) {
                    theConfig.activate(actived, activedContent);
                }
                currentTab = this;
            });
            t.find('div')
            .attr("content-id", "#" + tab.id)
            .html(tab.label);
            $('<div class="hrcms-tab-content-page"></div>').appendTo(tabContentHolder)
            .attr("id", tab.id)
            .hide();
            return t;
        }

        /**
         * config = {
         *   tab:[
         *     { id: "id1", label: "test1", click: clickHandler },
         *     { id: "id2", label: "test2", click: clickHandler },
         *     { id: "id3", label: "test3", click: clickHandler }
         *   ]
         * }
         */
        tThis.configure = function(config) {
            theConfig = config;
            var tab = config.tab;
            active(addTab(tab[0]));
            for (var i = 1; i < tab.length; ++i) {
                addTab(tab[i]);
            }

            return this;
        }
        // build tab list
        // end
        return tThis;

    }
});