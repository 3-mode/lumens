$(function() {
    if (!window.Hrcms)
        window.Hrcms = {};
    Hrcms.StatisticsView = {};
    Hrcms.StatisticsView.create = function(config) {
        var tThis = ContentView.create(config);

        // end
        return tThis;
    }
});

