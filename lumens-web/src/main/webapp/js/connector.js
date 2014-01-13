if (!window.Lumens)
    window.Lumens = {version: 1.0};

Lumens.DataConnector = Class.$extend({
    __init__: function(name, x, y) {
        this.name = name;
        this.x = x;
        this.y = y;
    },
    getName: function() {
        return this.name;
    },
    setName: function(name) {
        this.name = name;
        return this;
    },
    moveTo: function(x, y) {
        this.x = x;
        this.y = y;
        return this;
    },
    getXY: function() {
        return {x: this.x, y: this.y};
    }
});
