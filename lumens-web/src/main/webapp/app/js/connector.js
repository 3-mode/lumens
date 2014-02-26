/**
 * 
 * Connector implementation, logic and drawing
 */

Lumens.Link = Class.$extend({
    __init__: function() {
        this._paper = Raphael("id-data-comp-container", 1, 1);
    },
    from: function(_component) {
        this._from = _component;
        _component.to(this);
        return this;
    },
    to: function(_component) {
        this._to = _component;
        _component.from(this);
        return this;
    },
    draw: function() {
        var bb0 = this._from.getBBox();
        var bb1 = this._to.getBBox();
        var minXY = {
            "x": bb0.x < bb1.x ? bb0.x : bb1.x,
            "y": bb0.y < bb1.y ? bb0.y : bb1.y
        };
        var maxXY = {
            "x": bb0.x2 < bb1.x2 ? bb1.x2 : bb0.x2,
            "y": bb0.y2 < bb1.y2 ? bb1.y2 : bb0.y2
        };
        this._paper.setSize(maxXY.x - minXY.x, maxXY.y - minXY.y);
        $(this._paper.canvas).css("position", "absolute").css('left', minXY.x + 'px').css('top', minXY.y + 'px');
        this._connection = this._paper.connection(this._from, this._to, "#CCCCCC", "#CCCCCC");
    },
    redraw: function() {
        if (this._connection) {
            var bb0 = this._from.getBBox();
            var bb1 = this._to.getBBox();
            var minXY = {
                "x": bb0.x < bb1.x ? bb0.x : bb1.x,
                "y": bb0.y < bb1.y ? bb0.y : bb1.y
            };
            var maxXY = {
                "x": bb0.x2 < bb1.x2 ? bb1.x2 : bb0.x2,
                "y": bb0.y2 < bb1.y2 ? bb1.y2 : bb0.y2
            };
            this._paper.setSize(maxXY.x - minXY.x, maxXY.y - minXY.y);
            $(this._paper.canvas).css("position", "absolute").css('left', minXY.x + 'px').css('top', minXY.y + 'px');
            this._paper.connection(this._connection);
        }
        return this;
    }
});

Lumens.DataComponent = Class.$extend({
    __init__: function(_$parent, _component) {
        var template =
        '<div class="data-comp" style="left:50px;top:50px;">' +
        '<div class="data-comp-icon"><img/></div>' +
        '<img class="data-comp-stauts" src="css/img/ds/Deactive.png"/>' +
        '<div class="data-comp-text">' +
        '<div class="data-comp-title"><b id="id-product-name"></b></div>' +
        '<div id="id-shortdsc" class="data-comp-shortdsc"></div>' +
        '</div></div>';
        var _This = this;
        this._$elem = $(template).appendTo(_$parent).draggable({
            cursor: "move",
            drag: function(event, ui) {
                if (_This._to_list)
                    for (var i = 0; i < _This._to_list.length; ++i)
                        _This._to_list[i].redraw();
                if (_This._from_list)
                    for (var i = 0; i < _This._from_list.length; ++i)
                        _This._from_list[i].redraw();
            },
            stack: ".data-comp"
        });
        this._$elem.find('.data-comp-icon').find('img').attr('src', 'data:image/png;base64,' + _component.icon);
        this._$elem.find('#id-product-name').text(_component.product_name);
        this._$elem.find('#id-shortdsc').text(_component.short_desc);
        this._$elem.css("left", _component.x + 'px');
        this._$elem.css("top", _component.y + 'px');
        this._to_list = [];
        this._from_list = [];
    },
    getXY: function() {
        return {
            "x": this._$elem.cssInt("left"),
            "y": this._$elem.cssInt("top")
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
        this._to_list.push(link);
    },
    from: function(link) {
        this._from_list.push(link);
    },
    remove_from: function(_component) {
        var length = this._from_list.length;
        while (length > 0) {
            if (this._from_list[--length] === _component)
                break;
        }
        if (length >= 0)
            this._from_list.splice(length, 1);
        return this;
    },
    remove_to: function(_component) {
        var length = this._to_list.length;
        while (length > 0) {
            if (this._to_list[--length] === _component)
                break;
        }
        if (length >= 0)
            this._to_list.splice(length, 1);
        return this;
    }
});