/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

/**
 * Connector implementation, logic and drawing
 */
Lumens.Link = Class.$extend({
    __init__: function() {
        this.$paper = Raphael("id-data-comp-container", 1, 1);
    },
    from: function(component) {
        this.$from = component;
        component.to(this);
        return this;
    },
    to: function(component) {
        this.$to = component;
        component.from(this);
        return this;
    },
    draw: function() {
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
    getTo: function() {
        return this.$to;
    },
    getFrom: function() {
        return this.$from;
    }
});

Lumens.DataComponent = Class.$extend({
    __init__: function($parent, config) {
        this.$parent = $parent;
        this.configure = config;
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
            drag: function() {
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
            drop: function(event, ui) {
                event.preventDefault();
                if (ui.draggable.get(0) === $(this).find(".data-comp-icon").get(0))
                    return;
                var data_comp = ui.draggable.data("data-comp");
                new Lumens.Link().from(data_comp).to(__this).draw();
            }
        });
        this.$elem.find(".data-comp-icon")
        .on("dblclick", function() {
            if (__this.configure.onComponentDblclick)
                __this.configure.onComponentDblclick(__this);
        })
        .draggable({
            appendTo: $("#id-data-comp-container"),
            helper: function() {
                return $(this).clone().zIndex(2000).css("opacity", "0.7");
            }
        }).data("data-comp", this);

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
    setShortDescription: function(shortDesc) {
        this.$elem.find('#id-shortdsc').text(shortDesc);
    },
    getConfig: function() {
        return this.configure;
    },
    getXY: function() {
        return {
            "x": this.$elem.cssInt("left"),
            "y": this.$elem.cssInt("top")
        };
    },
    getBBox: function() {
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
    to: function(link) {
        this.$to_list.push(link);
    },
    from: function(link) {
        this.$from_list.push(link);
    },
    removeFromLink: function(link) {
        var length = this.$from_list.length;
        while (length > 0) {
            if (this.$from_list[--length] === link)
                break;
        }
        if (length >= 0)
            this.$from_list.splice(length, 1);
        return this;
    },
    removeToLink: function(link) {
        var length = this.$to_list.length;
        while (length > 0) {
            if (this.$to_list[--length] === link)
                break;
        }
        if (length >= 0)
            this.$to_list.splice(length, 1);
        return this;
    },
    getFromLinkList: function() {
        return this.$from_list;
    },
    getToLinkList: function() {
        return this.$to_list;
    }
});