Lumens.RootLayout = Class.$extend({
    __init__: function(container) {
        this.$parentContainer = container ? container : $('body');
        this.$theLayout = Lumens.Id($('<div class="lumens-root-layout"></div>').appendTo(this.$parentContainer));
    },
    configure: function() {
        var __this = this;
        this.layout = function(e) {
            if (e.target !== this)
                return;
            __this.$theLayout.trigger("resize");
        }
        $(window).bind("resize", this.layout);
        return this;
    },
    getElement: function() {
        return this.$theLayout;
    },
    remove: function() {
        this.$theLayout.unbind();
        this.$theLayout.remove();
        $(window).unbind("resize", this.layout);
    }
});

/**
 * Sub layout need to bind to its parent resize event
 * {
 *   mode: "horizontal/vertical",
 *   collapse: "1",
 *   part1Size: 200,
 *   part2Size: "100%"
 * }
 */
Lumens.SplitLayout = Class.$extend({
    __init__: function(container) {
        this.$parentContainer = container.getElement ? container.getElement() : container;
        this.$theLayout = Lumens.Id($('<div class="lumens-layout"></div>').appendTo(this.$parentContainer));
    },
    computePartsLayoutSize: function() {
        if (this.layoutConfig && this.layoutConfig.mode === "horizontal") {
            if (this.part1Layout && this.part2Layout) {
                this.part1Layout.css("height", "100%");
                this.part2Layout.css("height", "100%");
                if (this.layoutConfig.part1Size) {
                    this.part1Layout.css("width", this.layoutConfig.part1Size + "px");
                    this.part2Layout.css("width", (this.$theLayout.width() - this.layoutConfig.part1Size) + "px");
                }
                else if (this.layoutConfig.part2Size) {
                    this.part1Layout.css("width", (this.$theLayout.width() - this.layoutConfig.part2Size) + "px");
                    this.part2Layout.css("width", this.layoutConfig.part2Size + "px");
                }
            }
        } else if (this.layoutConfig && this.layoutConfig.mode === "vertical") {
            if (this.part1Layout && this.part2Layout) {
                this.part1Layout.css("width", "100%");
                this.part2Layout.css("width", "100%");
                if (this.layoutConfig.part1Size) {
                    this.part1Layout.css("height", this.layoutConfig.part1Size + "px");
                    this.part2Layout.css("height", (this.$theLayout.height() - this.layoutConfig.part1Size) + "px");
                }
                else if (this.layoutConfig.part2Size) {
                    this.part1Layout.css("height", (this.$theLayout.height() - this.layoutConfig.part2Size) + "px");
                    this.part2Layout.css("height", this.layoutConfig.part2Size + "px");
                }
            }
        }
    },
    configure: function(config) {
        var __this = this;
        this.layout = function(e) {
            if (e && e.target !== this)
                return;
            __this.$theLayout.css("width", __this.$parentContainer.width());
            __this.$theLayout.css("height", __this.$parentContainer.height());
            __this.computePartsLayoutSize();
            if (__this.part1Layout)
                __this.part1Layout.trigger("resize");
            if (__this.part2Layout)
                __this.part2Layout.trigger("resize");
        };
        this.$parentContainer.resize(this.layout);
        this.layout();
        this.layoutConfig = config;
        if (this.layoutConfig.mode === "horizontal") {
            this.part1Layout = Lumens.Id($('<div class="lumens-layout-horizontal"></div>').appendTo(this.$theLayout).css("float", "left"));
            this.part2Layout = Lumens.Id($('<div class="lumens-layout-horizontal"></div>').appendTo(this.$theLayout).css("float", "left"));
            // layout
            this.computePartsLayoutSize();
        } else if (this.layoutConfig.mode === "vertical") {
            this.part1Layout = Lumens.Id($('<div class="lumens-layout-vertical"></div>').appendTo(this.$theLayout).css("float", "top"));
            this.part2Layout = Lumens.Id($('<div class="lumens-layout-vertical"></div>').appendTo(this.$theLayout).css("float", "top"));
            // layout
            this.computePartsLayoutSize();
        } else {
            if (Lumens.debugEnabled)
                console.log("Unknown mode '" + this.layoutConfig.mode + "' !");
        }
        return this;
    },
    getPart1Element: function() {
        return  this.part1Layout;
    },
    getPart2Element: function() {
        return  this.part2Layout;
    },
    getElement: function() {
        return  this.$parentContainer;
    },
    remove: function() {
        if (this.part1Layout)
            this.part1Layout.unbind();
        if (this.part2Layout)
            this.part2Layout.unbind();
        this.$theLayout.unbind();
        this.$theLayout.remove();
        this.$parentContainer.unbind("resize", this.layout);
    }
});

Lumens.Panel = Class.$extend({
    __init__: function(container) {
        this.$parentContainer = container;
        this.$thePanel = Lumens.Id($('<div class="lumens-panel"></div>').appendTo(this.$parentContainer));
        this.size = {};
    },
    configure: function(config) {
        var __this = this;
        this.layout = function(e) {
            if (e && e.target !== this)
                return;
            if (__this.size.width)
                __this.$thePanel.css("width", __this.size.width);
            if (__this.size.height)
                __this.$thePanel.css("height", __this.size.height);
            __this.$thePanel.trigger("resize");
        };
        __this.$parentContainer.resize(this.layout);
        __this.layout();
        if (config.panelStyle) {
            //panelStyle = { "a" : "b", "c" : "d" }
            if (config.panelStyle.width)
                __this.size.width = config.panelStyle.width;
            if (config.panelStyle.height)
                __this.size.height = config.panelStyle.height;
            for (var key in config.panelStyle)
                __this.$thePanel.css(key, config.panelStyle[key]);
        }
        if (config.panelClass) {
            //panelClass: [ "class1", "class2" ]
            for (var i = 0; i < config.panelClass.length; ++i)
                __this.$thePanel.addClass(config.panelClass[i]);
        }
        return this;
    },
    getElement: function() {
        return this.$thePanel;
    },
    remove: function() {
        this.$thePanel.unbind();
        this.$thePanel.remove();
        this.$parentContainer.unbind("resize", this.layout);
    }
});