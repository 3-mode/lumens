/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

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
 *   useRatio: true,
 *   part1Size: 200,
 *   part2Size: "100%"
 * }
 */
Lumens.SplitLayout = Class.$extend({
    __init__: function(container) {
        this.$parentContainer = container.getElement ? container.getElement() : container;
        this.$theLayout = Lumens.Id($('<div class="lumens-layout"></div>').appendTo(this.$parentContainer));
    },
    computePartsLayoutSize: function(layoutConfig) {
        if (layoutConfig)
            this.layoutConfig = layoutConfig;

        if (this.layoutConfig) {
            // Compute ratio for all parts' size
            if (this.layoutConfig.useRatio) {
                if (!this.layoutConfig.part1Ratio && this.layoutConfig.part1Size)
                    this.layoutConfig.part1Ratio = $.percentToFloat(this.layoutConfig.part1Size);
                else if (!this.layoutConfig.part2Ratio && this.layoutConfig.part2Size)
                    this.layoutConfig.part2Ratio = $.percentToFloat(this.layoutConfig.part2Size);
            }

            if (this.layoutConfig.mode === "horizontal") {
                if (this.part1Layout && this.part2Layout) {
                    this.part1Layout.css("height", "100%");
                    this.part2Layout.css("height", "100%");
                    // Compute pane size from ratio
                    if (this.layoutConfig.part1Ratio)
                        this.layoutConfig.part1Size = this.layoutConfig.part1Ratio * this.$theLayout.width();
                    else if (this.layoutConfig.part2Ratio)
                        this.layoutConfig.part2Size = this.layoutConfig.part2Ratio * this.$theLayout.width();
                    // Compute child panel size
                    if (this.layoutConfig.part1Size) {
                        this.part1Layout.css("width", this.layoutConfig.part1Size + "px");
                        this.part2Layout.css("width", (this.$theLayout.width() - this.layoutConfig.part1Size) + "px");
                    }
                    else if (this.layoutConfig.part2Size) {
                        this.part1Layout.css("width", (this.$theLayout.width() - this.layoutConfig.part2Size) + "px");
                        this.part2Layout.css("width", this.layoutConfig.part2Size + "px");
                    }
                }
            } else if (this.layoutConfig.mode === "vertical") {
                if (this.part1Layout && this.part2Layout) {
                    this.part1Layout.css("width", "100%");
                    this.part2Layout.css("width", "100%");
                    // Compute pane size from ratio
                    if (this.layoutConfig.useRatio) {
                        if (this.layoutConfig.part1Ratio)
                            this.layoutConfig.part1Size = this.layoutConfig.part1Ratio * this.$theLayout.height();
                        else if (this.layoutConfig.part2Ratio)
                            this.layoutConfig.part2Size = this.layoutConfig.part2Ratio * this.$theLayout.height();
                    }
                    // Compute child panel size
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

Lumens.ComponentPanel = Lumens.Panel.$extend({
    __init__: function(container, $scope) {
        this.$super(container);
        this.$scope = $scope;
        this.componentList = [];
    },
    configure: function(panelStyle) {
        var __this = this;
        this.getElement()
        .attr("id", "id-data-comp-container")
        .droppable({
            accept: ".data-comp-node",
            drop: function(event, ui) {
                event.preventDefault();
                var data = $.data(ui.draggable.get(0), "item-data");
                __this.addComponent({x: ui.position.left, y: ui.position.top}, data);
            }
        });
        this.$super({
            panelClass: ["data-comp-container"],
            panelStyle: panelStyle
        });
        return this;
    },
    addComponent: function(position, component_category, component_info) {
        var __this = this;
        var component = new Lumens.DataComponent(this.getElement(), {
            x: position.x, y: position.y,
            category: component_category,
            component_info: component_info,
            short_desc: (component_info && component_info.name) ? component_info.name : "[to do]",
            dblclickHandler: function(dbl_component_info) {
                console.log(dbl_component_info);
                __this.$scope.$apply(function() {
                    __this.$scope.component = dbl_component_info;
                });
            }
        });
        this.componentList.push(component);
        return component;
    }
});

/**
 * Now only support vertical draggable resize split panel
 * {
 * mode: "vertical",
 * useRatio: true/false,
 * part1Size: "30%"/300
 * }
 */
Lumens.ResizableSplitLayout = Lumens.SplitLayout.$extend({
    __init__: function(container) {
        this.$super(container);
    },
    configure: function(config) {
        var __this = this;
        this.$super(config);
        this.resizablePanel = new Lumens.SplitLayout(this.getPart2Element())
        .configure({
            mode: config.mode,
            part1Size: "12"
        });
        this.resizablePanel.getElement().addClass("lumens-v-resizable-border");
        this.resizablePanel.getPart1Element().addClass("lumens-v-resize-grip")
        .append('<div class="lumens-v-resize-grip-border"><span class="lumens-v-resize-grip-button"/></div>')
        .draggable({
            axis: "horizontal" === config.mode ? "x" : "y",
            start: function() {
                __this.resizeStart();
            },
            stop: "horizontal" === config.mode ? function() {
                // TODO change other parts width value here
            } : function() {
                if (__this.layoutConfig.useRatio) {
                    __this.layoutConfig.part1Ratio = Math.abs(__this.layoutConfig.part1Size + $(this).cssFloat("top")) / __this.getElement().height();
                    var maxRatio = 1 - __this.getOffset() / __this.getElement().height();
                    if (__this.layoutConfig.part1Ratio > maxRatio)
                        __this.layoutConfig.part1Ratio = maxRatio;
                }
                else {
                    __this.layoutConfig.part1Size = __this.layoutConfig.part1Size + $(this).cssFloat("top");
                    var maxHeight = __this.getElement().height() - __this.getOffset();
                    if (__this.layoutConfig.part1Size < 0)
                        __this.layoutConfig.part1Size = 0;
                    else if (__this.layoutConfig.part1Size > maxHeight)
                        __this.layoutConfig.part1Size = maxHeight;
                }
                $(this).css("top", "0px");
                __this.getElement().trigger("resize");
                __this.resizeStop();
            }
        });
        return this;
    },
    resizeStart: function() {
        if (this.resizablePanel)
            this.resizablePanel.getPart1Element().toggleClass("lumens-v-drag-border");
    },
    resizeStop: function() {
        if (this.resizablePanel)
            this.resizablePanel.getPart1Element().toggleClass("lumens-v-drag-border");
    },
    getOffset: function() {
        return 12;
    },
    getPart2Element: function() {
        if (this.resizablePanel)
            return this.resizablePanel.getPart2Element();
        return this.$super();
    }
});

Lumens.ResizableVSplitLayoutExt = Lumens.ResizableSplitLayout.$extend({
    __init__: function(container) {
        this.$super(container);
    },
    configure: function(config) {
        if (!config.useRatio)
            throw "Ratio mode must be used";

        var __this = this;
        this.$super(config);
        this.$contentPanel = new Lumens.SplitLayout(this.getPart2Element()).configure({
            mode: "vertical",
            part1Size: "32"
        });
        this.$titleBar = $('<div class="lumens-window-title"><span id="id-title"></span></div>' +
        '<div class="lumens-window-buttons"><span class="lumens-min"/><span class="lumens-mid"/><span class="lumens-max"/></div>')
        .appendTo(this.$contentPanel.getPart1Element());
        function initBeforeMinMidMax() {
            __this.keepUseRatio = __this.layoutConfig.useRatio;
            __this.keepPart1Size = __this.layoutConfig.part1Size;
            __this.keepPart2Size = __this.layoutConfig.part2Size;
            __this.keepPart1Ratio = __this.layoutConfig.part1Ratio;
            __this.keepPart2Ratio = __this.layoutConfig.part2Ratio;
            __this.layoutConfig.useRatio = undefined;
            __this.layoutConfig.part1Size = undefined;
            __this.layoutConfig.part2Size = undefined;
            __this.layoutConfig.part1Ratio = undefined;
            __this.layoutConfig.part2Ratio = undefined;
        }
        this.$titleBar.find(".lumens-min").click(function() {
            __this.isMin = true;
            initBeforeMinMidMax();
            // Min size
            __this.layoutConfig.part2Size = __this.getOffset();
            __this.getElement().trigger("resize");

        });
        this.$titleBar.find(".lumens-mid").click(function() {
            initBeforeMinMidMax();
            __this.layoutConfig.useRatio = true;
            __this.layoutConfig.part1Ratio = 0.65;
            __this.getElement().trigger("resize");
        });
        this.$titleBar.find(".lumens-max").click(function() {
            initBeforeMinMidMax();
            __this.layoutConfig.useRatio = true;
            __this.layoutConfig.part1Ratio = 0.15;
            __this.getElement().trigger("resize");
        });
        return this;
    },
    resizeStart: function() {
        if (this.isMin) {
            this.layoutConfig.useRatio = true;
            this.layoutConfig.part1Size = this.getElement().height() - this.layoutConfig.part2Size;
            this.layoutConfig.part1Ratio = this.layoutConfig.part1Size / this.getElement().height();
            this.layoutConfig.part2Size = undefined;
            this.layoutConfig.part2Ratio = undefined;
            this.isMin = false;
        }
        this.$super();
    },
    getTitleElement: function() {
        return this.$titleBar.find('#id-title');
    },
    getOffset: function() {
        return this.$super() + 32;
    },
    getPart2Element: function() {
        if (this.$contentPanel)
            return this.$contentPanel.getPart2Element();
        return this.$super();
    }
});