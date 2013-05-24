$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.HomeView = {};
    Hrcms.HomeView.create = function(config) {
        var tThis = ContentView.create(config);

        // end
        return tThis;
    }
});
