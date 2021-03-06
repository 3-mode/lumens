/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

/**
 * Connector implementation, logic and drawing
 */
Lumens.Link = Class.$extend({
    __init__: function () {
        this.$paper = Raphael("id-data-comp-container", 1, 1);
    },
    from: function (component) {
        this.$from = component;
        component.to(this);
        return this;
    },
    to: function (component) {
        this.$to = component;
        component.from(this);
        return this;
    },
    draw: function () {
        var bb0 = this.$from.getBBox();
        var bb1 = this.$to.getBBox();
        var min = {"x": bb0.x < bb1.x ? bb0.x : bb1.x, "y": bb0.y < bb1.y ? bb0.y : bb1.y};
        var max = {"x": bb0.x2 < bb1.x2 ? bb1.x2 : bb0.x2, "y": bb0.y2 < bb1.y2 ? bb1.y2 : bb0.y2};
        this.$paper.setSize(max.x - min.x, max.y - min.y);
        $(this.$paper.canvas).css("position", "absolute").css('left', min.x + 'px').css('top', min.y + 'px');
        if (this.$connection)
            this.$paper.connection(this.$connection);
        else
            this.$connection = this.$paper.connection(this.$from, this.$to, "#CCCCCC");
        return this;
    },
    getTo: function () {
        return this.$to;
    },
    getFrom: function () {
        return this.$from;
    },
    remove: function () {
        if (this.$from)
            this.$from.removeToLink(this);
        if (this.$to)
            this.$to.removeFromLink(this);
        this.$paper.remove();
    }
});

