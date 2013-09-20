$(function() {
    // Root layout need to bind to the window resize event
    Hrcms.RootLayout = {}
    Hrcms.RootLayout.create = function(container) {
        var tThis = {};
        var parentContainer = container ? container : $('body');
        var rootLayout = Hrcms.Id($('<div class="hrcms-root-layout"></div>').appendTo(parentContainer));
        function layout(e) {
            if (e.target !== this)
                return
            rootLayout.trigger("resize");
        }
        $(window).bind("resize", layout);
        tThis.getElement = function() {
            return rootLayout;
        };
        tThis.remove = function() {
            rootLayout.unbind();
            rootLayout.remove();
            $(window).unbind("resize", layout);
        }
        // end
        return tThis;
    }

    /**
     * Sub layout need to bind to its parent resize event
     * {
     *   mode: "horizontal/vertical",
     *   collapse: "1",
     *   part1Size: 200,
     *   part2Size: "100%"
     * }
     */
    Hrcms.SplitLayout = {};
    Hrcms.SplitLayout.create = function(container) {
        var tThis = {};
        var parentContainer = container.getElement ? container.getElement() : container;
        var theLayout = Hrcms.Id($('<div class="hrcms-layout"></div>').appendTo(parentContainer));
        var layoutConfig, part1Layout, part2Layout;

        function resize(e) {
            if (e && e.target !== this)
                return;
            theLayout.css("width", parentContainer.width());
            theLayout.css("height", parentContainer.height());
            computePartsLayoutSize();
            if (part1Layout)
                part1Layout.trigger("resize");
            if (part2Layout)
                part2Layout.trigger("resize");
        }
        parentContainer.resize(resize);
        resize();

        function computePartsLayoutSize() {
            if (layoutConfig && layoutConfig.mode === "horizontal") {
                if (part1Layout && part2Layout) {
                    part1Layout.css("height", "100%");
                    part2Layout.css("height", "100%");
                    if (layoutConfig.part1Size) {
                        part1Layout.css("width", layoutConfig.part1Size + "px");
                        part2Layout.css("width", (theLayout.width() - layoutConfig.part1Size) + "px");
                    }
                    else if (layoutConfig.part2Size) {
                        part1Layout.css("width", (theLayout.width() - layoutConfig.part2Size) + "px");
                        part2Layout.css("width", layoutConfig.part2Size + "px");
                    }
                }
            } else if (layoutConfig && layoutConfig.mode === "vertical") {
                if (part1Layout && part2Layout) {
                    part1Layout.css("width", "100%");
                    part2Layout.css("width", "100%");
                    if (layoutConfig.part1Size) {
                        part1Layout.css("height", layoutConfig.part1Size + "px");
                        part2Layout.css("height", (theLayout.height() - layoutConfig.part1Size) + "px");
                    }
                    else if (layoutConfig.part2Size) {
                        part1Layout.css("height", (theLayout.height() - layoutConfig.part2Size) + "px");
                        part2Layout.css("height", layoutConfig.part2Size + "px");
                    }
                }
            }
        }
        tThis.configure = function(config) {
            layoutConfig = config;
            if (layoutConfig.mode === "horizontal") {
                part1Layout = Hrcms.Id($('<div class="hrcms-layout-horizontal"></div>').appendTo(theLayout).css("float", "left"));
                part2Layout = Hrcms.Id($('<div class="hrcms-layout-horizontal"></div>').appendTo(theLayout).css("float", "left"));
                // layout
                computePartsLayoutSize();
            } else if (layoutConfig.mode === "vertical") {
                part1Layout = Hrcms.Id($('<div class="hrcms-layout-vertical"></div>').appendTo(theLayout).css("float", "top"));
                part2Layout = Hrcms.Id($('<div class="hrcms-layout-vertical"></div>').appendTo(theLayout).css("float", "top"));
                // layout
                computePartsLayoutSize();
            } else {
                if (Hrcms.debugEnabled)
                    console.log("Unknown mode '" + layoutConfig.mode + "' !");
            }
            return this;
        }
        tThis.getPart1Element = function() {
            return part1Layout;
        }
        tThis.getPart2Element = function() {
            return part2Layout;
        }
        tThis.getElement = function() {
            return theLayout;
        }
        tThis.remove = function() {
            if (part1Layout)
                part1Layout.unbind();
            if (part2Layout)
                part2Layout.unbind();
            theLayout.unbind();
            theLayout.remove();
            parentContainer.unbind("resize", resize);
        }
        // end
        return tThis;
    }

    Hrcms.Panel = {};
    Hrcms.Panel.create = function(container) {
        var tThis = {};
        var parentContainer = container;
        var thePanel = Hrcms.Id($('<div class="hrcms-panel"></div>').appendTo(parentContainer));
        var size = {};
        function resize(e) {
            if (e && e.target !== this)
                return;
            if (size.width)
                thePanel.css("width", size.width);
            if (size.height)
                thePanel.css("height", size.height);
            thePanel.trigger("resize");
        }
        parentContainer.resize(resize);
        resize();

        tThis.configure = function(config) {
            if (config.panelStyle) {
                //panelStyle = { "a" : "b", "c" : "d" }
                if (config.panelStyle.width)
                    size.width = config.panelStyle.width;
                if (config.panelStyle.height)
                    size.height = config.panelStyle.height;
                for (var key in config.panelStyle)
                    thePanel.css(key, config.panelStyle[key]);
            }
            if (config.panelClass) {
                //panelClass: [ "class1", "class2" ]
                for (var i = 0; i < config.panelClass.length; ++i)
                    thePanel.addClass(config.panelClass[i]);
            }

            return this;
        }

        tThis.getElement = function() {
            return thePanel;
        }
        tThis.remove = function() {
            thePanel.unbind();
            thePanel.remove();
            parentContainer.unbind("resize", resize);
        }
        // end
        return tThis;
    }
});