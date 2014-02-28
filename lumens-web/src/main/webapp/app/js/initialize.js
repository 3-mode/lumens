if (!window.Lumens)
    window.Lumens = {version: 1.0};

jQuery.fn.cssInt = function(prop) {
    return parseInt(this.css(prop), 10) || 0;
};

jQuery.fn.cssFloat = function(prop) {
    return parseFloat(this.css(prop)) || 0;
};

Raphael.fn.connection = function(obj1, obj2, line) {
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
            line: this.path(path).attr({stroke: color, fill: "none", "stroke-width": 3}),
            circle: this.circle(x4, y4, 5).attr({stroke: color, fill: color}),
            from: obj1,
            to: obj2
        };
        connection.line.hover(function() {
            connection.line.attr({stroke: "#4696ca"});
            connection.circle.attr({stroke: "#4696ca", fill: "#4696ca"});
        }, function() {
            connection.line.attr({stroke: color});
            connection.circle.attr({stroke: color, fill: color});
        });
        connection.circle.hover(function() {
            connection.line.attr({stroke: "#4696ca"});
            connection.circle.attr({stroke: "#4696ca", fill: "#4696ca"});
        }, function() {
            connection.line.attr({stroke: color});
            connection.circle.attr({stroke: color, fill: color});
        });
        return connection;
    }
};

Lumens.Id = function($element) {
    var chars = "0123456789abcdefghiklmnopqrstuvwxyz", string_length = 16, id = '';
    for (var i = 0; i < string_length; i++) {
        var rnum = Math.floor(Math.random() * chars.length);
        id += chars.substring(rnum, rnum + 1);
    }
    return $element.attr("element-id", 'id$' + id);
};

Lumens.SysToolbar_Config = {
    default_active: "id-desinger-view",
    event_type: "SysToolbar",
    buttons:
            [
                {
                    name: "Dashboad",
                    module_id: "id-dashboard-view"
                },
                {
                    name: "Management",
                    module_id: "id-management-view"
                },
                {
                    name: "Business Design",
                    module_id: "id-desinger-view"
                }
            ]
};