Lumens.Component = Class.$extend({
    __init__: function ($parent, config) {
        this.$parent = $parent;
        this.configure = config;
        this.isMultipleLink = Lumens.isMultipleLink(config.category_info.class_type);
        var template =
        '<div class="data-comp lumens_boostrap" style="left:50px;top:50px;">' +
        '<div class="data-comp-icon"><img/></div>' +
        '<img class="data-comp-stauts" src="app/css/img/ds/Deactive.png"/>' +
        '<div class="data-comp-text">' +
        '<div class="data-comp-title"><b id="id-product-name"></b></div>' +
        '<div id="id-shortdsc" class="data-comp-shortdsc"></div>' +
        '</div></div>';
        var __this = this;
        this.$elem = $(template).appendTo($parent)
        .draggable({
            cursor: "move",
            stack: ".data-comp",
            drag: function () {
                if (__this.$to_list)
                    for (var i = 0; i < __this.$to_list.length; ++i)
                        __this.$to_list[i].draw();
                if (__this.$from_list)
                    for (var i = 0; i < __this.$from_list.length; ++i)
                        __this.$from_list[i].draw();
            }
        })
        .droppable({
            accept: ".data-comp-icon",
            drop: function (event, ui) {
                event.preventDefault();
                if (ui.draggable[0] === $(this).find(".data-comp-icon")[0])
                    return;
                var data_comp = ui.draggable.data("data-comp");
                if (!__this.isMultipleLink) {
                    if (__this.$from_list.length > 0) {
                        // TODO i18n, message box
                        alert("'" + __this.configure.short_desc + "' Can't link many times");
                        return;
                    }
                }
                if (!data_comp.isMultipleLink) {
                    if (data_comp.$to_list.length > 0) {
                        // TODO i18n, message box
                        alert("'" + data_comp.configure.short_desc + "' Can't link many times");
                        return;
                    }
                }
                new Lumens.Link().from(data_comp).to(__this).draw();
            }
        });
        this.$elem.find(".data-comp-icon")
        .on("dblclick", function () {
            if (__this.configure.onComponentDblclick)
                __this.configure.onComponentDblclick(__this);
        })
        .draggable({
            appendTo: $("#id-data-comp-container"),
            helper: function () {
                return $(this).clone().zIndex(2000).css("opacity", "0.7");
            }
        }).data("data-comp", this);
        this.$elem.find(".data-comp-stauts").dblclick(function () {
            if (__this.statusCallback) {
                var statOK = __this.statusCallback(__this);
                var bActive = $(this).attrBoolean("is-active");
                if (statOK && !bActive) {
                    $(this).attr("src", "app/css/img/ds/Active.png");
                    $(this).attr("is-active", true);
                }
                else {
                    $(this).attr("src", "app/css/img/ds/Deactive.png");
                    $(this).attr("is-active", false);
                }
            }
        });
        if (config.category_info.instance_icon)
            this.$elem.find('.data-comp-icon').find('img').attr('src', 'data:image/png;base64,' + config.category_info.instance_icon);
        else if (config.category_info.instance_icon_url)
            this.$elem.find('.data-comp-icon').find('img').attr('src', config.category_info.instance_icon_url);
        this.$elem.find('#id-product-name').text(config.category_info.name);
        this.$elem.find('#id-shortdsc').text(config.short_desc);
        this.$elem.css("left", config.x + 'px');
        this.$elem.css("top", config.y + 'px');
        this.$to_list = [];
        this.$from_list = [];
    },
    updateSelect: function (isSelect) {
        if (isSelect)
            this.$elem.addClass("data-comp-select");
        else
            this.$elem.removeClass("data-comp-select");
    },
    setShortDescription: function (shortDesc) {
        this.$elem.find('#id-shortdsc').text(shortDesc);
    },
    getShortDescription: function () {
        return this.$elem.find('#id-shortdsc').text();
    },
    getId: function () {
        return this.getCompData().id;
    },
    getConfig: function () {
        return this.configure;
    },
    getXY: function () {
        return {
            "x": this.$elem.cssInt("left"),
            "y": this.$elem.cssInt("top")
        };
    },
    getBBox: function () {
        var xy = this.getXY();
        return {
            "x": xy.x,
            "y": xy.y,
            "x2": xy.x + 260,
            "y2": xy.y + 90,
            "width": 260,
            "height": 90
        }
    },
    remove: function () {
        if (this.hasFrom())
            for (var i in this.getFromLinkList())
                this.getFromLinkList()[i].remove();
        if (this.hasTo())
            for (var i in this.getToLinkList())
                this.getToLinkList()[i].remove();
        this.$elem.remove();
    },
    to: function (link) {
        this.$to_list.push(link);
    },
    from: function (link) {
        this.$from_list.push(link);
    },
    removeFromLink: function (link) {
        var length = this.$from_list.length;
        while (length > 0) {
            if (this.$from_list[--length] === link)
                break;
        }
        if (length >= 0)
            this.$from_list.splice(length, 1);
        return this;
    },
    removeToLink: function (link) {
        var length = this.$to_list.length;
        while (length > 0) {
            if (this.$to_list[--length] === link)
                break;
        }
        if (length >= 0)
            this.$to_list.splice(length, 1);
        return this;
    },
    getFromLinkList: function () {
        return this.$from_list;
    },
    getToLinkList: function () {
        return this.$to_list;
    },
    changeStatus: function (callback) {
        this.statusCallback = callback;
    },
    hasFrom: function () {
        return this.getFromCount() > 0;
    },
    getFromCount: function () {
        return this.getFromLinkList() ? this.getFromLinkList().length : 0;
    },
    getFrom: function (index) {
        return this.getFromLinkList()[index].getFrom();
    },
    hasTo: function () {
        return this.getToCount() > 0;
    },
    getToCount: function () {
        return this.getToLinkList() ? this.getToLinkList().length : 0;
    },
    getTo: function (index) {
        return this.getToLinkList()[index].getTo();
    },
    getFormHtml: function () {
        return this.getCategory().html;
    },
    getClassType: function () {
        return this.getCategory().class_type;
    },
    getCategory: function () {
        return this.getConfig().category_info;
    },
    getCompData: function () {
        return this.getConfig().component_info;
    }
});

