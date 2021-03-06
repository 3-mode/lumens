/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
jQuery.fn.cssInt = function (prop) {
    return parseInt(this.css(prop), 10) || 0;
};
jQuery.fn.attrBoolean = function (prop) {
    var bActive = this.attr(prop);
    return bActive ? bActive === "true" : false;
}
jQuery.fn.cssFloat = function (prop) {
    return parseFloat(this.css(prop)) || 0;
};
jQuery.extend({
    percentToFloat: function (value) {
        return parseFloat(value.substring(0, value.length - 1)) / 100.0;
    }
});
jQuery.extend({
    currentTime: function () {
        return new Date().getTime();
    }
});
if (typeof String.prototype.format !== 'function') {
    String.prototype.format = function () {
        var formatted = this;
        for (var arg in arguments) {
            formatted = formatted.replace("{" + arg + "}", arguments[arg]);
        }
        return formatted;
    };
}
if (typeof String.prototype.endsWith !== 'function') {
    String.prototype.endsWith = function (suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };
}

Raphael.fn.connection = function (obj1, obj2, line) {
    if (obj1.line && obj1.from && obj1.to) {
        line = obj1;
        obj1 = line.from;
        obj2 = line.to;
    }
    //Offset by canvas, because the x and y are absolute values
    var cx = $(this.canvas).cssFloat("left");
    var cy = $(this.canvas).cssFloat("top");
    var bb1 = obj1.getBBox(), bb2 = obj2.getBBox(),
    p = [{x: bb1.x + bb1.width / 2 - cx, y: bb1.y - 1 - cy},
        {x: bb1.x + bb1.width / 2 - cx, y: bb1.y + bb1.height + 1 - cy},
        {x: bb1.x - 1 - cx, y: bb1.y + bb1.height / 2 - cy},
        {x: bb1.x + bb1.width + 1 - cx, y: bb1.y + bb1.height / 2 - cy},
        {x: bb2.x + bb2.width / 2 - cx, y: bb2.y - 1 - cy},
        {x: bb2.x + bb2.width / 2 - cx, y: bb2.y + bb2.height + 1 - cy},
        {x: bb2.x - 1 - cx, y: bb2.y + bb2.height / 2 - cy},
        {x: bb2.x + bb2.width + 1 - cx, y: bb2.y + bb2.height / 2 - cy}],
    d = {}, dis = [];
    for (var i = 0; i < 4; i++) {
        for (var j = 4; j < 8; j++) {
            var dx = Math.abs(p[i].x - p[j].x),
            dy = Math.abs(p[i].y - p[j].y);
            if ((i === j - 4) || (((i !== 3 && j !== 6) || p[i].x < p[j].x) && ((i !== 2 && j !== 7) || p[i].x > p[j].x) && ((i !== 0 && j !== 5) || p[i].y > p[j].y) && ((i !== 1 && j !== 4) || p[i].y < p[j].y))) {
                dis.push(dx + dy);
                d[dis[dis.length - 1]] = [i, j];
            }
        }
    }
    if (dis.length === 0) {
        var res = [0, 4];
    } else {
        res = d[Math.min.apply(Math, dis)];
    }
    var x1 = p[res[0]].x,
    y1 = p[res[0]].y,
    x4 = p[res[1]].x,
    y4 = p[res[1]].y;
    dx = Math.max(Math.abs(x1 - x4) / 2, 10);
    dy = Math.max(Math.abs(y1 - y4) / 2, 10);
    var x2 = [x1, x1, x1 - dx, x1 + dx][res[0]].toFixed(3),
    y2 = [y1 - dy, y1 + dy, y1, y1][res[0]].toFixed(3),
    x3 = [0, 0, 0, 0, x4, x4, x4 - dx, x4 + dx][res[1]].toFixed(3),
    y3 = [0, 0, 0, 0, y1 + dy, y1 - dy, y4, y4][res[1]].toFixed(3);
    var path = ["M", x1.toFixed(3), y1.toFixed(3), "C", x2, y2, x3, y3, x4.toFixed(3), y4.toFixed(3)].join(",");
    if (line && line.line) {
        line.line.attr({path: path});
        line.circle.attr({cx: x4, cy: y4});
    } else {
        var color = typeof line === "string" ? line : "#000";
        var connection = {
            color: color,
            line: this.path(path).attr({stroke: color, fill: "none", "stroke-width": 4}),
            circle: this.circle(x4, y4, 6).attr({stroke: color, fill: color}),
            from: obj1,
            to: obj2
        };
        var highlightColor = "#0086DB";
        connection.line.hover(function () {
            connection.line.attr({stroke: highlightColor});
            connection.circle.attr({stroke: highlightColor, fill: highlightColor});
        }, function () {
            connection.line.attr({stroke: color});
            connection.circle.attr({stroke: color, fill: color});
        });
        connection.circle.hover(function () {
            connection.line.attr({stroke: highlightColor});
            connection.circle.attr({stroke: highlightColor, fill: highlightColor});
        }, function () {
            connection.line.attr({stroke: color});
            connection.circle.attr({stroke: color, fill: color});
        });
        return connection;
    }
};

if (!window.Lumens)
    window.Lumens = {version: 1.0};

Lumens.Id = function ($element) {
    var chars = "0123456789abcdefghiklmnopqrstuvwxyz", string_length = 16, id = '';
    for (var i = 0; i < string_length; i++) {
        var rnum = Math.floor(Math.random() * chars.length);
        id += chars.substring(rnum, rnum + 1);
    }
    return $element.attr("element-id", 'id$' + id);
};

Lumens.isMultipleLink = function (class_type) {
    if (class_type === "datasource")
        return true;
    return false;
};

$.ajax({
    async: false,
    url: "rest/i18n/json",
    contentType: "application/json"
}).done(function (data) {
    Lumens.i18n = data;
});

Lumens.system = {
    designView: {}
};

Lumens.system.AngularJSView = 0;
Lumens.system.NormalView = 1;

Lumens.system.switchTo = function (viewMode) {
    if (Lumens.system.workspaceLayout) {
        Lumens.system.workspaceLayout.remove();
        Lumens.system.workspaceLayout = null;
    }
    if (viewMode === Lumens.system.AngularJSView) {
        $("#theWorkspaceContainer").css("height", "100%");
    }
    else if (viewMode === Lumens.system.NormalView) {
        $("#theWorkspaceContainer").css("height", "0");
    }
}

window.LumensLog = window.console ? window.console : {
    log: function () {
    }
};