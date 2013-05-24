$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.SystemView = {};
    Hrcms.SystemView.create = function(config) {
        var tThis = ContentView.create(config);

        // end
        return tThis;
    }
});