Lumens.DataComponent = Lumens.Component.$extend({
    __init: function ($parent, config) {
        this.$super($parent, config);
    },
    isDataSource: function () {
        return this.getCategory().class_type === "datasource";
    },
    isTransformer: function () {
        return this.getCategory().class_type === "transformer";
    },
    getFormatEntryList: function (direction) {
        if (!this.isDataSource())
            return;
        var formatList = this.getCompData().format_list;
        if (formatList && formatList.length > 0) {
            for (var i = 0; i < formatList.length > 0; ++i) {
                if (formatList[i].direction === direction) {
                    return formatList[i].format_entry;
                }
            }
        }
        return null;
    },
    setFormatEntryList: function (direction, formatEntry) {
        if (!this.isDataSource())
            return;
        var formatList = this.getCompData().format_list;
        if (formatList && formatList.length > 0) {
            for (var i = 0; i < formatList.length > 0; ++i) {
                if (formatList[i].direction === direction) {
                    formatList[i].format_entry = formatEntry;
                    return;
                }
            }
            formatList.push({
                direction: direction,
                format_entry: formatEntry
            });
        } else {
            this.getCompData().format_list = [{
                    direction: direction,
                    format_entry: formatEntry
                }];
        }

    },
    getRuleEntryList: function () {
        if (this.isDataSource())
            return null;
        return  this.getCompData().transform_rule_entry;
    },
    setRuleEntryList: function (ruleEntryList) {
        if (this.isDataSource())
            return;
        this.getCompData().transform_rule_entry = ruleEntryList;
    },
    removeRuleEntry: function (ruleName) {
        if (!this.isDataSource()) {
            var ruleList = this.getRuleEntryList();
            for (var i in ruleList) {
                if (ruleList[i].name === ruleName) {
                    var ruleEntry = ruleList[i];
                    ruleList.splice(i, 1);
                    return ruleEntry;
                }
            }
        }
    },
    removeInputFormat: function (regName) {
        var formatEntry = null;
        if (this.isDataSource()) {
            formatEntry = this.getFormatEntryList("IN");
        } else if (this.hasFrom()) {
            formatEntry = this.getFrom(0).getFormatEntryList("OUT");
        }
        if (formatEntry)
            for (var i in formatEntry)
                if (formatEntry[i].name === regName) {
                    var format = formatEntry[i].format;
                    formatEntry.splice(i, 1);
                    return format;
                }
    },
    removeOutputFormat: function (regName) {
        var formatEntry = null;
        if (this.isDataSource()) {
            formatEntry = this.getFormatEntryList("OUT");
        } else if (this.hasTo()) {
            formatEntry = this.getTo(0).getFormatEntryList("IN");
        }
        if (formatEntry)
            for (var i in formatEntry)
                if (formatEntry[i].name === regName) {
                    var format = formatEntry[i].format;
                    formatEntry.splice(i, 1);
                    return format;
                }
    },
    getInputFormat: function (regName) {
        var formatEntry = null;
        if (this.isDataSource()) {
            formatEntry = this.getFormatEntryList("IN");
        } else if (this.hasFrom()) {
            formatEntry = this.getFrom(0).getFormatEntryList("OUT");
        }
        if (formatEntry)
            for (var i in formatEntry)
                if (formatEntry[i].name === regName)
                    return formatEntry[i].format;
        return null;
    },
    getOutputFormat: function (regName) {
        var formatEntry = null;
        if (this.isDataSource()) {
            formatEntry = this.getFormatEntryList("OUT");
        } else if (this.hasTo()) {
            formatEntry = this.getTo(0).getFormatEntryList("IN");
        }
        if (formatEntry)
            for (var i in formatEntry)
                if (formatEntry[i].name === regName)
                    return formatEntry[i].format;
        return null;
    }
});